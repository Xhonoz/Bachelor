package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

/**
 * Created by Eier on 09.04.2018.
 */

public class Game {
private GameState gamestate;
private AIPlayer AI;

public Card getTopCard(){
    CardStack s = gamestate.getStack();
    if(!gamestate.getStack().isEmpty())
        return (Card)s.get(0);

    return null;
}

//public void PickCard(Player player, boolean first){
//    if(player == Player.NORTH)
//
//}








}
