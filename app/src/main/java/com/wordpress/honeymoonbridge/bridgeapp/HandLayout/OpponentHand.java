package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import java.util.ArrayList;

/**
 * Created by Eier on 13.04.2018.
 */

public class OpponentHand {


    private LinearLayout handLayout;

    private Context mContext;

    private int length;





    public OpponentHand(LinearLayout handLayout, Context context, int length) {
        this.handLayout = handLayout;
        mContext = context;
        SetUpLayout(length);
    }

    public void SetUpLayout(int length){
        handLayout.removeAllViews();
        for(int i = 0; i < length; i++){
            addImageViewToLayout((i == length-1));
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(){
        //Hugs å legge til margin på forrige kort
        addImageViewToLayout( true);
    }

    public void removeCard(int posistion){
        handLayout.removeViewAt(posistion);
        length --;
        if(length != 0)
            fixLast();
    }


//      <ImageView
//    android:layout_width="0dp"
//    android:onClick="onClick"
//    android:layout_weight="1"
//    android:layout_height="wrap_content"
//    android:src="@drawable/clubs_2"
//    android:layout_marginRight="-50dp"/>

    private void addImageViewToLayout(boolean last) {


        Log.i("HandAdapter", "addImageViewToLayout");

        Log.i("HandAdapter", "Last card: " + last);

        ImageView view = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        if(!last) {

            params.setMargins(0, 0, -150, 0);

        }else if(length != 0){
            fixLast();
        }

        view.setLayoutParams(params);

        length++;




        view.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.backside), 400, true));

        handLayout.addView(view);

    }

    private void fixLast(){
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.weight = 1;
        params2.setMargins(0, 0, -150, 0);

        handLayout.getChildAt(handLayout.getChildCount()-1).setLayoutParams(params2);

    }




}
