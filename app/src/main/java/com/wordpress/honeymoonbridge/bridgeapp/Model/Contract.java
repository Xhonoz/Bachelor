package com.wordpress.honeymoonbridge.bridgeapp.Model;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;

/**
 * Created by Carmen on 24.04.2018.
 */

public class Contract extends Bid {

    private final int TrickOffset = 6;
    private Trump trump;
    private int tricks;
    private boolean doubled;
    private boolean reDoubled;

    public Contract(int trump, int tricks, Player player) {
        super(player);
        this.trump = Trump.values()[trump];
        this.tricks = tricks;
    }

    public Contract(Trump trump, int tricks, Player player){
        super(player);
        this.trump = trump;
        this.tricks = tricks;

}

    //double or redouble constructor
    public Contract(Contract contract, boolean doubled, boolean reDoubled, Player player){
        super(player);
        trump = contract.getTrump();
        tricks = contract.getTricks();
        this.doubled = doubled;
        this.reDoubled = reDoubled;
    }

    public int Points(int result)
    {
        int points = 0;
        // Contract made
        if (result >= tricks + TrickOffset)
        {
            points += FirstTrickPoints();
            points += (tricks - 1) * SubsequentTricksPoints();
            points += Bonus(result);
        }
        // Contract failed
        else
        {
            points += PenaltyPoints(result);
        }
        return points;
    }

    private int PenaltyPoints(int result)
    {
        // Assume at least one trick down, split the rest into 2nd and 3rd, and subsequent
        int tricksDown2ndAnd3rd = tricks + TrickOffset - result - 1;
        tricksDown2ndAnd3rd = tricksDown2ndAnd3rd > 2 ? 2 : tricksDown2ndAnd3rd;
        int tricksDownSubsequent = tricks + TrickOffset - result - 3;
        tricksDownSubsequent = tricksDownSubsequent < 0 ? 0 : tricksDownSubsequent;

        int points = 0;

        if (reDoubled)
        {
            points -= 200;
            points -= 400 * tricksDown2ndAnd3rd;
            points -= 600 * tricksDownSubsequent;
        }
        else if (doubled)
        {
            points -= 100;
            points -= 200 * tricksDown2ndAnd3rd;
            points -= 300 * tricksDownSubsequent;
        }
        else
        {
            points -= 50;
            points -= 50 * tricksDown2ndAnd3rd;
            points -= 50 * tricksDownSubsequent;
        }

        return points;
    }

    private int Bonus(int result)
    {
        int bonus = 0;

        // Game or part-game
        if (IsGame())
            bonus += 300;
        else
            bonus += 50;

        // Slam or grand slam
        if (IsGrandSlam()) {
            bonus += 1000;
        } else if (IsSlam())
            bonus += 500;

        // Doubled or redoubled
        if (reDoubled)
            bonus += 100;
        else if (doubled)
            bonus += 50;

        // Overtricks
        int overtricks = result - tricks - TrickOffset;
        if (reDoubled)
            bonus += overtricks * 200;
        else if (doubled)
            bonus += overtricks * 100;
        else
            bonus += overtricks * SubsequentTricksPoints();

        return bonus;
    }


    private int FirstTrickPoints()
    {
        int points = 0;
        // Notrump
        if (trump.ordinal() == 4)
        {
            if (reDoubled)
                points = 160;
            else if (doubled)
                points = 80;
            else
                points = 40;
        }
        // Major trump 0(spades) and 1(hearts)
        else if (trump.ordinal() == 3 || trump.ordinal() == 2)
        {
            if (reDoubled)
                points = 120;
            else if (doubled)
                points = 60;
            else
                points = 30;
        }
        // Minor
        else
        {
            if (reDoubled)
                points = 80;
            else if (doubled)
                points = 40;
            else
                points = 20;
        }
        return points;
    }

    private int SubsequentTricksPoints()
    {
        int points = 0;
        // Notrump
        if (trump.ordinal() == 4)
        {
            if (reDoubled)
                points = 120;
            else if (doubled)
                points = 60;
            else
                points = 30;
        }
        // Major trump 0(spades) and 1(hearts)
        else if (trump.ordinal() == 3 || trump.ordinal() == 2)
        {
            if (reDoubled)
                points = 120;
            else if (doubled)
                points = 60;
            else
                points = 30;
        }
        // Minor
        else
        {
            if (reDoubled)
                points = 80;
            else if (doubled)
                points = 40;
            else
                points = 20;
        }
        return points;
    }

    private boolean IsGame()
    {
        int trickScore = FirstTrickPoints();
        for (int i = 1; i < tricks; i++)
            trickScore += SubsequentTricksPoints();
        return trickScore >= 100;
    }

    private boolean IsSlam()
    {
        return tricks >= 6;
    }

    private boolean IsGrandSlam()
    {
        return tricks == 7;
    }

    @Override
    public String toString()
    {
        String text = "" + tricks;
        switch (trump.ordinal())
        {
            case (4): text += "NT"; break;
            case (3): text += "♤"; break;
            case (2): text += "♡"; break;
            case (1): text += "♢"; break;
            case (0): text += "♧"; break;
            default: break;
        }
        if (reDoubled)
            text += "XX";
        else if (doubled)
            text += "X";
        return text;
    }




    public Trump getTrump() {
        return trump;
    }

    public void setTrump(Trump trump) {
        this.trump = trump;
    }

    public int getTricks() {
        return tricks;
    }

    public void setTricks(int tricks) {
        this.tricks = tricks;
    }

    public boolean isDoubled() {
        return doubled;
    }

    public void setDoubled(boolean doubled) {
        this.doubled = doubled;
    }

    public boolean isRedoubled() {
        return reDoubled;
    }

    public void setRedoubled(boolean redoubled) {
        reDoubled = redoubled;
    }
}
