package com.wordpress.honeymoonbridge.bridgeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        final int amountToMoveRight = 700;
        final int amountToMoveDown = -800;

        LinearLayout ll = findViewById(R.id.yourHand);


        final View view = ll.getChildAt(0);
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
