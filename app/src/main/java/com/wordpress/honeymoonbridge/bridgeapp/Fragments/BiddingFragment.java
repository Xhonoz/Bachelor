package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.wordpress.honeymoonbridge.bridgeapp.Adapters.BiddingHistoryAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.BiddingHistory;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;
import com.wordpress.honeymoonbridge.bridgeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class BiddingFragment extends Fragment implements View.OnClickListener {


    private RecyclerView northRecyclerView;
    private RecyclerView southRecyclerView;
    private RecyclerView.Adapter northAdapter;
    private RecyclerView.Adapter southAdapter;
    private RecyclerView.LayoutManager northLayoutManager;
    private RecyclerView.LayoutManager southLayoutManager;
    private NumberPicker LevelPicker;
    private NumberPicker TrumpPicker;

    private BiddingHistory biddingHistory;

    private Game game;



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
        void Bid(Bid bid);

        void Double();

        void pass();
    }

    private Callback mCallback;



    public void setUpNumberPickers(View view){
        LevelPicker = view.findViewById(R.id.np1);
        LevelPicker.setMaxValue(7);
        LevelPicker.setMinValue(1);
        LevelPicker.setWrapSelectorWheel(true);
        LevelPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        TrumpPicker = view.findViewById(R.id.np2);
        TrumpPicker.setMaxValue(5);
        TrumpPicker.setMinValue(1);
        TrumpPicker.setWrapSelectorWheel(true);
        TrumpPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        TrumpPicker.setDisplayedValues(new String[]{"♣", "♦", "♥", "♠", "NT"});



    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setUpRecyclerViews(View view){
        Log.i("BiddingFragment", "setUpRecyclerViews was called");
        northRecyclerView = (RecyclerView) view.findViewById(R.id.NorthHistory);
        southRecyclerView = (RecyclerView) view.findViewById(R.id.SouthHistory);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        northRecyclerView.setHasFixedSize(true);
        northRecyclerView = (RecyclerView) view.findViewById(R.id.NorthHistory);

        // use a linear layout manager

        northLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        southLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        northRecyclerView.setLayoutManager(northLayoutManager);
        southRecyclerView.setLayoutManager(southLayoutManager);




        // specify an adapter (see also next example)
        northAdapter = new BiddingHistoryAdapter(game.getGameState().getBiddingHistory().getNorth());
        southAdapter = new BiddingHistoryAdapter(game.getGameState().getBiddingHistory().getSouth());


        northRecyclerView.setAdapter(northAdapter);
        southRecyclerView.setAdapter(southAdapter);

    }

    public void updateRecyclerViews(){

        northAdapter = new BiddingHistoryAdapter(game.getGameState().getBiddingHistory().getNorth());
        southAdapter = new BiddingHistoryAdapter(game.getGameState().getBiddingHistory().getSouth());

        northRecyclerView.setAdapter(northAdapter);
        southRecyclerView.setAdapter(southAdapter);

        northLayoutManager.scrollToPosition(game.getGameState().getBiddingHistory().getNorth().size() - 1);
        southLayoutManager.scrollToPosition(game.getGameState().getBiddingHistory().getSouth().size() - 1);



    }

    private  void updateNumberPickers(){

//        Bid lastbid = biddingHistory.getLastNorthBid();
//        if(!lastbid.isPass()) {
//
//            int level = lastbid.getLevel();
//            int trumpInt = lastbid.getTrumpInt();


//            if the last bid was 7NT the numberPickers should be disabled
//            if ((level == 7 && trumpInt == 5)) {
//                LevelPicker.setEnabled(false);
//                TrumpPicker.setEnabled(false);
//
//            }
//             else {
//                if(trumpInt == 5) {
//                    LevelPicker.setValue(level + 1);
//                    LevelPicker.setMinValue(level + 1);
//                }else {
//                    LevelPicker.setValue(level);
//                    LevelPicker.setMinValue(level);
//
//                    LevelPicker.setWrapSelectorWheel(true);
//
////                    TrumpPicker.setValue(trumpInt + 1);
////                    TrumpPicker.setMinValue(trumpInt + 1);
//                }
//
//            }

//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bidding, container, false);

        setUpRecyclerViews(view);
        setUpNumberPickers(view);



        final int[] clickableIds = {

                R.id.bid_button,
                R.id.double_button,
                R.id.pass_button

        };

        for (int clickableId : clickableIds) {
            view.findViewById(clickableId).setOnClickListener(this);
        }

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bid_button:
                mCallback.Bid(new Contract(TrumpPicker.getValue() - 1, LevelPicker.getValue(), Player.SOUTH));
                break;
            case R.id.pass_button:
                mCallback.pass();
                break;
            case R.id.double_button:
                mCallback.Double();
                break;


        }
    }




}
