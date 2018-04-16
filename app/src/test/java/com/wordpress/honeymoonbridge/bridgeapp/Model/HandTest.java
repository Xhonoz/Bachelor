package com.wordpress.honeymoonbridge.bridgeapp.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Carmen on 13.04.2018.
 */
public class HandTest {
    Hand hand;
    Card card1;
    Card card2;
    Card card3;
    Card card4;

    Card card5;
    Card card6;
    Card card7;
    Card card8;

    Card card9;
    Card card10;
    Card card11;
    Card card12;

    Card card13;
    Card card14;
    Card card15;
    Card card16;

    @Before
    public void setup() {
        hand = new Hand();

        card1 = new Card(Suit.Clubs, 1);
        card2 = new Card(Suit.Clubs, 7);
        card3 = new Card(Suit.Clubs, 8);
        card4 = new Card(Suit.Clubs, 12);

        card5 = new Card(Suit.Hearts, 3);
        card6 = new Card(Suit.Hearts, 14);
        card7 = new Card(Suit.Hearts, 5);
        card8 = new Card(Suit.Hearts, 9);

        card9 = new Card(Suit.Diamonds, 11);
        card10 = new Card(Suit.Diamonds, 2);
        card11 = new Card(Suit.Diamonds, 5);
        card12 = new Card(Suit.Diamonds, 7);

        card13 = new Card(Suit.Spades, 1);
        card14 = new Card(Suit.Spades, 9);
        card15 = new Card(Suit.Spades, 10);
        card16 = new Card(Suit.Spades, 6);

    }

    @Test
    public void valueSortedClubs() {
        assertTrue(hand.getHandClub().size() == 0);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        hand.addCard(card4);

        for (int i = 0; i < hand.getHandClub().size(); i++) {
            System.out.println(hand.getHandClub().get(i).toString());
        }

        assertTrue(hand.getHandClub().size() == 4);


        assertTrue(hand.getHandClub().get(0).getCardValue() > hand.getHandClub().get(1).getCardValue());
        assertTrue(hand.getHandClub().get(1).getCardValue() > hand.getHandClub().get(2).getCardValue());
        assertTrue(hand.getHandClub().get(2).getCardValue() > hand.getHandClub().get(3).getCardValue());


    }

    @Test
    public void valueSortedHearts() {
        assertTrue(hand.getHandHeart().size() == 0);
        hand.addCard(card5);
        hand.addCard(card6);
        hand.addCard(card7);
        hand.addCard(card8);

        for (int i = 0; i < hand.getHandHeart().size(); i++) {
            System.out.println(hand.getHandHeart().get(i).toString());
        }

        assertTrue(hand.getHandHeart().size() == 4);


        assertTrue(hand.getHandHeart().get(0).getCardValue() > hand.getHandHeart().get(1).getCardValue());
        assertTrue(hand.getHandHeart().get(1).getCardValue() > hand.getHandHeart().get(2).getCardValue());
        assertTrue(hand.getHandHeart().get(2).getCardValue() > hand.getHandHeart().get(3).getCardValue());
    }

    @Test
    public void valueSortedSpades() {
        assertTrue(hand.getHandSpade().size() == 0);
        hand.addCard(card13);
        hand.addCard(card14);
        hand.addCard(card15);
        hand.addCard(card16);

        for (int i = 0; i < hand.getHandSpade().size(); i++) {
            System.out.println(hand.getHandSpade().get(i).toString());
        }

        assertTrue(hand.getHandSpade().size() == 4);


        assertTrue(hand.getHandSpade().get(0).getCardValue() > hand.getHandSpade().get(1).getCardValue());
        assertTrue(hand.getHandSpade().get(1).getCardValue() > hand.getHandSpade().get(2).getCardValue());
        assertTrue(hand.getHandSpade().get(2).getCardValue() > hand.getHandSpade().get(3).getCardValue());
    }

    @Test
    public void valueSortedDiamonds() {
        assertTrue(hand.getHandDiamond().size() == 0);
        hand.addCard(card9);
        hand.addCard(card10);
        hand.addCard(card11);
        hand.addCard(card12);

        for (int i = 0; i < hand.getHandDiamond().size(); i++) {
            System.out.println(hand.getHandDiamond().get(i).toString());
        }

        assertTrue(hand.getHandDiamond().size() == 4);


        assertTrue(hand.getHandDiamond().get(0).getCardValue() > hand.getHandDiamond().get(1).getCardValue());
        assertTrue(hand.getHandDiamond().get(1).getCardValue() > hand.getHandDiamond().get(2).getCardValue());
        assertTrue(hand.getHandDiamond().get(2).getCardValue() > hand.getHandDiamond().get(3).getCardValue());
    }

    @Test
    public void cardRemovedHeart(){
        assertTrue(hand.getHandHeart().size()==0);
        hand.removeCard(card5);
        assertTrue(hand.getHandHeart().size()==0);
        hand.addCard(card5);
        assertTrue(hand.getHandHeart().size()==1);
        assertEquals(card5, hand.removeCard(card5));
    }

    @Test
    public void cardRemovedSpade(){
        assertTrue(hand.getHandSpade().size()==0);
        hand.removeCard(card13);
        assertTrue(hand.getHandSpade().size()==0);
        hand.addCard(card13);
        assertTrue(hand.getHandSpade().size()==1);
        assertEquals(card13, hand.removeCard(card13));
    }

    @Test
    public void cardRemovedDiamond(){
        assertTrue(hand.getHandDiamond().size()==0);
        hand.removeCard(card9);
        assertTrue(hand.getHandDiamond().size()==0);
        hand.addCard(card9);
        assertTrue(hand.getHandDiamond().size()==1);
        assertEquals(card9, hand.removeCard(card9));
    }

    @Test
    public void cardRemovedCubs(){
        assertTrue(hand.getHandClub().size()==0);
        hand.removeCard(card1);
        assertTrue(hand.getHandClub().size()==0);
        hand.addCard(card1);
        assertTrue(hand.getHandClub().size()==1);
        assertEquals(card1, hand.removeCard(card1));
    }
}