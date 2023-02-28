package net.finmath.xva.trade;

import net.finmath.xva.cpty.Counterparty;

public abstract class AbstractTrade implements Trade {
	private final String tradeId;
	private final TradeType tradeType;
	private final Counterparty counterparty;
	private final String nettingSetId;
	private final String marginSetID;
	private final String currency;

	/**
	 * Constructs an abstrac trade.
	 * 
	 * @param tradeId
	 * @param tradeType
	 * @param counterparty
	 * @param nettingSetId
	 * @param marginSetID
	 * @param currency
	 */
	public AbstractTrade(String tradeId, TradeType tradeType, Counterparty counterparty, String nettingSetId,
			String marginSetID, String currency) {
		super();
		this.tradeId = tradeId;
		this.tradeType = tradeType;
		this.counterparty = counterparty;
		this.nettingSetId = nettingSetId;
		this.marginSetID = marginSetID;
		this.currency = currency;
	}

	/**
	 * Gets the tradeId.
	 * 
	 * @return the tradeId
	 */
	@Override
	public String getTradeId() {
		return tradeId;
	}

	/**
	 * Gets the tradeType.
	 * 
	 * @return the tradeType
	 */
	@Override
	public TradeType getTradeType() {
		return tradeType;
	}

	/**
	 * Gets the counterparty.
	 * 
	 * @return the counterparty
	 */
	@Override
	public Counterparty getCounterparty() {
		return counterparty;
	}

	/**
	 * Gets the nettingSetId.
	 * 
	 * @return the nettingSetId
	 */
	@Override
	public String getNettingSetId() {
		return nettingSetId;
	}

	/**
	 * Gets the marginSetID.
	 * 
	 * @return the marginSetID
	 */
	public String getMarginSetID() {
		return marginSetID;
	}

	/**
	 * Gets the currency.
	 * 
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

}
