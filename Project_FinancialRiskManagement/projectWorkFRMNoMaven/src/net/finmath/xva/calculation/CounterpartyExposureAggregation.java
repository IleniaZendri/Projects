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

public class CounterpartyExposureAggregation extends RiskCalculation{

	  public CounterpartyExposureAggregation(AnalyticModel model, String factorId, SurfaceContainer surfaceContainer,
	      CreditSupportAnnex csa, Counterparty cpty, Trade[] trades) {
	    super(model, factorId, surfaceContainer, csa, cpty, trades);
	  }

	  @Override
	  public RiskCalculation calculate() {
		//Dobbiamo cambiare la discretizzazione del tempo dobbiamo prendere
	    TimeDiscretization timeDiscretization = this.getSurfaceContainer().getTimeDiscretization();
	    
	    // prendo il mark-to-market cube, forse basta lavorare sul this al posto di creare un nuovo surfacecontainer
	    SurfaceContainer s = this.getSurfaceContainer();

	    //definisco la metrica e il factor type della factor key
	    Metric m = Metric.Value;
	    FactorType t = FactorType.Trade; //Forse non serve dato che il for cicla sui trade
	    RandomVariable[] somma = new RandomVariable[timeDiscretization.getNumberOfTimes()]; //Inizializzare a 0 tutte le VA
	    
	    this.initialize(somma);
	  
	    
	    /*
	     * Loop over all trades and compute the netting set exposure. (Caso semplice, tutti i trade che trova sono swap)
	     */
	    for(Trade trade : this.getTrades()) {
	        FactorKey f = new FactorKey(trade.getTradeId(), t, m);
	        Surface surf = s.getSurface(f);
	        
	        //Loop over time steps
	        for(int timeIndex = 0; timeIndex < timeDiscretization.getNumberOfTimes(); timeIndex++) {
	    	    somma[timeIndex] = somma[timeIndex].add(surf.getColumn(timeIndex));
	        }
	    }
	    
	    /*inserisco la somma in una nuova surface, in teoria, essendo la somma una surface unica per ogni SurfaceContainer, il suo ID può essere inizializzato
	    manualmente. */

	    Surface cptyValueSurface = new Surface("sommaSurface", FactorType.Cpty, m, "discountCurve", somma, timeDiscretization);
	    
	    //creo una nuovo SurfaceContainer con la nuova surface
	    SurfaceContainer newContainer = s.addSurface(cptyValueSurface);
	    return new CounterpartyExposureAggregation(this.getModel(), this.getFactorID(), newContainer, this.getCsa(), this.getCpty(), this.getTrades());
	  }

	}
