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
            longest = handSpade.size();
            color = Suit.Spades;
            if (longest <= handHeart.size()) {

                if(longest == handHeart.size()){
                    for(int i = 0; i < handHeart.size(); i++){
                        if(handHeart.get(i).getCardValue() > handSpade.get(i).getCardValue()){
                            longest = handHeart.size();
                            color = Suit.Hearts;
                            break;
                        }
                    }

                }else {
                    longest = handHeart.size();
                    color = Suit.Hearts;
                }
            }

            if (longest <= handDiamond.size()) {
                if(longest == handDiamond.size()){
                    for(int i = 0; i < handDiamond.size(); i++){
                        if(handDiamond.get(i).getCardValue() > handHeart.get(i).getCardValue()){
                            longest = handDiamond.size();
                            color = Suit.Diamonds;
                            break;
                        }
                    }

                }else {
                    longest = handDiamond.size();
                    color = Suit.Diamonds;
                }
            }
            if (longest <= handClub.size()) {
                if(longest == handHeart.size()){
                    for(int i = 0; i < handHeart.size(); i++){
                        if(handHeart.get(i).getCardValue() > handSpade.get(i).getCardValue()){
                            longest = handHeart.size();
                            color = Suit.Hearts;
                            break;
                        }
                    }

                }else {
                    longest = handHeart.size();
                    color = Suit.Hearts;
                }
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
            if(trump.equals(Trump.NoTrump) || hand.getCardsOfSuit(getSuitFromTrump(trump)).isEmpty()) {
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
            }else{
                return hand.getCardsOfSuit(getSuitFromTrump(trump)).get(hand.getCardsOfSuit(getSuitFromTrump(trump)).size()-1);
            }


        }
    }


        return null;
}

    public Suit getSuitFromTrump(Trump trump){
        switch(trump){
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

    public Trump getTrumpFromSuit(Suit suit){
        switch(suit) {
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

    private boolean isLegalBid(Bid bid, GameState gamestate){
        if((!gamestate.getBiddingHistory().isSouthEmpty())) {

                Bid lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            int lastLevel = lastBid.getLevel();
            int lastTrumpInt = lastBid.getTrumpInt();
            int newLevel = bid.getLevel();
            int newTrumpInt = bid.getTrumpInt();

            if(newLevel > lastLevel)
                return true;
            if((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
                return true;

            return false;
        }
        return true;
    }

    @Override
    public Bid bid(GameState state) {
         Hand hand = state.getNorthHand();
         int hcp = hand.hcp();
         if(hcp >= 10 && hcp <= 12){
             if(hand.isBalancedHand()) {
                 if(isLegalBid(new Bid(1, Trump.NoTrump),state))
                 return new Bid(1, Trump.NoTrump);
             }
             if(isLegalBid(new Bid(1, getTrumpFromSuit(hand.longestSuit())), state))
             return new Bid(1, getTrumpFromSuit(hand.longestSuit()));
         }
         if(hcp >= 13 && hcp <= 15){
             if(hand.isBalancedHand() && isLegalBid(new Bid(1, Trump.NoTrump),state))
             return new Bid(1, Trump.NoTrump);
             if(isLegalBid(new Bid(2, getTrumpFromSuit(hand.longestSuit())), state))
             return new Bid(2, getTrumpFromSuit(hand.longestSuit()));
         }

        if(hcp >= 14 && hcp <= 17){
            if(hand.isBalancedHand() && isLegalBid(new Bid(2, Trump.NoTrump),state))
                return new Bid(2, Trump.NoTrump);
            if(isLegalBid(new Bid(2, getTrumpFromSuit(hand.longestSuit())),state))
                return new Bid(2, getTrumpFromSuit(hand.longestSuit()));
        }
        if(hcp >= 18 &&  hcp <=23 ){
            if(hand.isBalancedHand() && isLegalBid(new Bid(3, Trump.NoTrump), state))
                return new Bid(3, Trump.NoTrump);
            if(isLegalBid(new Bid(4, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(4, getTrumpFromSuit(hand.longestSuit()));
        }

        if(hcp >= 24 && hcp <=29){
            if(hand.isBalancedHand() && isLegalBid(new Bid(4, Trump.NoTrump), state))
                return new Bid(4, Trump.NoTrump);
            if(isLegalBid(new Bid(6, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(6, getTrumpFromSuit(hand.longestSuit()));
        }

        if(hcp >= 30){
            if(hand.isBalancedHand() && isLegalBid(new Bid(7, Trump.NoTrump), state))
                return new Bid(7, Trump.NoTrump);
            if(isLegalBid(new Bid(7, getTrumpFromSuit(hand.longestSuit())), state))
                return new Bid(7, getTrumpFromSuit(hand.longestSuit()));
        }



        return new Bid();

    }

}
