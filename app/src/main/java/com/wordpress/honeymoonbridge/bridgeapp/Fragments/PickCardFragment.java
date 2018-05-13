package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
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
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.ImageHelper;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;


public class PickCardFragment extends Fragment implements View.OnClickListener {

    private Game game;
    private OpponentHand opponentHand;

    private CardViewAdapter firstCardView;
    private CardViewAdapter secondCardView;

    private boolean showingCards = false;
    private int animationSpeed = 1000;


    public void setGame(Game game) {
        this.game = game;
    }

    public void addToOpponentHand() {
        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout)getView().findViewById(R.id.opponentHand)).getChildCount());

        opponentHand.addToHand();

        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout)getView().findViewById(R.id.opponentHand)).getChildCount());

    }

    public void addToOpponentHand( boolean first) {
        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout)getView().findViewById(R.id.opponentHand)).getChildCount());

        opponentHand.addToHand(first);

        Log.i("PickCardFragment", "OpponentHand.childCount: " + ((LinearLayout)getView().findViewById(R.id.opponentHand)).getChildCount());

    }


    public interface Callback {
        // called when the user presses the send button to submit a message
        void pickCard(boolean first);
        void confirm();
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


    public void showCardPickedUI(boolean first) {
        if (game.getGameState().getPhase() == Phase.PICKING) {
            showingCards = true;
            showCardPicked(first);


        }
    }

    public void startPickingCardAnimation(final Card card, final ImageView newImg){
        ImageView oldImg = null;
        if(firstCardView.getCard().equals(card))
            oldImg = firstCardView.getImageView();
        if(secondCardView.getCard().equals(card))
            oldImg = secondCardView.getImageView();
        if(oldImg != null) {

            int oW = oldImg.getWidth();
            int oH = oldImg.getHeight();
            int nW = newImg.getWidth() + 400;
            int nH = newImg.getHeight() + 400;


            double imageRatio = ((double) oW) / oH;
//        if(highligthedView != null && highligthedView.equals(oldImg))
//             imageRatio += HIGHLIGHT_MARGIN;

            double imageViewRatio = ((double) nW) / nW;

            float drawX;
            double drawWidth;
            float drawY;


            drawY = newImg.getY();
            drawWidth = (imageRatio / imageViewRatio) * nW;
            drawX = (int) (nW - drawWidth) / 2;

            float scalingFactor = (float) drawWidth / oW;


            int[] coordinatesOld = new int[2];
            int[] coordinatesNew = new int[2];
            oldImg.getLocationOnScreen(coordinatesOld);
            newImg.getLocationOnScreen(coordinatesNew);


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

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    newImg.setImageResource(ImageHelper.cards[card.getIndex()]);
//                    TODO: fix, do this in a callback in gameActiivity
                    newCardsUI();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    public void newCardsUI() {
        if (game.getGameState().getPhase() == Phase.PICKING) {
            showingCards = false;
            firstCardView.removeHighlight();
            secondCardView.removeHighlight();
            Card top = game.peakTopCard();
            if (top != null)
                firstCardView.setCard(game.peakTopCard());
            secondCardView.setBackside();
        }
    }

    public void removeBothCards(){
        showingCards = false;
        firstCardView.setCard(null);
        secondCardView.setCard(null);
    }

    private void showCardPicked(boolean first) {
        secondCardView.setCard(game.getCardFromDeck(false));
        if (first)
            firstCardView.addHighlight();
        else
            secondCardView.addHighlight();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.firstCard:
                if (showingCards)
                    mCallback.confirm();
                else
                    mCallback.pickCard(true);
                break;
            case R.id.secondCard:
                if (showingCards)
                    mCallback.confirm();
                else
                    mCallback.pickCard(false);
                break;


        }
    }
}
