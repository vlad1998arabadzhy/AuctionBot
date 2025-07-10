package optimax.energy.de.strategy;

import optimax.energy.de.bot.BidsStatistic;

import java.util.List;
import java.util.Random;

/**
 * A bidding strategy that places a random bid based on the remaining budget
 * and the fixed lot size. The bid is calculated as a random integer within
 * [0, remainingBudget / amountOfQU] (inclusive), multiplied by 2, plus offset.
 */
public class RandomStrategy implements BiddingStrategy {

    /**
     * Decide the next bid by generating a pseudo-random value constrained
     * by the remaining budget and lot size.
     *
     * @param remainingBudget   how many MU this bot has left
     * @param history           chronological list of past round statistics
     * @param amountOfQU        the fixed amount of QU per lot
     * @return                  a random bid that does not exceed remainingBudget
     */
    @Override
    public int decideStrategy(int remainingBudget,
                              List<BidsStatistic> history,
                              int amountOfQU) {
        Random random = new Random();


        int range = remainingBudget / amountOfQU + 1;


        int bid = (random.nextInt(range) * 3) + range;


        return Math.min(bid, remainingBudget);
    }
}
