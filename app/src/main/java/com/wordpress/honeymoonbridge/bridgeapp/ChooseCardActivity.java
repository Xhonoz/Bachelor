package com.wordpress.honeymoonbridge.bridgeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.MockAI;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.CardViewAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

public class ChooseCardActivity extends AppCompatActivity {

    private HandAdapter handAdapter;
    private Game game;
    private CardViewAdapter cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);

        Button play = findViewById(R.id.trump);

        game = new Game(true, new MockAI());

        cardView = new CardViewAdapter((ImageView)findViewById(R.id.firstCard), getApplicationContext());

        updateChoiceUI();





        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseCardActivity.this, BiddingActivity.class);
                startActivity(i);
            }
        });


        handAdapter = new HandAdapter(new CardStack().hand(), (LinearLayout) findViewById(R.id.yourHand), getApplicationContext());
    }

    public void onClickFirst(View view){
        Card picked = game.UIPickCard(true);
        handAdapter.addToHand(picked);
        updateChoiceUI();
    }


    public void onClickSecond(View view){
        Card picked = game.UIPickCard(false);
        handAdapter.addToHand(picked);
        updateChoiceUI();

    }


    private void updateChoiceUI(){
        cardView.setCard(game.peakTopCard());

    }


    public void onClick(View view){

    }
}
