package optimax.energy.de.bot;

public abstract  class Bot implements Bidder {

    public abstract  int getBudget();


    public abstract String getName();


    public abstract int getQuantity();


    public abstract void increaseQuantity(int quantity);
}
