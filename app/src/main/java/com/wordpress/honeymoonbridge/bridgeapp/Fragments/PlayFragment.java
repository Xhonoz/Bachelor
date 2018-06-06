package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GameState;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters.TrickAdapter;
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
public class PlayFragment extends Fragment implements OpponentHand.Callback, TrickAdapter.Callback {

    private TrickAdapter trickAdapter;
    private Contract contract;


    private OpponentHand opponentHand;


    public void playCardFromOpponent(Card card){
//        opponentHand.startPlayAnimation(card, northPlayedCard.getImageView());
        northPlayCard(card);
    }

    public void removeCardFromNorthHand() {
        opponentHand.removeCard(0);
    }

    @Override
    public void finishedPlayAnimation(Card card) {
//        setNorthPlayedCard(card);
        mCallback.finishNorthPlayCardAnimation(card);

    }



    @Override
    public void playAnimationStart(Player player, Card card) {
        if(player == Player.SOUTH)
            mCallback.startSouthPlayingAnimation(card);
    }

    @Override
    public void playAnimationFinish(Player player, Card card) {
        if(player == Player.SOUTH)
            mCallback.finishSouthPlayingAnimation(card);
        else
            mCallback.finishNorthPlayCardAnimation(card);
    }

    @Override
    public void removeCardFromNorth() {
        opponentHand.removeCard(0);
    }

    public void wonTrick(Player player) {
        trickAdapter.playerWins(player);
    }


    public interface Callback {
        // TODO: Update argument type and name
        void finishNorthPlayCardAnimation(Card card);
        void startSouthPlayingAnimation(Card card);
        void finishSouthPlayingAnimation(Card card);
        void readyToStart();

    }


    private Callback mCallback;

    public PlayFragment() {
        // Required empty public constructor
    }

    public void northPlayCard(Card card) {
        opponentHand.removeCard(0);
        trickAdapter.addCard(Player.NORTH, card);

    }

    public void southPlayCard(Card card) {
        trickAdapter.addCard(Player.SOUTH, card);

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
        LinearLayout trickLinearLayout = (LinearLayout) view.findViewById(R.id.trick);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            trickLinearLayout.setZ(100f);
        }
        trickAdapter = new TrickAdapter(getContext(), trickLinearLayout);
        trickAdapter.setCallback(this);
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


    public OpponentHand getOpponentHand() {
        return opponentHand;
    }

    public Callback getmCallback() {
        return mCallback;
    }

    //TrickAdapter methods:



    public boolean trickInProgress(){
        return trickAdapter.isInProgress();
    }

    public boolean trickFinished(){
        return trickAdapter.isFinished();
    }

    public boolean trickNotStart(){
        return trickAdapter.isNotStarted();
    }

    public boolean northPlayCard(Card card, View fromView){
        return trickAdapter.addCard(Player.NORTH, card, fromView);
//        opponentHand.removeCard(0);

    }

    public void southPlayCard(Card card, View fromView){
        trickAdapter.addCard(Player.SOUTH, card, fromView);

    }
}
