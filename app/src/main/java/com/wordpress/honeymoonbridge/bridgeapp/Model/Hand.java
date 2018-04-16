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
                            handHeart.add(i,card);
                            return;
                        }
                    }
                    handHeart.add(i,card);
                }
            }
            if (card.getSuit() == (Suit.Spades)) {
                if (handSpade.size() < 1) {
                    handSpade.add(card);
                } else {

                    int i = 0;
                    for (; i < handSpade.size(); i++) {
                        if (card.getCardValue() >= handSpade.get(i).getCardValue()) {
                            handSpade.add(i,card);
                            return;
                        }
                    }
                    handSpade.add(i,card);
                }
            }
            if (card.getSuit() == (Suit.Diamonds)) {
                if (handDiamond.size() < 1) {
                    handDiamond.add(card);
                } else {

                    int i = 0;
                    for (; i < handDiamond.size(); i++) {
                        if (card.getCardValue() >= handDiamond.get(i).getCardValue()) {
                            handDiamond.add(i,card);
                            return;
                        }
                    }
                    handDiamond.add(i,card);
                }

            }
            if (card.getSuit() == (Suit.Clubs)) {
                if (handClub.size() < 1) {
                    handClub.add(card);
                } else {

                    int i = 0;
                    for (; i < handClub.size(); i++) {
                        if (card.getCardValue() >= handClub.get(i).getCardValue()) {
                            handClub.add(i,card);
                            return;
                        }
                    }
                    handClub.add(i,card);
                }
            }


        }

    }

    public Card removeCard(Card card){
        Card removed = null;

        if(card.getSuit().equals(Suit.Hearts)){

            for(int i = 0; i < handHeart.size(); i++){
                if(handHeart.get(0).equals(card))
                    removed = handHeart.remove(i);
            }

        }
        if(card.getSuit().equals(Suit.Clubs)){

            for(int i = 0; i < handClub.size(); i++){
                if(handClub.get(0).equals(card))
                    removed = handClub.remove(i);
            }

        }
        if(card.getSuit().equals(Suit.Spades)){

            for(int i = 0; i < handSpade.size(); i++){
                if(handSpade.get(0).equals(card))
                    removed = handSpade.remove(i);
            }

        }
        if(card.getSuit().equals(Suit.Diamonds)){

            for(int i = 0; i < handDiamond.size(); i++){
                if(handDiamond.get(0).equals(card))
                    removed = handDiamond.remove(i);
            }

        }
        
        return removed;
    }

    public ArrayList<Card> getSortedHand(Trump trump){
        //spar, hjerte, kløver, ruter
        //TODO:Skal vi flytte fargene slik at det alltid er annenhver rød og svart farge?
        ArrayList<Card> sorted = new ArrayList<>();

        if(trump.equals(Trump.NoTrump) || trump.equals(Trump.Diamonds)){
            sorted.addAll(handSpade);
            sorted.addAll(handHeart);
            sorted.addAll(handClub);
            sorted.addAll(handDiamond);
        }

        if(trump.equals(Trump.Spades)){
            sorted.addAll(handHeart);
            sorted.addAll(handClub);
            sorted.addAll(handDiamond);
            sorted.addAll(handSpade);
        }

        if(trump.equals(Trump.Hearts)){
            sorted.addAll(handSpade);
            sorted.addAll(handClub);
            sorted.addAll(handDiamond);
            sorted.addAll(handHeart);
        }

        if(trump.equals(Trump.Clubs)){
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

    public Hand clone(){

       return new Hand(handSpade,handHeart,handClub,handDiamond);

    }
}
