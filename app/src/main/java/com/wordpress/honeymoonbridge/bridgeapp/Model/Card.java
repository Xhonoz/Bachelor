package com.wordpress.honeymoonbridge.bridgeapp.Model;

/**
 * Created by Carmen on 09.04.2018.
 */

public class Card {
    private Suit suit;
    private int cardValue;

    public Card(Suit suit, int cardValue){
        this.suit = suit;
        this.cardValue = cardValue;
    }
}
