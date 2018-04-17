package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import android.widget.LinearLayout;

import com.wordpress.honeymoonbridge.bridgeapp.HandLayout.HandAdapter;

/**
 * Created by Eier on 16.04.2018.
 */

public class GlobalInformation {

    public static Game game;

    public static HandAdapter handAdapter;

    public static void setHandAdapter(HandAdapter handAdapter) {
        GlobalInformation.handAdapter = handAdapter;
    }

    public static void setGame(Game game) {
        GlobalInformation.game = game;
    }


}
