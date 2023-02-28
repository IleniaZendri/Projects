package net.finmath.xva.trade;

import java.util.HashMap;

/**
 * The trade envelope contains additional/optional information on the trade.
 * 
 * @author Alessandro Gnoatto
 */
public class TradeEnvelope {
	private final String tradeId;
	private final HashMap<String, Object> tradeInfo;

	/**
	 * An empty trade envelope. We use the tradeId as identifier.
	 * 
	 * @param tradeId trade identity
	 */
	public TradeEnvelope(String tradeId) {
		super();
		this.tradeId = tradeId;
		this.tradeInfo = new HashMap<String, Object>();
	}

	/**
	 * Construct a trade envelope from an id and a map.
	 * 
	 * @param tradeId             trade identity
	 * @param additionalTradeInfo map with additional fields
	 */
	public TradeEnvelope(String tradeId, HashMap<String, Object> additionalTradeInfo) {
		super();
		this.tradeId = tradeId;
		this.tradeInfo = additionalTradeInfo;
	}

	/**
	 * Gets the tradeId.
	 * 
	 * @return the tradeId
	 */
	public String getTradeId() {
		return tradeId;
	}

	/**
	 * Gets the additionalTradeInfo.
	 * 
	 * @return the additionalTradeInfo
	 */
	public HashMap<String, Object> getTradeInfo() {
		return tradeInfo;
	}

	public TradeEnvelope getCloneForModifiedData(String field, Object value) {
		/*
		 * The user should make sure that the field exists, otherwise this method will
		 * enlarge additional info.
		 */
		tradeInfo.put(field, value);
		return new TradeEnvelope(this.tradeId, this.getTradeInfo());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeEnvelope other = (TradeEnvelope) obj;
		if (tradeInfo == null) {
			if (other.tradeInfo != null)
				return false;
		} else if (!tradeInfo.equals(other.tradeInfo))
			return false;
		if (tradeId == null) {
			if (other.tradeId != null)
				return false;
		} else if (!tradeId.equals(other.tradeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TradeEnvelope [tradeId=" + tradeId + ", additionalTradeInfo=" + tradeInfo + "]";
	}

}
