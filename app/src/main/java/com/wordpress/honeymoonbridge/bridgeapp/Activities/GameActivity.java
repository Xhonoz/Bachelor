package com.wordpress.honeymoonbridge.bridgeapp.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.wordpress.honeymoonbridge.bridgeapp.AI.TopInLong;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.BiddingFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.HandFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PickCardFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PlayFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.ResultFragment;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.GooglePlayGames.GooglePlayServices;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.GlobalInformation;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import java.util.Locale;

public class GameActivity extends AppCompatActivity
        implements PickCardFragment.Callback,
        BiddingFragment.Callback,
        PlayFragment.Callback,
        HandFragment.Callback,
        Game.Callback,
        ResultFragment.OnFragmentInteractionListener, TextToSpeech.OnInitListener {

    //    Fragments
    private TextToSpeech mTTS;
    private BiddingFragment mBiddingFragment;
    private PickCardFragment mPickCardFragment;
    private PlayFragment mPlayFragment;
    private ResultFragment mResultFragment;
    private HandFragment mFullHandFragment;
    private HandFragment mPlayingHandFragment;

    private Menu mOptionsMenu;

    //    game
    private Game game;


    private Card picked;

    //    Booleans
    boolean waiting = false;
    boolean donePicking = false;
    boolean doneBidding = false;
    boolean donePlaying = false;
    private ImageView emptyImageView;
    private String TAG = "GameActivity";

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

        game = new Game(GlobalInformation.southStarts, new TopInLong());
        GlobalInformation.southStarts = !GlobalInformation.southStarts;
        game.getGameState().getStack().shuffleCardStack();
        game.setCallback(this);

        mPickCardFragment.setGame(game);
        mBiddingFragment.setGame(game);
        mResultFragment.setGame(game);

        mFullHandFragment.setUpHandAdapter(this);
        mPlayingHandFragment.setUpHandAdapter(this);


        switchToFragment(mPickCardFragment);
        switchHandFragment(mPlayingHandFragment);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,1);

    }



    private void signInSilently() {
        Log.d(TAG, "signInSilently()");

        GooglePlayServices.mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
    }

    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTTS = new TextToSpeech(this,  this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
        if (requestCode == GooglePlayServices.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }


    @Override
    public void onInit(int status) {
        mTTS.setLanguage(Locale.US);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        mOptionsMenu = menu;

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                return true;

            case R.id.item2:
                Intent i = new Intent(GameActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.item3:
                onShowAchievmentPressed();
                return true;

            case R.id.item4:
                if(!GooglePlayServices.signedIn)
                    startSignInIntent();
                else
                    signOut();

                return true;

            default:
                return true;
        }
    }

    public void onShowAchievmentPressed() {
        if(GooglePlayServices.signedIn)
            GooglePlayServices.achievementsClient.getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, GooglePlayServices.RC_UNUSED);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleException(e, getString(R.string.achievements_exception));
                        }
                    });
        else
            Toast.makeText(getApplicationContext(), "You have to sign in to see achievments", Toast.LENGTH_SHORT).show();
    }

    private void handleException(Exception e, String details) {
        int status = 0;

        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }

        String message = getString(R.string.status_exception_error, details, status, e);

        new AlertDialog.Builder(GameActivity.this)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    private void startSignInIntent() {
        startActivityForResult(GooglePlayServices.mGoogleSignInClient.getSignInIntent(), GooglePlayServices.RC_SIGN_IN);
    }

    private void signOut() {
        Log.d(TAG, "signOut()");

        if (!GooglePlayServices.signedIn) {
            Log.w(TAG, "signOut() called, but was not signed in!");
            return;
        }

        GooglePlayServices.mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(TAG, "signOut(): " + (successful ? "success" : "failed"));

                        onDisconnected();
                    }
                });
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        GooglePlayServices.mPlayersClient = null;
        if (mOptionsMenu != null)
            mOptionsMenu.getItem(3).setTitle(R.string.signin);
//        ((TextView)findViewById(R.id.greetingView)).setText("Not signed in");
        GooglePlayServices.signedIn = false;
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected(): connected to Google APIs");

        GooglePlayServices.achievementsClient = Games.getAchievementsClient(this, googleSignInAccount);


        GooglePlayServices.mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);

        if (mOptionsMenu != null)
            mOptionsMenu.getItem(3).setTitle(R.string.signout);

        // Set the greeting appropriately on main menu
        GooglePlayServices.mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<com.google.android.gms.games.Player>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.android.gms.games.Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                        } else {
                            Exception e = task.getException();
                            handleException(e, getString(R.string.players_exception));
                            displayName = "???";
                        }


//                        ((TextView)findViewById(R.id.greetingView)).setText("Hello, " + displayName);
                    }
                });

        GooglePlayServices.signedIn = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        game.startPickingPhase();
        mPickCardFragment.newCardsUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        signInSilently();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String color = prefs.getString("backgroundcolor", "green");
        LinearLayout l = findViewById(R.id.background);
        Log.i("COLOR", color);
        switch (color) {
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
        if (first)
            mPlayFragment.setSouthPlayedCard(null);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean on = prefs.getBoolean("readCards", true);
        if(on)
mTTS.speak(card.toTTSString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void wonTrick(Player player) {
        mPlayFragment.updateTricks(game.getGameState());
    }

    @Override
    public void finishPicking() {
        Log.i("GameActivity", "finishPicking");
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
        if(GooglePlayServices.achievementsClient != null)
        GooglePlayServices.achievementsClient.unlock(getString(R.string.achievement_playAGame));
        donePlaying = true;
//        if both pass
        if (!doneBidding) {
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
        if (donePicking)
            startBidding();
        else
        picked = game.getCardFromDeck(first);
        if (picked != null) {
            mPickCardFragment.showCardPickedUI(first);
        }

    }

    private void startBidding() {
        switchToFragment(mBiddingFragment);
    }

    private void endPlaying() {
        switchToFragment(mResultFragment);
        switchHandFragment(mFullHandFragment);
    }

    @Override
    public void confirm() {
        addCardToHand();
    }


    @Override
    public void onClickedCard(Card card) {
        if (!waiting) {
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
        if (mPlayFragment.getNorthPlayedCard().getCard() == null)
            game.northTakeTurn();
    }

    @Override
    public void onFinishPickingCard(Card card) {
        if (donePicking)
            mPickCardFragment.removeBothCards();
        else
            mPickCardFragment.newCardsUI();

    }

    public void onClickScreen(View view) {
        if (donePlaying) {
            endPlaying();
        } else if (doneBidding) {
//            TODO do stuff
        } else if (donePicking) {
            startBidding();
        }

        if (game.getGameState().getPhase() == Phase.PICKING) {
            addCardToHand();

        }
        if (game.getGameState().getPhase() == Phase.PLAYING) {
            if (game.getGameState().getTricks().isEmpty())
                game.northTakeTurn();
            if (mPlayFragment.getNorthPlayedCard().getCard() != null && mPlayFragment.getSouthPlayedCard().getCard() != null) {
                waiting = false;
                Card nC = mPlayFragment.getNorthPlayedCard().getCard();
                Card sC = mPlayFragment.getSouthPlayedCard().getCard();
                mPlayFragment.setNorthPlayedCard(null);
                mPlayFragment.setSouthPlayedCard(null);
                game.northTakeTurn();
            }

        }


    }

    public void addCardToHand() {
        if (game.getGameState().getPhase() == Phase.PICKING && game.getGameState().isSouthTurn() && picked != null) {
            boolean first = picked.equals(game.peakTopCard());
            game.UIPickCard(first);
            mPickCardFragment.removeCard(first);
            mFullHandFragment.addToHand(picked);
            View fromView = (first ? mPickCardFragment.getFirstCardView().getImageView() : mPickCardFragment.getSecondCardView().getImageView());
//            ImageView newimg = mPlayingHandFragment.addEmptyImageview(picked);
                mPlayingHandFragment.addToHand(picked, fromView);
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
