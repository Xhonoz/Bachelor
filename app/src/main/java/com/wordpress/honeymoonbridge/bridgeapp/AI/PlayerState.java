package com.wordpress.honeymoonbridge.bridgeapp.AI;

import android.os.Build;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class PlayerState {
    private ArrayList<Integer> Discards;
    private ArrayList<Boolean> OpponentDraw;
    private ArrayList<Integer> StartingHand;
    private ArrayList<ArrayList<Integer>> Hand;
    private int PlayerId;
    private int Tricks;

    private Stack<Integer> PlayHistory;
    private Stack<Integer> TurnHistory;
    private Stack<Integer> TricksHistory;

    private ArrayList<Bid> BidHistory;
    private Contract Contract;

    public PlayerState(int PlayerId) {
        Discards = new ArrayList<Integer>();
        OpponentDraw = new ArrayList<Boolean>();
        StartingHand = new ArrayList<Integer>();
        Hand = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 4; i++)
            Hand.add(new ArrayList<Integer>());

        this.PlayerId = PlayerId;
        Tricks = 0;

        PlayHistory = new Stack<Integer>();
        TurnHistory = new Stack<Integer>();
        TricksHistory = new Stack<Integer>();

        BidHistory = new ArrayList<Bid>();
    }


    public ArrayList<Integer> getDiscards() {
        return Discards;
    }


    public void setDiscards(ArrayList<Integer> discards) {
        Discards = discards;
    }


    public ArrayList<Boolean> getOpponentDraw() {
        return OpponentDraw;
    }


    public void setOpponentDraw(ArrayList<Boolean> opponentDraw) {
        OpponentDraw = opponentDraw;
    }


    public ArrayList<Integer> getStartingHand() {
        return StartingHand;
    }


    public void setStartingHand(ArrayList<Integer> startingHand) {
        StartingHand = startingHand;
    }


    public ArrayList<ArrayList<Integer>> getHand() {
        return Hand;
    }


    public void setHand(ArrayList<ArrayList<Integer>> hand) {
        Hand = hand;
    }


    public int getPlayerId() {
        return PlayerId;
    }


    public void setPlayerId(int playerId) {
        PlayerId = playerId;
    }


    public int getTricks() {
        return Tricks;
    }


    public void setTricks(int tricks) {
        Tricks = tricks;
    }


    public Stack<Integer> getPlayHistory() {
        return PlayHistory;
    }


    public void setPlayHistory(Stack<Integer> playHistory) {
        PlayHistory = playHistory;
    }


    public Stack<Integer> getTurnHistory() {
        return TurnHistory;
    }


    public void setTurnHistory(Stack<Integer> turnHistory) {
        TurnHistory = turnHistory;
    }


    public Stack<Integer> getTricksHistory() {
        return TricksHistory;
    }


    public void setTricksHistory(Stack<Integer> tricksHistory) {
        TricksHistory = tricksHistory;
    }


    public ArrayList<Bid> getBidHistory() {
        return BidHistory;
    }


    public void setBidHistory(ArrayList<Bid> bidHistory) {
        BidHistory = bidHistory;
    }


    public Contract getContract() {
        return Contract;
    }


    public void setContract(Contract contract) {
        Contract = contract;
    }


    public void Reset() {
        Discards.clear();
        OpponentDraw.clear();
        StartingHand.clear();
        for (ArrayList<Integer> i : Hand)
            i.clear();
        Tricks = 0;

        PlayHistory.clear();
        TurnHistory.clear();
        TricksHistory.clear();

        BidHistory.clear();
        Contract = null;
    }

    public int OpponentsHandCount() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (NoMoveYetInTrick())
                return (int) Hand.stream().flatMap(x -> x.stream()).count();

            else
                return (int) Hand.stream().flatMap(x -> x.stream()).count() - 1;

        }
        return -1;
    }

    public boolean NoMoveYetInTrick() {
        return PlayHistory.size() % 2 == 0;
    }

    public int LastPlayed() {
        return PlayHistory.peek();
    }

    public ArrayList<Integer> GenerateAllMovesLogic() {
        ArrayList<Integer> options = new ArrayList<Integer>();
        if (NoMoveYetInTrick()) // all options open
            for (ArrayList<Integer> s : Hand)
                options.addAll(s);
        else // max 3 options
            options = SecondCardOptions();
        return options;
    }

    public ArrayList<Integer> GenerateAllMovesUser() {
        ArrayList<Integer> options = new ArrayList<Integer>();
        if (NoMoveYetInTrick()) // all options open
            for (ArrayList<Integer> s : Hand)
                options.addAll(s);
        else {
            // can follow suit
            if (Hand.get(Card.SuitFromInt(LastPlayed())).size() > 0)
                options.addAll(Hand.get(Card.SuitFromInt(LastPlayed())));
            else
                for (ArrayList<Integer> s : Hand)
                    options.addAll(s);
        }
        return options;
    }

    private ArrayList<Integer> SecondCardOptions() {
        ArrayList<Integer> options = new ArrayList<Integer>();
        // Can not win by rank
        if (Hand.get(Card.SuitFromInt(LastPlayed())).size() == 0) {
            for (ArrayList<Integer> s : Hand)
                if (s.size() != 0)
                    options.add(s.get(s.size() - 1));
        }
        // Can win by rank


        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (Hand.get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).count() > 0)
                    options.add(Hand.get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).reduce((a, b) -> b).orElse(null));

                if (Hand.get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).count() == 0
                        || Hand.get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).reduce((a, b) -> b).orElse(null) != Hand.get(Card.SuitFromInt(LastPlayed())).get(Hand.get(Card.SuitFromInt(LastPlayed())).size() - 1))
                    options.add(Hand.get(Card.SuitFromInt(LastPlayed())).get(Hand.get(Card.SuitFromInt(LastPlayed())).size() - 1));
            }
        }

        return options;
    }

    public ArrayList<Boolean> VoidSuits() {
        ArrayList<Boolean> res = new ArrayList<Boolean>();
        for (int i = 0; i < 4; i++)
            res.add(false);
        // Showed void during play
        //Collections.reverse(PlayHistory);
        //Collections.reverse(TurnHistory);
        for (int i = 0; i < PlayHistory.size() - 1; i++)
            if (i % 2 == 0 && TurnHistory.get(i) == PlayerId
                    && Card.SuitFromInt(PlayHistory.get(i)) != Card.SuitFromInt(PlayHistory.get(i + 1)))
                res.set(Card.SuitFromInt(PlayHistory.get(i)), true);
        //Collections.reverse(PlayHistory);
        //Collections.reverse(TurnHistory);
        return res;
    }
}
