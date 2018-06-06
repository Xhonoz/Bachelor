package com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;

public class TrickAdapter {

    private LinearLayout trickLayout;
    private ImageView southCardView;
    private Card southCard;
    private ImageView northCardView;
    private Card northCard;
    private Context mContext;

    private Callback mCallback;
    private int animationSpeed = 500;

    private String TAG = "TrickAdapter";

    public TrickAdapter(Context context, LinearLayout ll){
        mContext = context;
        trickLayout = ll;
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                0);
//        params.bottomMargin = - 50;
        params.weight = 1;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                0);
//        params2.bottomMargin = - 50;
        params2.weight = 1;
        northCardView = new ImageView(context);
        northCardView.setLayoutParams(params);
        ll.addView(northCardView);
        southCardView = new ImageView(mContext);
        southCardView.setLayoutParams(params2);
        ll.addView(southCardView);
    }

    public void addCard(Player player, Card card){
        if(card != null) {
            if (player == Player.NORTH) {
                northCard = card;
                switchImage(northCardView, card);
            } else if (player == Player.SOUTH) {
                southCard = card;
                switchImage(southCardView, card);
            }
        }
    }

    private void switchImage(ImageView v, Card card) {
        v.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                ImageHelper.cards[card.getIndex()]), ImageHelper.scaleDownImageSize, true));
    }

    public boolean isInProgress(){
        return (southCard != null && northCard == null)|| (northCard != null && southCard == null);
    }

    public boolean isFinished(){
        return southCard != null && northCard != null;
    }

    public boolean isNotStarted(){
        return southCard == null && northCard == null;
    }


    public void playerWins(Player player){
        Animation animation = new TranslateAnimation(0, 0, 0, (player == Player.NORTH  ? 500: -500));
        animation.setDuration(animationSpeed);
        southCardView.startAnimation(animation);
        northCardView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.cancel();
                southCardView.setImageBitmap(null);
                northCardView.setImageBitmap(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void clearCards(){
        southCard = null;
        southCardView.setImageBitmap(null);
        northCard = null;
        northCardView.setImageBitmap(null);
    }

    public void addCard(final Player player, final Card card, View fromView){
        final ImageView oldImg = (ImageView)fromView;
        final ImageView newImg;
        if(player == Player.NORTH)
            newImg = northCardView;
        else
            newImg = southCardView;
        addCard(player, card);
        int oW = oldImg.getWidth();
        int oH = oldImg.getHeight();
        int nW = newImg.getWidth();
        int nH = newImg.getHeight();


        double oldRatio = ((double) oW / oH);


        double newRatio = ((double) nW) / nH;

        float drawX;
        double drawWidth;

        drawWidth = (oldRatio/newRatio) * nW;
        drawX = (int)(nW - drawWidth)/2;

        float scalingFactor = (float)(oW/drawWidth);



        int[] coordinatesOld = new int[2];
        int[] coordinatesNew = new int[2];
        oldImg.getLocationOnScreen(coordinatesOld);
        newImg.getLocationOnScreen(coordinatesNew);

        float blav = oldImg.getX();


        AnimationSet set = new AnimationSet(false);

        float fromXDelta = ((coordinatesOld[0] - coordinatesNew[0]))/scalingFactor - drawX;
        float toXDelta = 0;
        float fromYDelta = (coordinatesOld[1] - coordinatesNew[1])/scalingFactor;
        float toYDelta = 0;

        Animation animation1 = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        animation1.setDuration(animationSpeed);
        Animation animation2 = new ScaleAnimation(scalingFactor,1f,scalingFactor,1f, Animation.ABSOLUTE,0f,Animation.ABSOLUTE,0f);
        animation2.setDuration(animationSpeed);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        newImg.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "animation finished");
                mCallback.playAnimationStart(player, card);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.playAnimationFinish(player, card);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        // TODO: Update argument type and name
        void playAnimationStart(Player player, Card card);
        void playAnimationFinish(Player player, Card card);

    }
}



