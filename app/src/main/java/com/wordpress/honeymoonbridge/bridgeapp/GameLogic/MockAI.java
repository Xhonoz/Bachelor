package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;

/**
 * Created by Carmen on 11.04.2018.
 */

public class MockAI implements AIPlayer {



    @Override
    public int playCard(GameState state) {
        return 0;
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
