package optimax.energy.de.strategy;

import optimax.energy.de.bot.BidsStatistic;

import java.util.List;

/**
 * A bidding strategy that places a steady bid proportional to the remaining budget
 * and the fixed lot size. The bid is calculated as (remainingBudget / amountOfQU) * 2.
 */
public class SteadyStrategy implements BiddingStrategy {

    /**
     * Decide the next bid by dividing the remaining budget by the lot size
     * and scaling by a factor of 2.
     *
     * @param remainingBudget   how many MU this bot has left
     * @param history           chronological list of past round statistics
     * @param amountOfQU        the fixed amount of QU per lot
     * @return                  the calculated bid, or 0 if remainingBudget is less than amountOfQU
     */
    @Override
    public int decideStrategy(int remainingBudget,
                              List<BidsStatistic> history,
                              int amountOfQU) {

        int units = remainingBudget / (amountOfQU+ 1);
        int bid = units * 2;


        return Math.min(bid, remainingBudget);
    }
}
