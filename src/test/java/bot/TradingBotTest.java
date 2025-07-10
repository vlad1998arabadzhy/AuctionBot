package bot;

import optimax.energy.de.bot.TradingBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for TradingBot class.
 */
class TradingBotTest {

    private TradingBot tradingBot;

    @BeforeEach
    void setUp() {
        tradingBot = new TradingBot("TestBot");
    }

    @Test
    void testInitialization() {
        tradingBot.init(100, 1000);

        assertEquals(100, tradingBot.getQuantity());
        assertEquals(1000, tradingBot.getBudget());
        assertEquals("TestBot", tradingBot.getName());
    }

    @Test
    void testBeginOfAuctionUsesRandomStrategy() {
        tradingBot.init(100, 1000);
        tradingBot.setAmountOfQU(40);

        // At the beginning of auction (empty history) RandomStrategy should be used
        int bid = tradingBot.placeBid();

        assertTrue(bid >= 0, "Bid should be non-negative");
        assertTrue(bid <= 1000, "Bid should not exceed budget");
        // RandomStrategy gives random values, so we only check boundaries
    }

    @Test
    void testLessThanTenPercentBudgetUsesSteadyStrategy() {
        tradingBot.init(100, 1000);
        tradingBot.setAmountOfQU(40);

        // Add history so it's not the beginning of auction
        tradingBot.bids(100, 150);

        // Reduce budget to less than 10% of starting budget (1000/10 = 100)
        // Set budget to 80 (less than 100)
        for (int i = 0; i < 920; i++) {
            tradingBot.decreaseBudget(1);
        }

        int bid = tradingBot.placeBid();

        // SteadyStrategy: (budget/(amountOfQU+1))*2 = (80/41)*2 = 1*2 = 2
        assertEquals(2, bid);
    }

    @Test
    void testMoreThanTenPercentUsesMedianStrategy() {
        tradingBot.init(100, 1000);
        tradingBot.setAmountOfQU(40);

        // Add opponent bid history for MedianBasedStrategy
        tradingBot.bids(100, 200);
        tradingBot.bids(150, 300);
        tradingBot.bids(200, 100);

        // Budget 500 > 100 (10% of 1000), so MedianBasedStrategy should be used
        for (int i = 0; i < 500; i++) {
            tradingBot.decreaseBudget(1);
        }

        int bid = tradingBot.placeBid();

        // Median of [200, 300, 100] = [100, 200, 300] = 200
        assertEquals(200, bid);
    }

    @Test
    void testNoCashReturnsZero() {
        tradingBot.init(100, 0); // No money

        int bid = tradingBot.placeBid();

        assertEquals(0, bid);
    }

    @Test
    void testExactlyTenPercentBudgetUsesSteadyStrategy() {
        tradingBot.init(100, 1000);
        tradingBot.setAmountOfQU(40);

        // Add history so it's not the beginning of auction
        tradingBot.bids(100, 150);

        // Budget exactly 100 (10% of 1000) should use SteadyStrategy
        for (int i = 0; i < 900; i++) {
            tradingBot.decreaseBudget(1);
        }

        int bid = tradingBot.placeBid();

        // SteadyStrategy: (budget/(amountOfQU+1))*2 = (100/41)*2 = 2*2 = 4
        assertEquals(4, bid);
    }

    @Test
    void testJustAboveTenPercentUsesMedianStrategy() {
        tradingBot.init(100, 1000);
        tradingBot.setAmountOfQU(40);

        // Add opponent bid history for MedianBasedStrategy
        tradingBot.bids(100, 250);
        tradingBot.bids(100, 250);
        tradingBot.bids(100, 250);

        // Budget 101 > 100 (10% of 1000), so MedianBasedStrategy should be used
        for (int i = 0; i < 899; i++) {
            tradingBot.decreaseBudget(1);
        }

        int bid = tradingBot.placeBid();

        // Median of [250] = 250, but capped by budget 101
        assertEquals(101, bid);
    }

    @Test
    void testDefaultConstructor() {
        TradingBot defaultBot = new TradingBot();

        assertEquals("Default bot", defaultBot.getName());
    }

    @Test
    void testNamedConstructor() {
        TradingBot namedBot = new TradingBot("MyBot");

        assertEquals("MyBot", namedBot.getName());
    }
}