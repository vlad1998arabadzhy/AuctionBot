package optimax.energy.de.simulation;

import optimax.energy.de.bot.Bidder;
import optimax.energy.de.bot.Bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Lot {

    HashMap<Bot, Integer> bids;

    public Lot(List<Bot> participants) {
        bids= new HashMap<>();
        initBiddersForLot(participants);
    }

    public void playRound(){
        placeBids();
        if(isDraw()){
            processDraw();
        }else {
            Bot winner = determineWinner();
            winner.increaseQuantity(2);
        }


    }


    private void initBiddersForLot(List<Bot> bidders){
        for(Bot b: bidders ){
            bids.put(b, 0);
        }
    }


    private void placeBids(){
        for(Map.Entry<Bot, Integer>entry: bids.entrySet()){
            Bot bidder = entry.getKey();
            int bid = bidder.placeBid();
            updateBids(bidder, bid);

        }
    }


    private void updateBids(Bot bidder, int bid){
        bids.put(bidder, bid);
    }


    private Bot determineWinner(){
        return bids.entrySet()
               .stream()
               .max(Map.Entry.comparingByValue())
               .get()
               .getKey();
    }

    private boolean  isDraw(){
       return bids.values()
               .stream()
               .distinct()
               .count()==1;
    }

    private void processDraw(){
        bids.keySet()
                .forEach(bidder -> {bidder.increaseQuantity(1);});
    }

}
