package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;


public class PickCardFragment extends Fragment implements View.OnClickListener {

    private Game game;
    private OpponentHand opponentHand;

    private CardViewAdapter firstCardView;
    private CardViewAdapter secondCardView;


    public void setGame(Game game) {
        this.game = game;
    }

    public void addToOpponentHand() {
        Log.i("PickCardFragment", "AddToOpponentHand");
        opponentHand.addToHand();
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


        final int[] clickableIds = {

                R.id.firstCard,
                R.id.secondCard


        };

        for (int clickableId : clickableIds) {
            view.findViewById(clickableId).setOnClickListener(this);
        }

        firstCardView.setCard(game.peakTopCard());

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


    public void updateChoiceUI() {
        if (game.getGameState().getPhase() == Phase.PICKING) {

            showCardPicked();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 0.5s = 500ms
                    Card top = game.peakTopCard();
                    if (top != null)
                        firstCardView.setCard(game.peakTopCard());
                    secondCardView.setCard(null);

                }
            }, 500);


        }
    }

    private void showCardPicked(){
        secondCardView.setCard(game.getGameState().getSouth26Cards().get(game.getGameState().getSouth26Cards().size()-1));
    }

    private void addCardToOpponentHand() {
        opponentHand.addToHand();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.firstCard:
                mCallback.pickCard(true);
                break;
            case R.id.secondCard:
                mCallback.pickCard(false);
                break;


        }
    }
}
