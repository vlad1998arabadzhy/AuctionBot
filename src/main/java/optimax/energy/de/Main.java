package optimax.energy.de;

import optimax.energy.de.bot.Bot;
import optimax.energy.de.bot.GrokBot;
import optimax.energy.de.bot.RandomBot;
import optimax.energy.de.bot.TradingBot;
import optimax.energy.de.simulation.Auction;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws InterruptedException {

        Bot mainBot = new TradingBot("Main Bot");
        Bot randomBot = new RandomBot("Herr Zufall");
        Bot grokBot = new GrokBot("Grok");

        mainBot.init(0, 10000);
        randomBot.init(0, 10000);
        grokBot.init(0, 10000);

        Auction auction = new Auction(40);

        auction.addParticipant(randomBot);
        auction.addParticipant(grokBot);

        auction.start();

    }


}