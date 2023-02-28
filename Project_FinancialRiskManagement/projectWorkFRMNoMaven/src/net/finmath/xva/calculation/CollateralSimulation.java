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
 * A simple collateral simulation. We assume that collateral is exchanged at any
 * simulation point.
 * 
 * We also assume that
 */
public class CollateralSimulation extends RiskCalculation {

	public CollateralSimulation(AnalyticModel model, String factorId, SurfaceContainer surfaceContainer,
			CreditSupportAnnex csa, Counterparty cpty, Trade[] trades) {
		super(model, factorId, surfaceContainer, csa, cpty, trades);
	}

	@Override
	public RiskCalculation calculate() {
		TimeDiscretization timeDiscretization = this.getSurfaceContainer().getTimeDiscretization();

		// Storage for the cpty exposure
		RandomVariable[] collateralBalance = new RandomVariable[timeDiscretization.getNumberOfTimes()];
		RandomVariable[] positiveCollateralBalance = new RandomVariable[timeDiscretization.getNumberOfTimes()];
		RandomVariable[] negativeCollateralBalance = new RandomVariable[timeDiscretization.getNumberOfTimes()];

		this.initialize(collateralBalance);
		this.initialize(positiveCollateralBalance);
		this.initialize(negativeCollateralBalance);

		FactorKey factorKey = new FactorKey("sommaSurface", FactorType.Cpty, Metric.Value);
		Surface valueSurface = this.getSurface(factorKey);

		double TPost = this.getCsa().getThresholdPost();
		double TRec = this.getCsa().getThresholdRec();
		double MTAPost = 0.5 * this.getCsa().getMinimumTransferAmountPost();
		double MTARec = 0.5 * this.getCsa().getMinimumTransferAmountRec();

		/*
		 * Loop over all points in time -> evolve the collateral account
		 */
		 for(int timeIndex = 0; timeIndex < timeDiscretization.getNumberOfTimes(); timeIndex++) {
		    	//Exposure before collateral, dovrebbe essere la V che ho calcolato nella prima classe
		    	RandomVariable valueSlice = valueSurface.getColumn(timeIndex);
		    	
		    	//parte sx del minimo
		    	RandomVariable sommaPos = valueSlice.add(MTAPost).add(TPost);
		    	
		    	//Creo vettore delle realizzazioni della v.a. per poterli confrontare con 0
		    	double[] valoriSommaPos = sommaPos.getRealizations();
		    	
		    	//Confronto con 0 e cambio i valori nel vettore, quel vettore verrà poi utilizzato per creare la nuova v.a.
		    	for(int i = 0; i < valoriSommaPos.length; i++) {
		    		valoriSommaPos[i] = Math.min(valoriSommaPos[i], 0);
		    	}
		    	
		    	//Inserisco la v.a. nel vettore che le rappresenta
		    	positiveCollateralBalance[timeIndex] = new RandomVariableFromDoubleArray(valoriSommaPos.length, valoriSommaPos);
		    	
		    	//parte sx del massimo
		    	RandomVariable sommaNeg = valueSlice.sub(MTARec).sub(TRec);
		    	
		    	//Creo vettore delle realizzazioni della v.a. per poterli confrontare con 0
		    	double[] valoriSommaNeg = sommaNeg.getRealizations();
		    	
		    	//Confronto con 0 e cambio i valori nel vettore, quel vettore verrà poi utilizzato per creare la nuova v.a.
		    	for(int i = 0; i < valoriSommaPos.length; i++) {
		    		valoriSommaNeg[i] = Math.max(valoriSommaNeg[i], 0);
		    	}
		    	    	
		    	//Inserisco la v.a. nel vettore che le rappresenta
		    	negativeCollateralBalance[timeIndex] = new RandomVariableFromDoubleArray(valoriSommaNeg.length, valoriSommaNeg);
		    	
		    	//Calcolo collaterale
		    	collateralBalance[timeIndex] = positiveCollateralBalance[timeIndex].add(negativeCollateralBalance[timeIndex]);    	
		    }

		FactorType t = FactorType.Cpty;

		// definisco le nuove surface che andranno a contenere i risultati appena
		// calcolati
		Surface collateralSimulation = new Surface("simulazioneCollaterale", t, Metric.CollateralBalance, "discountCurve", collateralBalance, timeDiscretization);
		Surface positiveCollateralSimulation = new Surface("simulazioneCollateralePositivo", t,	Metric.PositiveCollateralBalance, "discountCurve", positiveCollateralBalance, timeDiscretization);
		Surface negativeCollateralSimulation = new Surface("simulazioneCollateraleNegativo", t, Metric.NegativeCollateralBalance, "discountCurve", negativeCollateralBalance, timeDiscretization);

		// Creo il nuovo cubo, composto da quello vecchio e le nuove surface appena definite
		SurfaceContainer container = this.getSurfaceContainer();
		container.addSurface(collateralSimulation);
		container.addSurface(positiveCollateralSimulation);
		container.addSurface(negativeCollateralSimulation);

		return new CollateralSimulation(this.getModel(), this.getFactorID(), container, this.getCsa(), this.getCpty(), this.getTrades());
	}

}