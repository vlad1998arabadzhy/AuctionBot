package strategy;



import optimax.energy.de.bot.BidsStatistic;
import optimax.energy.de.strategy.SteadyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for SteadyStrategy class.
 */
class SteadyStrategyTest {

    private SteadyStrategy strategy;
    private List<BidsStatistic> history;

    @BeforeEach
    void setUp() {
        strategy = new SteadyStrategy();
        history = new ArrayList<>();
    }

    @Test
    void testNormalCalculation() {
        // remainingBudget=1000, amountOfQU=40
        // units = 1000 / (40 + 1) = 1000 / 41 = 24
        // bid = 24 * 2 = 48
        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(48, result, "Should calculate bid as (budget/(QU+1))*2");
    }

    @Test
    void testBudgetCapLimit() {
        // remainingBudget=50, amountOfQU=2
        // units = 50 / (2 + 1) = 50 / 3 = 16
        // bid = 16 * 2 = 32, but should be capped at remainingBudget=50
        int result = strategy.decideStrategy(50, history, 2);

        assertEquals(32, result, "Should be capped by remaining budget when bid exceeds it");
    }

    @Test
    void testZeroBudget() {
        int result = strategy.decideStrategy(0, history, 40);

        assertEquals(0, result, "Should return 0 when no budget remaining");
    }
}