package com.wordpress.honeymoonbridge.bridgeapp.NetMock;

import android.util.Log;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.BiddingHistory;

import java.util.ArrayList;

/**
 * Created by Eier on 21.03.2018.
 */

public class AIMock {

    public static Bid getBid(BiddingHistory biddingHistory){
        ArrayList<Bid> south = biddingHistory.getSouth();
        Bid lastBid = south.get(south.size() - 1);

        int level = lastBid.getLevel();
        int trumpInt = lastBid.getTrumpInt();

        int newTrump = trumpInt + 1;
        int newLevel = level;
        if(newTrump > 5) {
            newTrump = 1;
            newLevel = level + 1;
        }


        return new Bid(newLevel, newTrump);

    }
}
