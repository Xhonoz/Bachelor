package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import android.util.Log;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Carmen on 10.04.2018.
 */
public class GameTest {

    Game game;
    Card firstCard;
    Card fourthCard;
    GameState gamestate;

    @Before
    public void setUp(){
        game = new Game();
        firstCard = new Card(Suit.Clubs, 2);
        fourthCard = new Card(Suit.Clubs, 5);
        gamestate = game.getGameState();

    }
    @Test
    public void correctTopCard() {
assertTrue(firstCard.equals(game.peakTopCard()));

    }

    @Test
    public void correctCardPopped(){

        assertEquals(52, gamestate.getStack().size());
        assertTrue(firstCard.equals(game.popTopCard()));
        assertFalse(firstCard.equals(game.peakTopCard()));
        assertEquals(51, gamestate.getStack().size());
    }

    @Test
    public void RightCardToNorthHand(){
assertEquals(0,gamestate.getNorth26Cards().size());
game.PickCard(Player.NORTH, true);
assertEquals(2, gamestate.getNorth26Cards().size());
assertTrue(gamestate.getNorthHand().size()==1);
assertTrue(gamestate.getSouth26Cards().size() == 0);

assertTrue(firstCard.equals(gamestate.getNorthHand().get(0)));
        game.PickCard(Player.NORTH, false);
        assertTrue(gamestate.getNorthHand().size()==2);
        assertTrue(fourthCard.equals(gamestate.getNorthHand().get(1)));

assertTrue(gamestate.getStack().size()==48);

    }

    @Test
    public void RightCardToSouthHand(){
assertEquals(0, gamestate.getSouth26Cards().size());
game.PickCard(Player.SOUTH, true);
assertEquals(2, gamestate.getSouth26Cards().size());
assertEquals(0, gamestate.getNorth26Cards().size());
        assertTrue(gamestate.getSouthHand().size()==1);
        assertTrue(firstCard.equals(gamestate.getSouthHand().get(0)));
        game.PickCard(Player.SOUTH, false);
        assertTrue(gamestate.getSouthHand().size()==2);
        assertTrue(fourthCard.equals(gamestate.getSouthHand().get(1)));

        assertTrue(gamestate.getStack().size()==48);
    }

}