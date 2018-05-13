package com.wordpress.honeymoonbridge.bridgeapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wordpress.honeymoonbridge.bridgeapp.Fragments.BiddingFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.HandFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PickCardFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PlayFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.ResultFragment;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.AI.TopInLong;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.R;

public class GameActivity extends AppCompatActivity
        implements PickCardFragment.Callback,
        BiddingFragment.Callback,
        PlayFragment.Callback,
        HandFragment.Callback,
        Game.Callback,
        ResultFragment.OnFragmentInteractionListener
{

    //    Fragments
    private BiddingFragment mBiddingFragment;
    private PickCardFragment mPickCardFragment;
    private PlayFragment mPlayFragment;
    private ResultFragment mResultFragment;
    private HandFragment mFullHandFragment;
    private HandFragment mPlayingHandFragment;

    //    game
    private Game game;



    private Card picked;

//    Booleans
    boolean waiting = false;
    boolean donePicking = false;
    boolean doneBidding = false;
    boolean donePlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // Create the fragments used by the UI.
        mBiddingFragment = new BiddingFragment();

        mPickCardFragment = new PickCardFragment();

        mPlayFragment = new PlayFragment();

        mResultFragment = new ResultFragment();

        mFullHandFragment = new HandFragment();

        mPlayingHandFragment = new HandFragment();


        game = new Game(false, new TopInLong());
        game.getGameState().getStack().shuffleCardStack();
        game.setCallback(this);

        mPickCardFragment.setGame(game);
        mBiddingFragment.setGame(game);
        mResultFragment.setGame(game);

        mFullHandFragment.setUpHandAdapter(this);
        mPlayingHandFragment.setUpHandAdapter(this);


        switchToFragment(mPickCardFragment);
        switchHandFragment(mPlayingHandFragment);






    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                return true;

            case R.id.item2:
                Intent i = new Intent(GameActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.item3:
                Toast toast2 = Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT);
                toast2.show();
                return true;

            default:
                return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        game.startPickingPhase();
        mPickCardFragment.newCardsUI();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String color =  prefs.getString("backgroundcolor", "green");
        LinearLayout l = findViewById(R.id.background);
        Log.i("COLOR", color);
        switch (color){
            case "yellow":
                l.setBackgroundColor(getResources().getColor(R.color.yellow));

                break;

            case "green":
                l.setBackgroundColor(getResources().getColor(R.color.green));
                break;

            case "blue":
                l.setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case "red":
                l.setBackgroundColor(getResources().getColor(R.color.red));
                break;

            case "white":
                l.setBackgroundColor(getResources().getColor(R.color.white));
                break;

            case "pink":
                l.setBackgroundColor(getResources().getColor(R.color.pink));
                break;
        }
    }

    // Switch UI to the given fragment
    private void switchToFragment(Fragment newFrag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag)
                .commit();
    }


    @Override
    public void AiPickedCard(boolean first) {
        mPickCardFragment.addToOpponentHand(first);

    }

    @Override
    public void AiBid(Bid bid) {

    }

    @Override
    public void AiPlayedCard(Card card, boolean first) {
        mPlayFragment.playCardFromOpponent(card);
        if(first)
            mPlayFragment.setSouthPlayedCard(null);

    }

    @Override
    public void wonTrick(Player player) {
        mPlayFragment.updateTricks(game.getGameState());
    }

    @Override
    public void finishPicking() {
        Log.i("GameActivity", "finishPicking");
        mPickCardFragment.removeBothCards();
//        TODO: check if bidding is enabled
        donePicking = true;
//        TODO: CHeck prefferences

    }

    @Override
    public void finishBidding() {
        doneBidding = true;
    }
    @Override
    public void startPlaying() {
            mPlayFragment.setContract(game.getGameState().getContract());
            switchToFragment(mPlayFragment);

    }




    @Override
    public void finishPlaying() {
        Log.i("GameActivity: ", "" + game.getGameState().getInitialSouthHand().getSize());
        donePlaying = true;
//        if both pass
        if(!doneBidding) {
            endPlaying();
        }


    }

    private void switchHandFragment(HandFragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.handFragmentContainer, frag)
                .commit();
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
        int resultCode = game.UIDoubleOrRedouble();

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
        if(donePicking)
            startBidding();
        else {
            picked = game.getCardFromDeck(first);
            if (picked != null) {
                mPickCardFragment.showCardPickedUI(first);
            }
        }
    }

    private void startBidding(){
        switchToFragment(mBiddingFragment);
    }

    private void endPlaying(){
        switchToFragment(mResultFragment);
        switchHandFragment(mFullHandFragment);
    }

    @Override
    public void confirm() {
        addCardToHand();
    }


    @Override
    public void onClickedCard(Card card) {
        if(!waiting) {
            if (game.getGameState().getPhase() == Phase.PLAYING) {
                boolean shoudlBeLegal = game.isLegal(Player.SOUTH, card);
                if (game.UIPlayCard(card)) {
                    waiting = true;
                    mPlayingHandFragment.playCardFromHand(card, mPlayFragment.getSouthPlayedCard().getImageView());
                }
                if (shoudlBeLegal && !game.getGameState().getTricks().isEmpty() && game.getGameState().getTricks().get(game.getGameState().getTricks().size() - 1).SecondCard == null) {
                    mPlayFragment.setNorthPlayedCard(null);
                    mPlayFragment.setSouthPlayedCard(null);
                }

            }
        }
    }

    @Override
    public void onFinishPlayingCard(Card card) {
//        if(mPlayFragment.getNorthPlayedCard() != null)
//            waiting = false;
        mPlayFragment.setSouthPlayedCard(card);
        if(mPlayFragment.getNorthPlayedCard().getCard() == null)
            game.northTakeTurn();
    }

    public void onClickScreen(View view) {
        if(donePlaying){
            endPlaying();
        }else if(doneBidding){
//            TODO do stuff
        }
        else if(donePicking){
            startBidding();
        }

        if(game.getGameState().getPhase() == Phase.PICKING) {
                addCardToHand();

        }
        if(game.getGameState().getPhase() == Phase.PLAYING){
            if(game.getGameState().getTricks().isEmpty())
                game.northTakeTurn();
            if(mPlayFragment.getNorthPlayedCard().getCard() != null && mPlayFragment.getSouthPlayedCard().getCard() != null) {
                waiting = false;
                Card nC = mPlayFragment.getNorthPlayedCard().getCard();
                Card sC = mPlayFragment.getSouthPlayedCard().getCard();
                mPlayFragment.setNorthPlayedCard(null);
                mPlayFragment.setSouthPlayedCard(null);
                game.northTakeTurn();
            }

        }


    }

    public void addCardToHand(){
        if(game.getGameState().getPhase() == Phase.PICKING && game.getGameState().isSouthTurn() && picked != null) {
            game.UIPickCard(picked.equals(game.peakTopCard()));
            mFullHandFragment.addToHand(picked);

            ImageView newimg = mPlayingHandFragment.addEmptyImageview(picked);
            mPickCardFragment.startPickingCardAnimation(picked, newimg);
            picked = null;
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void finishOpponentPlayCardAnimation(Card card) {

    }

    @Override
    public void readyToStart() {
        game.northTakeTurn();
    }
}
