package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Eier on 10.04.2018.
 */

public class HandAdapter {

    ArrayList<Card> hand;

    LinearLayout handLayout;

    Context mContext;



    int[] drawables = {
            R.drawable.clubs_2,
            R.drawable.clubs_3,
            R.drawable.clubs_4,
            R.drawable.clubs_5,
            R.drawable.clubs_6,
            R.drawable.clubs_7,
            R.drawable.clubs_8,
            R.drawable.clubs_9,
            R.drawable.clubs_10,
            R.drawable.jack_of_clubs2,
            R.drawable.queen_of_clubs2,
            R.drawable.king_of_clubs2,
            R.drawable.ace_of_clubs,
            R.drawable.diamonds_2,
            R.drawable.diamonds_3,
            R.drawable.diamonds_4,
            R.drawable.diamonds_5,
            R.drawable.diamonds_6,
            R.drawable.diamonds_7,
            R.drawable.diamonds_8,
            R.drawable.diamonds_9,
            R.drawable.diamonds_10,
            R.drawable.jack_of_diamonds2,
            R.drawable.queen_of_diamonds2,
            R.drawable.king_of_diamonds2,
            R.drawable.ace_of_diamonds,
            R.drawable.hearts_2,
            R.drawable.hearts_3,
            R.drawable.hearts_4,
            R.drawable.hearts_5,
            R.drawable.hearts_6,
            R.drawable.hearts_7,
            R.drawable.hearts_8,
            R.drawable.hearts_9,
            R.drawable.hearts_10,
            R.drawable.jack_of_hearts2,
            R.drawable.queen_of_hearts2,
            R.drawable.king_of_hearts2,
            R.drawable.ace_of_hearts,
            R.drawable.spades_2,
            R.drawable.spades_3,
            R.drawable.spades_4,
            R.drawable.spades_5,
            R.drawable.spades_6,
            R.drawable.spades_7,
            R.drawable.spades_8,
            R.drawable.spades_9,
            R.drawable.spades_10,
            R.drawable.jack_of_spades2,
            R.drawable.queen_of_spades2,
            R.drawable.king_of_spades2,
            R.drawable.ace_of_spades2,


    };

    public HandAdapter(ArrayList<Card> hand, LinearLayout handLayout, Context context) {
        this.hand = (ArrayList<Card>) hand.clone();
        this.handLayout = handLayout;
        mContext = context;
        SetUpLayout();
    }

    public void SetUpLayout(){
        handLayout.removeAllViews();
        for(int i = 0; i < hand.size(); i++){
            addImageViewToLayout(hand.get(i), (i == hand.size()-1));
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(Card card){
        hand.add(card);
        //Hugs å legge til margin på forrige kort
        addImageViewToLayout(card, true);
    }

    public void removeCard(int posistion){
        handLayout.removeViewAt(posistion);
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


        Log.i("HandAdapter", "addImageViewToLayout");

        Log.i("HandAdapter", "Last card: " + last);

        ImageView view = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        if(!last) {

            params.setMargins(0, 0, -150, 0);

        }else{
            fixLast();
        }

        view.setLayoutParams(params);

        int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

        Log.i("handAdapter", "index: " + index);
        Random rng = new Random();

        view.setImageBitmap(scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                drawables[index]), 400, true));

        handLayout.addView(view);

    }

    private void fixLast(){
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.weight = 1;
        params2.setMargins(0, 0, -150, 0);

        handLayout.getChildAt(handLayout.getChildCount()-1).setLayoutParams(params2);

    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

//    private int getImageId(Card card) {
//
//        return drawables[index];
//
//    }


}
