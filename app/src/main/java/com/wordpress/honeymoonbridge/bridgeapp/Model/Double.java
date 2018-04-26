package com.wordpress.honeymoonbridge.bridgeapp.Model;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;

/**
 * Created by Carmen on 25.04.2018.
 */

public class Double extends Bid {

    public Double(Player player){
        super(player);
    }

    @Override
    public String toString() {
        return "X";
    }


}
