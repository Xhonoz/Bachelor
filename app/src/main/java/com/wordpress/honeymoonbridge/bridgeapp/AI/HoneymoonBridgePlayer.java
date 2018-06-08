package com.wordpress.honeymoonbridge.bridgeapp.AI;

import android.os.Build;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GameState;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Double;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Pass;
import com.wordpress.honeymoonbridge.bridgeapp.Model.ReDouble;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.stream.Collectors;

public class HoneymoonBridgePlayer implements AIPlayer {

    private DoubleDummy DD;
    private int BidSimCount;
    private int PlaySimCount;

    public HoneymoonBridgePlayer(int bidSimCount, int playSimCount) {
        BidSimCount = bidSimCount;
        PlaySimCount = playSimCount;
        DD = new DoubleDummy();
    }

    private PlayerState MakePlayerState(GameState state) {
        PlayerState playerState = new PlayerState(1);

        ArrayList<ArrayList<Integer>> Hand = new ArrayList<ArrayList<Integer>>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Hand.add(0, new ArrayList<Integer>(state.getNorthHand().getHandSpade().stream().map(x -> x.ToIntDD()).collect(Collectors.toList())));
            Hand.add(1, new ArrayList<Integer>(state.getNorthHand().getHandHeart().stream().map(x -> x.ToIntDD()).collect(Collectors.toList())));
            Hand.add(2, new ArrayList<Integer>(state.getNorthHand().getHandDiamond().stream().map(x -> x.ToIntDD()).collect(Collectors.toList())));
            Hand.add(3, new ArrayList<Integer>(state.getNorthHand().getHandClub().stream().map(x -> x.ToIntDD()).collect(Collectors.toList())));

            ArrayList<Integer> StartingHand = new ArrayList<Integer>();
            StartingHand.addAll(Hand.get(0));
            StartingHand.addAll(Hand.get(1));
            StartingHand.addAll(Hand.get(2));
            StartingHand.addAll(Hand.get(3));

            ArrayList<Integer> Discards = new ArrayList<Integer>(state.getNorth26Cards().stream().map(x -> x.ToIntDD()).filter(x -> !StartingHand.contains(x)).collect(Collectors.toList()));

            Stack<Integer> PlayHistory = new Stack<Integer>();

            for (int i = 0; i < state.getTricks().size(); i++) {
                PlayHistory.push(state.getTricks().get(i).firstCard.ToIntDD());
                if (state.getTricks().get(i).SecondCard != null)
                    PlayHistory.push(state.getTricks().get(i).SecondCard.ToIntDD());
            }

            Stack<Integer> TurnHistory = new Stack<Integer>();
            for (int i = 0; i < state.getTricks().size(); i++) {
                TurnHistory.push(state.getTricks().get(i).lead == Player.SOUTH ? 0 : 1);
                if (state.getTricks().get(i).SecondCard != null)
                    TurnHistory.push(state.getTricks().get(i).lead == Player.SOUTH ? 1 : 0);
            }

            playerState.setStartingHand(StartingHand);
            playerState.setContract(state.getContract());
            playerState.setHand(Hand);
            playerState.setDiscards(Discards);
            playerState.setTricks(state.getNorthTricks());
            playerState.setPlayHistory(PlayHistory);
            playerState.setTurnHistory(TurnHistory);

        }
        return playerState;
    }

    @Override
    public Card playCard(GameState state) {
        PlayerState pstate = MakePlayerState(state);
        int play = 0;
        // only one option
        if (pstate.GenerateAllMovesLogic().size() == 1) {
            play = pstate.GenerateAllMovesLogic().get(0);
        }
        // more options, solve average and play best
        else {
            HashMap<Integer, java.lang.Double> options = DD.SolveAvg(pstate, PlaySimCount);
            java.lang.Double bestScore = Collections.max(options.values());
            int bestPlay = 0;
            for (Entry<Integer, java.lang.Double> r : options.entrySet()) {
                if (r.getValue() == bestScore)
                    bestPlay = r.getKey();
            }
            play = bestPlay;
        }
        // find equal options and play random from this collection
        ArrayList<Integer> equalOptions = EqualOptions(pstate, play);
        Collections.shuffle(equalOptions);
        return new Card(Suit.getSuitFromTrump(getTrumpFromInt(Card.SuitFromInt(equalOptions.get(0)))), Card.RankFromInt(equalOptions.get(0)));
    }

    private boolean SureTrick(PlayerState state, int option) {
        boolean winner = true;
        while (Card.RankFromInt(option) < 14) {
            if (!(state.getDiscards().contains(option + 1) || state.getStartingHand().contains(option + 1))) {
                winner = false;
                break;
            }
            option++;
        }
        return winner;
    }

    @Override
    public boolean pickCard(GameState state) {
        PlayerState pstate = MakePlayerState(state);
        int option = ((Card) state.getStack().get(0)).ToIntDD();

        ArrayList<Integer> suitCount = new ArrayList<Integer>();
        suitCount.add(0, state.getNorthHand().getHandSpade().size());
        suitCount.add(1, state.getNorthHand().getHandHeart().size());
        suitCount.add(2, state.getNorthHand().getHandDiamond().size());
        suitCount.add(3, state.getNorthHand().getHandClub().size());

        int suit = suitCount.indexOf(Collections.max(suitCount));
        boolean sureTrick = SureTrick(pstate, option);
        boolean take = false;
        if (Collections.max(suitCount) >= 4 && Card.SuitFromInt(option) == suit)
            take = true;
        else if (sureTrick)
            take = true;
        else if (Collections.max(suitCount) > 4)
            take = false;
        else
            take = Card.RankFromInt(option) >= 11;
        return take;
    }

    @Override
    public Bid bid(GameState state) {
        PlayerState pstate = MakePlayerState(state);
        HashMap<Integer, Integer> ContractPlan = DD.ContractSolveAvg(pstate, BidSimCount);

        // Planed bid from conservative average solve
        Bid planedBid = MakeBestLowestBid(state.getBiddingHistory().getLastBid(Player.SOUTH) == null ? new Pass(Player.NORTH) : state.getBiddingHistory().getLastBid(Player.SOUTH), ContractPlan, state);

        // if planned bid is pass, consider double and sacrifice before passing
        if (planedBid instanceof Pass && state.getBiddingHistory().getLastBid(Player.SOUTH) != null) {
            Bid lastBid = state.getBiddingHistory().getLastBid(Player.SOUTH);
            if (lastBid instanceof Contract) {
                // Pass option
                Contract lastBidContract = (Contract) lastBid;
                int stampSuitTricks = Collections.max(ContractPlan.values());
                int stampSuit = -2;
                for (int i = 3; i >= -1; i--) {
                    if (ContractPlan.get(i) == stampSuitTricks) {
                        stampSuit = i;
                    }
                }

                int stampLevel = lastBidContract.getTricks();
                if (getTrumpInt(lastBidContract.getTrump()) < stampSuit)
                    stampLevel += 1;

                // Sacrifice option
                Contract sacrificeBid = new Contract(getTrumpFromInt(stampSuit), stampLevel, Player.NORTH);
                sacrificeBid.setDoubled(true);

                // Double option
                Contract lastBidDoubled = new Contract(lastBidContract, true, false);

                // Solve average best
                HashMap<Bid, java.lang.Double> avgScore = DD.SacrificeSolveAvg(pstate, lastBidContract, lastBidDoubled, sacrificeBid, BidSimCount);
                java.lang.Double planedBidScore = new java.lang.Double(0.0);
                for (Entry<Bid, java.lang.Double> r : avgScore.entrySet()) {
                    if (r.getValue() > planedBidScore) {
                        planedBid = r.getKey();
                        planedBidScore = r.getValue();
                    }
                }
            }
        }
        return planedBid;
    }

    private Bid MakeBestLowestBid(Bid LastBid, HashMap<Integer, Integer> ContractPlan, GameState state) {
        if (LastBid instanceof Double || LastBid instanceof ReDouble)
            return new Pass(Player.NORTH);
        // 4 and 7 to make 1 club the lowest legal bid if opponent has not bid
        int suitBid = 4;
        int levelBid = 7;
        if (LastBid instanceof Contract) {
            Contract lastContract = (Contract) LastBid;
            suitBid = getTrumpInt(lastContract.getTrump());
            levelBid = lastContract.getTricks() + 6;
        }
        // filter out suits where estimate is under 6 tricks, then select contract and points if made as expected
        HashMap<Integer, Integer> contractPoints = new HashMap<Integer, Integer>();
        for (int i = -1; i < 4; i++) {
            if (ContractPlan.get(i) > 6) {
                Contract contract = new Contract(getTrumpFromInt(i), ContractPlan.get(i) - 6, Player.NORTH);
                contractPoints.put(i, contract.Points(ContractPlan.get(i)));
            } else {
                contractPoints.put(i, 0);
            }
        }
        // filter out leagal bids, then sort on points gained
        for (int i = -1; i < 4; i++) {
            if (!(ContractPlan.get(i) > levelBid || (i < suitBid && ContractPlan.get(i) >= levelBid))) {
                contractPoints.put(i, 0);
            }
        }
        int bestLeagalPoints = Collections.max(contractPoints.values());
        int bestLeagalTrump = -1;
        for (int i = -1; i < 4; i++)
            if (contractPoints.get(i) == bestLeagalPoints)
                bestLeagalTrump = i;

        Contract bestBid = new Contract(getTrumpFromInt(bestLeagalTrump), ContractPlan.get(bestLeagalTrump) - 6, Player.NORTH);
        // pass if no leagal bids
        if (bestLeagalPoints == 0)
            return new Pass(Player.NORTH);
        int tricks = bestBid.getTricks();
        // lower contract level as long as its leagal and points made remain the same
        while (bestBid.getTricks() > 0 && (bestBid.getTricks() - 1 > levelBid - 6 || (getTrumpInt(bestBid.getTrump()) < suitBid && bestBid.getTricks() - 1 >= levelBid - 6)) &&
                new Contract(bestBid.getTrump(), bestBid.getTricks() - 1, Player.NORTH).Points(tricks + 6) == contractPoints.get(bestLeagalTrump))
            bestBid.setTricks(bestBid.getTricks() - 1);
        return bestBid;
    }

    private ArrayList<Integer> EqualOptions(PlayerState state, int play) {
        ArrayList<ArrayList<Integer>> hand = state.getHand();
        ArrayList<Integer> equalOptions = new ArrayList<Integer>();
        equalOptions.add(play);
        int i = play - 1;
        // equal options going down from planned
        while (i >= hand.get(Card.SuitFromInt(play)).get(hand.get(Card.SuitFromInt(play)).size() - 1)) {
            final int ieq = i;
            if (!state.NoMoveYetInTrick() && i == state.LastPlayed())
                break;
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (state.getDiscards().stream().filter(x -> x == ieq).count() > 0 || state.getPlayHistory().contains(i))
                    i--;
                else if (hand.get(Card.SuitFromInt(i)).contains(i)) {
                    equalOptions.add(i);
                    i--;
                } else
                    break;
            }
        }
        i = play + 1;
        // equal options going up from planned
        while (i <= hand.get(Card.SuitFromInt(play)).get(0)) {
            final int ieq = i;
            if (!state.NoMoveYetInTrick() && i == state.LastPlayed())
                break;
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (state.getDiscards().stream().filter(x -> x == ieq).count() > 0 || state.getPlayHistory().contains(i))
                    i++;
                else if (hand.get(Card.SuitFromInt(i)).contains(i)) {
                    equalOptions.add(i);
                    i++;
                } else
                    break;
            }
        }
        return equalOptions;
    }


    public static int getTrumpInt(Trump t) {
        switch (t) {
            case Spades:
                return 0;
            case Hearts:
                return 1;
            case Diamonds:
                return 2;
            case Clubs:
                return 3;
            case NoTrump:
                return -1;
        }
        return -1;
    }

    public static Trump getTrumpFromInt(int t) {
        switch (t) {
            case 0:
                return Trump.Spades;
            case 1:
                return Trump.Hearts;
            case 2:
                return Trump.Diamonds;
            case 3:
                return Trump.Clubs;
            case -1:
                return Trump.NoTrump;
        }
        return Trump.NoTrump;
    }
}
