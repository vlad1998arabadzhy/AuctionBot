package optimax.energy.de.strategy;

import optimax.energy.de.bot.BidsStatistic;

import java.util.List;

/**
 * A bidding strategy that always increases the opponent’s last bid by a fixed offset.
 * If there is no bid history, returns 0.
 */
public class PressingStrategy implements BiddingStrategy {

    /**
     * Decide the next bid by taking the opponent’s most recent bid and adding 5 MU.
     *
     * @param remainingBudget   how many MU this bot has left
     * @param history           chronological list of past round statistics
     * @param amountOfQU        the fixed amount of QU per lot
     * @return                  the calculated bid (opponent’s last bid + 5), or 0 if no history
     */
    @Override
    public int decideStrategy(int remainingBudget,
                              List<BidsStatistic> history,
                              int amountOfQU) {
        if (!history.isEmpty()) {
            int lastIndex     = history.size() - 1;
            int lastEnemies   = history.get(lastIndex).getEnemiesBid();
            return lastEnemies + 5;
        }
        return 0;
    }
}
