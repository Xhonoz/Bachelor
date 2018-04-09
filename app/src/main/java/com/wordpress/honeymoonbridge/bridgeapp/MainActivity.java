package com.wordpress.honeymoonbridge.bridgeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button play;
    Spinner chooseNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChooseCardActivity.class);
                startActivity(i);
            }
        });

        chooseNet = findViewById(R.id.spinnerChooseNet);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nets_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        chooseNet.setAdapter(adapter);

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
               Intent i = new Intent(MainActivity.this, SettingsActivity.class);
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
}
