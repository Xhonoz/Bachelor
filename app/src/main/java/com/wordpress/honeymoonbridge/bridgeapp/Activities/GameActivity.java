package com.wordpress.honeymoonbridge.bridgeapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wordpress.honeymoonbridge.bridgeapp.Fragments.BiddingFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PickCardFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PlayFragment;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.TopInLong;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.R;

public class GameActivity extends AppCompatActivity
        implements PickCardFragment.Callback,
        BiddingFragment.Callback,
        PlayFragment.Callback,
        Game.Callback,
        HandAdapter.Callback{

    //    Fragments
    private BiddingFragment mBiddingFragment;
    private PickCardFragment mPickCardFragment;
    private PlayFragment mPlayFragment;

    //    game
    private Game game;

    //    Adapters
    private HandAdapter handAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // Create the fragments used by the UI.
        mBiddingFragment = new BiddingFragment();

        mPickCardFragment = new PickCardFragment();

        mPlayFragment = new PlayFragment();


        game = new Game(true, new TopInLong());
        game.getGameState().getStack().shuffleCardStack();
        game.setCallback(this);

        mPickCardFragment.setGame(game);
        mBiddingFragment.setGame(game);


        switchToFragment(mPickCardFragment);


        handAdapter = new HandAdapter(new Hand(), (LinearLayout) findViewById(R.id.yourHand), getApplicationContext());
        handAdapter.setCallback(this);




    }

    @Override
    protected void onStart() {
        super.onStart();
        game.startPickingPhase();
    }

    // Switch UI to the given fragment
    private void switchToFragment(Fragment newFrag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag)
                .commit();
    }


    @Override
    public void AiPickedCard(boolean first) {
        mPickCardFragment.addToOpponentHand();

    }

    @Override
    public void AiBid(Bid bid) {

    }

    @Override
    public void AiPlayedCard(Card card) {
        mPlayFragment.setNorthPlayedCard(card);
        mPlayFragment.removeCardFromNorthHand();
    }

    @Override
    public void finishPicking() {
        Log.i("GameActivity", "finishPicking");
//        TODO: check if bidding is enabled
        switchToFragment(mBiddingFragment);
//        TODO: CHeck prefferences
        game.startBiddingPhase();

    }

    @Override
    public void finishBidding() {
        switchToFragment(mPlayFragment);
//        TODO: CHeck prefferences
        game.startPlayingPhase();
    }

    @Override
    public void finishPlaying() {

    }


    @Override
    public void Bid(Bid bid) {

        int resultCode = game.UIBid(bid);

        if (resultCode == 1) {

            mBiddingFragment.updateRecyclerViews();

        } else if (resultCode == 2)
            Toast.makeText(this, "This bid is not valid, you must bid over " + game.getGameState().getBiddingHistory().getLastNorthBid(), Toast.LENGTH_SHORT).show();
        else if (resultCode == 3)
            Toast.makeText(this, "It's not your turn", Toast.LENGTH_SHORT).show();


    }

    //TODO: Fix Redoubles
    @Override
    public void Double() {
        int resultCode = game.UIDouble();

        if (resultCode == 1) {

            mBiddingFragment.updateRecyclerViews();

        } else if (resultCode == 2)
            Toast.makeText(this, "You can't double now", Toast.LENGTH_SHORT).show();
        else if (resultCode == 3)
            Toast.makeText(this, "It's not your turn", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void pass() {

        if (game.UIPass())
            mBiddingFragment.updateRecyclerViews();
        else
            Toast.makeText(this, "It's not your turn", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void pickCard(boolean first) {

        Card picked = game.UIPickCard(first);
        if (picked != null)
            handAdapter.addToHand(picked);
        mPickCardFragment.updateChoiceUI();

    }

    @Override
    public void clickedCard(Card card) {
        if(game.getGameState().getPhase() == Phase.PLAYING)
            if(game.UIPlayCard(card)) {
                handAdapter.removeCard(card);
                mPlayFragment.setSouthPlayedCard(card);
            }
    }
}
