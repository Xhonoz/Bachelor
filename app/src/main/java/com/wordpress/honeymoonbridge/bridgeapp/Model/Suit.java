package com.wordpress.honeymoonbridge.bridgeapp.Model;

/**
 * Created by Carmen on 09.04.2018.
 */

public enum Suit {
    Clubs, Diamonds, Hearts, Spades;

    public static Suit getSuitFromTrump(Trump trump) {
        switch (trump) {
            case Spades:
                return Suit.Spades;
            case Hearts:
                return Suit.Hearts;
            case Clubs:
                return Suit.Clubs;
            case Diamonds:
                return Suit.Diamonds;
            case NoTrump:
                return null;
        }
        return null;
    }
}
