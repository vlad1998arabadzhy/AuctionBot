package optimax.energy.de.strategy;

import optimax.energy.de.bot.BidsStatistic;

import java.util.List;

/**
 * Implements a bidding strategy that always bids the median of the opponent's past bids,
 * capped by the remaining budget.
 */
public class MedianBasedStrategy implements BiddingStrategy {

    /**
     * Decide the next bid based on the opponent’s bid history.
     *
     * @param remainingBudget   how many MU this bot has left
     * @param bidsStatistics    chronological list of past round bids
     * @param amountOfQU        the fixed amount of QU per lot
     * @return                  the bid (median of opponent’s bids, not exceeding remainingBudget)
     */
    @Override
    public int decideStrategy(int remainingBudget,
                              List<BidsStatistic> bidsStatistics,
                              int amountOfQU) {
        // If there is no bid history, return 0
        if (bidsStatistics.isEmpty()) {
            return 0;
        }

        List<Integer> enemyBids = getEnemyBids(bidsStatistics);
        int median = calculateMedian(enemyBids);

        return Math.min(median, remainingBudget);
    }

    /**
     * Gathers and sorts the list of the opponent's bids.
     *
     * @param history  chronological list of past round statistics
     * @return         a sorted list of the opponent’s bids
     */
    private List<Integer> getEnemyBids(List<BidsStatistic> history) {
        return history.stream()
                .map(BidsStatistic::getEnemiesBid)
                .sorted()
                .toList();
    }

    /**
     * Calculates the median from an already sorted list of bids.
     * For an even number of elements, returns the average of the two middle values.
     *
     * @param sortedBids  a list of bids sorted in ascending order
     * @return            the median bid value
     */
    private int calculateMedian(List<Integer> sortedBids) {
        int size = sortedBids.size();
        int mid  = size / 2;

        if (size % 2 == 0) {
            return ((sortedBids.get(mid - 1) + sortedBids.get(mid)) / 2)+10;
        } else {
            return sortedBids.get(mid)+10;
        }
    }
}
