package com.wordpress.honeymoonbridge.bridgeapp.AI;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GameState;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Trick;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Double;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Pass;
import com.wordpress.honeymoonbridge.bridgeapp.Model.ReDouble;
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

        if(bid instanceof Pass)
            return true;

        Player player = bid.getPlayer();

        if ((player == Player.SOUTH && !gamestate.getBiddingHistory().isNorthEmpty())
                || player == Player.NORTH && !gamestate.getBiddingHistory().isSouthEmpty()) {

            Bid lastBid = null;
            if (player == Player.SOUTH)
                lastBid = gamestate.getBiddingHistory().getLastNorthBid();
            if (player == Player.NORTH)
                lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            Contract lastContract = null;

            if(lastBid instanceof Pass)
                return true;

            if (lastBid instanceof Contract) {
                if (bid instanceof Double)
                    return true;
                lastContract = (Contract) lastBid;

            }
            if (lastBid instanceof Double) {
                if (bid instanceof ReDouble)
                    return true;
                if (player == Player.SOUTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getLastSouthBid();
                if (player == Player.NORTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getLastNorthBid();
            }

            if (lastBid instanceof ReDouble) {
                if (player == Player.SOUTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getNorth().get(gamestate.getBiddingHistory().getNorth().size() - 2);
                if (player == Player.NORTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getSouth().get(gamestate.getBiddingHistory().getSouth().size() - 2);
            }

            int lastLevel = lastContract.getTricks();
            int lastTrumpInt = lastContract.getTrump().ordinal();

            if(bid instanceof Contract) {
                int newLevel = ((Contract)bid).getTricks();
                int newTrumpInt = ((Contract)bid).getTrump().ordinal();

                    if (newLevel > lastLevel)
                        return true;
                if ((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
                    return true;

            }

            return false;
        }

        return true;
    }


    @Override
    public Bid bid(GameState state) {
        Hand hand = state.getNorthHand();
        int hcp = hand.hcp();
        if (hcp >= 10 && hcp <= 13) {
            if (hand.isBalancedHand()) {
               return new Pass(Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 1, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }
        if (hcp >= 14 && hcp <= 17) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Contract(Trump.NoTrump, 1, Player.NORTH), state))
                    return new Contract(Trump.NoTrump, 1 , Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 2, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }

        if (hcp >= 18 && hcp <= 21) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Contract(Trump.NoTrump, 2, Player.NORTH), state))
                    return new Contract(Trump.NoTrump, 2 , Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 3, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }

        if (hcp >= 22 && hcp <= 27) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Contract(Trump.NoTrump, 3, Player.NORTH), state))
                    return new Contract(Trump.NoTrump, 3 , Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 4, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }

        if (hcp >= 28 && hcp <= 31) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Contract(Trump.NoTrump, 4, Player.NORTH), state))
                    return new Contract(Trump.NoTrump, 4 , Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 6, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }

        if (hcp >= 32 && hcp <= 36) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Contract(Trump.NoTrump, 5, Player.NORTH), state))
                    return new Contract(Trump.NoTrump, 5 , Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 6, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }

        if (hcp >= 37) {
            if (hand.isBalancedHand()) {
                if (isLegalBid(new Contract(Trump.NoTrump, 7, Player.NORTH), state))
                    return new Contract(Trump.NoTrump, 7 , Player.NORTH);
            }
            Contract bid = new Contract(getTrumpFromSuit(hand.longestSuit()), 7, Player.NORTH);
            if (isLegalBid(bid, state))
                return bid;
        }


        return new Pass(Player.NORTH);

    }

}
