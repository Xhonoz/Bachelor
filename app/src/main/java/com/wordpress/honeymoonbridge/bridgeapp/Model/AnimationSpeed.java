package com.wordpress.honeymoonbridge.bridgeapp.Model;

public class AnimationSpeed {

    private static int play_ms;
    private static int draw_ms;
    private static int discard_ms;
    private static int win_ms;


    static private Speed speed;

    public static int getPlay_ms() {
        return play_ms;
    }

    public static int getDraw_ms() {
        return draw_ms;
    }

    public static int getDiscard_ms() {
        return discard_ms;
    }

    public static int getWin_ms() {
        return win_ms;
    }

    public static void setSpeed(Speed speed) {
        AnimationSpeed.speed = speed;
        switch (speed) {
//            Just a suggestion, may have to change the numbers
            case SLOW:
                play_ms = 600;
                draw_ms = 700;
                discard_ms = 700;
                win_ms = 300;
                break;
            case MEDIUM:
                play_ms = 400;
                draw_ms = 500;
                discard_ms = 500;
                win_ms = 200;
                break;
            case FAST:
                play_ms = 200;
                draw_ms = 300;
                discard_ms = 300;
                win_ms = 100;
                break;
        }
    }

    public static Speed getSpeed() {
        return speed;
    }
}
