package com.wordpress.honeymoonbridge.bridgeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BiddingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

    }

    public void playGame(View view){
        Intent i = new Intent(BiddingActivity.this, PlayActivity.class);
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
}
