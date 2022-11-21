package com.acme.mytrader.strategy;

import java.util.HashMap;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy implements PriceListener {

	// Declare Trading variable for TradingStrategy
	private Integer quantity;
	private ExecutionService executionService;
	private HashMap<String, Double> buyLimit;
	private HashMap<String, Double> sellLimit;
	// set the default Quantity to 100
	private static Integer defaultQuantity = 100;

	// Create Construtor for set Trading value
	public TradingStrategy(ExecutionService executionService, Integer quantity) {
		this.executionService = executionService;
		this.buyLimit = new HashMap<String, Double>();
		this.sellLimit = new HashMap<String, Double>();
		this.quantity = quantity;
	}

	public TradingStrategy(ExecutionService executionService) {
		this(executionService, defaultQuantity);
	}

	public void priceUpdate(String security, double price) {
		tradingOrder(security, price);
	}

	/**
	 * This method uses the trigger Trading Orders. based on the price will bounce
	 * up or down back from current price, hence buy or sell at low or high prices.
	 * 
	 * @param security The name of the security (instrument)
	 * @param price    The current market price of the instrument.
	 */
	private void tradingOrder(String security, Double price) {
		Double buyThreshold = buyLimit.get(security);
		Double sellThreshold = sellLimit.get(security);
		if (buyThreshold != null && price < buyThreshold) {
			executionService.buy(security, price, getquantity());
		}
		if (sellThreshold != null && price > sellThreshold) {
			executionService.sell(security, price, getquantity());
		}
	}

	/**
	 * Set the buy Limit for the given security.
	 * 
	 * @param security  The name of the security (instrument)
	 * @param threshold The threshold, below which a buy order should be placed.
	 */
	public void setBuyLimit(String security, Double buyThreshold) {
		// IDEA: Check if the buy threshold is lower than an existing sell threshold.
		// Such case is undefined in the user story.
		buyLimit.put(security, buyThreshold);
	}

	/**
	 * Sets the sell threshold for the given security.
	 * 
	 * @param security  The name of the security (instrument)
	 * @param threshold The threshold, above which a buy order should be placed.
	 */
	public void setSellLimit(String security, Double sellThreshold) {
		// IDEA: Check if the buy threshold is lower than an existing sell threshold.
		// Such case is undefined in the user story.

		sellLimit.put(security, sellThreshold);
	}

	/**
	 * This method reset all the buy and sell price limit.
	 */
	public void resetTrade() {
		sellLimit.clear();
		buyLimit.clear();
	}

	/**
	 * Return the quantity attribute.
	 * 
	 * @param quantity The volume with which we would like to trade.
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * Set the quantity attribute.
	 * 
	 * @return The volume quantity with which we would like to trade.
	 */
	public Integer getquantity() {
		return quantity;
	}
}
