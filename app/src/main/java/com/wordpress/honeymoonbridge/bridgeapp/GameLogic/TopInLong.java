package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;

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
        Trump trump = state.getTrump();
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

            ArrayList<ArrayList<Card>> suits = hand.getSuitArrays();
                       longest = suits.get(0).size();
            int longestIndex = 0;

            for (int i = 1; i < suits.size(); i++) {
                ArrayList<Card> current = suits.get(i);
                if (!current.isEmpty()) {
                    if (longest <= current.size()) {
                        if (longest == current.size()) {
                            for (int j = 0; j < current.size(); j++) {
                                if (current.get(j).getCardValue() > suits.get(longestIndex).get(j).getCardValue()) {
                                    longest = current.size();
                                    longestIndex = i;
                                    break;
                                } else if (current.get(j).getCardValue() < suits.get(longestIndex).get(j).getCardValue())
                                    break;
                            }
                        } else {
                            longest = current.size();
                            longestIndex = i;
                        }
                    }

                }
            }
            color = Suit.values()[longestIndex];

        }
//        If playing first card
        if (trick == null || trick.SecondCard != null) {
            if (!hand.getCardsOfSuit(color).isEmpty())
                return hand.getCardsOfSuit(color).get(0);
            else {

                ArrayList<ArrayList<Card>> suits = hand.getSuitArrays();

                int highest = 0;
                Card next = null;

                for(int i = 0; i < suits.size(); i++) {
                    ArrayList<Card> current = suits.get(i);
                    if (!current.isEmpty() && current.get(0).getCardValue() > highest) {
                        highest = current.get(0).getCardValue();
                        next = current.get(0);
                    }
                }

                return next;
            }

        }

//        If playing second card
        if (trick.SecondCard == null) {
            Card opponent = trick.firstCard;
            ArrayList<Card> sameSuitCards = hand.getCardsOfSuit(opponent.getSuit());
//            If has the suit
            if (!sameSuitCards.isEmpty()) {
//                If can win
                if (sameSuitCards.get(0).getCardValue() > opponent.getCardValue()) {
                    for(int i = sameSuitCards.size()-1; i >= 0; i--) {
                        if(sameSuitCards.get(i).getCardValue() > opponent.getCardValue())
                            return sameSuitCards.get(i);
                    }
                }
//                If can not win
                    return sameSuitCards.get(sameSuitCards.size() - 1);
            }

                if (trump.equals(Trump.NoTrump) || hand.getCardsOfSuit(getSuitFromTrump(trump)).isEmpty()) {
                    int smallest = 15;
                    Card next = null;

                    ArrayList<ArrayList<Card>> suits = hand.getSuitArrays();

                    for(ArrayList<Card> suit: suits) {
                        if(!suit.isEmpty()) {
                            Card current = suit.get(suit.size() - 1);
                            if (current.getCardValue() < smallest) {
                                smallest = current.getCardValue();
                                next = current;
                            }
                        }
                    }

                    return next;
                } else {
                    return hand.getCardsOfSuit(getSuitFromTrump(trump)).get(hand.getCardsOfSuit(getSuitFromTrump(trump)).size() - 1);
                }


            }



        return null;
    }

    public Suit getSuitFromTrump(Trump trump) {
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

    public Trump getTrumpFromSuit(Suit suit) {
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


    @Override
    public boolean pickCard(GameState state) {
        return ((Card) state.getStack().get(0)).getCardValue() > 10;
    }

    private boolean isLegalBid(Bid bid, GameState gamestate) {
        if ((!gamestate.getBiddingHistory().isSouthEmpty())) {

            Bid lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            int lastLevel = lastBid.getLevel();
            int lastTrumpInt = lastBid.getTrumpInt();
            int newLevel = bid.getLevel();
            int newTrumpInt = bid.getTrumpInt();

            if (newLevel > lastLevel)
                return true;
            if ((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
                return true;

            return false;
        }
        return true;
    }

    @Override
    public Bid bid(GameState state) {
        Hand hand = state.getNorthHand();
        int hcp = hand.hcp();
        if (hcp >= 10 && hcp <= 12) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Bid(1, Trump.NoTrump), state))
                    return new Bid(1, Trump.NoTrump);
            }
            if (isLegalBid(new Bid(1, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(1, getTrumpFromSuit(hand.longestSuit()));
        }
        if (hcp >= 13 && hcp <= 15) {
            if (hand.isBalancedHand() && isLegalBid(new Bid(1, Trump.NoTrump), state))
                return new Bid(1, Trump.NoTrump);
            if (isLegalBid(new Bid(2, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(2, getTrumpFromSuit(hand.longestSuit()));
        }

        if (hcp >= 14 && hcp <= 17) {
            if (hand.isBalancedHand() && isLegalBid(new Bid(2, Trump.NoTrump), state))
                return new Bid(2, Trump.NoTrump);
            if (isLegalBid(new Bid(2, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(2, getTrumpFromSuit(hand.longestSuit()));
        }
        if (hcp >= 18 && hcp <= 23) {
            if (hand.isBalancedHand() && isLegalBid(new Bid(3, Trump.NoTrump), state))
                return new Bid(3, Trump.NoTrump);
            if (isLegalBid(new Bid(4, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(4, getTrumpFromSuit(hand.longestSuit()));
        }

        if (hcp >= 24 && hcp <= 29) {
            if (hand.isBalancedHand() && isLegalBid(new Bid(4, Trump.NoTrump), state))
                return new Bid(4, Trump.NoTrump);
            if (isLegalBid(new Bid(6, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(6, getTrumpFromSuit(hand.longestSuit()));
        }

        if (hcp >= 30) {
            if (hand.isBalancedHand() && isLegalBid(new Bid(7, Trump.NoTrump), state))
                return new Bid(7, Trump.NoTrump);
            if (isLegalBid(new Bid(7, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(7, getTrumpFromSuit(hand.longestSuit()));
        }


        return new Bid();

    }

}
