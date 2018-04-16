package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Eier on 10.04.2018.
 */

public class HandAdapter {

    ArrayList<Card> hand;

    LinearLayout handLayout;

    Context mContext;





    public HandAdapter(ArrayList<Card> hand, LinearLayout handLayout, Context context) {
        this.hand = (ArrayList<Card>) hand.clone();
        this.handLayout = handLayout;
        mContext = context;
        SetUpLayout();
    }

    public void SetUpLayout(){
        handLayout.removeAllViews();
        Log.i("HandAdapter", "hand size: " + hand.size());
        for(int i = 0; i < hand.size(); i++){
            Log.i("HandAdapter", "Card: " + hand.get(i));
            Log.i("HandAdapter", "hand size: " + hand.size());
            addImageViewToLayout(hand.get(i), (i == hand.size()-1));
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(Card card){
        //Hugs å legge til margin på forrige kort
        hand.add(card);
        addImageViewToLayout(card, true);
    }

    public void removeCard(int posistion){
        handLayout.removeViewAt(posistion);
    if(hand.size() != 0)
        fixLast();
    }


//      <ImageView
//    android:layout_width="0dp"
//    android:onClick="onClick"
//    android:layout_weight="1"
//    android:layout_height="wrap_content"
//    android:src="@drawable/clubs_2"
//    android:layout_marginRight="-50dp"/>

    private void addImageViewToLayout(Card card, boolean last) {

        if(card != null) {
            Log.i("HandAdapter", "addImageViewToLayout");

            Log.i("HandAdapter", "Last card: " + last);

            ImageView view = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            if (!last) {

                params.setMargins(0, 0, -150, 0);

            } else if (hand.size() != 1) {
                fixLast();
            }


            view.setLayoutParams(params);

            int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;


            view.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                    ImageHelper.drawables[index]), 400, true));

            handLayout.addView(view);

        }
    }

    private void fixLast(){
        Log.i("HandAdapter", "Fix last is called");
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.weight = 1;
        params2.setMargins(0, 0, -150, 0);

        handLayout.getChildAt(handLayout.getChildCount()-1).setLayoutParams(params2);

    }




}
