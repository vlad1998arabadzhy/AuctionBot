package optimax.energy.de.bot;

public class TradingBot extends Bot{


    public TradingBot(String name){
        super(name);
    }



    @Override
    public int placeBid() {
        while (hasCash()) return 6;
        return 0;
    }





}
