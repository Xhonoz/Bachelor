package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters.ImageHelper;
import com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;


public class PickCardFragment extends Fragment implements View.OnClickListener {

    private Game game;
    private OpponentHand opponentHand;

    private CardViewAdapter firstCardView;
    private CardViewAdapter secondCardView;

    private boolean showingCards = false;
    private int animationSpeed = 500;
    private boolean waiting = false;


    public void setGame(Game game) {
        this.game = game;
    }

    public void addToOpponentHand() {
        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout) getView().findViewById(R.id.opponentHand)).getChildCount());

        opponentHand.addToHand();

        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout) getView().findViewById(R.id.opponentHand)).getChildCount());

    }

    public void addToOpponentHand(boolean first) {
        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout) getView().findViewById(R.id.opponentHand)).getChildCount());

        opponentHand.addToHand(first);

        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout) getView().findViewById(R.id.opponentHand)).getChildCount());

    }

    public void removeCard(boolean first) {
        if (first)
            firstCardView.setCard(null);
        else
            secondCardView.setCard(null);

    }


    public interface Callback {
        // called when the user presses the send button to submit a message
        void pickCard(boolean first);
    }

    private Callback mCallback = null;

    public PickCardFragment() {
        // Required empty public constructor
    }

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_card, container, false);

        opponentHand = new OpponentHand((LinearLayout) view.findViewById(R.id.opponentHand), getContext(), 0);
        firstCardView = new CardViewAdapter((ImageView) view.findViewById(R.id.firstCard), getContext());
        secondCardView = new CardViewAdapter((ImageView) view.findViewById(R.id.secondCard), getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById(R.id.discardLayout).setZ(10f);
        }


        final int[] clickableIds = {

                R.id.firstCard,
                R.id.secondCard


        };

        for (int clickableId : clickableIds) {
            view.findViewById(clickableId).setOnClickListener(this);
        }


        // Inflate the layout for this fragment
        return view;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    public void newCardsUI() {
        if (game.getGameState().getPhase() == Phase.PICKING) {
            Card top = game.peakTopCard();
            if (top != null)
                secondCardView.setBackside();
            else
                secondCardView.setCard(null);
            firstCardView.setCard(top);


        }
    }

    public void removeBothCards() {
        showingCards = false;
        firstCardView.setCard(null);
        secondCardView.setCard(null);
    }

    public void discard(boolean first, Card card) {
        View oldImg;
        View newImg = getView().findViewById(R.id.discards);
        if (!first)
            oldImg = secondCardView.getImageView();
        else
            oldImg = firstCardView.getImageView();
        switchImage((ImageView) newImg, card);
        int highlightColor = getContext().getResources().getColor(R.color.greyedOut);
        ((ImageView) newImg).setColorFilter(highlightColor, PorterDuff.Mode.MULTIPLY);

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
        animation1.setDuration(animationSpeed);
        Animation animation2 = new ScaleAnimation(scalingFactor, 1f, scalingFactor, 1f, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
        animation2.setDuration(animationSpeed);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        newImg.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                waiting = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void switchImage(ImageView v, Card card) {
        v.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(getContext().getResources(),
                ImageHelper.cards[card.getIndex()]), ImageHelper.scaleDownImageSize, true));
    }


    public CardViewAdapter getFirstCardView() {
        return firstCardView;
    }

    public CardViewAdapter getSecondCardView() {
        return secondCardView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.firstCard:
                if (!waiting) {
                    waiting = true;
                    mCallback.pickCard(true);
                }
                break;
            case R.id.secondCard:
                if (!waiting) {
                    waiting = true;
                    mCallback.pickCard(false);
                }
                break;


        }
    }
}
