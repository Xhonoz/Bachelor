package com.wordpress.honeymoonbridge.bridgeapp.Model;

/**
 * Created by Eier on 20.03.2018.
 */

public enum Trump {
    Clubs, Diamonds, Hearts, Spades, NoTrump;

    public static Trump getTrumpFromSuit(Suit suit) {
        switch (suit) {
            case Spades:
                return Trump.Spades;
            case Hearts:
                return Trump.Hearts;
            case Clubs:
                return Trump.Clubs;
            case Diamonds:
                return Trump.Diamonds;
        }
        return null;
    }
}
