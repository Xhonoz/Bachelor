package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;

import java.util.ArrayList;

/**
 * Created by Eier on 09.04.2018.
 */

public class GameState {

    private boolean isSouthTurn;
    private Phase phase;

    private ArrayList<Card> north26Cards;
    private ArrayList<Boolean> northChoseFirst;
    private ArrayList<Card> south26Cards;
    private ArrayList<Boolean> southChoseFirst;

    private ArrayList<Bid> northBid;
    private ArrayList<Bid> southBid;

    private ArrayList<Trick> tricks;

    private Hand northHand;
    private Hand southHand;


    private CardStack stack;

    public GameState(boolean isSouthTurn) {
        this.isSouthTurn = isSouthTurn;
        this.north26Cards = new ArrayList<>();
        this.northChoseFirst = new ArrayList<>();
        this.south26Cards = new ArrayList<>();
        this.southChoseFirst = new ArrayList<>();
        this.northBid = new ArrayList<>();
        this.southBid = new ArrayList<>();
        this.tricks = new ArrayList<>();
        this.northHand = new Hand();
        this.southHand = new Hand();
        this.stack = new CardStack();
        phase = Phase.PICKING;
    }

    public boolean isSouthTurn() {
        return isSouthTurn;
    }

    public ArrayList<Card> getNorth26Cards() {
        return north26Cards;
    }

    public ArrayList<Boolean> getNorthChoseFirst() {
        return northChoseFirst;
    }

    public ArrayList<Card> getSouth26Cards() {
        return south26Cards;
    }

    public ArrayList<Boolean> getSouthChoseFirst() {
        return southChoseFirst;
    }

    public ArrayList<Bid> getNorthBid() {
        return northBid;
    }

    public ArrayList<Bid> getSouthBid() {
        return southBid;
    }

    public ArrayList<Trick> getTricks() {
        return tricks;
    }

    public Hand getNorthHand() {
        return northHand;
    }

    public Hand getSouthHand() {
        return southHand;
    }

    public CardStack getStack() {
        return stack;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
