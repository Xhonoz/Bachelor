package com.wordpress.honeymoonbridge.bridgeapp.AI;

import com.wordpress.honeymoonbridge.bridgeapp.AI.State;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TranspositionTable {
	private ConcurrentHashMap<Long, ConcurrentHashMap<Long, TTentry>> TTdist;

	public TranspositionTable() {
		TTdist = new ConcurrentHashMap<Long, ConcurrentHashMap<Long, TTentry>>();
	}
	
	public void clear() {
		TTdist.clear();
	}

	public synchronized int GetLowLimit(State state)
    {
        if (TTdist.containsKey(MakeDistKey(state)) && TTdist.get(MakeDistKey(state)).containsKey(MakeRankKey(state)))
        {
            return TTdist.get(MakeDistKey(state)).get(MakeRankKey(state)).getMin();
        }
        return 0;
    }

	public synchronized int GetUpperLimit(State state)
    {
        if (TTdist.containsKey(MakeDistKey(state)) && TTdist.get(MakeDistKey(state)).containsKey(MakeRankKey(state)))
        {
            return TTdist.get(MakeDistKey(state)).get(MakeRankKey(state)).getMax();
        }
        return 13;
    }

	public synchronized void SaveNewPosition(State state, int Max, int Min)
    {
        Long distKey = MakeDistKey(state);
        Long rankKey = MakeRankKey(state);
        if (TTdist.containsKey(distKey))
        {
            if (TTdist.get(distKey).containsKey(rankKey))
            {
            	TTdist.get(distKey).get(rankKey).setMax(Math.min(Max, TTdist.get(distKey).get(rankKey).getMax()));
            	TTdist.get(distKey).get(rankKey).setMin(Math.max(Min, TTdist.get(distKey).get(rankKey).getMin()));
            }
            else
            {
            	TTdist.get(distKey).put(rankKey, new TTentry(Max, Min));
            }
        }
        else
        {
            TTdist.put(distKey, new ConcurrentHashMap<Long, TTentry>());
            TTdist.get(distKey).put(rankKey, new TTentry(Max, Min));
        }
    }

	public Long MakeDistKey(State state) {
		Long key = new Long(0);
		int bitIncr = 4;
		int bitPos = 0;
		key |= state.getSouth().get(0).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getSouth().get(1).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getSouth().get(2).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getSouth().get(3).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getNorth().get(0).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getNorth().get(1).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getNorth().get(2).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getNorth().get(3).size() << bitPos;
		bitPos += bitIncr;
		key |= state.getTurn() << bitPos;
		bitPos += bitIncr;
		key |= state.getTrump() + 1 << bitPos;

		return key;
	}

	public Long MakeRankKey(State state)
    {
        Long key = new Long(0);
        int bitIncr = 4;
        int bitPos = 0;

        List<Integer> relativeRanks = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            relativeRanks = GetRelativeRanks(state).stream().flatMap(x -> x.stream()).collect(Collectors.toList());
        }
        for (int i = 0; i < relativeRanks.size(); i++)
        {
            key |= relativeRanks.get(i) << bitPos;
            bitPos += bitIncr;
        }

        return key;
    }
	
	public ArrayList<ArrayList<Integer>> GetRelativeRanks(State state)
    {
		ArrayList<ArrayList<Integer>> relativeRanks = new ArrayList<ArrayList<Integer>>();
        for (int s = 0; s < state.getSouth().size(); s++)
        {
        	ArrayList<Integer> ranksInSuit = new ArrayList<Integer>();
            int j = 0;
            for (int i = 0; i < state.getSouth().get(s).size(); i++)
            {
                while (j < state.getNorth().get(s).size() && state.getNorth().get(s).get(j) > state.getSouth().get(s).get(i))
                    j++;
                ranksInSuit.add(i + j);
            }
            relativeRanks.add(ranksInSuit);
        }
        return relativeRanks;
    }
}
