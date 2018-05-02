package com.wordpress.honeymoonbridge.bridgeapp.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Carmen on 13.04.2018.
 */

public class Hand {
    private ArrayList<Card> handClub;
    private ArrayList<Card> handSpade;
    private ArrayList<Card> handDiamond;
    private ArrayList<Card> handHeart;

    public Hand() {
        handClub = new ArrayList<>();
        handSpade = new ArrayList<>();
        handDiamond = new ArrayList<>();
        handHeart = new ArrayList<>();
    }

    public ArrayList<ArrayList<Card>> getSuitArrays(){

        ArrayList<ArrayList<Card>> a =  new ArrayList<ArrayList<Card>>();
        a.add(handClub);
        a.add(handDiamond);
        a.add(handHeart);
        a.add(handSpade);
        return a;
    }

    public Hand(ArrayList<Card> handClub, ArrayList<Card> handSpade, ArrayList<Card> handDiamond, ArrayList<Card> handHeart) {
        this.handClub = handClub;
        this.handSpade = handSpade;
        this.handDiamond = handDiamond;
        this.handHeart = handHeart;
    }

    public void addCard(Card card) {

        if (card != null) {
            if (card.getSuit() == (Suit.Hearts)) {
                if (handHeart.size() < 1) {
                    handHeart.add(card);
                } else {

                    int i = 0;
                    for (; i < handHeart.size(); i++) {
                        if (card.getCardValue() >= handHeart.get(i).getCardValue()) {
                            handHeart.add(i, card);
                            return;
                        }
                    }
                    handHeart.add(i, card);
                }
            }
            if (card.getSuit() == (Suit.Spades)) {
                if (handSpade.size() < 1) {
                    handSpade.add(card);
                } else {

                    int i = 0;
                    for (; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                            handSpade.add(i, card);
                            return;
                        }
                    }
                    handSpade.add(i, card);
                }
            }
            if (card.getSuit() == (Suit.Diamonds)) {
                if (handDiamond.size() < 1) {
                    handDiamond.add(card);
                } else {

                    int i = 0;
                    for (; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            handDiamond.add(i, card);
                            return;
                        }
                    }
                    handDiamond.add(i, card);
                }

            }
            if (card.getSuit() == (Suit.Clubs)) {
                if (handClub.size() < 1) {
                    handClub.add(card);
                } else {

                    int i = 0;
                    for (; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                            handClub.add(i, card);
                            return;
                        }
                    }
                    handClub.add(i, card);
                }
            }


        }

    }

    public Card removeCard(Card card) {

        Log.i("Hand", "Trying to remove " + card);

        if (card.getSuit().equals(Suit.Hearts)) {

            if(handHeart.remove(card))
                return card;

        }
        if (card.getSuit().equals(Suit.Clubs)) {

           if(handClub.remove(card))
               return card;

        }
        if (card.getSuit().equals(Suit.Spades)) {

            if(handSpade.remove(card))
                return card;

        }
        if (card.getSuit().equals(Suit.Diamonds)) {

          if(handDiamond.remove(card))
                  return card;

        }
        return null;

    }

    public ArrayList<Card> getSortedHand(Trump trump) {
        //spar, hjerte, kløver, ruter
        //TODO:Skal vi flytte fargene slik at det alltid er annenhver rød og svart farge?
        ArrayList<Card> sorted = new ArrayList<>();

        if (trump.equals(Trump.NoTrump) || trump.equals(Trump.Diamonds)) {
            sorted.addAll(handSpade);
            sorted.addAll(handHeart);
            sorted.addAll(handClub);
            sorted.addAll(handDiamond);
        }

        if (trump.equals(Trump.Spades)) {
            sorted.addAll(handHeart);
            sorted.addAll(handClub);
            sorted.addAll(handDiamond);
            sorted.addAll(handSpade);
        }

        if (trump.equals(Trump.Hearts)) {
            sorted.addAll(handSpade);
            sorted.addAll(handClub);
            sorted.addAll(handDiamond);
            sorted.addAll(handHeart);
        }

        if (trump.equals(Trump.Clubs)) {
            sorted.addAll(handSpade);
            sorted.addAll(handHeart);
            sorted.addAll(handDiamond);
            sorted.addAll(handClub);
        }


        return sorted;
    }

    public ArrayList<Card> getHandClub() {
        return handClub;
    }

    public ArrayList<Card> getHandSpade() {
        return handSpade;
    }

    public ArrayList<Card> getHandDiamond() {
        return handDiamond;
    }

    public ArrayList<Card> getHandHeart() {
        return handHeart;
    }

    public Hand clone() {

        return new Hand(handSpade, handHeart, handClub, handDiamond);

    }

    public int getSize() {
        return handClub.size() + handSpade.size() + handDiamond.size() + handHeart.size();
    }

    public int getNewIndex(Card card, Trump trump) {
        int index = 0;
        switch (trump) {
            case NoTrump:
                if (card.getSuit().equals(Suit.Spades)) {
                    for (int i = 0; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handSpade.size();
                }
                index += handSpade.size();
                if (card.getSuit().equals(Suit.Hearts)) {
                    for (int i = 0; i < handHeart.size(); i++) {
                        if (card.getCardValue() >= handHeart.get(i).getCardValue()) {
                            return index + i;
                        }

                    }
                    return index + handHeart.size();

                }
                index += handHeart.size();
                if (card.getSuit().equals(Suit.Clubs)) {
                    for (int i = 0; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                           return index + i;
                        }
                    }
                    return index + handClub.size();
                }

                index += handClub.size();
                if (card.getSuit().equals(Suit.Diamonds)) {
                    for (int i = 0; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                        return index + handDiamond.size();
                }

                return index;

            case Spades:
                if (card.getSuit().equals(Suit.Hearts)) {
                    for (int i = 0; i < handHeart.size(); i++) {
                        if (card.getCardValue() >= handHeart.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handHeart.size();
                }
                index += handHeart.size();
                if (card.getSuit().equals(Suit.Clubs)) {
                    for (int i = 0; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handClub.size();
                }
                index += handClub.size();
                if (card.getSuit().equals(Suit.Diamonds)) {
                    for (int i = 0; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            return index += i;
                        }
                    }
                    return index + handDiamond.size();
                }
                index += handDiamond.size();
                if (card.getSuit().equals(Suit.Spades)) {
                    for (int i = 0; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                            index += i;
                        }
                        return index + i;
                    }

                    return index + handSpade.size();
                }
                return index;

            case Hearts:
                if (card.getSuit().equals(Suit.Spades)) {
                    for (int i = 0; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handSpade.size();
                }
                index += handSpade.size();
                if (card.getSuit().equals(Suit.Clubs)) {
                    for (int i = 0; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handClub.size();
                }
                index += handClub.size();
                if (card.getSuit().equals(Suit.Diamonds)) {
                    for (int i = 0; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            return index + i;
                        }
                    }

                    return index + handDiamond.size();
                }
                index += handDiamond.size();
                if (card.getSuit().equals(Suit.Hearts)) {
                    for (int i = 0; i < handHeart.size(); i++) {
                        if (card.getCardValue() >= handHeart.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handHeart.size();
                }
                return index;

            case Clubs:
                if (card.getSuit().equals(Suit.Spades)) {
                    for (int i = 0; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                           return index + i;
                        }
                    }
                    return index + handSpade.size();
                }
                index += handSpade.size();
                if (card.getSuit().equals(Suit.Hearts)) {
                    for (int i = 0; i < handHeart.size(); i++) {
                        if (card.getCardValue() >= handHeart.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handHeart.size();
                }
                index += handHeart.size();
                if (card.getSuit().equals(Suit.Diamonds)) {
                    for (int i = 0; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handDiamond.size();
                }
                index += handDiamond.size();
                if (card.getSuit().equals(Suit.Clubs)) {
                    for (int i = 0; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handClub.size();
                }
                return index;
            case Diamonds:
                if (card.getSuit().equals(Suit.Spades)) {
                    for (int i = 0; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handSpade.size();
                }
                index += handSpade.size();
                if (card.getSuit().equals(Suit.Hearts)) {
                    for (int i = 0; i < handHeart.size(); i++) {
                        if (card.getCardValue() >= handHeart.get(i).getCardValue()) {
                            return index + i;
                        }
                    }

                    return index + handHeart.size();
                }
                index += handHeart.size();
                if (card.getSuit().equals(Suit.Clubs)) {
                    for (int i = 0; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handClub.size();
                }

                index += handClub.size();
                if (card.getSuit().equals(Suit.Diamonds)) {
                    for (int i = 0; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            return index + i;
                        }
                    }
                    return index + handDiamond.size();
                }
            default:
                return 0;
        }
    }

    public ArrayList<Card> getCardsOfSuit(Suit suit){
        switch (suit){
            case Clubs:
                return handClub;
            case Diamonds:
                return handDiamond;
            case Hearts:
                return handHeart;
            case Spades:
                return handSpade;
        }
        return null;
    }

    public boolean isBalancedHand(){
        int doubeltonCount = 0;
        if(handClub.size() == 1 || handHeart.size() == 1 || handSpade.size() == 1 || handDiamond.size() == 1)
            return false;
        if(handDiamond.size()==2)
            doubeltonCount++;
        if(handSpade.size() == 2)
            doubeltonCount++;
        if(handHeart.size() == 2)
            doubeltonCount++;

        return doubeltonCount > 1;

    }

    public int hcp(){
        int hcp = 0;
        for(int i = 0; i < handSpade.size(); i++){
            if(handSpade.get(i).getCardValue() == 11)
                hcp += 1;
            if(handSpade.get(i).getCardValue() == 12)
                hcp += 2;
            if(handSpade.get(i).getCardValue() == 13)
                hcp += 3;
            if(handSpade.get(i).getCardValue() == 14)
                hcp += 4;
        }

        for(int i = 0; i < handHeart.size(); i++){
            if(handHeart.get(i).getCardValue() == 11)
                hcp += 1;
            if(handHeart.get(i).getCardValue() == 12)
                hcp += 2;
            if(handHeart.get(i).getCardValue() == 13)
                hcp += 3;
            if(handHeart.get(i).getCardValue() == 14)
                hcp += 4;
        }

        for(int i = 0; i < handClub.size(); i++){
            if(handClub.get(i).getCardValue() == 11)
                hcp += 1;
            if(handClub.get(i).getCardValue() == 12)
                hcp += 2;
            if(handClub.get(i).getCardValue() == 13)
                hcp += 3;
            if(handClub.get(i).getCardValue() == 14)
                hcp += 4;
        }

        for(int i = 0; i < handDiamond.size(); i++){
            if(handDiamond.get(i).getCardValue() == 11)
                hcp += 1;
            if(handDiamond.get(i).getCardValue() == 12)
                hcp += 2;
            if(handDiamond.get(i).getCardValue() == 13)
                hcp += 3;
            if(handDiamond.get(i).getCardValue() == 14)
                hcp += 4;
        }
return hcp;
    }

    public Suit longestSuit(){

        int longest = -1;
        Suit suit = null;
       ArrayList<ArrayList<Card>> suits = getSuitArrays();
       for(int i = 0; i < suits.size(); i++){
           if(suits.size() > longest){
               longest = suits.size();
               suit = Suit.values()[i];
           }
       }

        return suit;
    }

    @Override
    public String toString() {
        String str = "";
        ArrayList<Card> cards = getCardsOfSuit(Suit.Clubs);
        for(Card c : cards)
            str += (c + " ");
        str += "\n";
        cards = getCardsOfSuit(Suit.Diamonds);
        for(Card c : cards)
            str += (c + " ");
        str += "\n";
        cards = getCardsOfSuit(Suit.Hearts);
        for(Card c : cards)
            str += (c + " ");
        str += "\n";
        cards = getCardsOfSuit(Suit.Spades);
        for(Card c : cards)
            str += (c + " ");
        str += "\n";
        return str;
    }
}
