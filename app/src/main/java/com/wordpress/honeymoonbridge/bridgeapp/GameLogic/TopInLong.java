package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Carmen on 16.04.2018.
 */

public class TopInLong implements AIPlayer {
    private int longest;
    private Suit color;

    @Override
    public Card playCard(GameState state) {

        Hand hand = state.getNorthHand();
        ArrayList<Card> handSpade = hand.getHandSpade();
        ArrayList<Card> handHeart = hand.getHandHeart();
        ArrayList<Card> handDiamond = hand.getHandDiamond();
        ArrayList<Card> handClub = hand.getHandClub();
        Trick trick = null;

        if (!state.getTricks().isEmpty()) {
            trick = state.getTricks().get(state.getTricks().size() - 1);
        }


//        First time playing
        if (state.getTricks().isEmpty() || state.getTricks().get(0).SecondCard == null) {
            longest = handSpade.size();
            color = Suit.Spades;
            if (longest < handHeart.size()) {
                longest = handHeart.size();
                color = Suit.Hearts;
            }
            if (longest < handDiamond.size()) {
                longest = handDiamond.size();
                color = Suit.Diamonds;
            }
            if (longest < handClub.size()) {
                longest = handClub.size();
                color = Suit.Clubs;
            }
        }

        if (trick == null || trick.SecondCard != null){
        if (!hand.getCardsOfSuit(color).isEmpty())
            return hand.getCardsOfSuit(color).get(0);
        else {
             int highest = 0;
                Card next = null;
                if (!handClub.isEmpty() && handClub.get(0).getCardValue() > highest) {
                    highest = handClub.get(0).getCardValue();
                    next = handClub.get(0);
                }
                if ((!handSpade.isEmpty()) && (handSpade.get(0).getCardValue() > highest)) {
                    highest = handSpade.get(0).getCardValue();
                    next = handSpade.get(0);
                }
                if ((!handDiamond.isEmpty()) && (handDiamond.get(0).getCardValue() > highest)) {
                    highest = handDiamond.get(0).getCardValue();
                    next = handDiamond.get(0);
                }
                if ((!handHeart.isEmpty()) && (handHeart.get(0).getCardValue() > highest)) {
                    highest = handHeart.get(0).getCardValue();
                    next = handHeart.get(0);
                }

                return next;
            }

        }


        if(trick.SecondCard ==null)

    {
        Card opponent = trick.firstCard;
        if (hand.getCardsOfSuit(opponent.getSuit()).size() != 0) {
            if (hand.getCardsOfSuit(opponent.getSuit()).get(0).getCardValue() > opponent.getCardValue()) {
                return hand.getCardsOfSuit(opponent.getSuit()).get(0);
            } else {
                if (hand.getCardsOfSuit(opponent.getSuit()).size() == 1)
                    return hand.getCardsOfSuit(opponent.getSuit()).get(0);
                return hand.getCardsOfSuit(opponent.getSuit()).get(hand.getCardsOfSuit(opponent.getSuit()).size() - 1);
            }
        } else {
            int smallest = 14;
            Card next = null;
            if (!handClub.isEmpty() && handClub.get(handClub.size() - 1).getCardValue() < smallest) {
                smallest = handClub.get(handClub.size() - 1).getCardValue();
                next = handClub.get(handClub.size() - 1);
            }
            if ((!handSpade.isEmpty()) && (handSpade.get(handSpade.size() - 1).getCardValue() > smallest)) {
                smallest = handSpade.get(handSpade.size() - 1).getCardValue();
                next = handSpade.get(handSpade.size() - 1);
            }
            if ((!handDiamond.isEmpty()) && (handDiamond.get(handDiamond.size() - 1).getCardValue() > smallest)) {
                smallest = handDiamond.get(handDiamond.size() - 1).getCardValue();
                next = handDiamond.get(handDiamond.size() - 1);
            }
            if ((!handHeart.isEmpty()) && (handHeart.get(handHeart.size() - 1).getCardValue() > smallest)) {
                smallest = handHeart.get(handHeart.size() - 1).getCardValue();
                next = handHeart.get(handHeart.size() - 1);
            }

            return next;

        }
    }


        return null;
}

    @Override
    public boolean pickCard(GameState state) {
        return ((Card) state.getStack().get(0)).getCardValue() > 10;
    }

    @Override
    public Bid bid(GameState state) {
        return null;
    }
}
