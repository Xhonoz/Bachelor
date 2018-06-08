package com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.wordpress.honeymoonbridge.bridgeapp.R;

/**
 * Created by Eier on 13.04.2018.
 */

public class ImageHelper {

    public static int scaleDownImageSize = 300;

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static int[] cards = {
            R.drawable.clubs_2,
            R.drawable.clubs_3,
            R.drawable.clubs_4,
            R.drawable.clubs_5,
            R.drawable.clubs_6,
            R.drawable.clubs_7,
            R.drawable.clubs_8,
            R.drawable.clubs_9,
            R.drawable.clubs_10,
            R.drawable.jack_of_clubs2,
            R.drawable.queen_of_clubs2,
            R.drawable.king_of_clubs2,
            R.drawable.ace_of_clubs,
            R.drawable.diamonds_2,
            R.drawable.diamonds_3,
            R.drawable.diamonds_4,
            R.drawable.diamonds_5,
            R.drawable.diamonds_6,
            R.drawable.diamonds_7,
            R.drawable.diamonds_8,
            R.drawable.diamonds_9,
            R.drawable.diamonds_10,
            R.drawable.jack_of_diamonds2,
            R.drawable.queen_of_diamonds2,
            R.drawable.king_of_diamonds2,
            R.drawable.ace_of_diamonds,
            R.drawable.hearts_2,
            R.drawable.hearts_3,
            R.drawable.hearts_4,
            R.drawable.hearts_5,
            R.drawable.hearts_6,
            R.drawable.hearts_7,
            R.drawable.hearts_8,
            R.drawable.hearts_9,
            R.drawable.hearts_10,
            R.drawable.jack_of_hearts2,
            R.drawable.queen_of_hearts2,
            R.drawable.king_of_hearts2,
            R.drawable.ace_of_hearts,
            R.drawable.spades_2,
            R.drawable.spades_3,
            R.drawable.spades_4,
            R.drawable.spades_5,
            R.drawable.spades_6,
            R.drawable.spades_7,
            R.drawable.spades_8,
            R.drawable.spades_9,
            R.drawable.spades_10,
            R.drawable.jack_of_spades2,
            R.drawable.queen_of_spades2,
            R.drawable.king_of_spades2,
            R.drawable.ace_of_spades2,


    };

    public static int[] cardsMarked = {
            R.drawable.clubs_2_marked,
            R.drawable.clubs_3_marked,
            R.drawable.clubs_4_marked,
            R.drawable.clubs_5_marked,
            R.drawable.clubs_6_marked,
            R.drawable.clubs_7_marked,
            R.drawable.clubs_8_marked,
            R.drawable.clubs_9_marked,
            R.drawable.clubs_10_marked,
            R.drawable.jack_of_clubs2_marked,
            R.drawable.queen_of_clubs2_marked,
            R.drawable.king_of_clubs2_marked,
            R.drawable.ace_of_clubs_marked,
            R.drawable.diamonds_2_marked,
            R.drawable.diamonds_3_marked,
            R.drawable.diamonds_4_marked,
            R.drawable.diamonds_5_marked,
            R.drawable.diamonds_6_marked,
            R.drawable.diamonds_7_marked,
            R.drawable.diamonds_8_marked,
            R.drawable.diamonds_9_marked,
            R.drawable.diamonds_10_marked,
            R.drawable.jack_of_diamonds2_marked,
            R.drawable.queen_of_diamonds2_marked,
            R.drawable.king_of_diamonds2_marked,
            R.drawable.ace_of_diamonds_marked,
            R.drawable.hearts_2_marked,
            R.drawable.hearts_3_marked,
            R.drawable.hearts_4_marked,
            R.drawable.hearts_5_marked,
            R.drawable.hearts_6_marked,
            R.drawable.hearts_7_marked,
            R.drawable.hearts_8_marked,
            R.drawable.hearts_9_marked,
            R.drawable.hearts_10_marked,
            R.drawable.jack_of_hearts2_marked,
            R.drawable.queen_of_hearts2_marked,
            R.drawable.king_of_hearts2_marked,
            R.drawable.ace_of_hearts_marked,
            R.drawable.spades_2_marked,
            R.drawable.spades_3_marked,
            R.drawable.spades_4_marked,
            R.drawable.spades_5_marked,
            R.drawable.spades_6_marked,
            R.drawable.spades_7_marked,
            R.drawable.spades_8_marked,
            R.drawable.spades_9_marked,
            R.drawable.spades_10_marked,
            R.drawable.jack_of_spades2_marked,
            R.drawable.queen_of_spades2_marked,
            R.drawable.king_of_spades2_marked,
            R.drawable.ace_of_spades2_marked,


    };


    public static Rect getBitmapPositionInsideImageView(ImageView imageView) {

        if (imageView == null || imageView.getDrawable() == null)
            return new Rect(0,0,0,0);

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        int leftRect = imageView.getLeft() + left;
        int topRect = imageView.getTop() + top;
        int rightRect = imageView.getLeft() + left + actW;
        int bottomRect = imageView.getTop() + top + actH;
        return new Rect(leftRect, topRect, rightRect, bottomRect);
    }
}
