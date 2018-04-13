package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

/**
 * Created by Eier on 09.04.2018.
 */

public class Game {
    private GameState gamestate;
    private AIPlayer AI;

    public Game(boolean isSouthTurn, AIPlayer aiPlayer){
        gamestate = new GameState(isSouthTurn);
        AI = aiPlayer;

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

    public Card UIPickCard(boolean first){
        if(gamestate.getPhase().equals(Phase.PICKING) && gamestate.isSouthTurn()){
            Card c = PickCard(Player.SOUTH, first);
            //TODO: Should later be a seperate thread
            AITakesTurnPicking();
            return c;
        }
        return null;
    }

    public Card PickCard(Player player, boolean first){

        Card picked = null;
        if(!gamestate.getStack().isEmpty()) {
            Card fi = popTopCard();
            Card se = popTopCard();
            if (player == Player.NORTH) {
                gamestate.getNorth26Cards().add(fi);
                gamestate.getNorth26Cards().add(se);
                gamestate.getNorthChoseFirst().add(first);
                if (first) {
                    picked = fi;
                    gamestate.getNorthHand().add(fi);
                }
                else {
                    picked = se;
                    gamestate.getNorthHand().add(se);
                }
            }
            if (player == Player.SOUTH) {
                gamestate.getSouth26Cards().add(fi);
                gamestate.getSouth26Cards().add(se);
                gamestate.getSouthChoseFirst().add(first);
                if (first) {
                    picked = fi;
                    gamestate.getSouthHand().add(fi);
                }
                else {
                    picked = se;
                    gamestate.getSouthHand().add(se);
                }
            }
        } else{
            //TODO:Husk å endre hvis spilleren har huket av i settings at bidding ikke skal være med da går vi rett til spille fasen
            gamestate.setPhase(Phase.BIDDING);
        }

        return picked;
    }

    public GameState getGameState() {
        return gamestate;
    }

    private void AITakesTurnPicking(){
        boolean first = AI.pickCard(gamestate);
        PickCard(Player.SOUTH,first);
    }
}
