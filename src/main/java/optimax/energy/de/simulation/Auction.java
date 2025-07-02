package optimax.energy.de.simulation;

import optimax.energy.de.bot.Bidder;
import optimax.energy.de.bot.Bot;


import java.util.*;
import java.util.logging.Logger;

public class Auction {
    private  static Logger logger=Logger.getLogger(Auction.class.getName());
    private List<Bot> participants;

    private int quantity;

    public Auction( int quantity, Bot ... bidders) {
        this.participants = new ArrayList<>();
        this.quantity = quantity;
        addParticipant(bidders);
    }

    public void addParticipant(Bot ... bidders) {
        for (Bot bidder : bidders){
            participants.add(bidder);
            logger.info(bidder.getName()+" added to the auction");
        }

    }

    private void processNextLot() {
        logger.info("Round has started");

        Lot lot = new Lot(participants);
        quantity-=2;
        lot.playRound();

        logger.info("Round has finished");
    }

    public void start(){
        logger.info("Auction begins!");
        while (quantity==0){
            processNextLot();

        }
        determineWinnerOfTheAuction();
        logger.info("Auction has finished");

    }

    private  void determineWinnerOfTheAuction(){
        Bot winner = participants.stream()
                .max(Comparator.comparing(Bot::getQuantity))
                .orElseThrow(NoSuchElementException::new);
        logger.info(winner.getName()+" has won with quantity " +winner.getQuantity());
    }







}



