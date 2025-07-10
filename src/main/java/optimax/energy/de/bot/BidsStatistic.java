package optimax.energy.de.bot;

/**
 * Stores the bid values from a single auction round for one bot:
 * - the amount this bot bid (ownBid)
 * - the amount the opponent bid (enemiesBid)
 *
 * This class is immutable and provides accessors for retrieving each bid value,
 * as well as a formatted toString() for logging or console output.
 */
public class BidsStatistic {

    /** The amount this bot bid in the round. */
    private final int ownBid;
    /** The amount the opponent bid in the same round. */
    private final int enemiesBid;

    /**
     * Constructs a new BidsStatistic with the specified bid values.
     *
     * @param ownBid      the MU amount this bot placed
     * @param enemiesBid  the MU amount the opponent placed
     */
    public BidsStatistic(int ownBid, int enemiesBid) {
        this.ownBid = ownBid;
        this.enemiesBid = enemiesBid;
    }

    /**
     * Returns the MU amount this bot bid.
     *
     * @return the bot's bid in the round
     */
    public int getOwnBid() {
        return ownBid;
    }

    /**
     * Returns the MU amount the opponent bid.
     *
     * @return the opponent's bid in the round
     */
    public int getEnemiesBid() {
        return enemiesBid;
    }

    /**
     * Returns a formatted string showing both bids for logging or display.
     *
     * @return a multi-line string with own and enemy bid values
     */
    @Override
    public String toString() {
        return "Your bid: " + ownBid + " MU\n" +
                "Enemy's bid: " + enemiesBid + " MU\n";
    }
}
