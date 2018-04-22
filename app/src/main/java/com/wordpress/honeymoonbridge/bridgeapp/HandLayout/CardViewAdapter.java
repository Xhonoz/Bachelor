package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.R;

/**
 * Created by Eier on 13.04.2018.
 */

public class CardViewAdapter {

    private Card card;
    private ImageView imageView;
    private Context mContext;

    public CardViewAdapter(ImageView imageView, Context context) {
        this.imageView = imageView;
        mContext = context;
    }

    public void setCard(Card card) {
        if(card != null) {
            this.card = card;

            int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

            imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                    ImageHelper.drawables[index]), 400, true));

        }else
            imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.backside), 400, true));
    }

    public Card getCard() {
        return card;
    }
}
