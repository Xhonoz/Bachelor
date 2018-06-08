package com.wordpress.honeymoonbridge.bridgeapp.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.wordpress.honeymoonbridge.bridgeapp.AI.HoneymoonBridgePlayer;
import com.wordpress.honeymoonbridge.bridgeapp.AI.TopInLong;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.BiddingFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.HandFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.InformationFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PickCardFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.PlayFragment;
import com.wordpress.honeymoonbridge.bridgeapp.Fragments.ResultFragment;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Player;
import com.wordpress.honeymoonbridge.bridgeapp.GooglePlayGames.GooglePlayServices;
import com.wordpress.honeymoonbridge.bridgeapp.Model.AnimationSpeed;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.GlobalInformation;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Speed;
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


    //    Booleans
    boolean waiting = false;
    boolean donePicking = false;
    boolean doneBidding = false;
    boolean donePlaying = false;
    private ImageView emptyImageView;
    private String TAG = "GameActivity";

    private Player lastWinner;

    private Bid firstNorthBid = null;
    private boolean firstTime = true;

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String difficulty = prefs.getString("difficulty_pref", "medium");

        if(difficulty.equals("medium")) {
            game = new Game(GlobalInformation.southStarts, new HoneymoonBridgePlayer(2, 12));
        }else if(difficulty.equals("hard")){
            game = new Game(GlobalInformation.southStarts, new HoneymoonBridgePlayer(3, 25));

        }else{
            game = new Game(GlobalInformation.southStarts, new HoneymoonBridgePlayer(1, 5));

        }


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
        startActivityForResult(checkIntent, 1);


    }


    private void signInSilently() {
        GooglePlayServices.mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                        } else {
                            onDisconnected();
                        }
                    }
                });
    }

    @Override
    public void startSpinner() {
        MenuItem item = mOptionsMenu.getItem(0);
        item.setActionView(new ProgressBar(this));

    }

    @Override
    public void stopSpinner() {
        MenuItem item = mOptionsMenu.getItem(0);
        item.setActionView(null);
    }

    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTTS = new TextToSpeech(this, this);
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
                if (!GooglePlayServices.signedIn)
                    startSignInIntent();
                else
                    signOut();

                return true;

            default:
                return true;
        }
    }

    public void onShowAchievmentPressed() {
        if (GooglePlayServices.signedIn)
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
        if (!GooglePlayServices.signedIn) {
            return;
        }

        GooglePlayServices.mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        onDisconnected();
                    }
                });
    }

    private void onDisconnected() {
        GooglePlayServices.mPlayersClient = null;
        if (mOptionsMenu != null)
            mOptionsMenu.getItem(3).setTitle(R.string.signin);
//        ((TextView)findViewById(R.id.greetingView)).setText("Not signed in");
        GooglePlayServices.signedIn = false;
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean on = prefs.getBoolean("tutorial", true);

        if (firstTime) {
            firstTime = false;
            String str = (getResources().getText(R.string.pickingInformation)).toString();
            if (on) {
                InformationFragment i = InformationFragment.newInstance(str);

                i.show(getFragmentManager(), "information");


            }
        }

        mPickCardFragment.newCardsUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String as = prefs.getString("animationSpeed", "normal");
        if(as.equals("slow")){
            AnimationSpeed.setSpeed(Speed.SLOW);
        }
        if(as.equals("normal")){
            AnimationSpeed.setSpeed(Speed.NORMAL);
        }
        if(as.equals("fast")){
            AnimationSpeed.setSpeed(Speed.FAST);
        }

//        if (GooglePlayServices.signedIn)
//            Games.getGamesClient(GameActivity.this, GoogleSignIn.getLastSignedInAccount(this)).setViewForPopups(findViewById(android.R.id.content));

        signInSilently();


        String color = prefs.getString("backgroundcolor", "green");
        LinearLayout l = findViewById(R.id.background);
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
        mBiddingFragment.updateRecyclerViews();
    }

    @Override
    public void AiPlayedCard(Card card, boolean first) {

        boolean playedNow = mPlayFragment.northPlayCard(card, mPlayFragment.getOpponentHand().getLastView());
        if (mPlayFragment.trickFinished() && playedNow)
            turnOnTapIcon();
        else if (!game.getGameState().getSouthHand().getCardsOfSuit(card.getSuit()).isEmpty())
            mPlayingHandFragment.setPlayableSuit(card.getSuit());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean on = prefs.getBoolean("readCards", false);

        if (on)
            mTTS.speak(card.toTTSString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void wonTrick(Player player) {
        mPlayingHandFragment.setPlayableSuit(null);
        mPlayFragment.updateTricks(game.getGameState());
        lastWinner = player;
    }

    @Override
    public void finishPicking() {
        //        TODO: check if bidding is enabled
        donePicking = true;
//        TODO: CHeck prefferences
        turnOnTapIcon();

    }

    private void turnOffTapIcon() {
        ((ImageView) findViewById(R.id.tapView)).setVisibility(View.GONE);
    }

    private void turnOnTapIcon() {
        ((ImageView) findViewById(R.id.tapView)).setVisibility(View.VISIBLE);
    }

    @Override
    public void finishBidding() {
        doneBidding = true;
    }

    @Override
    public void startPlaying() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean on = prefs.getBoolean("tutorial", true);
        if (on) {
            InformationFragment i = InformationFragment.newInstance(getResources().getString(R.string.playingInformation));
            i.show(getFragmentManager(), "information");
        }
        mPlayingHandFragment.setCardsArePlayable(true);
        mPlayFragment.setContract(game.getGameState().getContract());
        switchToFragment(mPlayFragment);

    }


    @Override
    public void finishPlaying() {
        mPlayingHandFragment.setCardsArePlayable(false);
        if (GooglePlayServices.achievementsClient != null)
            GooglePlayServices.achievementsClient.unlock(getString(R.string.achievement_play_a_game));

        donePlaying = true;
//        if both pass
        if (!doneBidding) {
            endPlaying();

        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putBoolean("tutorial", false).commit();

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
    public void recycleViewsLoaded() {
        if (firstNorthBid != null)
            mBiddingFragment.updateRecyclerViews();
    }


    @Override
    public void pickCard(boolean first) {
        boolean b = game.getGameState().isSouthTurn();
        if (donePicking) {
            turnOffTapIcon();
            startBidding();

        } else {
            if (game.getGameState().getPhase() == Phase.PICKING && game.getGameState().isSouthTurn()) {

                final Card picked = game.UIPickCard(first);
                Card notPicked = game.getPreviousDiscard(Player.SOUTH);
                mPickCardFragment.discard(!first, notPicked);


                final View fromView = (first ? mPickCardFragment.getFirstCardView().getImageView() : mPickCardFragment.getSecondCardView().getImageView());


                mFullHandFragment.addToHand(picked);
                mPlayingHandFragment.addToHand(picked, fromView);


                if (mPlayingHandFragment.getHandAdapter().getHandLayout().getChildCount() < 13)
                    mPickCardFragment.newCardsUI();
                else
                    mPickCardFragment.removeBothCards();

            }
        }

    }

    private void startBidding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.fragment_container).setZ(10f);
        }
        switchToFragment(mBiddingFragment);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean on = prefs.getBoolean("tutorial", true);

        if (on) {
            InformationFragment i = InformationFragment.newInstance(getResources().getString(R.string.biddingInformation));

            i.show(getFragmentManager(), "information");
        }
    }

    private void endPlaying() {
        switchToFragment(mResultFragment);
        switchHandFragment(mFullHandFragment);
    }


    @Override
    public void onClickedCard(Card card) {
        if (!waiting) {
            if (game.getGameState().getPhase() == Phase.PLAYING) {
                if (game.UIPlayCard(card)) {
                    if (mPlayFragment.trickFinished())
                        mPlayFragment.wonTrick(lastWinner);
                    waiting = true;
                    mPlayFragment.southPlayCard(card, mPlayingHandFragment.getHandAdapter().getHandLayout().findViewById(card.getIndex()));
                    if (mPlayFragment.trickFinished())
                        turnOnTapIcon();
                }


            }
        }
    }

    @Override
    public void onFinishPlayingCard(Card card) {


    }

    @Override
    public void onFinishPickingCard(Card card) {
        if (donePicking)
            mPickCardFragment.removeBothCards();


    }

    public void onClickScreen(View view) {
        turnOffTapIcon();
        if (donePlaying) {
            endPlaying();
        } else if (doneBidding) {
//            TODO do stuff
        } else if (donePicking) {
            startBidding();
        }

//        if (game.getGameState().getPhase() == Phase.PICKING) {
//            addCardToHand();
//        }
        if (game.getGameState().getPhase() == Phase.PLAYING) {
            if (game.getGameState().getTricks().isEmpty())
                game.northTakeTurn();
            if (mPlayFragment.trickFinished()) {
                waiting = false;
                mPlayFragment.wonTrick(lastWinner);
                game.northTakeTurn();
            }

        }


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void finishNorthPlayCardAnimation(Card card) {

    }

    @Override
    public void startSouthPlayingAnimation(Card card) {
        mPlayingHandFragment.removeCard(card);
    }

    @Override
    public void finishSouthPlayingAnimation(Card card) {
        waiting = false;
        if (mPlayFragment.trickInProgress())
            game.northTakeTurn();
    }

    @Override
    public void readyToStart() {
        mPlayFragment.setStartPlayer((game.getGameState().isSouthTurn() ? Player.SOUTH : Player.NORTH));
        game.northTakeTurn();
    }


}
