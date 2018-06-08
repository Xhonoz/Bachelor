package com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.Model.AnimationSpeed;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import java.util.ArrayList;

/**
 * Created by Eier on 10.04.2018.
 */

public class HandAdapter implements View.OnClickListener, View.OnTouchListener {

    Hand hand;

    LinearLayout handLayout;

    Context mContext;

    Trump trump;

    Callback mCallback;

    String TAG = "HandAdapter";

    View highligthedView = null;

    private final int MARGIN_RIGHT = -200;

    private final int HIGHLIGHT_MARGIN = -50;


    private Suit playableSuit = null;

    private boolean cardsAreSelectable = false;

    public void setPlayableSuit(Suit playableSuit) {
        this.playableSuit = playableSuit;
        for (int i = 0; i < handLayout.getChildCount(); i++) {
            View current = handLayout.getChildAt(i);
            if (playableSuit != null && new Card(current.getId()).getSuit() != playableSuit)
                greyOut(current);
            else
                removeGretOut(current);
        }

    }

    private void removeGretOut(View current) {
        if (current != null)
            ((ImageView) current).setColorFilter(null);
    }

    private void greyOut(View current) {
        if (current != null)
            ((ImageView) current).setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }

    public void setCardsAreSelectable(boolean cardsAreSelectable) {
        this.cardsAreSelectable = cardsAreSelectable;
    }

    private ArrayList<Rect> cardHitBoxes;    // Variable rect to hold the bounds of the view

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (cardsAreSelectable) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    changeHighlightedView(view);
                    cardHitBoxes = new ArrayList<>();
                    for (int i = 0; i < handLayout.getChildCount(); i++) {
                        View v = handLayout.getChildAt(i);
                        Rect rect = ImageHelper.getBitmapPositionInsideImageView((ImageView)v);
                        cardHitBoxes.add(rect);

                    }




                    break;


                case MotionEvent.ACTION_MOVE:
                    boolean outsideOfHand = true;

                    for (int i = 0; i < cardHitBoxes.size(); i++) {
                        Rect rect = cardHitBoxes.get(i);
                        View v = handLayout.getChildAt(i);
                        if (rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())) {
                            // User moved outside bounds
                            changeHighlightedView(v);
                            outsideOfHand = false;
                        }
                    }

                    if (outsideOfHand)
                        changeHighlightedView(null);
                    break;

                case MotionEvent.ACTION_UP:
                    if (highligthedView != null) {
                        Card card = new Card(highligthedView.getId());
                        changeHighlightedView(null);
                        mCallback.clickedCard(card);


                    }
                    break;


            }
        }
        return false;
    }

    public void startPlayCardAnimation(final Card card, final ImageView newImg, boolean highlighted) {
        final ImageView oldImg = handLayout.findViewById(card.getIndex());
        int oW = oldImg.getWidth();
        int oH = oldImg.getHeight();
        int nW = newImg.getWidth();
        int nH = newImg.getHeight();


        double imageRatio = ((double) oldImg.getWidth()) / oH;
//        if(highligthedView != null && highligthedView.equals(oldImg))
//             imageRatio += HIGHLIGHT_MARGIN;

        double imageViewRatio = ((double) newImg.getWidth()) / newImg.getHeight();

        float drawX;
        double drawWidth;
        float drawY;


        drawY = newImg.getY();
        drawWidth = (imageRatio / imageViewRatio) * newImg.getWidth();
        drawX = (int) (newImg.getWidth() - drawWidth) / 2;

        float scalingFactor = (float) drawWidth / oldImg.getWidth();


        int[] coordinatesOld = new int[2];
        int[] coordinatesNew = new int[2];
        oldImg.getLocationOnScreen(coordinatesOld);
        newImg.getLocationOnScreen(coordinatesNew);


        AnimationSet set = new AnimationSet(false);

        float fromXDelta = 0;
        float toXDelta = -(oldImg.getX() - drawX) / scalingFactor;
        float fromYDelta = HIGHLIGHT_MARGIN;
        float toYDelta = -(coordinatesOld[1] - ((highlighted) ? HIGHLIGHT_MARGIN : 0) - coordinatesNew[1]) / scalingFactor;

        Animation animation1 = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        animation1.setDuration(AnimationSpeed.getPlay_ms());
        Animation animation2 = new ScaleAnimation(1f, scalingFactor, 1f, scalingFactor, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
        animation2.setDuration(AnimationSpeed.getPlay_ms());
        animation1.setFillAfter(true);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        oldImg.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                removeCard(card);
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

    private void changeHighlightedView(View view) {

        if (highligthedView == null || !highligthedView.equals(view)) {
            if (highligthedView != null) {

                ((ImageView) highligthedView).setColorFilter(null);

                LinearLayout.LayoutParams newParams = (LinearLayout.LayoutParams) ((ImageView) highligthedView).getLayoutParams();
                newParams.bottomMargin = 0;
                newParams.topMargin = 0;
                highligthedView.setLayoutParams(newParams);
            }
            if (view != null && (playableSuit == null ||  (new Card(view.getId()).getSuit() == playableSuit))) {
                highligthedView = view;
                if (highligthedView != null) {
                    int highlightColor = mContext.getResources().getColor(R.color.highlight);
                    ((ImageView) highligthedView).setColorFilter(highlightColor, PorterDuff.Mode.MULTIPLY);
                    LinearLayout.LayoutParams newParams = (LinearLayout.LayoutParams) ((ImageView) highligthedView).getLayoutParams();
                    newParams.bottomMargin = -HIGHLIGHT_MARGIN;
                    newParams.topMargin = HIGHLIGHT_MARGIN;
                    highligthedView.setLayoutParams(newParams);

                    int newWidth = highligthedView.getWidth();

                }
            } else
                highligthedView = null;
        }
    }

    private void changeImage(ImageView v, int cardIndex) {
        v.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                ImageHelper.cards[cardIndex]), ImageHelper.scaleDownImageSize, true));
    }


    public interface Callback {
        void clickedCard(Card card);

        void finishedPlayAnimation(Card card);

        void finishEnteringAnimation(Card card);
    }


    public HandAdapter(Hand hand, LinearLayout handLayout, Context context) {
        mCallback = null;
        trump = Trump.NoTrump;
        this.hand = hand.clone();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            handLayout.setZ(-100f);
        }
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
        for (int i = 0; i < sortedHand.size(); i++) {
            addImageViewToLayout(sortedHand.get(i), (i == sortedHand.size() - 1));
        }
//        addImageViewToLayout(hand.get(0));
    }

    public void addToHand(Card card) {
        //TODO: make Async Task or other multiThread suport
        hand.addCard(card);
        addImageViewToLayout(card, hand.getNewIndex(card, trump), false);
    }

    public void addToHand(final Card card, final View fromView) {
        //TODO: make Async Task or other multiThread suport
        hand.addCard(card);
        final View view = addImageViewToLayout(card, hand.getNewIndex(card, trump), false);
        predrawAction(view, new Runnable() {
            @Override
            public void run() {
                startAnimationAddToHandAnimation(fromView, card);
            }
        });
    }

    private void startAnimationAddToHandAnimation(View fromView, final Card card) {
        final ImageView oldImg = (ImageView) fromView;
        final ImageView newImg = handLayout.findViewById(card.getIndex());
        int oW = oldImg.getWidth();
        int oH = oldImg.getHeight();
        int nW = newImg.getWidth();
        int nH = newImg.getHeight();


        double oldRatio = ((double) oW / oH);


        double newRatio = ((double) nW) / nH;

        float drawX;
        double drawWidth;

        drawWidth = (oldRatio / newRatio) * nW;
        drawX = (int) (nW - drawWidth) / 2;

        float scalingFactor = (float) (oW / drawWidth);


        int[] coordinatesOld = new int[2];
        int[] coordinatesNew = new int[2];
        oldImg.getLocationOnScreen(coordinatesOld);
        newImg.getLocationOnScreen(coordinatesNew);

        float blav = oldImg.getX();


        AnimationSet set = new AnimationSet(false);

        float fromXDelta = ((coordinatesOld[0] - coordinatesNew[0])) / scalingFactor - drawX;
        float toXDelta = 0;
        float fromYDelta = (coordinatesOld[1] - coordinatesNew[1]) / scalingFactor;
        float toYDelta = 0;

        Animation animation1 = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        animation1.setDuration(AnimationSpeed.getDraw_ms());
        Animation animation2 = new ScaleAnimation(scalingFactor, 1f, scalingFactor, 1f, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
        animation2.setDuration(AnimationSpeed.getDraw_ms());
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        newImg.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.finishEnteringAnimation(card);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public ImageView addEmptyImageView(Card card) {
        //TODO: make Async Task or other multiThread suport
        hand.addCard(card);
        return addImageViewToLayout(card, hand.getNewIndex(card, trump), true);
    }

    public void removeCard(Card card) {

        int cardIndex = card.getIndex();
        hand.removeCard(card);
        View child = handLayout.findViewById(cardIndex);
        if (handLayout.getChildCount() > 1 && handLayout.getChildAt(handLayout.getChildCount() - 1).equals(child)) {
            handLayout.removeView(child);
            fixLast();
        } else
            handLayout.removeView(child);


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

            ImageView view = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            params.gravity = Gravity.BOTTOM;
            if (!last) {

                params.setMargins(0, 0, MARGIN_RIGHT, 0);

            } else if (hand.getSize() != 1) {
                fixNotLast();
            }


            view.setLayoutParams(params);


            int index = card.getIndex();

            view.setId(index);
            view.setOnClickListener(this);
            view.setOnTouchListener(this);
            changeImage(view, index);


            handLayout.addView(view);

        }
    }

    private ImageView addImageViewToLayout(Card card, int index, boolean empty) {

        boolean last = index == hand.getSize() - 1;

        ImageView view = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.BOTTOM;
        if (!last) {

            params.setMargins(0, 0, MARGIN_RIGHT, 0);

        } else if (hand.getSize() != 1) {
            fixNotLast();
        }


        view.setLayoutParams(params);

        int indexCard = card.getIndex();
        view.setId(indexCard);
        view.setOnClickListener(this);
        view.setOnTouchListener(this);
        if (!empty)
            changeImage(view, indexCard);

        handLayout.addView(view, index);
        return view;
    }

    private void fixNotLast() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.BOTTOM;
        params.setMargins(0, 0, MARGIN_RIGHT, 0);

        handLayout.getChildAt(handLayout.getChildCount() - 1).setLayoutParams(params);

    }

    private void fixLast() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.BOTTOM;
        params.setMargins(0, 0, 0, 0);

        handLayout.getChildAt(handLayout.getChildCount() - 1).setLayoutParams(params);
    }


    @Override
    public void onClick(View view) {

    }

    public static void predrawAction(final View view, final Runnable runnable) {
        final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                runnable.run();
                return true;
            }
        };
        view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    }


}

