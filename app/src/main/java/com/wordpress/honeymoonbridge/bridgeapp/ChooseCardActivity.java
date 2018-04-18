package com.wordpress.honeymoonbridge.bridgeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GlobalInformation;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.MockAI;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Phase;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.TopInLong;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.OpponentHand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;

import java.util.ArrayList;

public class ChooseCardActivity extends AppCompatActivity implements Game.Callback {

    private HandAdapter handAdapter;
    private OpponentHand opponentHand;
    private Game game;
    private CardViewAdapter cardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);

        Button play = findViewById(R.id.trump);

        game = new Game(true, new TopInLong());
        game.getGameState().getStack().shuffleCardStack();
        GlobalInformation.setGame(game);
        game.setCallback(this);

        cardView = new CardViewAdapter((ImageView)findViewById(R.id.firstCard), getApplicationContext());

        updateChoiceUI();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseCardActivity.this, BiddingActivity.class);
                startActivity(i);
            }
        });


        LinearLayout ll = new LinearLayout(getApplicationContext());
        ((LinearLayout)findViewById(R.id.yourHand)).addView(ll);

        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        opponentHand = new OpponentHand((LinearLayout) findViewById(R.id.opponentHand), getApplicationContext(), 0);
        handAdapter = new HandAdapter(new Hand(), ll, getApplicationContext());

        GlobalInformation.setHandAdapter(handAdapter);
    }

    public void onClickFirst(View view){
        Card picked = game.UIPickCard(true);
        if(picked != null)
            handAdapter.addToHand(picked);
        updateChoiceUI();
    }


    public void onClickSecond(View view){
        Card picked = game.UIPickCard(false);
        if(picked != null)
            handAdapter.addToHand(picked);
        updateChoiceUI();

    }


    private void updateChoiceUI(){
        if(game.getGameState().getPhase() == Phase.PICKING){
            Card top = game.peakTopCard();
            if(top != null)
                cardView.setCard(game.peakTopCard());
        }


    }


    public void onClick(View view){

    }


    private void addCardToOpponentHand() {
        opponentHand.addToHand();
    }


    @Override
    public void AiPickedCard(boolean first) {

        addCardToOpponentHand();

    }



    @Override
    public void AiBid(Bid bid) {

    }

    @Override
    public void AiPlayedCard(Card card) {

    }

    @Override
    public void finishBidding() {

    }

    @Override
    public void finishPicking() {
        //TODO: check settings if bidding is activated

        Intent intent = new Intent(ChooseCardActivity.this, BiddingActivity.class);
        startActivity(intent);

    }

    @Override
    public void finishPlaying() {

    }

    @Override
    public void onPause() {
        super.onPause();
        ((LinearLayout) findViewById(R.id.yourHand)).removeViewAt(0);
    }

}
