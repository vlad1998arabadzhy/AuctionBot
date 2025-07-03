package optimax.energy.de.simulation;
import optimax.energy.de.bot.Bot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Lot {
    private  static final Logger logger=Logger.getLogger(Lot.class.getName());
    public static  final int QUANTITY_PER_LOT=2;
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
            winner.increaseQuantity(QUANTITY_PER_LOT);
            logger.info("Winner "+winner.getName()+" has quantity: "+ winner.getQuantity());
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
            logger.info(bidder.getName()+" submitted a bid with amount "+  bid+" MU.");
        }
        notifyParticipents();

    }



    private void notifyParticipents(){
        List <Bot> part = bids.keySet().stream().toList();
        Bot first = part.get(0);
        Bot second = part.get(1);

        first.bids(bids.get(first), bids.get(second));
        second.bids(bids.get(second), bids.get(first));


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
