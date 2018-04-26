package com.wordpress.honeymoonbridge.bridgeapp.Model;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;

/**
 * Created by Carmen on 25.04.2018.
 */

public abstract class Bid {
   private Player player;

   public Bid(Player player){
       this.player = player;
   }

   public abstract String toString();


    public Player getPlayer() {
        return player;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }
}


