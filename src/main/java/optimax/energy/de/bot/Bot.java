package optimax.energy.de.bot;

import netscape.javascript.JSObject;
import optimax.energy.de.simulation.Auction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract  class Bot implements Bidder {
    private  static final Logger logger=Logger.getLogger(Bot.class.getName());

    String name;
    int quantity;
    int budget;
    List<BidsStatistic> bidsStatistics;

    public Bot(String name){
        this.name=name;
        this.bidsStatistics =new ArrayList<>();
    }
    public Bot(){
        this.name="Default bot";
        this.bidsStatistics =new ArrayList<>();
    }

    @Override
    public void init(int quantity, int cash){
        this.quantity=quantity;
        this.budget=cash;
    }



    public   int getBudget(){
        return this.budget;
    }


    public  String getName(){
        return this.name;
    };


    public  int getQuantity(){
        return quantity;
    }
    
    public boolean hasCash(){
        logger.info("Bot "+name+ "has amount: "+budget);
        return this.budget>0;
    }


    public  void increaseQuantity(int quantity)
    {this.quantity+=quantity;};

    @Override
    public void bids(int own, int other){
        bidsStatistics.add(new BidsStatistic(own, other));
    }

    public void showBids(){
        logger.info("\nBot's name:"+name+"\n" +
                "\n+++++++++++++++++++++++++++++++++++STAT++++++++++++++++++++++++++++++++++++++++++++++++:\n");
        bidsStatistics.forEach(stat ->{logger.info(stat.toString());});
    }
}
