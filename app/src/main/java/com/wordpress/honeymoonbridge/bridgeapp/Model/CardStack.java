package com.wordpress.honeymoonbridge.bridgeapp.Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Carmen on 09.04.2018.
 */

public class CardStack extends ArrayList{

    public CardStack(){
        this.add(new Card(Suit.Clubs,2));
        this.add(new Card(Suit.Clubs,3));
        this.add(new Card(Suit.Clubs,4));
        this.add(new Card(Suit.Clubs,5));
        this.add(new Card(Suit.Clubs,6));
        this.add(new Card(Suit.Clubs,7));
        this.add(new Card(Suit.Clubs,8));
        this.add(new Card(Suit.Clubs,9));
        this.add(new Card(Suit.Clubs,10));
        this.add(new Card(Suit.Clubs,11));
        this.add(new Card(Suit.Clubs,12));
        this.add(new Card(Suit.Clubs,13));
        this.add(new Card(Suit.Clubs,14));

        this.add(new Card(Suit.Diamonds,2));
        this.add(new Card(Suit.Diamonds,3));
        this.add(new Card(Suit.Diamonds,4));
        this.add(new Card(Suit.Diamonds,5));
        this.add(new Card(Suit.Diamonds,6));
        this.add(new Card(Suit.Diamonds,7));
        this.add(new Card(Suit.Diamonds,8));
        this.add(new Card(Suit.Diamonds,9));
        this.add(new Card(Suit.Diamonds,10));
        this.add(new Card(Suit.Diamonds,11));
        this.add(new Card(Suit.Diamonds,12));
        this.add(new Card(Suit.Diamonds,13));
        this.add(new Card(Suit.Diamonds,14));

        this.add(new Card(Suit.Hearts,2));
        this.add(new Card(Suit.Hearts,3));
        this.add(new Card(Suit.Hearts,4));
        this.add(new Card(Suit.Hearts,5));
        this.add(new Card(Suit.Hearts,6));
        this.add(new Card(Suit.Hearts,7));
        this.add(new Card(Suit.Hearts,8));
        this.add(new Card(Suit.Hearts,9));
        this.add(new Card(Suit.Hearts,10));
        this.add(new Card(Suit.Hearts,11));
        this.add(new Card(Suit.Hearts,12));
        this.add(new Card(Suit.Hearts,13));
        this.add(new Card(Suit.Hearts,14));

        this.add(new Card(Suit.Spades,2));
        this.add(new Card(Suit.Spades,3));
        this.add(new Card(Suit.Spades,4));
        this.add(new Card(Suit.Spades,5));
        this.add(new Card(Suit.Spades,6));
        this.add(new Card(Suit.Spades,7));
        this.add(new Card(Suit.Spades,8));
        this.add(new Card(Suit.Spades,9));
        this.add(new Card(Suit.Spades,10));
        this.add(new Card(Suit.Spades,11));
        this.add(new Card(Suit.Spades,12));
        this.add(new Card(Suit.Spades,13));
        this.add(new Card(Suit.Spades,14));

    }

    //for testing
    public ArrayList<Card> hand(){
    shuffleCardStack();
    ArrayList<Card> temp = new ArrayList<>();
    for(int i = 0; i < 13; i++){
        temp.add((Card)this.get(i));
       this.remove(i);
    }
     return temp;
    }

    public void shuffleCardStack(){
        Collections.shuffle(this);
    }


}
