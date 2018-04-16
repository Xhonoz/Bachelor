package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

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
