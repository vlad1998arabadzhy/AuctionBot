package strategy;
import optimax.energy.de.bot.BidsStatistic;
import optimax.energy.de.strategy.RandomStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomStrategyTest {

    private RandomStrategy strategy;
    private List<BidsStatistic> history;

    @BeforeEach
    void setUp() {
        strategy = new RandomStrategy();
        history = new ArrayList<>();
    }

    @Test
    void testBidWithinBudgetRange() {
        int remainingBudget = 1000;
        int amountOfQU = 40;

        for (int i = 0; i < 100; i++) {
            int result = strategy.decideStrategy(remainingBudget, history, amountOfQU);

            assertTrue(result >= 0, "Bid should be non-negative");
            assertTrue(result <= remainingBudget, "Bid should not exceed remaining budget");
        }
    }

    @Test
    void testZeroBudget() {
        int result = strategy.decideStrategy(0, history, 40);

        assertEquals(0, result, "Should return 0 when no budget remaining");
    }

    @Test
    void testSmallBudget() {
        // remainingBudget=5, amountOfQU=10
        // range = 5/10 + 1 = 0 + 1 = 1
        // random.nextInt(1) = 0, bid = (0 * 3) + 1 = 1
        int result = strategy.decideStrategy(5, history, 10);

        assertTrue(result >= 0, "Should handle small budget gracefully");
        assertTrue(result <= 5, "Should not exceed small budget");
    }
}
