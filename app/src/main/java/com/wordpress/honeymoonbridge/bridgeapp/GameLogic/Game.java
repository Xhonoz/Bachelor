package com.wordpress.honeymoonbridge.bridgeapp.GameLogic;

import android.os.AsyncTask;
import android.util.Log;

import com.wordpress.honeymoonbridge.bridgeapp.AI.AIPlayer;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Bid;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Card;
import com.wordpress.honeymoonbridge.bridgeapp.Model.CardStack;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Contract;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Double;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Hand;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Pass;
import com.wordpress.honeymoonbridge.bridgeapp.Model.ReDouble;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Suit;
import com.wordpress.honeymoonbridge.bridgeapp.Model.Trump;

import java.util.ArrayList;


/**
 * Created by Eier on 09.04.2018.
 */

public class Game {
    private GameState gamestate;
    private com.wordpress.honeymoonbridge.bridgeapp.AI.AIPlayer AI;
    private AIPlayer AIPlayer;


    public interface Callback {
        // called when the user presses the send button to submit a message
        void AiPickedCard(boolean first);

        void AiBid(Bid bid);

        void AiPlayedCard(Card card, boolean first);

        void wonTrick(Player player);

        void finishPicking();

        void finishBidding();

        void startPlaying();

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

    public void startPickingPhase() {
        if (!gamestate.isSouthTurn() && gamestate.getPhase() == Phase.PICKING)
            AITakesTurnPicking();

    }

    public void startBiddingPhase() {
        Log.i("GAME", "startBiddingPhase");
        gamestate.setPhase(Phase.BIDDING);
        gamestate.setSouthTurn(gamestate.getDealer() == Player.SOUTH);
        if (!gamestate.isSouthTurn())
            AiTakesTurnBidding();

    }

    public void startPlayingPhase() {
        gamestate.lockInitialHands();
        Log.i("GAME", "Size of initialSouthHand: " + gamestate.getInitialSouthHand().getSize());
        Contract contract = getContract();
        if (contract != null) {
            gamestate.setContract(contract);
            gamestate.setSouthTurn((contract.getPlayer() == Player.NORTH));
            gamestate.setPhase(Phase.PLAYING);
            mCallback.startPlaying();
        } else
            mCallback.finishPlaying();

    }

    private void finishGame() {
        gamestate.setPhase(Phase.FINISHED);
        mCallback.finishPlaying();
    }

    public Card getCardFromDeck(boolean first) {
        if (first)
            return peakTopCard();
        else
            return (Card) gamestate.getStack().get(1);
    }


    private Contract getContract() {
        boolean doubled = false;
        boolean reDoubled = false;
        Contract baseContract = null;
        ArrayList<Bid> bids = gamestate.getBiddingHistory().getAllBids(gamestate.getDealer());
        for (int i = bids.size() - 1; i >= 0; i--) {
            Bid current = bids.get(i);
            if (current instanceof Contract) {
                baseContract = (Contract) current;
                break;
            }
            if (current instanceof ReDouble)
                reDoubled = true;
            if (current instanceof Double)
                doubled = true;
        }

        if (baseContract == null)
            return null;
        return new Contract(baseContract, doubled, reDoubled);
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
        if (!gamestate.isSouthTurn() && player == Player.SOUTH)
            return false;
        if (gamestate.isSouthTurn() && player == Player.NORTH)
            return false;
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
                Log.i("GAME", "Card: " + card + " is not legal to play \nYou still have " + suit + " left, for example: " + hand.getCardsOfSuit(suit).get(0));
                return false;
            }

        }


    }


    //        Returns true if firstCard wins
    public boolean compareCards(Trump trump, Card firstCard, Card secondCard) {

        Log.i("GAME", "Comparing cards: " + firstCard + " and " + secondCard + " with trump: " + trump);
        if (trump == Trump.NoTrump) {
            if (!firstCard.getSuit().equals(secondCard.getSuit()))
                return true;
            if (firstCard.getCardValue() > secondCard.getCardValue())
                return true;
            return false;
        }

        if (!firstCard.getSuit().equals(secondCard.getSuit()) && !secondCard.getSuit().equals(Suit.getSuitFromTrump(trump)))
            return true;
        if (firstCard.getSuit().equals(secondCard.getSuit()) && firstCard.getCardValue() > secondCard.getCardValue())
            return true;
        return false;


    }


    public boolean Play(Card card, Player player) {

        Hand hand = gamestate.getSouthHand();

        Log.i("GAME", "Player: " + player + "  Card: " + card + "  southTurn: " + gamestate.isSouthTurn());
        if (card == null)
            return false;
        if (gamestate.isSouthTurn() && player.equals(Player.SOUTH) && isLegal(player, card)) {
            Log.i("GAME", "(Before Play) SouthHand:\n" + gamestate.getSouthHand());
            gamestate.getSouthHand().removeCard(card);
            Log.i("GAME", "(After Play) SouthHand:\n" + gamestate.getSouthHand());

            if (gamestate.getTricks().isEmpty() || gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard != null) {
                gamestate.getTricks().add(new Trick(player, card, null));
                gamestate.setSouthTurn(false);
            } else {
                gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard = card;
                gamestate.setSouthTurn(!compareCards(gamestate.getTrump(), gamestate.getTricks().get(gamestate.getTricks().size() - 1).firstCard, card));
                if (gamestate.isSouthTurn()) {
                    gamestate.incrementSouthTricks();
                    mCallback.wonTrick(Player.SOUTH);
                } else {
                    gamestate.incrementNorthTricks();
                    mCallback.wonTrick(Player.NORTH);
                }
            }
            if (gamestate.getNorthHand().getSize() == 0 && gamestate.getSouthHand().getSize() == 0)
                finishGame();
            return true;
        }

        if (!gamestate.isSouthTurn() && player.equals(Player.NORTH) && isLegal(player, card)) {
            gamestate.getNorthHand().removeCard(card);
            if (gamestate.getTricks().isEmpty() || gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard != null) {
                gamestate.getTricks().add(new Trick(player, card, null));
                gamestate.setSouthTurn(true);
            } else {
                gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard = card;
                gamestate.setSouthTurn(compareCards(gamestate.getTrump(), gamestate.getTricks().get(gamestate.getTricks().size() - 1).firstCard, card));
                if (gamestate.isSouthTurn()) {
                    gamestate.incrementSouthTricks();
                    mCallback.wonTrick(Player.SOUTH);
                } else {
                    gamestate.incrementNorthTricks();
                    mCallback.wonTrick(Player.NORTH);
                }
            }
            if (gamestate.getNorthHand().getSize() == 0 && gamestate.getSouthHand().getSize() == 0)
                finishGame();
            return true;

        }
        return false;
    }


    public boolean UIPlayCard(Card card) {
        Log.i("GAME", "South is trying to play: " + card);
        if (Play(card, Player.SOUTH)) {
//            TODO: new thread
            return true;

        }
        return false;
    }

    public Card getPreviousDiscard(Player player) {
        if (player == Player.SOUTH) {
            if (!gamestate.getSouth26Cards().isEmpty() && !gamestate.getSouthChoseFirst().isEmpty()) {
                int index = gamestate.getSouth26Cards().size() - 1;
                if (!gamestate.getSouthChoseFirst().get(gamestate.getSouthChoseFirst().size() - 1))
                    index--;
                return gamestate.getSouth26Cards().get(index);
            }
        }
        if (player == Player.NORTH) {
            if (!gamestate.getNorth26Cards().isEmpty() && !gamestate.getNorthChoseFirst().isEmpty()) {
                int index = gamestate.getNorth26Cards().size() - 1;
                if (!gamestate.getNorthChoseFirst().get(gamestate.getNorthChoseFirst().size() - 1))
                    index--;
                return gamestate.getNorth26Cards().get(index);

            }
        }
        return null;
    }

    private void AITakesTurnPlaying() {
        new AIPlayCard().execute(gamestate);

    }

    public void northTakeTurn() {
        if (gamestate.getPhase() == Phase.PLAYING)
            if (!gamestate.isSouthTurn())
                AITakesTurnPlaying();
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
            startBiddingPhase();
        }

        Log.i("GAME", "Deck length: " + gamestate.getStack().size());


        return picked;
    }

    private void AITakesTurnPicking() {
        if (!gamestate.getStack().isEmpty()) {

//            new AIPickCard().execute(gamestate);
            boolean first = AI.pickCard(gamestate);
            PickCard(Player.NORTH, first);
            mCallback.AiPickedCard(first);
            gamestate.setSouthTurn(true);
            if (gamestate.getStack().isEmpty()) {
                startBiddingPhase();
                mCallback.finishPicking();

            }


        } else {
            //TODO:Husk å endre hvis spilleren har huket av i settings at bidding ikke skal være med da går vi rett til spille fasen
            startBiddingPhase();
            mCallback.finishPicking();

        }
    }

    //Bidding Phase:


    // 1 = succsessfull
    // 2 = To low bid
    // 3 = Not your turn
    public int UIBid(Bid bid) {

        if (!gamestate.isSouthTurn())
            return 3;
        if (!isLegalBid(bid))
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
    public int UIDoubleOrRedouble() {
        if (gamestate.getBiddingHistory().isNorthEmpty())
            return 2;
        if (gamestate.getBiddingHistory().getLastNorthBid() instanceof Double)
            return UIReDouble();
        return UIDouble();
    }

    // 1 = succsessfull
    // 2 = Can't Double
    // 3 = Not your turn
    public int UIDouble() {

        if (!gamestate.isSouthTurn())
            return 3;

        if (isLegalBid(new Double(Player.SOUTH))) {
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

        if (isLegalBid(new ReDouble(Player.SOUTH))) {
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
            startPlayingPhase();
            mCallback.finishBidding();
//                TODO: Check if just wanto bid and not play
        } else {
            gamestate.setSouthTurn(false);
//        TODO: ny tråd
            AiTakesTurnBidding();

        }
        return true;
    }

    private void AiTakesTurnBidding() {

        new AIBid().execute(gamestate);

    }


    private boolean isLegalBid(Bid bid) {

        if (bid instanceof Pass)
            return true;

        Player player = bid.getPlayer();

        if ((player == Player.SOUTH && !gamestate.getBiddingHistory().isNorthEmpty())
                || player == Player.NORTH && !gamestate.getBiddingHistory().isSouthEmpty()) {

            Bid lastBid = null;
            if (player == Player.SOUTH)
                lastBid = gamestate.getBiddingHistory().getLastNorthBid();
            if (player == Player.NORTH)
                lastBid = gamestate.getBiddingHistory().getLastSouthBid();

            Contract lastContract = null;

            if (lastBid instanceof Pass)
                return true;

            if (lastBid instanceof Contract) {
                if (bid instanceof Double)
                    return true;
                lastContract = (Contract) lastBid;

            }
            if (lastBid instanceof Double) {
                if (bid instanceof ReDouble)
                    return true;
                if (player == Player.SOUTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getLastSouthBid();
                if (player == Player.NORTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getLastNorthBid();
            }

            if (lastBid instanceof ReDouble) {
                if (player == Player.SOUTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getNorth().get(gamestate.getBiddingHistory().getNorth().size() - 2);
                if (player == Player.NORTH)
                    lastContract = (Contract) gamestate.getBiddingHistory().getSouth().get(gamestate.getBiddingHistory().getSouth().size() - 2);
            }

            int lastLevel = lastContract.getTricks();
            int lastTrumpInt = lastContract.getTrump().ordinal();

            if (bid instanceof Contract) {
                int newLevel = ((Contract) bid).getTricks();
                int newTrumpInt = ((Contract) bid).getTrump().ordinal();

                if (newLevel > lastLevel)
                    return true;
                if ((newLevel == lastLevel) && (newTrumpInt > lastTrumpInt))
                    return true;

            }

            return false;
        }

        return true;
    }


    private void addDoubleBid(Player player) {
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new Double(player));
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new Double(player));
    }

    private void addRedoubleBid(Player player) {
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new ReDouble(player));
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new ReDouble(player));
    }

    private void addPass(Player player) {
        if (player == Player.SOUTH)
            gamestate.getBiddingHistory().getSouth().add(new Pass(player));
        if (player == Player.NORTH)
            gamestate.getBiddingHistory().getNorth().add(new Pass(player));

    }

    private boolean biddingIsOver() {
        if (gamestate.isSouthTurn()) {
            if (gamestate.getBiddingHistory().getLastSouthBid() instanceof Pass && !gamestate.getBiddingHistory().getNorth().isEmpty())
                return true;
        } else {
            if (gamestate.getBiddingHistory().getLastNorthBid() instanceof Pass && !gamestate.getBiddingHistory().getSouth().isEmpty())
                return true;
        }
        return false;
    }


    private class AIPlayCard extends AsyncTask<GameState, Integer, Card> {
        protected Card doInBackground(GameState... gameState) {
            return AI.playCard(gameState[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Card card) {
            if (Play(card, Player.NORTH))
                mCallback.AiPlayedCard(card, gamestate.getTricks().get(gamestate.getTricks().size() - 1).SecondCard == null);
        }
    }

    private class AIBid extends AsyncTask<GameState, Integer, Bid> {
        protected Bid doInBackground(GameState... gameState) {
            return AI.bid(gameState[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Bid bid) {
            gamestate.getBiddingHistory().getNorth().add(bid);
            mCallback.AiBid(bid);
            if (bid instanceof Pass) {
                if (biddingIsOver()) {
                    startPlayingPhase();
                    mCallback.finishBidding();
//                TODO: Check if just wanto bid and not play

                } else {
                    gamestate.setSouthTurn(true);
                }

            } else
                gamestate.setSouthTurn(true);
        }
    }

    private class AIPickCard extends AsyncTask<GameState, Integer, Boolean> {
        protected Boolean doInBackground(GameState... gameState) {
            return AI.pickCard(gameState[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Boolean first) {
            PickCard(Player.NORTH, first);
            mCallback.AiPickedCard(first);
            gamestate.setSouthTurn(true);
            if (gamestate.getStack().isEmpty()) {
                startBiddingPhase();
                mCallback.finishPicking();

            }
        }
    }


}
