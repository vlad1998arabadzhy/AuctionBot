package optimax.energy.de.strategy;

import optimax.energy.de.bot.BidsStatistic;

import java.util.List;

/**
 * Implementation of Grok's adaptive bidding strategy.
 * This strategy tries to outbid the opponent based on previous bids by bidding one more
 * than the opponent's last bid, but caps the bid to not exceed the average remaining
 * cash per remaining round plus 1. In the last round, it adjusts to try to secure
 * the win if behind.
 *
 * Strategy explanation:
 * - Learns from opponent's bids and tries to minimally outbid them
 * - Manages budget by capping bids to sustainable levels
 * - Special handling for the last round to prioritize winning when necessary
 */
public class GrokStrategy implements BiddingStrategy {

    private int totalRoundsPlayed = 0;
    private int ownQuantityWon = 0;

    /**
     * Decide the next bid using Grok's adaptive approach.
     *
     * @param remainingBudget   how many MU this bot has left
     * @param history           chronological list of past round statistics
     * @param amountOfQU        the fixed amount of QU per lot (total QU in auction)
     * @return                  the calculated bid
     */
    @Override
    public int decideStrategy(int remainingBudget, List<BidsStatistic> history, int amountOfQU) {
        // Update internal state based on history
        updateInternalState(history);

        // Calculate remaining rounds
        int remainingQU = amountOfQU - totalRoundsPlayed * 2;
        int remainingRounds = remainingQU / 2;

        if (remainingRounds <= 0) return 0;

        // Get opponent's last bid, default to 1 if no history
        int lastOpponentBid = getLastOpponentBid(history);
        int predicted = lastOpponentBid >= 0 ? lastOpponentBid : 1;
        int bid = predicted + 1;

        // Cap to average cash per round + 1
        int averageCashPerRound = (int) Math.floor((double) remainingBudget / remainingRounds);
        bid = Math.min(bid, averageCashPerRound + 1);

        // Special logic for last round
        if (remainingRounds == 1) {
            bid = adjustForLastRound(bid, predicted, remainingBudget);
        }

        // Final budget constraint
        bid = Math.min(bid, remainingBudget);

        return bid;
    }

    /**
     * Updates internal tracking of rounds played and quantity won.
     *
     * @param history the complete bid history
     */
    private void updateInternalState(List<BidsStatistic> history) {
        // Reset and recalculate from full history
        totalRoundsPlayed = history.size();
        ownQuantityWon = 0;

        // Calculate total quantity won from history
        for (BidsStatistic stat : history) {
            int ownBid = stat.getOwnBid();
            int opponentBid = stat.getEnemiesBid();

            if (ownBid > opponentBid) {
                ownQuantityWon += 2; // Won the round
            } else if (ownBid == opponentBid) {
                ownQuantityWon += 1; // Tied the round
            }
            // If ownBid < opponentBid, won 0 (already initialized)
        }
    }

    /**
     * Gets the opponent's last bid from the history.
     *
     * @param history the bid history
     * @return the last opponent bid, or -1 if no history
     */
    private int getLastOpponentBid(List<BidsStatistic> history) {
        if (history.isEmpty()) {
            return -1;
        }
        return history.get(history.size() - 1).getEnemiesBid();
    }

    /**
     * Adjusts the bid for the last round based on current position.
     *
     * @param currentBid      the current calculated bid
     * @param predictedBid    the opponent's predicted bid
     * @param remainingBudget the remaining budget
     * @return the adjusted bid for the last round
     */
    private int adjustForLastRound(int currentBid, int predictedBid, int remainingBudget) {
        // Calculate opponent's current quantity
        int opponentQuantity = totalRoundsPlayed * 2 - ownQuantityWon;
        int quantityDifference = ownQuantityWon - opponentQuantity;

        if (quantityDifference <= 0) {
            // Behind or tied - need to win to have a chance
            // Bid more aggressively
            return Math.min(predictedBid + 2, remainingBudget);
        } else if (quantityDifference == 1) {
            // Slightly ahead - a tie is acceptable
            return Math.min(predictedBid, remainingBudget);
        } else {
            // Comfortably ahead - can bid conservatively
            return currentBid;
        }
    }
}