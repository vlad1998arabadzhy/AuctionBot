package optimax.energy.de.bot;

import optimax.energy.de.strategy.RandomStrategy;

/**
 * A bot implementation that uses a random bidding strategy.
 * This bot will place a bid determined by RandomStrategy,
 * and used only as a test-competitor for TradingBot.
 */
public class RandomBot extends Bot {

    /**
     * Constructs a RandomBot with the given name and assigns its strategy.
     *
     * @param name the display name of the bot
     */
    public RandomBot(String name) {
        super(name);
        setStrategy(new RandomStrategy());
    }

    /**
     * Determines and returns the next bid amount using the assigned strategy.
     * If the bot has no remaining budget, returns 0.
     *
     * @return the MU amount this bot will bid in the next round
     */
    @Override
    public int placeBid() {
        if (hasCash()) {
            return biddingStrategy.decideStrategy(budget, bidsStatistics, amountOfQU);
        }
        return 0;
    }
}
