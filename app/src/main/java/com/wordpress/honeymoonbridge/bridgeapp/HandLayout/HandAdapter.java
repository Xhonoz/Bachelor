package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import java.util.ArrayList;

/**
 * Created by Eier on 10.04.2018.
 */

public class HandAdapter {

    ArrayList<Card> hand;

    LinearLayout handLayout;

    Context mContext;

    public HandAdapter(ArrayList<Card> hand, LinearLayout handLayout, Context context) {
        this.hand = hand;
        this.handLayout = handLayout;
        mContext = context;
        SetUpLayout();
    }

    public void SetUpLayout(){
        handLayout.removeAllViews();
        for(int i = 0; i < hand.size(); i++){
            addImageViewToLayout(hand.get(i));
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(Card card){
        hand.add(card);
        addImageViewToLayout(card);
    }


//      <ImageView
//    android:layout_width="0dp"
//    android:onClick="onClick"
//    android:layout_weight="1"
//    android:layout_height="wrap_content"
//    android:src="@drawable/clubs_2"
//    android:layout_marginRight="-50dp"/>

    private void addImageViewToLayout(Card card) {

        Log.i("HandAdapter", "addImageViewToLayout");

        ImageView view = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,-50,0);
        params.weight = 1;
        view.setLayoutParams(params);

        view.setImageResource(getImageResource(card));

        handLayout.addView(view);


    }

    private int getImageResource(Card card) {
        Resources r = mContext.getResources();
        int[] ids = r.getIntArray(R.array.CardIDs);
        int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

        Log.i("handAdapter", "" + ids[index]);

        return ids[index];


    }


}
