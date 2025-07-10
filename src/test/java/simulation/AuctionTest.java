package simulation;

import optimax.energy.de.bot.Bot;
import optimax.energy.de.simulation.Auction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AuctionTest {

    private static class TestBot extends Bot {
        private final int fixedBid;

        public TestBot(String name, int fixedBid) {
            super(name);
            this.fixedBid = fixedBid;
        }

        @Override
        public int placeBid() {
            return hasCash() ? fixedBid : 0;
        }
    }

    @Test
    void testAuctionCompletesWithZeroQuantityRemaining() {
        TestBot bot1 = new TestBot("Bot1", 10);
        TestBot bot2 = new TestBot("Bot2", 20);

        bot1.init(0, 100);
        bot2.init(0, 100);

        Auction auction = new Auction(6);
        auction.addParticipant(bot1, bot2);

        auction.start();

        // After end of auction QU must be 0
        assertEquals(0, auction.getQuantity());
        // And all pieces of QU have to be distributed
        assertEquals(6, bot1.getQuantity() + bot2.getQuantity());
    }

    @Test
    void testWinnerDeterminationByQuantity() {
        TestBot bot1 = new TestBot("Bot1", 5);
        TestBot bot2 = new TestBot("Bot2", 25);

        bot1.init(0, 100);
        bot2.init(0, 100);

        Auction auction = new Auction(6);
        auction.addParticipant(bot1, bot2);

        auction.start();

        Bot winner = auction.determineWinnerOfTheAuction();

        // Bot2 has more QU and must win
        assertEquals(bot2, winner);
        assertTrue(bot2.getQuantity() >= bot1.getQuantity());
    }

    @Test
    void testDrawDetectionAndHandling() {
        TestBot bot1 = new TestBot("Bot1", 15);
        TestBot bot2 = new TestBot("Bot2", 15);

        bot1.init(0, 50);  // Less money
        bot2.init(0, 100); // More money

        Auction auction = new Auction(4);
        auction.addParticipant(bot1, bot2);

        auction.start();

        // Draw by quantity
        assertEquals(bot1.getQuantity(), bot2.getQuantity());

        //The one who saved more MU wins
        Bot winner = auction.handleDraw();
        assertEquals(bot2, winner);
        assertTrue(bot2.getBudget() > bot1.getBudget());
    }

    @Test
    void testIsDrawByQuantityReturnsTrueWhenEqualBudgets() {
        TestBot bot1 = new TestBot("Bot1", 20);
        TestBot bot2 = new TestBot("Bot2", 20);

        bot1.init(0, 80);
        bot2.init(0, 80);

        Auction auction = new Auction(4);
        auction.addParticipant(bot1, bot2);

        auction.start();


        assertTrue(auction.isDrawByQuantity());
    }

    @Test
    void testIsDrawByQuantityReturnsFalseWhenDifferentBudgets() {
        TestBot bot1 = new TestBot("Bot1", 10);
        TestBot bot2 = new TestBot("Bot2", 20);

        bot1.init(0, 100);
        bot2.init(0, 100);

        Auction auction = new Auction(4);
        auction.addParticipant(bot1, bot2);

        auction.start();

        // Draw is not possible, Bot2 wins the auction.
        assertFalse(auction.isDrawByQuantity());
    }
}