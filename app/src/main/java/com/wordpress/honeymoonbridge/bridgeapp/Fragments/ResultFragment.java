package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wordpress.honeymoonbridge.bridgeapp.Activities.GameActivity;
import com.wordpress.honeymoonbridge.bridgeapp.Activities.MainActivity;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import org.w3c.dom.Text;


public class ResultFragment extends Fragment implements View.OnClickListener {



    private Game game;

    private OnFragmentInteractionListener mListener;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_result, container, false);
        int tricksTaken = 0;
        if(game.getGameState().getContract() != null) {
            if (game.getGameState().getContract().getPlayer() == Player.NORTH) {
                tricksTaken = game.getGameState().getNorthTricks();
                ((TextView) view.findViewById(R.id.contract)).setText(getString(R.string.contract) + " " + game.getGameState().getContract().toStringWithPlayerAndWithTricks(tricksTaken));
                ((TextView) view.findViewById(R.id.trickScore)).setText("-" + game.getGameState().getContract().contractTrickscore(tricksTaken));
                if(game.getGameState().getContract().contractOverUnder(tricksTaken) == 0) {
                    ((TextView) view.findViewById(R.id.overUnder)).setText("" + game.getGameState().getContract().contractOverUnder(tricksTaken));
                }else{
                    ((TextView) view.findViewById(R.id.overUnder)).setText("-" + game.getGameState().getContract().contractOverUnder(tricksTaken));

                }
                ((TextView) view.findViewById(R.id.bonus)).setText("-" + game.getGameState().getContract().contractBonus(tricksTaken));
                ((TextView) view.findViewById(R.id.totalPoints)).setText("-" + (game.getGameState().getContract().Points(tricksTaken)));
            }
            else if (game.getGameState().getContract().getPlayer() == Player.SOUTH) {
                tricksTaken = game.getGameState().getSouthTricks();

                ((TextView) view.findViewById(R.id.contract)).setText(getString(R.string.contract) + " " + game.getGameState().getContract().toStringWithPlayerAndWithTricks(tricksTaken));
                ((TextView) view.findViewById(R.id.trickScore)).setText("" + game.getGameState().getContract().contractTrickscore(tricksTaken));
                ((TextView) view.findViewById(R.id.overUnder)).setText("" + game.getGameState().getContract().contractOverUnder(tricksTaken));
                ((TextView) view.findViewById(R.id.bonus)).setText("" + game.getGameState().getContract().contractBonus(tricksTaken));
                ((TextView) view.findViewById(R.id.totalPoints)).setText("" + (game.getGameState().getContract().Points(tricksTaken)));
            }
        }else{
            ((TextView) view.findViewById(R.id.contract)).setText(getString(R.string.passed_out));
            ((TextView) view.findViewById(R.id.trickScore)).setText("0");
            ((TextView) view.findViewById(R.id.overUnder)).setText("0");
            ((TextView) view.findViewById(R.id.bonus)).setText("0");
            ((TextView) view.findViewById(R.id.totalPoints)).setText("0");
        }

        final int[] clickableIds = {

                R.id.replay,
                R.id.backToMain


        };

        for (int clickableId : clickableIds) {
            view.findViewById(clickableId).setOnClickListener(this);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.replay:
                Intent i = getActivity().getIntent();
                getActivity().finish();
                startActivity(i);

                break;

            case R.id.backToMain:
                Intent im = new Intent(getActivity(), MainActivity.class);
                startActivity(im);
                break;
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
