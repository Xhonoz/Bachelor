package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

/**
 * Created by Eier on 09.04.2018.
 */

public class Trick {
    Player lead;
    Card firstCard;
    Card SecondCard;

    public Trick(Player lead, Card firstCard, Card secondCard) {
        this.lead = lead;
        this.firstCard = firstCard;
        SecondCard = secondCard;
    }
}
