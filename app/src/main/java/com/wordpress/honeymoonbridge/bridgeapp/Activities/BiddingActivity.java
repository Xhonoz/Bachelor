package com.wordpress.honeymoonbridge.bridgeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.wordpress.honeymoonbridge.bridgeapp.Adapters.BiddingHistoryAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.Game;
import com.wordpress.honeymoonbridge.bridgeapp.GameLogic.GlobalInformation;
import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.BiddingHistory;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.R;

import java.util.ArrayList;

public class BiddingActivity extends AppCompatActivity {

    private RecyclerView northRecyclerView;
    private RecyclerView southRecyclerView;
    private RecyclerView.Adapter northAdapter;
    private RecyclerView.Adapter southAdapter;
    private RecyclerView.LayoutManager northLayoutManager;
    private RecyclerView.LayoutManager southLayoutManager;
    private NumberPicker LevelPicker;
    private NumberPicker TrumpPicker;

    private BiddingHistory biddingHistory;
    private HandAdapter handAdapter;
    private ArrayList<Card> hand;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bidding2);
        biddingHistory = new BiddingHistory();

        hand = new CardStack().hand();
        game = GlobalInformation.game;

        handAdapter = GlobalInformation.handAdapter;

        ((LinearLayout)findViewById(R.id.yourHand)).addView(handAdapter.getHandLayout());

        setUpRecyclerViews();
        setUpNumberPickers();


        Log.i("BiddingActivity", "" + ((LinearLayout) findViewById(R.id.yourHand)).getChildCount());
    }

    public void setUpNumberPickers(){
        LevelPicker = findViewById(R.id.np1);
        LevelPicker.setMaxValue(7);
        LevelPicker.setMinValue(1);
        LevelPicker.setWrapSelectorWheel(true);
        LevelPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        TrumpPicker = findViewById(R.id.np2);
        TrumpPicker.setMaxValue(5);
        TrumpPicker.setMinValue(1);
        TrumpPicker.setWrapSelectorWheel(true);
        TrumpPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        TrumpPicker.setDisplayedValues(new String[]{"♣", "♦", "♥", "♠", "NT"});



    }

    public void setUpRecyclerViews(){
        northRecyclerView = (RecyclerView) findViewById(R.id.NorthHistory);
        southRecyclerView = (RecyclerView) findViewById(R.id.SouthHistory);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        northRecyclerView.setHasFixedSize(true);
        northRecyclerView = (RecyclerView) findViewById(R.id.NorthHistory);

        // use a linear layout manager

        northLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        southLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        northRecyclerView.setLayoutManager(northLayoutManager);
        southRecyclerView.setLayoutManager(southLayoutManager);




        // specify an adapter (see also next example)
        northAdapter = new BiddingHistoryAdapter(biddingHistory.getNorth());
        southAdapter = new BiddingHistoryAdapter(biddingHistory.getSouth());


        northRecyclerView.setAdapter(northAdapter);
        southRecyclerView.setAdapter(southAdapter);

    }

    private void updateRecyclerViews(){

        northAdapter = new BiddingHistoryAdapter(game.getGameState().getBiddingHistory().getNorth());
        southAdapter = new BiddingHistoryAdapter(game.getGameState().getBiddingHistory().getSouth());

        northRecyclerView.setAdapter(northAdapter);
        southRecyclerView.setAdapter(southAdapter);

        northLayoutManager.scrollToPosition(biddingHistory.getNorth().size() - 1);
        southLayoutManager.scrollToPosition(biddingHistory.getSouth().size() - 1);



    }

    private  void updateNumberPickers(){

        Bid lastbid = biddingHistory.getLastNorthBid();
        if(!lastbid.isPass()) {

            int level = lastbid.getLevel();
            int trumpInt = lastbid.getTrumpInt();


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

        }

    }

    public void playGame(View view){
        Intent i = new Intent(BiddingActivity.this, ChooseCardActivity.class);
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                Intent i = new Intent(BiddingActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.item2:
                Intent is = new Intent(BiddingActivity.this, SettingsActivity.class);
                startActivity(is);
                return true;

            case R.id.item3:
                Toast toast2 = Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT);
                toast2.show();
                return true;

            default:
                return true;
        }
    }

    public void onClickBid(View view) {

        Bid bid = new Bid(LevelPicker.getValue(), TrumpPicker.getValue());

        int resultCode = game.UIBid(bid);

        if (resultCode == 1) {

            updateRecyclerViews();

        } else if (resultCode == 2)
            Toast.makeText(this, "This bid is not valid, you must bid over " + game.getGameState().getBiddingHistory().getLastNorthBid(), Toast.LENGTH_SHORT).show();
        else if (resultCode == 3)
            Toast.makeText(this, "It's not your turn", Toast.LENGTH_SHORT).show();

    }

//TODO: Fix Redoubles
    public void onClickDouble(View view){

        int resultCode = game.UIDouble();

        if (resultCode == 1) {

            updateRecyclerViews();

        } else if (resultCode == 2)
            Toast.makeText(this, "You can't double now", Toast.LENGTH_SHORT).show();
        else if (resultCode == 3)
            Toast.makeText(this, "It's not your turn", Toast.LENGTH_SHORT).show();



    }

    public void onClickPass(View view){

        if(game.UIPass())
            updateRecyclerViews();
        else
            Toast.makeText(this, "It's not your turn", Toast.LENGTH_SHORT).show();


    }


//    Does not handle double and redouble
    private boolean playerBidIsValid(Bid bid){
        if(!biddingHistory.isNorthEmpty()) {
            Bid lastBid = biddingHistory.getLastNorthBid();
            int lastLevel = lastBid.getLevel();
            int lastTrumpInt = lastBid.getTrumpInt();
            int newLevel = bid.getLevel();
            int newTrumpInt = bid.getTrumpInt();

            if(newLevel > lastLevel)
                return true;
            if((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
                return true;

            return false;
        }
        return true;

    }

    public void OpponentBid(Bid bid){

        biddingHistory.getNorth().add(bid);

        updateRecyclerViews();
        updateNumberPickers();

    }

}
