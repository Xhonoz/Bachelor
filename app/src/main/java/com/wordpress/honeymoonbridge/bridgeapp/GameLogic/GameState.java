package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.BiddingHistory;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;

import java.util.ArrayList;

/**
 * Created by Eier on 09.04.2018.
 */

public class GameState {

    private boolean isSouthTurn;
    private Phase phase;
    private Trump trump;
    private Player dealer;

    private ArrayList<Card> north26Cards;
    private ArrayList<Boolean> northChoseFirst;
    private ArrayList<Card> south26Cards;
    private ArrayList<Boolean> southChoseFirst;

    private BiddingHistory biddingHistory;

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
        this.biddingHistory = new BiddingHistory();
        this.tricks = new ArrayList<>();
        this.northHand = new Hand();
        this.southHand = new Hand();
        this.stack = new CardStack();
        phase = Phase.PICKING;
        trump = Trump.NoTrump;

        if(isSouthTurn)
            dealer = Player.NORTH;
        else
            dealer = Player.SOUTH;
    }

    public Player getDealer() {
        return dealer;
    }

    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    public void setSouthTurn(boolean southTurn) {
        isSouthTurn = southTurn;
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

    public BiddingHistory getBiddingHistory() {
        return biddingHistory;
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

    public void setNorth26Cards(ArrayList<Card> north26Cards) {
        this.north26Cards = north26Cards;
    }

    public void setNorthChoseFirst(ArrayList<Boolean> northChoseFirst) {
        this.northChoseFirst = northChoseFirst;
    }

    public void setSouth26Cards(ArrayList<Card> south26Cards) {
        this.south26Cards = south26Cards;
    }

    public void setSouthChoseFirst(ArrayList<Boolean> southChoseFirst) {
        this.southChoseFirst = southChoseFirst;
    }

    public void setBiddingHistory(BiddingHistory biddingHistory) {
        this.biddingHistory = biddingHistory;
    }

    public void setTricks(ArrayList<Trick> tricks) {
        this.tricks = tricks;
    }

    public void setNorthHand(Hand northHand) {
        this.northHand = northHand;
    }

    public void setSouthHand(Hand southHand) {
        this.southHand = southHand;
    }

    public void setStack(CardStack stack) {
        this.stack = stack;
    }

    public Trump getTrump() {
        return trump;
    }

    public void setTrump(Trump trump) {
        this.trump = trump;
    }
}
