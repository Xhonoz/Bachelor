package com.wordpress.honeymoonbridge.bridgeapp.Model;

import java.util.ArrayList;

/**
 * Created by Eier on 20.03.2018.
 */

public class BiddingHistory {

    private ArrayList<Bid> North;
    private ArrayList<Bid> South;


    public BiddingHistory() {
        North = new ArrayList<Bid>();
        South = new ArrayList<Bid>();
    }

    public ArrayList<Bid> getNorth() {
        return North;
    }

    public ArrayList<Bid> getSouth() {
        return South;
    }

    public boolean isNorthEmpty(){
        return North.isEmpty();
    }

    public boolean isSouthEmpty(){
        return South.isEmpty();
    }

    public Bid getLastSouthBid(){
        return South.get(getSouth().size() - 1);
    }

    public Bid getLastNorthBid(){
        return North.get(getNorth().size() - 1);
    }


}
