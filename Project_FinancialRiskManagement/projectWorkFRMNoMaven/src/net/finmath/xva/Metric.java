package net.finmath.xva;

/**
 * Metrics are results of risk calculations.
 */
public enum Metric {
	Value, // V
	ValueMarginPeriodOfRisk,

	PositiveExposure, /// max{V_t - C_t,0}
	NegativeExposure, /// max{C_t - V_t,0}
	Exposure, // post collateral, i.e. V_t - C_t

	NegativeCollateralBalance, PositiveCollateralBalance, CollateralBalance, // C_t

	PostedInitialMargin, ReceivedInitialMargin,

	CVA, // Only cpty can default
	BCVA, // Both can default
	DVA, // Only cpty can default
	BDVA, // Both can default
	FBA0, // No defaults,
	FBA1, // Only cpty can default
	FBA2, // Both can default
	FCA0, // No default,
	FCA1, // Only cpty can default
	FCA2, // Both can default
	FVA0, // No default
	FVA1, // Only cpty can default
	FVA2, // Both can default
	COLVA0, // No default
	COLVA1, // Only cpty can default
	COLVA2, // Both can default
	MVA0, // No default,
	MVA1, // Only cpty can default
	MVA2 // Both can default

}
