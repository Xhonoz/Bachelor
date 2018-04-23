package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

/**
 * Created by Eier on 21.04.2018.
 */

public class TurnManager {

    private Game game;

    public TurnManager(Game game){
        this.game = game;
    }

    public boolean next(){
        if(game.getGameState().isSouthTurn() )
        return true;
        return false;
    }
}
