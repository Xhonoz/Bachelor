package com.wordpress.honeymoonbridge.bridgeapp.HandLayout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.ResourcesCompat;
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
                    ImageHelper.cards[index]), ImageHelper.scaleDownImageSize, true));

//            imageView.setId(index);

        }else
            imageView.setImageBitmap(null);
    }

    public void setBackside(){
        imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.backside), ImageHelper.scaleDownImageSize, true));
    }

    public void addHighlight(){
        int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

        imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                ImageHelper.cardsMarked[index]), ImageHelper.scaleDownImageSize, true));
    }

    public void removeHighlight() {
        if (card != null){
            int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

        imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                ImageHelper.cards[index]), ImageHelper.scaleDownImageSize, true));
    }
    }

    public Card getCard() {
        return card;
    }
}
