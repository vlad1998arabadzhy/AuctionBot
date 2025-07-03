package optimax.energy.de.bot;

import java.util.List;

public class BidsStatistic {

    private int round=0;
    private final int ownBid;
    private final int enemiesBid;
    public BidsStatistic(int ownBid, int enemiesBid){
        this.round++;
        this.ownBid=ownBid;
        this.enemiesBid=enemiesBid;
    }

    public int getRound() {
        return round;
    }

    public int getOwnBid() {
        return ownBid;
    }

    public int getEnemiesBid() {
        return enemiesBid;
    }

    @Override
    public String toString(){
        return "Round: "+round+"\nYour bid: "+ownBid+"\nEnemy's bid: "+enemiesBid+"\n";
    }
}
