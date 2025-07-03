package optimax.energy.de;

import optimax.energy.de.bot.Bidder;
import optimax.energy.de.bot.Bot;
import optimax.energy.de.bot.RandomBot;
import optimax.energy.de.bot.TradingBot;
import optimax.energy.de.simulation.Auction;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        Bot bot1 = new TradingBot("Main Bot");
        Bot bot2 = new RandomBot("Herr Zufal");

        bot1.init(0, 1000);
        bot2.init(0, 1000);


        Auction auction = new Auction(20);

        auction.addParticipant(bot1);
        auction.addParticipant(bot2);

        auction.start();

    }


}