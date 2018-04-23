package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import android.util.Log;

import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;

/**
 * Created by Eier on 09.04.2018.
 */

public class Game {
    private GameState gamestate;
    private AIPlayer AI;
    private AIPlayer AIPlayer;

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

    public void startPickingPhase(){
        if(!gamestate.isSouthTurn() && gamestate.getPhase() == Phase.PICKING)
            AITakesTurnPicking();

    }
    public void startBiddingPhase(){
        Log.i("GAME", "startBiddingPhase");
        if(!gamestate.isSouthTurn() && gamestate.getPhase() == Phase.BIDDING)
            AiTakesTurnBidding();

    }
    public void startPlayingPhase(){
        if(!gamestate.isSouthTurn() && gamestate.getPhase() == Phase.PLAYING)
            AITakesTurnPlaying();

    }

    public boolean doNext(){
        return true;
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

    public boolean isLegal(Player player, Card card) {
        Trick trick = null;
        Hand hand;
        if (player.equals(Player.NORTH)) {
            hand = gamestate.getNorthHand();
        } else {
            hand = gamestate.getSouthHand();
        }
        if (gamestate.getTricks().isEmpty()) {
            return true;
        } else {
            trick = gamestate.getTricks().get(gamestate.getTricks().size() - 1);
        }

        if (trick.SecondCard != null) {
            return true;
        } else {
            Suit suit = trick.firstCard.getSuit();

            if (card.getSuit().equals(suit) || hand.getCardsOfSuit(suit).isEmpty()) {
                return true;
            } else {
                return false;
            }

        }


    }

    public boolean Play(Card card, Player player) {
        if(card == null)
            return false;
        if (gamestate.isSouthTurn() && player.equals(Player.SOUTH) && isLegal(player, card)) {
            gamestate.getSouthHand().removeCard(card);
//            Is leading a card
            if (gamestate.getTricks().isEmpty() || gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard != null) {
                gamestate.getTricks().add(new Trick(player, card, null));
//               Is Playing second card
            } else {
                gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard = card;
            }

            gamestate.setSouthTurn(false);
            return true;
        }

        if (!gamestate.isSouthTurn() && player.equals(Player.NORTH) && isLegal(player, card)) {
            gamestate.getNorthHand().removeCard(card);
//            Is leading a card
            if (gamestate.getTricks().isEmpty() || gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard != null) {
                gamestate.getTricks().add(new Trick(player, card, null));
//            Is Playing second card

            } else {
                gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard = card;
            }

            gamestate.setSouthTurn(true);
            return true;
        }
        return false;
    }

    public boolean UIPlayCard(Card card) {
        if (Play(card, Player.SOUTH)) {
//            TODO: new thread
            AITakesTurnPlaying();
            return true;

        }
        return false;
    }

    private void AITakesTurnPlaying() {
        Card card = AI.playCard(gamestate);
        if (Play(card, Player.NORTH))
            mCallback.AiPlayedCard(card);

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
                    Log.i("GAME", "AI Chose the first card: " + fi + " and discarded: " + se);
                    picked = fi;
                    gamestate.getNorthHand().addCard(fi);
                } else {
                    Log.i("GAME", "AI Chose the second card: " + se + " and discarded: " + fi);
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
            if (gamestate.getStack().isEmpty()) {
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
    public int UIBid(Bid bid) {

        if (!gamestate.isSouthTurn())
            return 3;
        if (!isLegalBid(bid, Player.SOUTH))
            return 2;
        gamestate.getBiddingHistory().getSouth().add(bid);
//        TODO: ny tråd
        gamestate.setSouthTurn(false);
        AiTakesTurnBidding();
        return 1;
    }

    // 1 = succsessfull
    // 2 = Can't Double
    // 3 = Not your turn
    public int UIDouble() {

        if (!gamestate.isSouthTurn())
            return 3;

        if (isLegalToDouble(Player.SOUTH)) {
            addDoubleBid(Player.SOUTH);
            gamestate.setSouthTurn(false);
//        TODO: ny tråd
            AiTakesTurnBidding();
            return 1;
        }
        return 2;
    }


    // 1 = succsessfull
    // 2 = To low bid
    // 3 = Not your turn
    public int UIReDouble() {

        if (!gamestate.isSouthTurn())
            return 3;

        if (isLegalToRedouble(Player.SOUTH)) {
            addRedoubleBid(Player.SOUTH);
            gamestate.setSouthTurn(false);
//        TODO: ny tråd
            AiTakesTurnBidding();

            return 1;
        }
        return 2;

    }

    public boolean UIPass() {
        if (!gamestate.isSouthTurn())
            return false;
        addPass(Player.SOUTH);
        if (biddingIsOver()) {
            mCallback.finishBidding();
//                TODO: Check if just wanto bid and not play
            gamestate.setPhase(Phase.PLAYING);
        } else {
            gamestate.setSouthTurn(false);
//        TODO: ny tråd
            AiTakesTurnBidding();

        }
        return true;
    }

    private void AiTakesTurnBidding() {

        Bid bid = AI.bid(gamestate);
//        TODO: Add check to if bid is legal
        gamestate.getBiddingHistory().getNorth().add(bid);
        mCallback.AiBid(bid);
        if (bid.isPass())
            if (biddingIsOver()) {
                mCallback.finishBidding();
//                TODO: Check if just wanto bid and not play
                gamestate.setPhase(Phase.PLAYING);
            }
        gamestate.setSouthTurn(true);

    }


    //    Doues not handle Dourbles or Redoubles
    private boolean isLegalBid(Bid bid, Player player) {
        if ((player == Player.SOUTH && !gamestate.getBiddingHistory().isNorthEmpty())
                || player == Player.NORTH && !gamestate.getBiddingHistory().isSouthEmpty()) {

            Bid lastBid = null;
            if (player == Player.SOUTH)
                lastBid = gamestate.getBiddingHistory().getLastNorthBid();
            if (player == Player.NORTH)
                lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            int lastLevel = lastBid.getLevel();
            int lastTrumpInt = lastBid.getTrumpInt();
            int newLevel = bid.getLevel();
            int newTrumpInt = bid.getTrumpInt();

            if (newLevel > lastLevel)
                return true;
            if ((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
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

    private void addPass(Player player) {
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new Bid());
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new Bid());

    }

    private boolean biddingIsOver() {
        if (gamestate.isSouthTurn()) {
            if (gamestate.getBiddingHistory().getLastSouthBid().isPass() && !gamestate.getBiddingHistory().getNorth().isEmpty())
                return true;
        } else {
            if (gamestate.getBiddingHistory().getLastNorthBid().isPass() && !gamestate.getBiddingHistory().getSouth().isEmpty())
                return true;
        }
        return false;
    }


}
