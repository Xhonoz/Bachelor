package com.wordpress.honeymoonbridge.bridgeapp.Model;

import java.util.ArrayList;

/**
 * Created by Carmen on 13.04.2018.
 */

public class Hand {
    private ArrayList<Card> handClub;
    private ArrayList<Card> handSpade;
    private ArrayList<Card> handDiamond;
    private ArrayList<Card> handHeart;

    public Hand(){
        handClub = new ArrayList<>();
        handSpade = new ArrayList<>();
        handDiamond = new ArrayList<>();
        handHeart = new ArrayList<>();
    }

}
