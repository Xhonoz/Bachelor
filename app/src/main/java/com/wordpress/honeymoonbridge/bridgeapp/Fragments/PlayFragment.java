package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GameState;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;
import com.wordpress.honeymoonbridge.bridgeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment implements OpponentHand.Callback {

    private CardViewAdapter northPlayedCard;
    private CardViewAdapter southPlayedCard;

    private Contract contract;


    private OpponentHand opponentHand;

    public void setNorthPlayedCard(Card card) {
        northPlayedCard.setCard(card);
    }

    public void setSouthPlayedCard(Card card) {
        southPlayedCard.setCard(card);
    }

    public void playCardFromOpponent(Card card){
        opponentHand.startPlayAnimation(card, northPlayedCard.getImageView());
    }

    public void removeCardFromNorthHand() {
        opponentHand.removeCard(0);
    }

    @Override
    public void finishedPlayAnimation(Card card) {
        setNorthPlayedCard(card);
        mCallback.finishOpponentPlayCardAnimation(card);
    }


    public interface Callback {
        // TODO: Update argument type and name
        void finishOpponentPlayCardAnimation(Card card);
        void readyToStart();

    }


    private Callback mCallback;

    public PlayFragment() {
        // Required empty public constructor
    }

    public void northPlayCard(Card card) {
        opponentHand.removeCard(0);
        northPlayedCard.setCard(card);

    }

    public void SouthPlayCard(Card card) {
        southPlayedCard.setCard(card);

    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);

        opponentHand = new OpponentHand((LinearLayout) view.findViewById(R.id.opponentHand), getContext(), 13);
        opponentHand.setmCallback(this);
        northPlayedCard = new CardViewAdapter((ImageView) view.findViewById(R.id.OpponentPlayedCard), getContext());
        southPlayedCard = new CardViewAdapter((ImageView) view.findViewById(R.id.YourPlayedCard), getContext());
        if (contract != null)
            ((TextView) view.findViewById(R.id.contractView)).setText(contract.toStringWithPlayer());
        ((TextView)view.findViewById(R.id.northTrickView)).setText("N: 0");
        ((TextView)view.findViewById(R.id.southTrickView)).setText("S: 0");


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public void updateTricks(GameState gm){
        ((TextView)getView().findViewById(R.id.northTrickView)).setText("N: " + gm.getNorthTricks());
        ((TextView)getView().findViewById(R.id.southTrickView)).setText("S: " + gm.getSouthTricks());
    }

    public CardViewAdapter getNorthPlayedCard() {
        return northPlayedCard;
    }

    public CardViewAdapter getSouthPlayedCard() {
        return southPlayedCard;
    }

    public OpponentHand getOpponentHand() {
        return opponentHand;
    }

    public Callback getmCallback() {
        return mCallback;
    }
}
