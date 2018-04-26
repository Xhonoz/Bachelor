package com.wordpress.honeymoonbridge.bridgeapp.AI;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GameState;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

/**
 * Created by Eier on 09.04.2018.
 */

public interface AIPlayer {

    Card playCard(GameState state);

    boolean pickCard(GameState state);

    Bid bid(GameState state);
}
