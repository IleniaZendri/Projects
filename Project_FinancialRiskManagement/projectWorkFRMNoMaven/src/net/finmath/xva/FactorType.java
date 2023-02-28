package net.finmath.xva;

public enum FactorType {
	/*
	 * Trade Aggregation
	 */
	Cpty, // All trades of the cpty
	MarginSet, // All trades in a CSA
	NettingSet, // Aggregation over multiple CSAs
	Trade, // A single trade

	/*
	 * Market Risk Factors
	 */
	InterestRate,
	// ForeignExchangeRate...
}
