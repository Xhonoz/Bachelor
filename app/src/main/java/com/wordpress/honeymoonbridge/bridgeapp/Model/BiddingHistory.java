package com.wordpress.honeymoonbridge.bridgeapp.Model;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;

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

    public Bid getLastBid(Player player){
        if(player == Player.NORTH && !North.isEmpty())
            return North.get(North.size()-1);
        if(player == Player.SOUTH && !South.isEmpty())
            return South.get(South.size()-1);
        return null;
    }

    public ArrayList<Bid> getAllBids(Player dealer){
        if(dealer == Player.NORTH){
           return merge(North, South);
        }else if( dealer == Player.SOUTH){
            return merge(South,North);
        }
        return null;
    }

    private ArrayList merge(ArrayList<Bid> a, ArrayList<Bid> b) {
        ArrayList<Bid> merged = new ArrayList<Bid>();
        int j = 0;
        for(int i = 0; i < a.size(); i++){
            merged.add(a.get(i));
            if(j < b.size())
                merged.add(b.get(j));
            j++;
        }
        return merged;
    }


    public boolean isNorthEmpty(){
        return North.isEmpty();
    }

    public boolean isSouthEmpty(){
        return South.isEmpty();
    }

    public Bid getLastSouthBid(){
        if(South.isEmpty())
            return null;
        return South.get(getSouth().size() - 1);
    }

    public Bid getLastNorthBid(){
        return North.get(getNorth().size() - 1);
    }


}
