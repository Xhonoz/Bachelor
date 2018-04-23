package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment {

    private CardViewAdapter northPlayedCard;
    private CardViewAdapter southPlayedCard;



    private OpponentHand opponentHand;

    public void setNorthPlayedCard(Card card) {
        northPlayedCard.setCard(card);
    }

    public void setSouthPlayedCard(Card card) {
        southPlayedCard.setCard(card);
    }

    public void removeCardFromNorthHand(){
        opponentHand.removeCard(0);
    }


    public interface Callback {
    // TODO: Update argument type and name




}


    private Callback mCallback;

    public PlayFragment() {
        // Required empty public constructor
    }

    public void northPlayCard(Card card){
        opponentHand.removeCard(0);
        northPlayedCard.setCard(card);

    }

    public void SouthPlayCard(Card card){
        southPlayedCard.setCard(card);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);

        opponentHand = new OpponentHand((LinearLayout) view.findViewById(R.id.opponentHand), getContext(), 13);
        northPlayedCard = new CardViewAdapter((ImageView) view.findViewById(R.id.OpponentPlayedCard), getContext());
        southPlayedCard = new CardViewAdapter((ImageView) view.findViewById(R.id.YourPlayedCard), getContext());
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


}
