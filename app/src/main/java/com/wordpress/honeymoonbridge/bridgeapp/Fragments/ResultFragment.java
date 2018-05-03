package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import org.w3c.dom.Text;


public class ResultFragment extends Fragment {



    private Game game;

    private OnFragmentInteractionListener mListener;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_result, container, false);
        ((TextView) view.findViewById(R.id.contract)).setText(getString(R.string.contract) + " " + game.getGameState().getContract());
        ((TextView)view.findViewById(R.id.trickScore)).setText("" + game.getGameState().getContract().Points(game.getGameState().getContract().getTricks()));
        ((TextView)view.findViewById(R.id.bonus)).setText("" + game.getGameState().getContract().Bonus(game.getGameState().getContract().getTricks()));
        ((TextView)view.findViewById(R.id.totalPoints)).setText("" + (game.getGameState().getContract().Bonus(game.getGameState().getContract().getTricks()) + game.getGameState().getContract().Points(game.getGameState().getContract().getTricks())) );


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
