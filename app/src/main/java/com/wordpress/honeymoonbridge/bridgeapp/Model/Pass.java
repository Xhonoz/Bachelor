package com.wordpress.honeymoonbridge.bridgeapp.Model;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;

/**
 * Created by Carmen on 25.04.2018.
 */

public class Pass extends Bid {

    public Pass(Player player){
        super(player);
    }

    @Override
    public String toString() {
        return "Pass";
    }
}
