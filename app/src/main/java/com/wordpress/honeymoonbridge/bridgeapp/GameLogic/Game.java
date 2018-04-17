package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import android.util.Log;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.BiddingHistory;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;

import java.io.Serializable;

/**
 * Created by Eier on 09.04.2018.
 */

public class Game{
    private GameState gamestate;
    private AIPlayer AI;

    public interface Callback {
        // called when the user presses the send button to submit a message
        void AiPickedCard(boolean first);

        void AiBid(Bid bid);

        void AiPlayedCard(Card card);

        void finishPicking();

        void finishBidding();

        void finishPlaying();


    }

    private Callback mCallback = null;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public GameState getGameState() {
        return gamestate;
    }

    public Game(boolean isSouthTurn, AIPlayer aiPlayer) {
        gamestate = new GameState(isSouthTurn);
        AI = aiPlayer;

    }

    //Picking Phase:

    public Card peakTopCard() {
        CardStack s = gamestate.getStack();
        if (!gamestate.getStack().isEmpty())
            return (Card) s.get(0);

        return null;
    }

    public Card popTopCard() {
        CardStack s = gamestate.getStack();
        if (!gamestate.getStack().isEmpty()) {
            Card c = (Card) s.get(0);
            s.remove(c);
            return c;
        }
        return null;
    }

    public Card UIPickCard(boolean first) {
        if (gamestate.getPhase().equals(Phase.PICKING) && gamestate.isSouthTurn()) {
            Card c = PickCard(Player.SOUTH, first);
            gamestate.setSouthTurn(false);
            //TODO: Should later be a seperate thread
            AITakesTurnPicking();
            return c;
        }
        return null;
    }

    public Card PickCard(Player player, boolean first) {

        Log.i("GAME", "Deck length: " + gamestate.getStack().size());

        Card picked = null;
        if (!gamestate.getStack().isEmpty()) {
            Card fi = popTopCard();
            Card se = popTopCard();
            if (player == Player.NORTH) {
                gamestate.getNorth26Cards().add(fi);
                gamestate.getNorth26Cards().add(se);
                gamestate.getNorthChoseFirst().add(first);
                if (first) {
                    picked = fi;
                    gamestate.getNorthHand().addCard(fi);
                } else {
                    picked = se;
                    gamestate.getNorthHand().addCard(se);
                }
            }
            if (player == Player.SOUTH) {
                gamestate.getSouth26Cards().add(fi);
                gamestate.getSouth26Cards().add(se);
                gamestate.getSouthChoseFirst().add(first);
                if (first) {
                    picked = fi;
                    gamestate.getSouthHand().addCard(fi);
                } else {
                    picked = se;
                    gamestate.getSouthHand().addCard(se);
                }
            }
        } else {
            //TODO:Husk å endre hvis spilleren har huket av i settings at bidding ikke skal være med da går vi rett til spille fasen
            mCallback.finishPicking();
            gamestate.setPhase(Phase.BIDDING);
        }

        Log.i("GAME", "Deck length: " + gamestate.getStack().size());


        return picked;
    }

    private void AITakesTurnPicking() {
        if (!gamestate.getStack().isEmpty()) {

            boolean first = AI.pickCard(gamestate);
            PickCard(Player.NORTH, first);
            mCallback.AiPickedCard(first);
            gamestate.setSouthTurn(true);
            if(gamestate.getStack().isEmpty()) {
                gamestate.setPhase(Phase.BIDDING);
                mCallback.finishPicking();

            }

        } else {
            //TODO:Husk å endre hvis spilleren har huket av i settings at bidding ikke skal være med da går vi rett til spille fasen
            mCallback.finishPicking();
            gamestate.setPhase(Phase.BIDDING);
        }
    }

    //Bidding Phase:


    // 1 = succsessfull
    // 2 = To low bid
    // 3 = Not your turn
    public int UIBid(Bid bid){

        if(!gamestate.isSouthTurn())
            return 3;
        if(!isLegalBid(bid, Player.SOUTH))
            return 2;
        gamestate.getBiddingHistory().getSouth().add(bid);
//        TODO: ny tråd
            AiBid();
        return 1;
    }

    // 1 = succsessfull
    // 2 = Can't Double
    // 3 = Not your turn
    public int UIDouble(){

        if(!gamestate.isSouthTurn())
            return 3;

        if(isLegalToDouble(Player.SOUTH)) {
            addDoubleBid(Player.SOUTH);
//        TODO: ny tråd
            AiBid();
            return 1;
        }
        return 2;
    }



    // 1 = succsessfull
    // 2 = To low bid
    // 3 = Not your turn
    public int UIReDouble(){

        if(!gamestate.isSouthTurn())
            return 3;

        if(isLegalToRedouble(Player.SOUTH)) {
            addRedoubleBid(Player.SOUTH);
//        TODO: ny tråd
            AiBid();

            return 1;
        }
        return 2;

    }

    public boolean UIPass(){
        if(!gamestate.isSouthTurn())
            return false;
        addPass(Player.SOUTH);
        if(biddingIsOver())
            mCallback.finishBidding();
        else
            AiBid();

    }



//    Doues not handle Dourbles or Redoubles
    private boolean isLegalBid(Bid bid, Player player){
        if((player == Player.SOUTH && !gamestate.getBiddingHistory().isNorthEmpty())
          || player == Player.NORTH && !gamestate.getBiddingHistory().isSouthEmpty()) {

            Bid lastBid = null;
            if(player == Player.SOUTH)
                lastBid = gamestate.getBiddingHistory().getLastNorthBid();
            if(player == Player.NORTH)
                lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            int lastLevel = lastBid.getLevel();
            int lastTrumpInt = lastBid.getTrumpInt();
            int newLevel = bid.getLevel();
            int newTrumpInt = bid.getTrumpInt();

            if(newLevel > lastLevel)
                return true;
            if((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
                return true;

            return false;
        }
        return true;
    }

    private boolean isLegalToDouble(Player player) {
        if ((player == Player.SOUTH && !gamestate.getBiddingHistory().isNorthEmpty())
                || player == Player.NORTH && !gamestate.getBiddingHistory().isSouthEmpty()) {

            Bid lastBid = null;
            if (player == Player.SOUTH)
                lastBid = gamestate.getBiddingHistory().getLastNorthBid();
            if (player == Player.NORTH)
                lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            if (lastBid.isDouble() || lastBid.isRedouble() || lastBid.isPass())
                return false;
            return true;
        }
        return false;
    }

    private boolean isLegalToRedouble(Player player) {
        if ((player == Player.SOUTH && !gamestate.getBiddingHistory().isNorthEmpty())
                || player == Player.NORTH && !gamestate.getBiddingHistory().isSouthEmpty()) {

            Bid lastBid = null;
            if (player == Player.SOUTH)
                lastBid = gamestate.getBiddingHistory().getLastNorthBid();
            if (player == Player.NORTH)
                lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            if (lastBid.isDouble())
                return true;
            return false;
        }
        return false;
    }

    private void addDoubleBid(Player player) {
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new Bid(gamestate.getBiddingHistory().getLastNorthBid(), true, false));
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new Bid(gamestate.getBiddingHistory().getLastSouthBid(), true, false));
    }

    private void addRedoubleBid(Player player) {
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new Bid(gamestate.getBiddingHistory().getLastNorthBid(), false, true));
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new Bid(gamestate.getBiddingHistory().getLastSouthBid(), false, true));
    }

    private void addPass(Player player){
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new Bid());
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new Bid());

}


}
