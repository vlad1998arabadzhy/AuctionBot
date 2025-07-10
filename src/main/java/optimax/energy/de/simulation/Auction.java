package optimax.energy.de.simulation;

import optimax.energy.de.bot.Bot;
import java.util.*;
import java.util.logging.Logger;

/**
 * Simulates an auction for a fixed total quantity of product (x QU),
 * running successive lots of size 2 until the supply is exhausted.
 * Each Bot participant bids for each lot, pays its bid regardless of outcome,
 * and receives QU according to the auction rules:
 * - Highest bidder wins all QU_PER_LOT;
 * - Ties grant each bidder half of QU_PER_LOT;
 * The overall winner is the bot with the most QU; in case of a tie, the one with
 * the higher remaining budget wins.
 */
public class Auction {
    private static final Logger logger = Logger.getLogger(Auction.class.getName());

    /** List of bots participating in the auction. */
    private final List<Bot> participants;

    /** Remaining quantity units to be auctioned (starts at x and decreases by QUANTITY_PER_LOT). */
    private int quantity;

    /**
     * Constructs a new Auction with the given total quantity and participants.
     * Each bot is initialized with the starting budget equal to the total quantity.
     *
     * @param quantity the initial total quantity (in QU) to auction
     * @param bidders the bots that will participate
     */
    public Auction(int quantity, Bot... bidders) {
        this.participants = new ArrayList<>();
        this.quantity = quantity;
        addParticipant(bidders);
    }


    //Getters und setters

    public List<Bot> getParticipants() {
        return participants;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Adds one or more bots to the auction and sets their initial state.
     * Each bot's internal quantity and budget are initialized to the auction parameters.
     *
     * @param bidders the bots to add
     */
    public void addParticipant(Bot... bidders) {
        for (Bot bidder : bidders) {
            participants.add(bidder);
            bidder.setAmountOfQU(quantity);
            logger.info(bidder.getName() + " has joined the auction");
        }
    }

    /**
     * Runs the auction until all quantity is distributed.
     * Each iteration runs one lot, then determines and logs the overall winner.
     */
    public void start() {
        logger.info("\nAuction begins!");

        while (quantity > 0) {
            processNextLot();
        }

        // Optionally display bidding history for the first participant
        participants.get(0).showBids();

        Bot winner = isDrawByQuantity() ? handleDraw() : determineWinnerOfTheAuction();
        displayWinner(winner);

        logger.info("\nAuction has finished");
    }

    /**
     * Conducts a single lot: creates a new Lot, decreases the remaining quantity,
     * and executes the bidding round.
     */
    private void processNextLot() {
        logger.info("Round has started");
        Lot lot = new Lot(participants);
        quantity -= Lot.QUANTITY_PER_LOT;
        lot.playRound();
        logger.info("Round has finished \n");
    }

    /**
     * Determines the overall winner by comparing total QU won.
     *
     * @return the bot with the greatest quantity won
     */
    public Bot determineWinnerOfTheAuction() {
        return participants.stream()
                .max(Comparator.comparing(Bot::getQuantity))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Checks if the auction ends in a draw on QU, by seeing if all bots have
     * placed identical final budgets (after bidding). If so, the budget tie-breaker applies.
     *
     * @return true if budgets are identical for all participants
     */
    public boolean isDrawByQuantity() {
        return participants.stream()
                .map(Bot::getBudget)
                .distinct()
                .count() == 1;
    }

    /**
     * Tie-breaker when QU counts are equal: selects the bot with the highest remaining budget.
     *
     * @return the bot with the highest budget
     */
    public Bot handleDraw() {
        return participants.stream()
                .max(Comparator.comparing(Bot::getBudget))
                .get();
    }

    /**
     * Logs the name and final QU count of the winning bot.
     *
     * @param winner the overall auction winner
     */
    private void displayWinner(Bot winner) {
        logger.info(winner.getName() + " has won with quantity " + winner.getQuantity()+" and cash "+winner.getBudget());

    }
}
