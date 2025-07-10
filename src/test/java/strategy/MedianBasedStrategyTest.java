package strategy;

import optimax.energy.de.bot.BidsStatistic;
import optimax.energy.de.strategy.MedianBasedStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for MedianBasedStrategy class.
 */
class MedianBasedStrategyTest {

    private MedianBasedStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MedianBasedStrategy();
    }

    @Test
    void testEmptyBidHistory() {
        List<BidsStatistic> emptyHistory = new ArrayList<>();

        int result = strategy.decideStrategy(1000, emptyHistory, 40);

        assertEquals(0, result, "Should return 0 when no bid history exists");
    }

    @Test
    void testSingleBidInHistory() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(100, 50));

        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(50, result, "Should return the single enemy bid as median");
    }

    @Test
    void testOddNumberOfBids() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(10, 30)); // enemy: 30
        history.add(new BidsStatistic(20, 10)); // enemy: 10
        history.add(new BidsStatistic(30, 50)); // enemy: 50
        // Sorted: [10, 30, 50], median = 30

        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(30, result, "Should return middle value for odd number of bids");
    }

    @Test
    void testEvenNumberOfBids() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(10, 20)); // enemy: 20
        history.add(new BidsStatistic(20, 40)); // enemy: 40
        history.add(new BidsStatistic(30, 10)); // enemy: 10
        history.add(new BidsStatistic(40, 60)); // enemy: 60
        // Sorted: [10, 20, 40, 60], median = (20+40)/2 = 30

        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(30, result, "Should return average of two middle values for even number of bids");
    }

    @Test
    void testMedianExceedsRemainingBudget() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(100, 200));
        history.add(new BidsStatistic(150, 300));
        history.add(new BidsStatistic(200, 400));
        // Sorted: [200, 300, 400], median = 300

        int result = strategy.decideStrategy(250, history, 40);

        assertEquals(250, result, "Should be capped by remaining budget");
    }

    @Test
    void testZeroRemainingBudget() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(100, 50));

        int result = strategy.decideStrategy(0, history, 40);

        assertEquals(0, result, "Should return 0 when no budget remaining");
    }

    @Test
    void testEnemyBidsWithZeros() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(50, 0));
        history.add(new BidsStatistic(60, 20));
        history.add(new BidsStatistic(70, 0));
        // Sorted: [0, 0, 20], median = 0

        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(0, result, "Should handle zero bids correctly");
    }


    @Test
    void testIdenticalEnemyBids() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(10, 100));
        history.add(new BidsStatistic(20, 100));
        history.add(new BidsStatistic(30, 100));

        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(100, result, "Should return the common value when all bids are identical");
    }

    @Test
    void testLargeNumbers() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(1000, 5000));
        history.add(new BidsStatistic(2000, 7000));
        history.add(new BidsStatistic(3000, 6000));
        // Sorted: [5000, 6000, 7000], median = 6000

        int result = strategy.decideStrategy(10000, history, 40);

        assertEquals(6000, result, "Should handle large numbers correctly");
    }

    @Test
    void testTwoBidsEvenCase() {
        List<BidsStatistic> history = new ArrayList<>();
        history.add(new BidsStatistic(50, 100));
        history.add(new BidsStatistic(60, 200));
        // Sorted: [100, 200], median = (100+200)/2 = 150

        int result = strategy.decideStrategy(1000, history, 40);

        assertEquals(150, result, "Should calculate average correctly for two bids");
    }
}