package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

/**
 * Created by Carmen on 11.04.2018.
 */

public class MockAI implements AIPlayer {



    @Override
    public Card playCard(GameState state) {
        return null;
    }

    @Override
    public boolean pickCard(GameState state) {
        return true;
    }

    @Override
    public Bid bid(GameState state) {
        return null;
    }
}
