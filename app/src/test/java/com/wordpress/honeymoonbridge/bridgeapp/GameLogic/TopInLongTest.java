package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Carmen on 17.04.2018.
 */
public class TopInLongTest {

    GameState gm;

    @Before
    public void setupGameState(){

        gm = new GameState(false);

        Hand northHand = new Hand();

        northHand.addCard( new Card(Suit.Spades, 2));
        northHand.addCard( new Card(Suit.Spades, 9));
        northHand.addCard( new Card(Suit.Spades, 11));
        northHand.addCard( new Card(Suit.Clubs, 13));
        northHand.addCard( new Card(Suit.Clubs, 14));

        gm.setNorthHand(northHand);
    }

    @Test
    public void playCardTest(){
        AIPlayer topInLong = new TopInLong();

        for(int i = 0; i < 5; i++) {
            Card c = topInLong.playCard(gm);
            System.out.println(c);
            gm.getTricks().add(new Trick(Player.NORTH, c, c));
            gm.getNorthHand().removeCard(c);

        }



    }

}