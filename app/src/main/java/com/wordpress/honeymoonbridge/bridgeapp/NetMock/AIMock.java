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
        Bid lastBid = biddingHistory.getLastSouthBid();

        Log.i("AIMock", lastBid.getInfo());

        int level = lastBid.getLevel();
        int trumpInt = lastBid.getTrumpInt();
        if(lastBid.isPass())
            return new Bid();
        if(lastBid.isDouble())
            return new Bid(lastBid, false, true);
        if(lastBid.isRedouble())
            return new Bid();
        if(lastBid.getLevel() == 3)
            return new Bid(lastBid, true, false);

        int newTrump = trumpInt + 1;
        int newLevel = level;
        if(newTrump > 5) {
            newTrump = 1;
            newLevel = level + 1;
        }


        return new Bid(newLevel, newTrump);

    }
}
