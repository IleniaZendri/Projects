package net.finmath.xva.calculation;

import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.xva.CreditSupportAnnex;
import net.finmath.xva.FactorKey;
import net.finmath.xva.Surface;
import net.finmath.xva.SurfaceContainer;
import net.finmath.xva.cpty.Counterparty;
import net.finmath.xva.trade.Trade;

/**
 * Base class for a general risk calculation / risk report.
 * 
 * @author Alessandro Gnoatto
 */
public abstract class RiskCalculation {
	private final AnalyticModel model;
	private final String factorId;
	private final SurfaceContainer surfaceContainer;
	private final CreditSupportAnnex csa;
	private final Counterparty cpty;
	private final Trade[] trades;

	public RiskCalculation(AnalyticModel model, String factorId, SurfaceContainer surfaceContainer,
			CreditSupportAnnex csa, Counterparty cpty, Trade[] trades) {
		super();
		this.model = model;
		this.factorId = factorId;
		this.surfaceContainer = surfaceContainer;
		this.csa = csa;
		this.cpty = cpty;
		this.trades = trades;
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public AnalyticModel getModel() {
		return model;
	}

	/**
	 * Gets the factorId.
	 * 
	 * @return the model
	 */
	public String getFactorID() {
		return factorId;
	}

	/**
	 * Gets the surfaceContainer.
	 * 
	 * @return the surfaceContainer
	 */
	public SurfaceContainer getSurfaceContainer() {
		return surfaceContainer;
	}

	/**
	 * Gets the csa.
	 * 
	 * @return the csa
	 */
	public CreditSupportAnnex getCsa() {
		return csa;
	}

	/**
	 * Gets the cpty.
	 * 
	 * @return the cpty
	 */
	public Counterparty getCpty() {
		return cpty;
	}

	public boolean hasTrades() {
		return this.trades.length > 0 ? true : false;
	}

	/**
	 * Gets the array of trades;
	 * 
	 * @return the trades
	 */
	public Trade[] getTrades() {
		if (hasTrades()) {
			return this.trades;
		} else {
			Trade[] nullTrade = {};
			return nullTrade;
		}
	}

	public Surface getSurface(FactorKey factorKey) {
		return this.surfaceContainer.getSurface(factorKey);
	}

	/**
	 * This method should return a clone of itself where all risk calculations have
	 * been performed.
	 * 
	 * @return a calculated clone.
	 */
	public abstract RiskCalculation calculate();

	// Questo metodo inizializza a 0 il vettore di RandomVariable
	public void initialize(RandomVariable[] surface) {
		RandomVariable temp = new RandomVariableFromDoubleArray(0.0);
		for (int i = 0; i < surface.length; i++) {
			surface[i] = temp;
		}
	}
}