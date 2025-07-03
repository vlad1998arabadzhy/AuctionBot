package optimax.energy.de.simulation;


import optimax.energy.de.bot.Bot;


import java.util.*;
import java.util.logging.Logger;

public class Auction {
    private  static final Logger logger=Logger.getLogger(Auction.class.getName());
    private final List<Bot> participants;


    private int quantity;
    private int round;

    public Auction( int quantity, Bot ... bidders) {
        this.participants = new ArrayList<>();
        this.quantity = quantity;
        this.round=0;
        addParticipant(bidders);
    }

    public void addParticipant(Bot ... bidders) {
        for (Bot bidder : bidders){
            participants.add(bidder);
            logger.info(bidder.getName()+" has joined  the auction");
        }

    }

    private void processNextLot() {
        logger.info("Round has started");

        Lot lot = new Lot(participants);
        quantity-= Lot.QUANTITY_PER_LOT;
        lot.playRound();

        logger.info("Round has finished \n");
    }

    public void start(){
        Bot winner;

        logger.info("Auction begins!");
        while (quantity>0){
            processNextLot();
        }
        participants.get(0).showBids();

        if(isDraw()){
             winner = handleDraw();
                    }else {
          winner=determineWinnerOfTheAuction();
        }
        logger.info("Auction has finished");
        displayWinner(winner);
    }

    private  Bot determineWinnerOfTheAuction(){
        return  participants.stream()
                .max(Comparator.comparing(Bot::getQuantity))
                .orElseThrow(NoSuchElementException::new);

    }

    private boolean isDraw(){
        return participants.stream()
                .map(Bot::getBudget)
                .distinct()
                .count()==1;
    }

    private Bot handleDraw(){
        return  participants.stream()
                .max(Comparator.comparing(Bot::getBudget))
                .get();

    }
    private void displayWinner(Bot winner){
        logger.info(winner.getName()+" has won with quantity " +winner.getQuantity());
    }







}



