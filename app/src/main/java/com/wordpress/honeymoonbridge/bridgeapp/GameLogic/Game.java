package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

/**
 * Created by Eier on 09.04.2018.
 */

public class Game {
private GameState gamestate;
private AIPlayer AI;

public Game(){
    gamestate = new GameState(true);

}

public Card peakTopCard(){
    CardStack s = gamestate.getStack();
    if(!gamestate.getStack().isEmpty())
        return (Card)s.get(0);

    return null;
}

    public Card popTopCard(){
        CardStack s = gamestate.getStack();
        if(!gamestate.getStack().isEmpty()) {
            Card c = (Card) s.get(0);
            s.remove(c);
            return c;
        }

        return null;
    }

public void PickCard(Player player, boolean first){

        Card fi = popTopCard();
        Card se = popTopCard();
    if(player == Player.NORTH) {
        gamestate.getNorth26Cards().add(fi);
        gamestate.getNorth26Cards().add(se);
        gamestate.getNorthChoseFirst().add(first);
        if (first)
            gamestate.getNorthHand().add(fi);
        else
            gamestate.getNorthHand().add(se);
    }
    if(player == Player.SOUTH) {
        gamestate.getSouth26Cards().add(fi);
        gamestate.getSouth26Cards().add(se);
        gamestate.getSouthChoseFirst().add(first);
        if (first)
            gamestate.getSouthHand().add(fi);
        else
            gamestate.getSouthHand().add(se);
    }
}

    public GameState getGamestate() {
        return gamestate;
    }
}
