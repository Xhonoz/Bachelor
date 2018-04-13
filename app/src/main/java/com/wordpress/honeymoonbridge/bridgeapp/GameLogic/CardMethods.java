package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Carmen on 11.04.2018.
 */

public class CardMethods {

    public static void sort(ArrayList<Card> cards){
        ArrayList<Card> tempClubs = new ArrayList<>();
        ArrayList<Card> tempSpade = new ArrayList<>();
        ArrayList<Card> tempHeart = new ArrayList<>();
        ArrayList<Card> tempDiamond = new ArrayList<>();
        ArrayList<Card> temp = new ArrayList<>();
        if(!cards.isEmpty()) {
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getSuit().equals(Suit.Clubs))
                    tempClubs.add(cards.get(i));
                if(cards.get(i).getSuit().equals(Suit.Diamonds))
                    tempDiamond.add(cards.get(i));
                if(cards.get(i).getSuit().equals(Suit.Hearts))
                    tempHeart.add(cards.get(i));
                if(cards.get(i).getSuit().equals(Suit.Spades))
                    tempSpade.add(cards.get(i));

            }
            Collections.sort(tempClubs);
            Collections.sort(tempDiamond);
            Collections.sort(tempHeart);
            Collections.sort(tempSpade);

            cards.clear();
            cards.addAll(tempSpade);
            cards.addAll(tempHeart);
            cards.addAll(tempClubs);
            cards.addAll(tempDiamond);



        }


    }
}
