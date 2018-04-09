package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

import java.util.ArrayList;

/**
 * Created by Eier on 09.04.2018.
 */

public class GameState {

    private boolean isSouthTurn;

    private ArrayList<Card> north26Cards;
    private ArrayList<Boolean> northChoseFirst;
    private ArrayList<Card> south26Cards;
    private ArrayList<Boolean> southChoseFirst;

    private ArrayList<Bid> northBid;
    private ArrayList<Bid> southBid;

    private ArrayList<Trick> tricks;

    private ArrayList<Card> northHand;
    private ArrayList<Card> southHand;

    public GameState(boolean isSouthTurn) {
        this.isSouthTurn = isSouthTurn;
        this.north26Cards = new ArrayList<Card>();
        this.northChoseFirst = northChoseFirst;
        this.south26Cards = south26Cards;
        this.southChoseFirst = southChoseFirst;
        this.northBid = northBid;
        this.southBid = southBid;
        this.tricks = tricks;
        this.northHand = northHand;
        this.southHand = southHand;
    }

}
