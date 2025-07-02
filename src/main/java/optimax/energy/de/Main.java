package optimax.energy.de;

import optimax.energy.de.bot.Bidder;
import optimax.energy.de.bot.Bot;
import optimax.energy.de.bot.TradingBot;
import optimax.energy.de.simulation.Auction;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        Bot bot1 = new TradingBot();
        Bot bot2 = new TradingBot();
        Auction auction = new Auction(20);

        auction.addParticipant(bot1);
        auction.addParticipant(bot2);

        auction.start();

    }


}