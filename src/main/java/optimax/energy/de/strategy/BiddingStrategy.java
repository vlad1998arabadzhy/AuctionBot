package optimax.energy.de.strategy;

import optimax.energy.de.bot.BidsStatistic;

import java.util.List;
/**
 * Defines the contract for a bidding strategy in the auction simulation.
 */
public interface BiddingStrategy {
    /**
     * Decide the next bid amount according to the strategy.
     *
     * @param remainingBudget   the amount of MU the bot still has available
     * @param bidsStatistics    chronological list of past round statistics
     * @param amountOfQU        the fixed number of QU offered per lot
     * @return                  the computed bid, which must be between 0 and remainingBudget
     */
    int decideStrategy( int remainingBudget, List<BidsStatistic> bidsStatistics, int amountOfQU);
}
