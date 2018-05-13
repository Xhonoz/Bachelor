package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.telecom.Call;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;


/**
 * Created by Eier on 13.04.2018.
 */

public class OpponentHand {


    private LinearLayout handLayout;

    private Context mContext;

    private int length;

    private final String TAG = "OpponentHand";
    private int animationSpeed = 200;

    private Callback mCallback;

    public interface Callback{
        void finishedPlayAnimation(Card card);
    }

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public OpponentHand(LinearLayout handLayout, Context context, int length) {
        this.handLayout = handLayout;
        mContext = context;
        SetUpLayout(length);
    }

    public void SetUpLayout(int length){
        handLayout.removeAllViews();
        for(int i = 0; i < length; i++){
            addImageViewToLayout((i == length-1), -1);
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(){
        //Hugs å legge til margin på forrige kort
        addImageViewToLayout( true, -1);
    }

    public void addToHand(boolean first){
        //Hugs å legge til margin på forrige kort
        if(first)
            addImageViewToLayout( true, R.color.firstColor);
        else
            addImageViewToLayout(true, R.color.secondColor);
    }

    public void removeCard(int posistion){
        handLayout.removeViewAt(posistion);
        length --;
    }


//      <ImageView
//    android:layout_width="0dp"
//    android:onClick="onClick"
//    android:layout_weight="1"
//    android:layout_height="wrap_content"
//    android:src="@drawable/clubs_2"
//    android:layout_marginRight="-50dp"/>

    private void addImageViewToLayout(boolean last, int ColorId) {


        Log.i("HandAdapter", "addImageViewToLayout");

        Log.i("HandAdapter", "Last card: " + last);

        ImageView view = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        if(!last) {

            params.setMargins(0, 0, -150, 0);

        }else if(length != 0){
            fixNotLast();
        }

        view.setLayoutParams(params);

        length++;




        view.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.backside), ImageHelper.scaleDownImageSize, true));

        if(ColorId != -1){
            int highlightColor = mContext.getResources().getColor(ColorId);
             view.setColorFilter(highlightColor, PorterDuff.Mode.MULTIPLY);
        }


        handLayout.addView(view);

    }

    public void startPlayAnimation(final Card card, ImageView newLocation){

        Log.i(TAG, "Animation, on Card: " +  card);
        if(handLayout.getChildCount() > 0) {
            final ImageView oldImg = (ImageView)handLayout.getChildAt(handLayout.getChildCount() - 1);
            oldImg.setImageResource(ImageHelper.cards[card.getIndex()]);
            int oW = oldImg.getWidth();
            int oH = oldImg.getHeight();
            int nW = newLocation.getWidth();
            int nH = newLocation.getHeight();


            double imageRatio = ((double) oldImg.getWidth()) / oH;
//        if(highligthedView != null && highligthedView.equals(oldImg))
//             imageRatio += HIGHLIGHT_MARGIN;

            double imageViewRatio = ((double) newLocation.getWidth()) / newLocation.getHeight();

            float drawX;
            double drawWidth;
            float drawY;


            drawY = newLocation.getY();
            drawWidth = (imageRatio / imageViewRatio) * newLocation.getWidth();
            drawX = (int) (newLocation.getWidth() - drawWidth) / 2;

            float scalingFactor = (float) drawWidth / oldImg.getWidth();


            int[] coordinatesOld = new int[2];
            int[] coordinatesNew = new int[2];
            oldImg.getLocationOnScreen(coordinatesOld);
            newLocation.getLocationOnScreen(coordinatesNew);


            AnimationSet set = new AnimationSet(false);

            Animation animation1 = new TranslateAnimation(0, -(oldImg.getX() - drawX) / scalingFactor, 0, -(coordinatesOld[1] - coordinatesNew[1]) / scalingFactor);
            animation1.setDuration(animationSpeed);
            Animation animation2 = new ScaleAnimation(1f, scalingFactor, 1f, scalingFactor, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
            animation2.setDuration(animationSpeed);
            set.addAnimation(animation1);
            set.addAnimation(animation2);
            oldImg.startAnimation(set);

            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    removeCard(handLayout.getChildCount() - 1);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mCallback.finishedPlayAnimation(card);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

    }

    private void fixNotLast(){
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params2.weight = 1;
        params2.setMargins(0, 0, -150, 0);

        handLayout.getChildAt(handLayout.getChildCount()-1).setLayoutParams(params2);

    }




}
