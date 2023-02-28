package net.finmath.xva;

import net.finmath.xva.cpty.Counterparty;

public class CreditSupportAnnex {

	// Variation margin parameters
	private String BaseCurrency;
	private String[] ElegibleCurrency;
	
	// Creo 2 campi diversi a seconda che sia receiving o posting del contratto
	private double ThresholdRec;
	private double ThresholdPost;
	private double MinimumTransferAmountRec;
	private double MinimumTransferAmountPost;

	private double IndependentAmount;
	private String[] ElegibleCreditSupport;
	private String CallFrequency;
	

	public CreditSupportAnnex() {
		super();
	}

	public CreditSupportAnnex(double thresholdRec, double thresholdPost, double minimumTransferAmountRec, double minimumTransferAmountPost) {
		super();
		ThresholdRec = thresholdRec;
		ThresholdPost = thresholdPost;
		MinimumTransferAmountRec = minimumTransferAmountRec;
		MinimumTransferAmountPost = minimumTransferAmountPost;
	}

	public CreditSupportAnnex(String baseCurrency, String[] elegibleCurrency, double thresholdRec, double thresholdPost,
			double minimumTransferAmountRec, double minimumTransferAmountPost, double independentAmount,
			String[] elegibleCreditSupport, String callFrequency) {
		super();
		BaseCurrency = baseCurrency;
		ElegibleCurrency = elegibleCurrency;
		ThresholdRec = thresholdRec;
		ThresholdPost = thresholdPost;
		MinimumTransferAmountRec = minimumTransferAmountRec;
		MinimumTransferAmountPost = minimumTransferAmountPost;
		IndependentAmount = independentAmount;
		ElegibleCreditSupport = elegibleCreditSupport;
		CallFrequency = callFrequency;
	}

	public String getBaseCurrency() {
		return BaseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		BaseCurrency = baseCurrency;
	}

	public String[] getElegibleCurrency() {
		return ElegibleCurrency;
	}

	public void setElegibleCurrency(String[] elegibleCurrency) {
		ElegibleCurrency = elegibleCurrency;
	}

	public double getIndependentAmount() {
		return IndependentAmount;
	}

	public void setIndependentAmount(double independentAmount) {
		IndependentAmount = independentAmount;
	}

	public String[] getElegibleCreditSupport() {
		return ElegibleCreditSupport;
	}

	public void setElegibleCreditSupport(String[] elegibleCreditSupport) {
		ElegibleCreditSupport = elegibleCreditSupport;
	}

	public String getCallFrequency() {
		return CallFrequency;
	}

	public void setCallFrequency(String callFrequency) {
		CallFrequency = callFrequency;
	}

	public double getThresholdRec() {
		return ThresholdRec;
	}

	public void setThresholdRec(double thresholdRec) {
		ThresholdRec = thresholdRec;
	}

	public double getThresholdPost() {
		return ThresholdPost;
	}

	public void setThresholdPost(double thresholdPost) {
		ThresholdPost = thresholdPost;
	}

	public double getMinimumTransferAmountRec() {
		return MinimumTransferAmountRec;
	}

	public void setMinimumTransferAmountRec(double minimumTransferAmountRec) {
		MinimumTransferAmountRec = minimumTransferAmountRec;
	}

	public double getMinimumTransferAmountPost() {
		return MinimumTransferAmountPost;
	}

	public void setMinimumTransferAmountPost(double minimumTransferAmountPost) {
		MinimumTransferAmountPost = minimumTransferAmountPost;
	}

}
