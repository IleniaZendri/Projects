package net.finmath.xva;

import net.finmath.marketdata.model.AnalyticModel;

public class InitialMarginParameters {
	private final AnalyticModel model;
	private final String initialMarginRateCurve;
	private final String initialMarginModel;
	private final String postInitialMarginSide;

	public InitialMarginParameters(AnalyticModel model, String initialMarginRateCurve, String initialMarginModel,
			String postInitialMarginSide) {
		super();
		this.model = model;
		this.initialMarginRateCurve = initialMarginRateCurve;
		this.initialMarginModel = initialMarginModel;
		this.postInitialMarginSide = postInitialMarginSide;
	}

	/**
	 * Gets the initialMarginRateCurve.
	 * 
	 * @return the initialMarginRateCurve
	 */
	public String getInitialMarginRateCurve() {
		return initialMarginRateCurve;
	}

	/**
	 * Gets the initialMarginModel.
	 * 
	 * @return the initialMarginModel
	 */
	public String getInitialMarginModel() {
		return initialMarginModel;
	}

	/**
	 * Gets the postInitialMarginSide.
	 * 
	 * @return the postInitialMarginSide
	 */
	public String getPostInitialMarginSide() {
		return postInitialMarginSide;
	}

	/**
	 * Returns the funding spread.
	 * 
	 * @param fundingCurveName name of the funding curve.
	 * @param time             valuation time.
	 * @param deltaTime        step size.
	 * @return the funding spread
	 */
	public double getFundingSpread(String fundingCurveName, double time, double deltaTime) {
		return model.getForwardCurve(fundingCurveName).getForward(model, time, deltaTime)
				- model.getForwardCurve(initialMarginRateCurve).getForward(model, time, deltaTime);
	}
}
