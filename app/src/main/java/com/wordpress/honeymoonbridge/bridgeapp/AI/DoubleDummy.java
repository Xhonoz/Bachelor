package com.wordpress.honeymoonbridge.bridgeapp.AI;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Double;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Pass;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DoubleDummy {
    
	private TranspositionTable TT;

    public DoubleDummy()
    {
        TT = new TranspositionTable();
    }

    public HashMap<Bid, java.lang.Double> SacrificeSolveAvg(PlayerState playerState, Contract passOption, Contract doubleOption, Contract sacrificeOption, int iterations)
    {
        // Create options and store in dictionary
        Pass pass = new Pass(Player.NORTH);
        Double x = new Double(Player.NORTH);
        Contract doubleMaking = new Contract(doubleOption, true, true);
        Contract sacrifice = new Contract(sacrificeOption.getTrump(), sacrificeOption.getTricks(), Player.NORTH);
        HashMap<Bid, java.lang.Double> res = new HashMap<Bid, java.lang.Double>();
        res.put(pass, 0.0);
        res.put(x, 0.0);
        res.put(sacrifice, 0.0);

        // tmp state
        State state = new State(0);
        state.setSouth(playerState.getHand());
        state.setTurn(1);

        // candidates for opponents hand
        ArrayList<Integer> options = FindOptions(playerState);

        for (int i = 0; i < iterations; i++)
        {
            // set new north hand
            NewNorthHand(state, options);

            // Pass option
            state.setTrump(HoneymoonBridgePlayer.getTrumpInt(passOption.getTrump()));
            state.setTurn(0);
            res.put(pass, res.get(pass) - passOption.Points(13 - MaxTricks(state)));

            // Double option
            int trickLimit = 13 - MaxTricks(state);
            res.put(x, res.get(x) + new java.lang.Double(trickLimit >= doubleOption.getTricks() + 6 ? -doubleMaking.Points(13 - MaxTricks(state)) : -doubleOption.Points(13 - MaxTricks(state))));

            // SacrificeOption
            state.setTrump(HoneymoonBridgePlayer.getTrumpInt(sacrificeOption.getTrump()));
            state.setTurn(1);
            trickLimit = MaxTricks(state);
            res.put(sacrifice, res.get(sacrifice) + new java.lang.Double(trickLimit >= sacrificeOption.getTricks() + 6 ? sacrifice.Points(trickLimit) : sacrificeOption.Points(MaxTricks(state))));
        }
        res.put(pass, res.get(pass) / iterations);
        res.put(x, res.get(x) / iterations);
        res.put(sacrifice, res.get(sacrifice) / iterations);

        return res;
    }


    public HashMap<Integer, java.lang.Double> SolveAvg(PlayerState state, int iterations)
    {
        State tmpState = new State(0);
        tmpState.setNorth(state.getHand());
        tmpState.setCardsHistory(state.getPlayHistory());
        tmpState.setTricks(state.getTricks());
        tmpState.setTurn(1);
        tmpState.setTrump(HoneymoonBridgePlayer.getTrumpInt(state.getContract().getTrump()));

        // candidates for opponents hand
        ArrayList<Integer> options = FindOptions(state);

        HashMap<Integer, java.lang.Double> res = new HashMap<Integer, java.lang.Double>();
        for (Integer move : state.GenerateAllMovesLogic())
            res.put(move, new java.lang.Double(0.0));

        for (int t = 0; t < iterations; t++)
            AvgRoutine(state, tmpState, options, res);

        // average scores
        for (Entry<Integer, java.lang.Double> r : res.entrySet()) {
        	res.put(r.getKey(), res.get(r.getKey()) / iterations);
        }

        return res;
    }

    private void AvgRoutine(PlayerState state, State tmpState, ArrayList<Integer> options, HashMap<Integer, java.lang.Double> res)
    {
        // options ordering
    	Collections.shuffle(options);

        // make new hand for opponent and inserting cards (add many)
        ArrayList<ArrayList<Integer>> randomHand = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 4; i++)
        	randomHand.add(new ArrayList<Integer>());
        for (int i = 0; i < state.OpponentsHandCount(); i++)
        	randomHand.get(Card.SuitFromInt(options.get(i))).add(options.get(i));

        // sort hand and set it in state object
        Collections.sort(randomHand.get(0));
        Collections.reverse(randomHand.get(0));
        Collections.sort(randomHand.get(1));
        Collections.reverse(randomHand.get(1));
        Collections.sort(randomHand.get(2));
        Collections.reverse(randomHand.get(2));
        Collections.sort(randomHand.get(3));
        Collections.reverse(randomHand.get(3));
        tmpState.setSouth(randomHand);

        // update results
        for (Entry<Integer, Integer> r : Solve(tmpState).entrySet())
        {
            // Solve returns tricks for 0 (south) 
            int tricksPlayer = 13 - r.getValue();
            // If opponent has the contract, points are negative
            int points = state.getContract().getPlayer() == Player.NORTH ? state.getContract().Points(tricksPlayer) : - state.getContract().Points(13 - tricksPlayer);
            res.put(r.getKey(), res.get(r.getKey()) + points);
        }
    }
    
    public HashMap<Integer, Integer> ContractSolveAvg(PlayerState playerState, int iterations)
    {
        // tmp state
        State state = new State(0);
        state.setSouth(playerState.getHand());
        state.setTurn(1);
        state.setTrump(-1);

        // candidates for opponents hand
        ArrayList<Integer> options = FindOptions(playerState);

        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        for (int i = -1; i < 4; i++)
            res.put(i, 0);

        for (int i = 0; i < iterations; i++)
            AvgRoutineContract(state, options, res);

        res.put(-1, res.get(-1) / iterations);
        res.put(0, res.get(0) / iterations);
        res.put(1, res.get(1) / iterations);
        res.put(2, res.get(2) / iterations);
        res.put(3, res.get(3) / iterations);
        
        return res;
    }

    private void AvgRoutineContract(State state, ArrayList<Integer> options, HashMap<Integer, Integer> res)
    {
        // set new north hand
        NewNorthHand(state, options);

        // different trump options and result
        for (int i = -1; i < 4; i++)
        {
            state.setTrump(i);
            res.put(i, res.get(i) + MaxTricks(state));
        }
    }
    
    private void NewNorthHand(State state, ArrayList<Integer> options)
    {
        // shuffle options
    	Collections.shuffle(options);

        // draw opponents hand useing provided function
        for (ArrayList<Integer> s : state.getNorth())
            s.clear();

        for (int i = 0; i < 26; i += 2)
        {
            if (Card.RankFromInt(options.get(i)) > 10)
                state.getNorth().get(Card.SuitFromInt(options.get(i))).add(options.get(i));
            else
                state.getNorth().get(Card.SuitFromInt(options.get(i + 1))).add(options.get(i + 1));
        }

        // sort hand
        Collections.sort(state.getNorth().get(0));
        Collections.reverse(state.getNorth().get(0));
        Collections.sort(state.getNorth().get(1));
        Collections.reverse(state.getNorth().get(1));
        Collections.sort(state.getNorth().get(2));
        Collections.reverse(state.getNorth().get(2));
        Collections.sort(state.getNorth().get(3));
        Collections.reverse(state.getNorth().get(3));
    }
    
    private ArrayList<Integer> FindOptions(PlayerState state)
    {
    	ArrayList<Integer> options = new ArrayList<Integer>();
    	ArrayList<Integer> hand = state.getStartingHand();
        ArrayList<Boolean> isVoid = state.VoidSuits();
        for (int i = 1; i <= 52; i++)
            // if the card is not played, not in hand and opponent has not shown a void in the suit
            if (!state.getPlayHistory().contains(i) && !state.getDiscards().contains(i) && !isVoid.get(Card.SuitFromInt(i)) && !hand.contains(i))
                options.add(i);
        return options;
    }

    /**
     * Generate dictionary of all moves and associated trick limit
     **/
    public HashMap<Integer, Integer> Solve(State state)
    {
        // adds all options to limits dictionary
        HashMap<Integer, Integer> limits = new HashMap<Integer, Integer>();
        for (Integer move : state.GenerateAllMoves())
            limits.put(move, 0);

        for (Integer c : state.GenerateMoves())
        {
            state.Make(c);
            limits.put(c, TrickLimit(state, 13, 6));
            state.Undo();
        }

        if (state.NoMoveYetInTrick()) // lead
            Fill(limits);

        TT.clear();

        return limits;
    }

    /**
     * Same method as TrickLimit but use multi threading and is public
     **/
    public Integer MaxTricks(State state)
    {
        return state.getTurn() == 0 ? Collections.max(Solve(state).values()) : Collections.min(Solve(state).values());
    }

    /**
     * Fill limits in cases where opponents exact hand limit generated moves
     **/
    private void Fill(HashMap<Integer, Integer> limits)
    {
        int last = 0;
        List<Entry<Integer,Integer>> limitsList = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            limitsList = limits.entrySet().stream().collect(Collectors.toList());
        }
        // Mulig feil
        Collections.sort(limitsList, (o1, o2) -> o1.getKey().compareTo(o2.getKey())); // sjekke at sort fungerer synkende
        Collections.reverse(limitsList);
        for (Entry<Integer, Integer> limit : limitsList)
        {
            if (limit.getValue() == 0)
                limits.put(limit.getKey(), last);
            else
                last = limit.getValue();
        }
    }

    /**
     * Finds trick limit for 0 (south) side.
     * This is for internal use, singel thread solve.
     **/
    private int TrickLimit(State state, int maxTricks, int estimate)
    {
        int upperbound = maxTricks;
        int lowerbound = 0;
        int g = estimate;
        int tricks = 0;
        do
        {
            if (g == lowerbound)
                tricks = g + 1;
            else
                tricks = g;
            // - 2 because last trick is solved in evaluate method
            if (!Search(state, tricks, (maxTricks * 2) - 2 - state.getCardsHistory().size()))
            {
                upperbound = tricks - 1;
                g = upperbound;
            }
            else
            {
                lowerbound = tricks;
                g = lowerbound;
            }
        }
        while (lowerbound < upperbound);
        return g;
    }

    /**
     * Search function as described in http://privat.bahnhof.se/wb758135/bridge/Alg-dds_x.pdf
     **/
    private boolean Search(State posPoint, int target, int depth)
    {
        if (depth == 0)
            return posPoint.Evaluate() >= target;
        else
        {
            if (posPoint.NoMoveYetInTrick())
            {
                if (TT.GetLowLimit(posPoint) + posPoint.getTricks() >= target)
                    return true;
                if (TT.GetUpperLimit(posPoint) + posPoint.getTricks() < target)
                    return false;
                if (posPoint.TargetAlreadyObtained(target))
                    return true;
                if (posPoint.TargetCanNoLongerBeObtained(target))
                    return false;
                if (posPoint.getTurn() == 0 && posPoint.QuickTricks(target))
                    return true;
                if (posPoint.getTurn() == 1 && posPoint.LateTricks(target))
                    return false;
            }
            ArrayList<Integer> options = posPoint.GenerateMoves();
            boolean value;
            if (posPoint.getTurn() == 0) // South
            {
                value = false;
                for (int c : options)
                {
                    posPoint.Make(c);
                    value = Search(posPoint, target, depth - 1);
                    posPoint.Undo();
                    if (value)
                        break;
                }
            }
            else                    // North
            {
                value = true;
                for (int c : options)
                {
                    posPoint.Make(c);
                    value = Search(posPoint, target, depth - 1);
                    posPoint.Undo();
                    if (!value)
                        break;
                }
            }
            if (posPoint.NoMoveYetInTrick())
            {
                if (value)
                    TT.SaveNewPosition(posPoint, 13, target - posPoint.getTricks());
                else
                    TT.SaveNewPosition(posPoint, target - posPoint.getTricks() - 1, 0);
            }
            return value;
        }
    }
}
