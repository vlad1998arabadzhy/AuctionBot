package optimax.energy.de.bot;

import optimax.energy.de.strategy.BiddingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract base class for auction participants, encapsulating common state and behavior.
 * Each Bot maintains its name, current quantity won, remaining budget, and bid history.
 * Concrete subclasses configure and apply a BiddingSrategy to decide bids.
 */
public abstract class Bot implements Bidder {

    private static final Logger logger = Logger.getLogger(Bot.class.getName());

    /** The bot's display name. */
    protected String name;
    /** Quantity units (QU) this bot has won so far. */
    protected int quantity;
    /** Remaining monetary units (MU) this bot can use to bid. */
    protected int budget;
    /** History of past rounds' bids (own vs. opponent). */
    public List<BidsStatistic> bidsStatistics;
    /** The bidding strategy used to determine next bid. */
    protected BiddingStrategy biddingStrategy;
    /** The fixed lot size (QU) per auction round. */
    protected int amountOfQU;

    /**
     * Constructs a Bot with the given name and initializes bid history.
     *
     * @param name the bot's name
     */
    public Bot(String name) {
        this.name = name;
        this.bidsStatistics = new ArrayList<>();
    }

    /**
     * Constructs a Bot with a default name and initializes bid history.
     */
    public Bot() {
        this("Default bot");
    }

    /**
    * Initializes the bidder with the production quantity and the allowed cash limit.
     *
     * @param quantity the quantity
     * @param cash the cash limit
    * */
    @Override
    public void init(int quantity, int cash) {
        this.quantity = quantity;
        this.budget = cash;
        logger.info("\n" + name + " has cash " + this.budget);
        logger.info(name + " has quantity " + this.quantity + "\n");
    }

    /**
     * Returns the bot's remaining budget.
     *
     * @return current budget in MU
     */
    public int getBudget() {
        return this.budget;
    }

    /**
     * Returns the bot's name.
     *
     * @return the display name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the total quantity units the bot has won.
     *
     * @return current quantity won
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Configures the bot with a new bidding strategy.
     *
     * @param strategy the strategy to apply for bidding decisions
     */
    protected void setStrategy(BiddingStrategy strategy) {
        this.biddingStrategy = strategy;
    }

    /**
     * Sets the fixed lot size (QU) before the auction begins.
     *
     * @param amountOfQU the number of QU offered per lot
     */
    public void setAmountOfQU(int amountOfQU) {
        this.amountOfQU = amountOfQU;
    }

    /**
     * Checks if the bot still has funds available to bid.
     * Logs current budget for tracing.
     *
     * @return true if budget > 0, false otherwise
     */
    public boolean hasCash() {
        logger.info("Bot " + name + " has budget: " + budget);
        return this.budget > 0;
    }

    /**
     * Deducts the given bid amount from the bot's budget.
     *
     * @param bid the MU amount to subtract
     */
    public void decreaseBudget(int bid) {
        this.budget -= bid;
    }

    /**
     * Increments the bot's won quantity by the specified amount.
     *
     * @param quantity number of QU to add
     */
    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    /**
     * Records the bids from the last round: own and opponent.
     *
     * @param own   the MU this bot bid
     * @param other the MU the opponent bid
     */
    @Override
    public void bids(int own, int other) {
        bidsStatistics.add(new BidsStatistic(own, other));
    }

    /**
     * Logs the full history of this bot's bids and the opponent's bids.
     */
    public void showBids() {
        logger.info("\nBot's name: " + name + "\n" +
                "\n+++++++++++++++++++++++++++++++++++STAT++++++++++++++++++++++++++++++++++++++++++++++++:\n");
        bidsStatistics.forEach(stat -> logger.info(stat.toString()));
    }
}
