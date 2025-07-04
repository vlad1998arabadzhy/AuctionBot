package optimax.energy.de.bot;


public interface Bidder {
    /**
     * Инициализирует участника с объёмом продукции и доступным бюджетом.
     *
     * @param quantity объём продукции
     * @param cash     доступный денежный лимит
     */
     void init(int quantity, int cash);

    /**
     * Возвращает следующую ставку на товар (может быть ноль).
     *
     * @return следующая ставка
     */
    int placeBid();

    /**
     * Показывает ставки двух участников.
     *
     * @param own   ставка этого участника
     * @param other ставка другого участника
     */
    void bids(int own, int other);


    //------------------------------------------------------------------------------

}