package optimax.energy.de.bot;

import optimax.energy.de.strategy.*;

/**
 * An intelligent trading bot that adapts its bidding strategy based on the current auction state.
 * This bot uses three different strategies depending on the auction phase and remaining budget:
 * - RandomStrategy at the beginning of the auction
 * - SteadyStrategy when budget is low (less than 10% of initial budget)
 * - MedianBasedStrategy for all other cases
 *
 * The bot aims to maximize the quantity won while managing budget efficiently throughout the auction.
 */
public class TradingBot extends Bot{

    /** The initial budget amount set during initialization */
    private int startBudget;

    /** Strategy instance for median-based bidding */
    private final MedianBasedStrategy medianBasedStrategy=new MedianBasedStrategy();

    /** Strategy instance for random bidding */
    private final RandomStrategy randomStrategy=new RandomStrategy();

    /** Strategy instance for steady bidding */
    private final SteadyStrategy steadyStrategy=new SteadyStrategy();

    /**
     * Constructs a TradingBot with the specified name.
     *
     * @param name the display name for this bot
     */
    public TradingBot(String name){
        super(name);
    }

    /**
     * Constructs a TradingBot with the default name.
     */
    public TradingBot(){
        super();
    }

    /**
     * Initializes the bot with starting quantity and budget.
     * Records the initial budget for later strategy decisions.
     *
     * @param quantity the initial quantity units owned
     * @param cash the initial monetary units available for bidding
     */
    @Override
    public void init(int quantity, int cash){
        this.quantity=quantity;
        this.budget=cash;
        this.startBudget=cash;

    }

    /**
     * Determines and places the next bid using an adaptive strategy.
     * The strategy selection follows this priority:
     * 1. RandomStrategy if this is the beginning of the auction (no bid history)
     * 2. SteadyStrategy if remaining budget is 10% or less of the initial budget
     * 3. MedianBasedStrategy for all other situations
     *
     * @return the bid amount in monetary units, or 0 if no cash available
     */
    @Override
    public int placeBid() {
        if (hasCash()) {
            if (isBeginOfAuction()) {
                return playStrategy(randomStrategy);
            }
            else if (hasLessTenPercentOfTheBudget()) {
                return playStrategy(steadyStrategy);
            }
            return playStrategy(medianBasedStrategy);
        }
        return 0;
    }

    /**
     * Executes the given bidding strategy and returns the calculated bid.
     * Sets the strategy for this bot and delegates the bid calculation to the strategy.
     *
     * @param strategy the bidding strategy to use for this bid
     * @return the bid amount calculated by the strategy
     */
    private int playStrategy(BiddingStrategy strategy){
        setStrategy(strategy);
        int bid = biddingStrategy.decideStrategy(budget,bidsStatistics, amountOfQU);
        return bid;
    }

    /**
     * Checks if this is the beginning of the auction.
     * The auction is considered to be at the beginning if there is no bid history.
     *
     * @return true if no bids have been placed yet, false otherwise
     */
    private boolean isBeginOfAuction(){
        return bidsStatistics.isEmpty();
    }

    /**
     * Checks if the remaining budget is 10% or less of the initial budget.
     * This condition triggers the use of SteadyStrategy for conservative bidding.
     *
     * @return true if current budget is 10% or less of the starting budget, false otherwise
     */
    private boolean hasLessTenPercentOfTheBudget(){
        return budget <= startBudget / 10;
    }

    /**
     * Checks if the remaining budget is more than half of the initial budget.
     * This method is currently unused but maintained for potential future strategy extensions.
     *
     * @return true if current budget is more than 50% of the starting budget, false otherwise
     */
    private boolean hasMoreThanHalf(){
        return budget > startBudget / 2;
    }
}