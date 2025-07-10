package simulation;

import optimax.energy.de.bot.*;
import optimax.energy.de.simulation.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
;import java.util.List;


class LotTest {


    static class FixedBidBot extends Bot {
        private final int fixedBid;
        private int lastOwn;
        private int lastOther;

        FixedBidBot(String name, int fixedBid, int budget) {
            super(name);
            this.fixedBid = fixedBid;
            init(0, budget);
        }

        @Override public int placeBid() {
            return fixedBid;
        }
        @Override public void bids(int own, int other) {
            lastOwn   = own;
            lastOther = other;
        }
        @Override public String getName() { return name; }

        int getLastOwn()   { return lastOwn;   }
        int getLastOther() { return lastOther; }
    }

    private FixedBidBot highBidder;   //  10 MU
    private FixedBidBot lowBidder;    //   5 MU
    private FixedBidBot drawBidder1;  //  3 MU
    private FixedBidBot drawBidder2;  //  3 MU

    @BeforeEach
    void setUp() {
        highBidder  = new FixedBidBot("High", 10, 100);
        lowBidder   = new FixedBidBot("Low" ,  5, 100);
        drawBidder1 = new FixedBidBot("A"   ,  3, 50);
        drawBidder2 = new FixedBidBot("B"   ,  3, 50);
    }


    @Test
    void playRound_winnerGetsTwoQuantities() {
        List<Bot> participants = List.of(highBidder, lowBidder);
        Lot lot = new Lot(participants);

        lot.playRound();

        //  HighBidder gets 2 QU
        assertEquals(2, highBidder.getQuantity());
        assertEquals(0, lowBidder.getQuantity());

        // Budget was decreased
        assertEquals(90, highBidder.getBudget());
        assertEquals(95, lowBidder.getBudget());

        // Callback bids() has transmitted correct values
        assertEquals(10, highBidder.getLastOwn());
        assertEquals( 5, highBidder.getLastOther());
        assertEquals( 5, lowBidder .getLastOwn());
        assertEquals(10, lowBidder .getLastOther());
    }


    @Test
    void playRound_drawSplitsQuantity() {
        List<Bot> participants = List.of(drawBidder1, drawBidder2);
        Lot lot = new Lot(participants);

        lot.playRound();


        assertEquals(1, drawBidder1.getQuantity());
        assertEquals(1, drawBidder2.getQuantity());


        assertEquals(47, drawBidder1.getBudget());
        assertEquals(47, drawBidder2.getBudget());
    }
}