package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;

/**
 * Created by Eier on 09.04.2018.
 */

public interface AIPlayer {

    int playCard(GameState state);

    boolean pickCard(GameState state);

    Bid bid(GameState state);
}
