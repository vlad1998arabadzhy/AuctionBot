
# Trading Bot - Auction Trading System

## Project Description

The project simulates an auction where bots compete to purchase goods. Each bot has a limited budget and applies various bidding strategies to maximize the quantity of goods won.

## Auction Rules

* Goods are sold in **lots of 2 QU** (quantity units) each
* Participants place bids simultaneously
* **Lot winner**: the highest bidder receives the full lot (2 QU)
* **Draw**: in case of equal bids, each participant receives 1 QU
* **Payment**: all participants pay their bid amount regardless of the result
* **Auction winner**: the participant with the most QU. In case of a tie, the one with more money left wins

## Architecture

### Main Components

#### 1. Bot (abstract class)

Base class for all auction participants. Contains:

* Bot name
* Current budget (MU - monetary units)
* Quantity won (QU - quantity units)
* Bid history
* Bidding strategy

#### 2. TradingBot

The main bot of the project with an adaptive strategy:

**Strategy selection logic:**

* **Auction start** (no history) → `RandomStrategy`
* **Less than 10% of initial budget left** → `SteadyStrategy`
* **All other cases** → `MedianBasedStrategy`

#### 3. RandomBot

A simple testing bot that always uses `RandomStrategy`.

### Bidding Strategies

#### RandomStrategy

```java
// Random bid based on budget and lot size
int range = remainingBudget / amountOfQU + 1;
int bid = (random.nextInt(range) * 3) + range;
return Math.min(bid, remainingBudget);
```

#### SteadyStrategy

```java
// Steady bid proportional to the budget
int units = remainingBudget / (amountOfQU + 1);
int bid = units * 2;
return Math.min(bid, remainingBudget);
```

#### MedianBasedStrategy

```java
// Bid based on the median of the opponent’s previous bids
int median = calculateMedian(enemyBids);
return Math.min(median, remainingBudget);
```

### Simulation

#### Auction

Manages the overall auction process:

* Adding participants
* Running lots until the goods run out
* Determining the winner

#### Lot

Manages individual lots:

* Collecting bids from participants
* Determining the lot winner
* Distributing goods and deducting funds

## Project Execution

### Main Class

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bot mainBot = new TradingBot("Main Bot");
        Bot randomBot = new RandomBot("Herr Zufall");

        mainBot.init(0, 10000);
        randomBot.init(0, 10000);

        Auction auction = new Auction(40);
        auction.addParticipant(mainBot);
        auction.addParticipant(randomBot);

        auction.start();
    }
}
```

### Philosophy

TradingBot uses an adaptive approach:

1. **At the beginning** – random bids to study the opponent
2. **When the budget is low** – conservative strategy to preserve funds
3. **With sufficient budget** – analyze opponent behavior and bid based on the median

