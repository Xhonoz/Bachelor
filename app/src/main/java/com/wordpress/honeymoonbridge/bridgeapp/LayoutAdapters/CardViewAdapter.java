package com.wordpress.honeymoonbridge.bridgeapp.LayoutAdapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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

    public ImageView getImageView() {
        return imageView;
    }

    public void setCard(Card card) {
        if(card != null) {
            this.card = card;

            int index = card.getSuit().ordinal() * 13 + card.getCardValue() - 2;

            imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                    ImageHelper.cards[index]), ImageHelper.scaleDownImageSize, true));

//            imageView.setId(index);

        }else {
            this.card = null;
            imageView.setImageBitmap(null);
        }
    }

    public void setBackside(){
        imageView.setImageBitmap(ImageHelper.scaleDown(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.backside), ImageHelper.scaleDownImageSize, true));
    }

    public void addHighlight(){
        int highlightColor = mContext.getResources().getColor(R.color.highlight);
        imageView.setColorFilter(highlightColor, PorterDuff.Mode.MULTIPLY);
    }

    public void removeHighlight() {
        imageView.setColorFilter(null);
    }

    public Card getCard() {
        return card;
    }
}
