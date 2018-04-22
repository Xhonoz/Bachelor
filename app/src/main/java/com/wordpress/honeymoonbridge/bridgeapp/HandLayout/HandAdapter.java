package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;

import java.util.ArrayList;

/**
 * Created by Eier on 10.04.2018.
 */

public class HandAdapter implements View.OnClickListener{

    Hand hand;

    LinearLayout handLayout;

    Context mContext;

    Trump trump;

    Callback mCallback;

    public interface Callback{
        void clickedCard(Card card);
    }


    public HandAdapter(Hand hand, LinearLayout handLayout, Context context) {
        mCallback = null;
        trump = Trump.NoTrump;
        this.hand = hand.clone();
        this.handLayout = handLayout;
        mContext = context;
        SetUpLayout();

    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public Trump getTrump() {
        return trump;
    }

    public void setTrump(Trump trump) {
        this.trump = trump;
    }

    public LinearLayout getHandLayout() {
        return handLayout;
    }

    public void SetUpLayout() {
        handLayout.removeAllViews();
        ArrayList<Card> sortedHand = hand.getSortedHand(trump);
        Log.i("HandAdapter", "hand size: " + sortedHand.size());
        for (int i = 0; i < sortedHand.size(); i++) {
            Log.i("HandAdapter", "Card: " + sortedHand.get(i));
            Log.i("HandAdapter", "hand size: " + sortedHand.size());
            addImageViewToLayout(sortedHand.get(i), (i == sortedHand.size() - 1));
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(Card card) {
        //Hugs å legge til margin på forrige kort
        hand.addCard(card);
        addImageViewToLayout(card, hand.getNewIndex(card, trump));
    }

    public void removeCard(Card card) {
//        TODO: Implement removeCard
//        handLayout.removeViewAt(posistion);
//
//        if (hand) != 0)
//            fixLast();


    }


//      <ImageView
//    android:layout_width="0dp"
//    android:onClick="onClick"
//    android:layout_weight="1"
//    android:layout_height="wrap_content"
//    android:src="@drawable/clubs_2"
//    android:layout_marginRight="-50dp"/>

    private void addImageViewToLayout(Card card, boolean last) {

        if (card != null) {
            Log.i("HandAdapter", "addImageViewToLayout");

            Log.i("HandAdapter", "Last card: " + last);

            ImageView view = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            if (!last) {

                params.setMargins(0, 0, -150, 0);

            } else if (hand.getSize() != 1) {
                fixLast();
            }


            view.setLayoutParams(params);


            int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

            view.setId(index);
            view.setOnClickListener(this);
            view.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                    ImageHelper.drawables[index]), 400, true));

            handLayout.addView(view);

        }
    }

    private void addImageViewToLayout(Card card, int index) {
        boolean last = index == hand.getSize() - 1;

        if (card != null) {
            Log.i("HandAdapter", "addImageViewToLayout with index: " + index);


            ImageView view = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            if (!last) {

                params.setMargins(0, 0, -150, 0);

            } else if (hand.getSize() != 1) {
                fixLast();
            }


            view.setLayoutParams(params);

            int indexCard = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

            view.setId(indexCard);
            view.setOnClickListener(this);
            view.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                    ImageHelper.drawables[indexCard]), 400, true));

            handLayout.addView(view, index);

        }
    }

    private void fixLast() {
        Log.i("HandAdapter", "Fix last is called");
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.weight = 1;
        params2.setMargins(0, 0, -150, 0);

        handLayout.getChildAt(handLayout.getChildCount() - 1).setLayoutParams(params2);

    }


    @Override
    public void onClick(View view) {
        if(mCallback != null) {
            int index = view.getId();
            Card card = new Card(index);
            mCallback.clickedCard(card);
        }
    }
}
