package net.finmath.xva.calculation;

import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.xva.CreditSupportAnnex;
import net.finmath.xva.FactorKey;
import net.finmath.xva.FactorType;
import net.finmath.xva.Metric;
import net.finmath.xva.Surface;
import net.finmath.xva.SurfaceContainer;
import net.finmath.xva.cpty.Counterparty;
import net.finmath.xva.trade.Trade;


/**
 * This risk calculation comes after CounterpartyExposureAggregation and after
 * CollateralSimulation.
 */
public class PostCollateralExposureSimulation extends RiskCalculation{

	public PostCollateralExposureSimulation(AnalyticModel model, String factorId, SurfaceContainer surfaceContainer, CreditSupportAnnex csa, Counterparty cpty, 
			Trade[] trades) {
		super(model, factorId, surfaceContainer, csa, cpty, trades);
	}

	@Override
	public RiskCalculation calculate() {
		TimeDiscretization timeDiscretization = this.getSurfaceContainer().getTimeDiscretization();
    
		//Initialize Positive and negative post collateral exposure. Arrays of Random Variables, all filled with 0.
		RandomVariable[] PE_t = new RandomVariableFromDoubleArray[timeDiscretization.getNumberOfTimes()];
		RandomVariable[] NE_t = new RandomVariableFromDoubleArray[timeDiscretization.getNumberOfTimes()];
		RandomVariable[] E_t = new RandomVariableFromDoubleArray[timeDiscretization.getNumberOfTimes()];
		
		this.initialize(PE_t);
		this.initialize(NE_t);
		this.initialize(E_t);
		
		//Prendo il collateral
		FactorKey collKey = new FactorKey("simulazioneCollaterale", FactorType.Cpty, Metric.CollateralBalance);
		boolean isCollateralized = this.getSurfaceContainer().hasFactor(collKey);
		
				
		//Prendo il valore di portafoglio
		FactorKey valueKey = new FactorKey("sommaSurface", FactorType.Cpty, Metric.Value);
		Surface valueSurface = this.getSurface(valueKey);
		
		
		if(isCollateralized) {
			//Vado a trovare surface con i collaterali
			Surface collateralSurface = this.getSurface(collKey);
			
			for(int timeIndex = 0; timeIndex < timeDiscretization.getNumberOfTimes(); timeIndex++) {
				//V_t
				RandomVariable V_t = valueSurface.getColumn(timeIndex);
		    	
		    	//C_t
				RandomVariable C_t = collateralSurface.getColumn(timeIndex);
				
				//V_t - C_t
		    	RandomVariable VsubC = V_t.sub(C_t);
		    
		    	double[] positiveExposureVsubC = VsubC.getRealizations();
		    	double[] negativeExposureVsubC = VsubC.getRealizations();
		    	double[] exposureVsubC = VsubC.getRealizations();
		    	double[] tempVsubC = VsubC.getRealizations();
		    	
		    	
				for(int i = 0; i < tempVsubC.length; i++) {
		    		//Aggiungo il massimo tra uno scenario e 0
		    		positiveExposureVsubC[i] = Math.max(tempVsubC[i], 0);
			    	negativeExposureVsubC[i] = Math.max(-1*tempVsubC[i], 0);
		    	}
		    	
		    	PE_t[timeIndex] = PE_t[timeIndex].add(new RandomVariableFromDoubleArray(positiveExposureVsubC.length, positiveExposureVsubC));
		    	
		    	NE_t[timeIndex] = NE_t[timeIndex].add(new RandomVariableFromDoubleArray(negativeExposureVsubC.length, negativeExposureVsubC));
		    	
		    	E_t[timeIndex] = E_t[timeIndex].add(new RandomVariableFromDoubleArray(exposureVsubC.length, exposureVsubC));
		    	
			}
			
		}else{
			
			for(int timeIndex = 0; timeIndex < timeDiscretization.getNumberOfTimes(); timeIndex++) {
				
				//V_t
				RandomVariable V_t = valueSurface.getColumn(timeIndex);
				
				//max{V_t,0}, PositiveExposure, max{-V_t, 0}, NegativeExposure utilizzando lo stesso for
				//Creo vettore delle realizzazioni della v.a. per poterli confrontare con 0
		    	double[] positiveExposureV = V_t.getRealizations();
		    	double[] negativeExposureV = V_t.getRealizations();
		    	double[] exposureV = V_t.getRealizations();
		    	double[] tempV_t = V_t.getRealizations();
		    	
		    	//Confronto con 0 e cambio i valori nel vettore, quel vettore verrà poi utilizzato per creare la nuova v.a.
		    	for(int i = 0; i < tempV_t.length; i++) {
		    		//Aggiungo il massimo tra uno scenario e 0
		    		positiveExposureV[i] = Math.max(tempV_t[i], 0);
			    	negativeExposureV[i] = Math.max(-1*tempV_t[i], 0);
		    	}
		    	
		    	PE_t[timeIndex] = PE_t[timeIndex].add(new RandomVariableFromDoubleArray(positiveExposureV.length, positiveExposureV));
		    	
		    	NE_t[timeIndex] = NE_t[timeIndex].add(new RandomVariableFromDoubleArray(negativeExposureV.length, negativeExposureV));
		    	
		    	E_t[timeIndex] = E_t[timeIndex].add(new RandomVariableFromDoubleArray(exposureV.length, exposureV));
			}
		}
		
		Surface positiveExposureSurface = new Surface("EsposizionePositiva", FactorType.Cpty, Metric.PositiveExposure, "discountCurve", PE_t, timeDiscretization);
		Surface negativeExposureSurface = new Surface("EsposizioneNegativa", FactorType.Cpty, Metric.NegativeExposure, "discountCurve", NE_t, timeDiscretization);
		Surface postCollateralExposureSurface = new Surface("Esposizione", FactorType.Cpty, Metric.Exposure, "discountCurve", E_t, timeDiscretization);
    	
		SurfaceContainer container = this.getSurfaceContainer();
		container.addSurface(positiveExposureSurface);
		container.addSurface(negativeExposureSurface);
		container.addSurface(postCollateralExposureSurface);
		
		return new PostCollateralExposureSimulation(this.getModel(), this.getFactorID(),container, this.getCsa(), this.getCpty(), this.getTrades());
	}

}