package net.finmath.xva.trade;

import net.finmath.montecarlo.interestrate.products.Swap;
import net.finmath.xva.AssetClass;
import net.finmath.xva.cpty.Counterparty;

public class SwapTrade extends AbstractTrade implements Trade {
	private final Swap swap;

	public SwapTrade(String tradeId, Counterparty counterparty, String nettingSetId, String marginSetID,
			String currency, Swap swap) {
		super(tradeId, TradeType.Swap, counterparty, nettingSetId, marginSetID, currency);
		this.swap = swap;
	}

	@Override
	public TradeType getTradeType() {
		return TradeType.Swap;
	}

	@Override
	public Swap getProduct() {
		return this.swap;
	}

	@Override
	public AssetClass getAssetClass() {
		return AssetClass.InterestRate;
	}

}
