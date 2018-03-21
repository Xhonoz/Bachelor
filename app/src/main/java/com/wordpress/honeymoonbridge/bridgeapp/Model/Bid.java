package com.wordpress.honeymoonbridge.bridgeapp.Model;

/**
 * Created by Eier on 20.03.2018.
 */

public class Bid {
    private Trump trump;
    private int level;
    private boolean isDouble = false;
    private boolean isRedouble = false;
    private boolean isPass = false;

    public boolean isDouble() {
        return isDouble;
    }

    public boolean isRedouble() {
        return isRedouble;
    }



    public Bid(){
        isPass = true;
        trump = null;
        level = -1;
    }

    public Bid(int level, String trump){

         this.level = level;

        switch(trump){

            case "♣":
                this.trump = Trump.Clubs;
                break;

            case "♦":
                this.trump = Trump.Diamonds;
                break;

            case "♥":
                this.trump = Trump.Hearts;
                break;

            case "♠":
                this.trump = Trump.Spades;
                break;

            case "NT":
                this.trump = Trump.NoTrump;
                break;

        }

    }

    public Bid(int level, int trump){

        this.level = level;

        switch(trump){

            case 1:
                this.trump = Trump.Clubs;
                break;

            case 2:
                this.trump = Trump.Diamonds;
                break;

            case 3:
                this.trump = Trump.Hearts;
                break;

            case 4:
                this.trump = Trump.Spades;
                break;

            case 5:
                this.trump = Trump.NoTrump;
                break;

        }

    }

    public Bid(int level,  Trump trump) {
        this.trump = trump;
        this.level = level;
    }

    public Bid(int level, String trump, boolean isDouble, boolean isRedouble){

        this.isDouble = isDouble;
        this.isRedouble = isRedouble;

        this.level = level;

        switch(trump){

            case "♣":
                this.trump = Trump.Clubs;
                break;

            case "♦":
                this.trump = Trump.Diamonds;
                break;

            case "♥":
                this.trump = Trump.Hearts;
                break;

            case "♠":
                this.trump = Trump.Spades;
                break;

            case "NT":
                this.trump = Trump.NoTrump;
                break;

        }

    }

    public Bid(int level, int trump, boolean isDouble, boolean isRedouble){

        this.isDouble = isDouble;
        this.isRedouble = isRedouble;

        this.level = level;

        switch(trump){

            case 1:
                this.trump = Trump.Clubs;
                break;

            case 2:
                this.trump = Trump.Diamonds;
                break;

            case 3:
                this.trump = Trump.Hearts;
                break;

            case 4:
                this.trump = Trump.Spades;
                break;

            case 5:
                this.trump = Trump.NoTrump;
                break;

        }

    }

    public Bid(int level,  Trump trump, boolean isDouble, boolean isRedouble) {
        this.isDouble = isDouble;
        this.isRedouble = isRedouble;
        this.trump = trump;
        this.level = level;
    }

    public boolean isPass() {
        return isPass;
    }

    public String toString(){

        if(isPass)
            return "PASS";

        if(isDouble)
            return "X";
        if(isRedouble)
            return "XX";

        String s = "" + level;
        switch(trump){

            case Clubs:
                s += "♣";
                break;

            case Diamonds:
                s += "♦";
                break;

            case Hearts:
                s += "♥";
                break;

            case Spades:
                s += "♠";
                break;

            case NoTrump:
                s += "NT";
                break;
        }
        return s;
    }

    public Trump getTrump() {
        return trump;
    }

    public int getTrumpInt(){
        switch(trump){

            case Clubs:
                return 1;

            case Diamonds:
                return 2;

            case Hearts:
                return 3;


            case Spades:
                return 4;

            case NoTrump:
                return 5;

        }
        return 0;
    }

    public int getLevel() {
        return level;
    }

    public void setTrump(Trump trump) {
        this.trump = trump;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
