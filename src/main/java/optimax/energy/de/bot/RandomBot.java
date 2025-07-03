package optimax.energy.de.bot;

public class RandomBot extends Bot{
    private int budget;

    public RandomBot(String name){
        super(name);
    }






    @Override
    public int placeBid() {
        return 7;
    }


}
