package com.wordpress.honeymoonbridge.bridgeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ImageView nextActivity = findViewById(R.id.YourPlayedCard);

        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlayActivity.this,ResultActivity.class);
                startActivity(i);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                Intent i = new Intent(PlayActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.item2:
                Intent is = new Intent(PlayActivity.this, SettingsActivity.class);
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

    public void onClick(View view){
        View target = findViewById(R.id.OpponentPlayedCard);
        target.getWidth();

        final int amountToMoveRight = (int)(target.getX() - view.getX());
        final int amountToMoveDown = (int)(view.getY() - target.getY());

        LinearLayout ll = findViewById(R.id.yourHand);

        TranslateAnimation anim = new TranslateAnimation(0, amountToMoveRight, 0, amountToMoveDown);
        anim.setDuration(1000);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
//                params.topMargin += amountToMoveDown;
//                params.leftMargin += amountToMoveRight;
//                view.setLayoutParams(params);
            }
        });

        view.startAnimation(anim);


    }


}
