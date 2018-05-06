package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;
import com.wordpress.honeymoonbridge.bridgeapp.R;


public class HandFragment extends Fragment implements HandAdapter.Callback {




    private Callback mCallback = null;

    private HandAdapter handAdapter;
    private OpponentHand o;

    private LinearLayout ll;

    public void setUpHandAdapter(Context context){
        ll = new LinearLayout(context);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        handAdapter = new HandAdapter(new Hand(), ll, context);
        handAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.i("HandFragment", "onCreateView is called");
        View view = inflater.inflate(R.layout.fragment_hand, container, false);
        ((LinearLayout)view.findViewById(R.id.HandLinearLayout)).addView(ll);

        return view;
    }

    public void addToHand(Card card){
        handAdapter.addToHand(card);

    }

    public void removeFromHand(Card card){
        handAdapter.removeCard(card);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("HandFragment", "onAttach is called");

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

    @Override
    public void clickedCard(Card card) {
        mCallback.onClickedCard(card);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        // TODO: Update argument type and name
        void onClickedCard(Card card);
    }
}
