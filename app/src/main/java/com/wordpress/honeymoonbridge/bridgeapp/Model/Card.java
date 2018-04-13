package com.wordpress.honeymoonbridge.bridgeapp.Model;

import android.support.annotation.NonNull;

/**
 * Created by Carmen on 12.04.2018.
 */

public class Card implements Comparable<Card>{

    private Suit suit;
    private int cardValue;

    public Card(Suit suit, int cardValue){
        this.suit = suit;
        this.cardValue = cardValue;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getCardValue() {
        return cardValue;
    }
    public boolean equals(Card card){
        return this.suit == (card.suit) && this.cardValue == card.cardValue;
    }

    @Override
    public int compareTo(Card card) {
        int compareValue = card.getCardValue();
        return compareValue - this.cardValue;
    }

    @Override
    public String toString(){
        return "" + this.getSuit() + this.getCardValue();
    }
}
