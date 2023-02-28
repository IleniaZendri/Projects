package net.finmath.xva.trade;

import net.finmath.modelling.Product;
import net.finmath.xva.AssetClass;
import net.finmath.xva.cpty.Counterparty;

/**
 * A trade is a transaction with a counterparty on a certain product.
 * 
 * @author Alessandro Gnoatto
 */
public interface Trade {

	/**
	 * Every trade in the portfolio has a unique identifier.
	 * 
	 * @return the trade ID.
	 */
	public String getTradeId();

	/**
	 * Every trade has a trade type.
	 * 
	 * @return the trade type.
	 */
	public TradeType getTradeType();

	/**
	 * @return the counterparty of the trade.
	 */
	public Counterparty getCounterparty();

	/**
	 * Returns the identifier of the netting set. A counterparty might have more
	 * than one sub-portoflio.
	 * 
	 * @return the ID of the netting set.
	 */
	public String getNettingSetId();

	/**
	 * Returns a generic product to be evaluated.
	 * 
	 * @return the product.
	 */
	public Product getProduct();

	/**
	 * Returns the asset class of the product.
	 * 
	 * @return the asset class of the product;
	 */
	public AssetClass getAssetClass();

}
