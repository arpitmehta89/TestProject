package com.acme.mytrader.strategy;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceSource;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

	TradingStrategy trStrategy;

	// create the mock object PriceSource and ExecutionService to be injected
	@Mock
	PriceSource priceSource;
	@Mock
	ExecutionService exeService;

	@Before
	public void init() {
		trStrategy = new TradingStrategy(exeService, 100);
		trStrategy.setBuyLimit("Dell", 55.0);
		trStrategy.setSellLimit("Dell", 550.0);
	}

	@Test
	public void testTrading() {
		// Update the price ABOVE buy-limit
		trStrategy.priceUpdate("Dell", 56.0);

		// No buy oders should be placed.
		Mockito.verify(exeService, Mockito.times(0)).buy(anyString(), anyDouble(), anyInt());
		trStrategy.priceUpdate("Dell", 54.0);

		// Dell price dropped below threshold. Exactly one buy order must be placed.
		Mockito.verify(exeService, Mockito.times(1)).buy("Dell", 54.0, 100);
	}
}