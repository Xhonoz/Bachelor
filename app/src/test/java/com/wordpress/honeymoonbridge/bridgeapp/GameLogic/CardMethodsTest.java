package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Carmen on 12.04.2018.
 */
public class CardMethodsTest {
    ArrayList<Card> cardList;
    CardStack hand;
    ArrayList<Card> cardSorted;
    @Before
    public void setup(){
        cardList = new ArrayList<>();
        hand = new CardStack();
        cardList = hand.hand();
        cardSorted = new ArrayList<>();
        cardSorted = cardList;
    }

    @Test
    public void sortHandTest(){
        for(int i = 0; i < cardList.size(); i++) {
            System.out.println("Farge: " + cardList.get(i).getSuit() + cardList.get(i).getCardValue());
        }
        System.out.println();
        for(int i = 0; i < cardSorted.size(); i++) {
            System.out.println("Farge: " + cardSorted.get(i).getSuit() + cardSorted.get(i).getCardValue());
        }
        assertTrue(cardList.size()==13);

        System.out.println();

        System.out.println();

        assertTrue(cardSorted.size() == 13);
        CardMethods.sort(cardSorted);


        for(int i = 0; i < cardSorted.size(); i++) {
            System.out.println("Farge: " + cardSorted.get(i).getSuit() + cardSorted.get(i).getCardValue());
        }

    }




}