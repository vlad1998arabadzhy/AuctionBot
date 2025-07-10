package optimax.energy.de.simulation;

import optimax.energy.de.bot.Bot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents a single auction lot for a fixed quantity of product (2 QU per lot).
 * This class manages the bidding process for one lot: collecting bids from each bot,
 * notifying participants of each other's bids, determining the winner or a draw,
 * adjusting each bot's budget and quantity won accordingly, and logging the outcome.
 */
public class Lot {
    private static final Logger logger = Logger.getLogger(Lot.class.getName());

    /** Quantity units awarded per winning bid in this lot. */
    public static final int QUANTITY_PER_LOT = 2;

    /** Maps each bot to its current bid amount for this lot. */
    private final Map<Bot, Integer> bids;

    /**
     * Initializes a new Lot for the given participants, with starting bids of zero.
     *
     * @param participants the list of bots participating in this lot
     */
    public Lot(List<Bot> participants) {
        this.bids = new HashMap<>();
        initBiddersForLot(participants);
    }

    /**
     * Executes the auction process for this lot:
     * 1. Collects bids from each bot.
     * 2. Notifies each bot of both bids.
     * 3. Determines if the lot is a draw or has a single winner.
     * 4. Updates budgets and quantities accordingly.
     * 5. Logs the result.
     */
    public void playRound() {
        placeBids();
        if (isDraw()) {
            processDraw();
        } else {
            Bot winner = determineWinner();
            processVictory(winner);
        }
    }

    /**
     * Sets initial bid of zero for each bot in this lot.
     *
     * @param bidders the bots to initialize for this lot
     */
    private void initBiddersForLot(List<Bot> bidders) {
        for (Bot b : bidders) {
            bids.put(b, 0);
        }
    }

    /**
     * Invokes each bot's placeBid(), records the bid, and logs it.
     */
    private void placeBids() {
        for (Map.Entry<Bot, Integer> entry : bids.entrySet()) {
            Bot bidder = entry.getKey();
            int bid = bidder.placeBid();
            updateBids(bidder, bid);
            logger.info(bidder.getName() + " submitted a bid with amount " + bid + " MU.");
        }
        notifyParticipants();
    }

    /**
     * Notifies each bot of its own bid and its opponent's bid via the bids() callback.
     */
    private void notifyParticipants() {
        List<Bot> part = bids.keySet().stream().toList();
        Bot first = part.get(0);
        Bot second = part.get(1);
        first.bids(bids.get(first), bids.get(second));
        second.bids(bids.get(second), bids.get(first));
    }

    /**
     * Updates the recorded bid for a given bot.
     *
     * @param bidder the bot that placed the bid
     * @param bid    the bid amount
     */
    private void updateBids(Bot bidder, int bid) {
        bids.put(bidder, bid);
    }

    /**
     * Determines which bot has the highest bid. In case of equal bids, both
     * are considered a draw elsewhere.
     *
     * @return the bot with the highest bid
     */
    private Bot determineWinner() {
        return bids.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Checks whether all bids in this lot are identical, indicating a draw.
     *
     * @return true if every bot bid the same amount; false otherwise
     */
    private boolean isDraw() {
        return bids.values()
                .stream()
                .distinct()
                .count() == 1;
    }

    /**
     * Processes a victory: awards the full lot quantity to the winner,
     * deducts each bot's bid from its budget, and logs the winner.
     *
     * @param winner the bot that won this lot
     */
    private void processVictory(Bot winner) {
        winner.increaseQuantity(QUANTITY_PER_LOT);
        // Deduct each bot's bid from its budget
        bids.keySet().forEach(bidder -> bidder.decreaseBudget(bids.get(bidder)));
        logger.info("Winner " + winner.getName() + " has quantity: " + winner.getQuantity());
    }

    /**
     * Processes a draw: awards each bot half the lot (1 QU),
     * deducts each bot's bid from its budget, and logs the draw.
     */
    private void processDraw() {
        bids.keySet().forEach(bidder -> {
            bidder.increaseQuantity(1);
            bidder.decreaseBudget(bids.get(bidder));
        });
        logger.info("DRAW! Everybody received 1 QU");
    }
}
