package optimax.energy.de.bot;

import optimax.energy.de.strategy.GrokStrategy;

public class GrokBot extends Bot {
    public GrokBot(String name) {
        super(name);
        setStrategy(new GrokStrategy());
    }

    @Override
    public int placeBid() {
        return hasCash() ?
                biddingStrategy.decideStrategy(budget, bidsStatistics, amountOfQU) : 0;
    }
}