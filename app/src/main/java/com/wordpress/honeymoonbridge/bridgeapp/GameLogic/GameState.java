package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

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
        this.northHand = new ArrayList<>();
        this.southHand = new ArrayList<>();
        this.stack = new CardStack();
    }

    public boolean isSouthTurn() {
        return isSouthTurn;
    }

<<<<<<< HEAD
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

    public ArrayList<Card> getNorthHand() {
        return northHand;
    }

    public ArrayList<Card> getSouthHand() {
        return southHand;
    }

    public CardStack getStack() {
        return stack;
    }
=======
>>>>>>> 367cd807f9392408ade231f6e83b99563a23a488
}
