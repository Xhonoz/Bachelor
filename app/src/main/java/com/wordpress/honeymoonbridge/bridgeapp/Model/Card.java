package com.wordpress.honeymoonbridge.bridgeapp.Model;

import android.support.annotation.NonNull;

/**
 * Created by Carmen on 12.04.2018.
 */

public class Card implements Comparable<Card> {

    private Suit suit;
    private int cardValue;

    public Card(Suit suit, int cardValue) {
        this.suit = suit;
        this.cardValue = cardValue;
    }

    public Card(int index) {
        suit = Suit.values()[index / 13];
        cardValue = index % 13 + 2;

    }

    public int getIndex() {
        return suit.ordinal() * 13 + cardValue - 2;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getCardValue() {
        return cardValue;
    }


    public boolean equals(Card card) {
        if (card == null) return false;
        if (card == this) return true;
        return (this.suit == card.getSuit()) && (this.cardValue == card.getCardValue());
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Card)) return false;
        Card o = (Card) obj;
        return (this.suit == o.getSuit()) && (this.cardValue == o.getCardValue());
    }


    @Override
    public int compareTo(Card card) {
        int compareValue = card.getCardValue();
        return compareValue - this.cardValue;
    }

    @Override
    public String toString() {
        String str = "";
        switch (suit) {

            case Clubs:
                str = "♣";
                break;
            case Diamonds:
                str = "♦";
                break;


            case Hearts:
                str = "♥";
                break;

            case Spades:
                str = "♠";
                break;

        }
        str += cardValue;
        return str;
    }

    public String toTTSString() {
        String str ="";
        if (this.cardValue < 11)
            str = this.cardValue + " of ";
        else {
            if (this.cardValue == 11)
                str = "jack of ";
            if (this.cardValue == 12)
                str = "queen of ";
            if (this.cardValue == 13)
                str = "king of ";
            if(this.cardValue == 14)
                str = "ace of ";

        }
        switch (suit) {

            case Clubs:
                str += "clubs";
                break;
            case Diamonds:
                str += "diamonds";
                break;


            case Hearts:
                str += "hearts";
                break;

            case Spades:
                str += "spades";
                break;

        }
        return str;
    }
}
