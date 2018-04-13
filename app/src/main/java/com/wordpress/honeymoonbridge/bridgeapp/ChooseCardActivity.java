package com.wordpress.honeymoonbridge.bridgeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

public class ChooseCardActivity extends AppCompatActivity {

    private HandAdapter handAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);

        Button play = findViewById(R.id.trump);



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
        

    }

    public void onClickSecond(View view){


    }

    public void onClick(View view){

    }
}
