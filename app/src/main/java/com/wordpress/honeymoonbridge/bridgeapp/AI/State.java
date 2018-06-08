package com.wordpress.honeymoonbridge.bridgeapp.AI;

import android.os.Build;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class State {
    private ArrayList<ArrayList<Integer>> South;
    private ArrayList<ArrayList<Integer>> North;
    private ArrayList<Integer> SouthStart;
    private ArrayList<Integer> NorthStart;

    // 0 = South, 1 = North
    private int Turn;
    
    private int Tricks;
    // -1 = Notrump
    private int Trump;
    private Stack<Integer> CardsHistory;
    private Stack<Integer> TurnHistory;
    private Stack<Integer> TrickHistory;
    
    public State(int Turn)
    {
        // Main
        SouthStart = new ArrayList<Integer>();
        South = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 4; i++)
        	South.add(new ArrayList<Integer>());
        NorthStart = new ArrayList<Integer>();
        North = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 4; i++)
        	North.add(new ArrayList<Integer>());
        
        this.Turn = Turn;

        Tricks = 0;
        Trump = -1;
        CardsHistory = new Stack<Integer>();
        TurnHistory = new Stack<Integer>();
        TrickHistory = new Stack<Integer>();
    }
    
    // copy constructor
    public State(State state)
    {
        // Main
        SouthStart = new ArrayList<Integer>(state.getSouthStart());
        South = new ArrayList<ArrayList<Integer>>();
        South.add(0, new ArrayList<Integer>(state.getSouth().get(0)));
        South.add(1, new ArrayList<Integer>(state.getSouth().get(1)));
        South.add(2, new ArrayList<Integer>(state.getSouth().get(2)));
        South.add(3, new ArrayList<Integer>(state.getSouth().get(3)));
        
        NorthStart = new ArrayList<Integer>(state.getNorthStart());
        North = new ArrayList<ArrayList<Integer>>();
        North.add(0, new ArrayList<Integer>(state.getNorth().get(0)));
        North.add(1, new ArrayList<Integer>(state.getNorth().get(1)));
        North.add(2, new ArrayList<Integer>(state.getNorth().get(2)));
        North.add(3, new ArrayList<Integer>(state.getNorth().get(3)));
        
        Turn = state.getTurn();

        // Play
        Tricks = state.getTricks();
        Trump = state.getTrump();
        CardsHistory = new Stack<Integer>();
        CardsHistory.addAll(state.getCardsHistory()); // Mulig feil
        Collections.reverse(CardsHistory);
        TurnHistory = new Stack<Integer>();
        TurnHistory.addAll(state.getTurnHistory());   // Mulig feil
        Collections.reverse(TurnHistory);
        TrickHistory = new Stack<Integer>();
        TrickHistory.addAll(state.getTrickHistory()); // Mulig feil
        Collections.reverse(TrickHistory);
    }
    
    public ArrayList<ArrayList<Integer>> getSouth() {
		return South;
	}



	public void setSouth(ArrayList<ArrayList<Integer>> south) {
		South = south;
	}



	public ArrayList<ArrayList<Integer>> getNorth() {
		return North;
	}



	public void setNorth(ArrayList<ArrayList<Integer>> north) {
		North = north;
	}



	public ArrayList<Integer> getSouthStart() {
		return SouthStart;
	}



	public void setSouthStart(ArrayList<Integer> southStart) {
		SouthStart = southStart;
	}



	public ArrayList<Integer> getNorthStart() {
		return NorthStart;
	}



	public void setNorthStart(ArrayList<Integer> northStart) {
		NorthStart = northStart;
	}



	public int getTurn() {
		return Turn;
	}



	public void setTurn(int turn) {
		Turn = turn;
	}



	public int getTricks() {
		return Tricks;
	}



	public void setTricks(int tricks) {
		Tricks = tricks;
	}



	public int getTrump() {
		return Trump;
	}



	public void setTrump(int trump) {
		Trump = trump;
	}



	public Stack<Integer> getCardsHistory() {
		return CardsHistory;
	}



	public void setCardsHistory(Stack<Integer> cardsHistory) {
		CardsHistory = cardsHistory;
	}



	public Stack<Integer> getTurnHistory() {
		return TurnHistory;
	}



	public void setTurnHistory(Stack<Integer> turnHistory) {
		TurnHistory = turnHistory;
	}



	public Stack<Integer> getTrickHistory() {
		return TrickHistory;
	}



	public void setTrickHistory(Stack<Integer> trickHistory) {
		TrickHistory = trickHistory;
	}



	public void FlipTurn()
    {
        Turn = Turn == 0 ? 1 : 0;
    }
    
    public boolean SouthTurn()
    {
        return Turn == 0;
    }

    public boolean NorthTurn()
    {
        return Turn == 1;
    }
    
    public boolean NoMoveYetInTrick()
    {
        return CardsHistory.size() % 2 == 0;
    }
    
    public int LastPlayed()
    {
        return CardsHistory.peek();
    }
    
    private boolean WinByRank(Integer card)
    {
        return Card.SuitFromInt(card) == Card.SuitFromInt(LastPlayed()) && card > LastPlayed();
    }

    private boolean WinByTrump(Integer card)
    {
        return Card.SuitFromInt(card) != Card.SuitFromInt(LastPlayed()) && Card.SuitFromInt(card) == Trump;
    }
    
    public ArrayList<ArrayList<Integer>> SideToPlay()
    {
        return SouthTurn() ? South : North;
    }

    public ArrayList<ArrayList<Integer>> WaitingSide()
    {
        return SouthTurn() ? North : South;
    }
    
    public boolean TargetAlreadyObtained(int target)
    {
        return Tricks >= target;
    }

    public boolean TargetCanNoLongerBeObtained(int target)
    {
        return Tricks + ((13 * 2) - (CardsHistory.size() / 2)) < target;
    }
    
    public void Make(Integer c)
    {
        if (SouthTurn())
            South.get(Card.SuitFromInt(c)).remove(c);
        else
            North.get(Card.SuitFromInt(c)).remove(c);

        // Save state
        TrickHistory.push(Tricks);
        TurnHistory.push(Turn);

        // First card in trick, flip turn
        if (NoMoveYetInTrick())
            FlipTurn();
        // 2nd card in trick and a win by rank or trump, increment trick count if south played
        else if (!NoMoveYetInTrick() && ((WinByRank(c)) || (WinByTrump(c))))
            Tricks += SouthTurn() ? 1 : 0;
        // 2nd card in trick and loss, flip turn and increment trick if north played
        else
        {
            Tricks += NorthTurn() ? 1 : 0;
            FlipTurn();
        }
        // Save move
        CardsHistory.push(c);
    }

    public void Undo()
    {
        // Pop state
        Integer lastCard = CardsHistory.pop();
        Turn = TurnHistory.pop();
        Tricks = TrickHistory.pop();
        AddCardSideToPlay(lastCard);
    }

    private void AddCardSideToPlay(int card)
    {
        ArrayList<ArrayList<Integer>> hand = SideToPlay();
        int i = 0;
        while (i < hand.get(Card.SuitFromInt(card)).size() && hand.get(Card.SuitFromInt(card)).get(i) > card)
            i++;
        hand.get(Card.SuitFromInt(card)).add(i,card);
    }

    public int Evaluate()
    {
        ArrayList<Integer> options = GenerateMoves();
        Make(options.get(0));
        options = GenerateMoves();
        Make(options.get(0));
        int tricks = Tricks;
        Undo();
        Undo();
        return tricks;
    }
    
    public ArrayList<Integer> GenerateMoves()
    {
    	ArrayList<ArrayList<Integer>> lead = SideToPlay();
    	ArrayList<ArrayList<Integer>> other = WaitingSide();
        ArrayList<Integer> options = new ArrayList<Integer>();
        if (NoMoveYetInTrick()) // all options open
        {
            for (int s = 0; s < lead.size(); s++)
            {
                int j = 0;
                int i = 0;
                while (i < lead.get(s).size())
                {
                    options.add(lead.get(s).get(i));
                    if (other.get(s).size() == 0)
                        break;
                    while (i < lead.get(s).size() && j < other.get(s).size() && other.get(s).get(j) > lead.get(s).get(i))
                        j++;
                    while (i < lead.get(s).size() && ((j < other.get(s).size() && lead.get(s).get(i) > other.get(s).get(j)) || j == other.get(s).size()))
                        i++;
                    while (i < lead.get(s).size() && j < other.get(s).size() && other.get(s).get(j) > lead.get(s).get(i))
                        j++;
                }
            }
        }
        else // 2nd card in trick, max 3 options
            options = SecondCardOptions();
        return options;
    }
    
    public ArrayList<Integer> GenerateAllMoves()
    {
        ArrayList<ArrayList<Integer>> lead = SideToPlay();
        ArrayList<ArrayList<Integer>> other = WaitingSide();
        ArrayList<Integer> options = new ArrayList<Integer>();
        if (NoMoveYetInTrick()) // all options open
            for (ArrayList<Integer> s : lead)
                options.addAll(s);
        else // max 3 options
            options = SecondCardOptions();
        return options;
    }
    
    private ArrayList<Integer> SecondCardOptions()
    {
    	 ArrayList<Integer> options = new  ArrayList<Integer>();
        // Can not win by rank
        if (SideToPlay().get(Card.SuitFromInt(LastPlayed())).size() == 0)
        {
            for (ArrayList<Integer> s : SideToPlay())
                if (s.size() != 0)
                    options.add(s.get(s.size() - 1));
        }
        // Can win by rank
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (SideToPlay().get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).count() > 0)
                    options.add(SideToPlay().get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).reduce((a, b) -> b).orElse(null));
                if (SideToPlay().get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).count() == 0
                        || SideToPlay().get(Card.SuitFromInt(LastPlayed())).stream().filter(x -> x > LastPlayed()).reduce((a, b) -> b).orElse(null) != SideToPlay().get(Card.SuitFromInt(LastPlayed())).get(SideToPlay().get(Card.SuitFromInt(LastPlayed())).size() - 1))
                    options.add(SideToPlay().get(Card.SuitFromInt(LastPlayed())).get(SideToPlay().get(Card.SuitFromInt(LastPlayed())).size() - 1));
            }
        }
        return options;
    }
    
    public boolean QuickTricks(int target)
    {
        return Tricks + QuickTricksCount() >= target;
    }

    public boolean LateTricks(int target)
    {
        return Tricks + (((13 * 2) - (CardsHistory.size() / 2)) - QuickTricksCount()) < target;
    }

    // tricks that can be taken by the side to move
    private int QuickTricksCount()
    {
        ArrayList<ArrayList<Integer>> lead = SideToPlay();
        ArrayList<ArrayList<Integer>> opponent = WaitingSide();
        int qtricks = 0;
        int tricksInTrump = 0;
        boolean opponentCanTrump = false;
        if (Trump != -1)
        {
            tricksInTrump = QuickTricksInSuit(Trump, lead, opponent, false);
            // Side to move can not draw trump
            if (tricksInTrump < opponent.get(Trump).size())
                opponentCanTrump = true;
            qtricks += tricksInTrump;
        }
        for (int s = 0; s < 4; s++) // for each suit
            if (s != Trump)
                qtricks += QuickTricksInSuit(s, lead, opponent, opponentCanTrump);
        return qtricks;
    }

    private int QuickTricksInSuit(int suit, ArrayList<ArrayList<Integer>> lead, ArrayList<ArrayList<Integer>> opponent, boolean canTrump)
    {
        int qtricks = 0;
        int i = 0;
        // lead and opponent side has cards in suit
        if (lead.get(suit).size() > 0 && opponent.get(suit).size() > 0)
        {
            // count winners in suit
            while (i < lead.get(suit).size() && lead.get(suit).get(i) > opponent.get(suit).get(0) && i < opponent.get(suit).size())
                i++;
            // if winner count is more than opponent cards in suit and no trmup, all cards are winners
            if (i >= opponent.get(suit).size() && !canTrump)
                i = lead.get(suit).size();
            qtricks += i;
        }
        // opponent has void suit and can't trump
        else if (lead.get(suit).size() > 0 && opponent.get(suit).size() == 0 && !canTrump)
        {
            qtricks += lead.get(suit).size();
        }
        return qtricks;
    }
}
