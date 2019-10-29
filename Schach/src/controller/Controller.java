package controller;

import engine.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controller gets initialized by View, do not implement a classical game loop here.
 * Controller manages the Board and the order in which players are called to commit their moves.
 * Moves are checked for validity here. Players should select from valid moves sent to them by Controller.
 **/

public class Controller implements Serializable {
    private List<ControllerListener> listeners = new ArrayList<>();
    private ObservableList<Move> moveHistory = FXCollections.observableArrayList();    // last added = latest move, dont create a new instance!, dont overwrite!
    private List<Move> validMovesForActivePlayer;

    private Board board;

    private Player whitePlayer;
    private Player blackPlayer;

    private int reversibleMoveCount = 0;
    private HashMap<Situation, Integer> situationCounts;

    private boolean isWhitePlayersTurn = true;

    private boolean gameStopped = false;

    public Board getBoard() {
        return board;
    }

    public void loadGameState(GameState o) {
        this.board = o.board;
        moveHistory.clear();
        moveHistory.addAll(o.moveHistory);
        this.reversibleMoveCount = o.reversibleMoveCount;
        this.gameStopped = o.gameStopped;
        this.situationCounts = o.situationCounts;
    }

    public GameState generateGameState() {
        GameState r = new GameState();
        r.board = this.board;
        r.moveHistory = new ArrayList<>(this.moveHistory);
        r.reversibleMoveCount = this.reversibleMoveCount;
        r.gameStopped = this.gameStopped;
        r.situationCounts = this.situationCounts;
        return r;
    }

    public ObservableList<Move> getMoveHistory() {
        return moveHistory;
    }

    /**
     * Sets up all data for a new game, clears listeners
     * @param whitePlayer The white Player
     * @param blackPlayer The black Player
     * @param doResetData Whether to clear the moveHistory and create a new board. Set it to false if you called setupLoadedGame before.
     */
    public void setupNewGame(Player whitePlayer, Player blackPlayer, boolean doResetData) {
        listeners.clear();
        if (doResetData) {
            moveHistory.clear();
            board = new Board();
            situationCounts = new HashMap<Situation, Integer>();
        }
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        isWhitePlayersTurn = (moveHistory.size() & 1) == 0;    //anzahl moves gerade -> weiss am Zug
	    this.gameStopped = false;
    }

    /**
     * Sets up the Controller data with a pre-loaded board and a move history
     * @param brd The board as a Piece Array
     * @param movHist The move history as a list
     */
    public void setupLoadedGame(Piece[][] brd, ArrayList<Move> movHist) {
        board = new Board(brd);
        moveHistory.clear();
        moveHistory.addAll(movHist);
    }

    /**
     * Adds given ControllerListener to the listeners of this controller
     * @param listener The ControllerListener to add
     */
    public void addControllerListener(ControllerListener listener) {
        listeners.add(listener);
    }

    /**
     * starts a game by requesting the first move by white, other moves are then requested and committed by offerMove(), gets called after setupNewGame()
     **/
    public void start() {
        requestNextMove();
    }

    /**
     * requests next move from player defined by isWhiteTurn, checks for checked kings, recalculates valid moves for player, checks if game is finished or move can be requested
     */
    private void requestNextMove() {
        validMovesForActivePlayer = board.getAllPossibleMovesForPlayer(isWhitePlayersTurn);

        boolean isWhitePlayerChecked = board.isPlayerChecked(true);
        for (ControllerListener cl : listeners)
            cl.onCheckedChange(true, board.getKingPosition(true), isWhitePlayerChecked);

        boolean isBlackPlayerChecked = board.isPlayerChecked(false);
        for (ControllerListener cl : listeners)
            cl.onCheckedChange(false, board.getKingPosition(false), isBlackPlayerChecked);

        performGameStep(isWhitePlayerChecked, isBlackPlayerChecked);
    }

    private void performGameStep(boolean isWhitePlayerChecked, boolean isBlackPlayerChecked) {
    	if (gameStopped) return;

        if (validMovesForActivePlayer.isEmpty()) {
            notifyOnGameFinishedListeners(isWhitePlayerChecked, isBlackPlayerChecked);
            return;
        }

        Player currentPlayer = isWhitePlayersTurn ? whitePlayer : blackPlayer;
        currentPlayer.requestMove(this, validMovesForActivePlayer);
    }

    private Player getWinningPlayer(boolean isWhitePlayerChecked, boolean isBlackPlayerChecked) {
        return (isWhitePlayersTurn && isWhitePlayerChecked) ? blackPlayer :
               (!isWhitePlayersTurn && isBlackPlayerChecked) ? whitePlayer : null;
    }

    private void notifyOnGameFinishedListeners(boolean isWhitePlayerChecked, boolean isBlackPlayerChecked) {
        Player player = getWinningPlayer(isWhitePlayerChecked, isBlackPlayerChecked);
        String reason = player == null ? String.format("%s is unable to move.", isWhitePlayersTurn ? "White" : "Black") : "";
        endGame(player, reason);
    }

    private void endGame(Player winner, String reason) {
    	gameStopped = true;
	    for (ControllerListener cl : listeners) cl.onGameFinished(winner, reason);
    }

    /**
     * Feedback method for players to commit their selected move (Move with equal source and destination as one of the valid moves sent). Move gets checked for validity and then performs it or requests a new move
     * @param equalMove the played move matching the source and destination coordinates of a move in validMovesForActivePlayer
     * @param isWhite player color
     */
    public void offerMove(Move equalMove, boolean isWhite) {
        System.out.println("Move offered"); //debug print
        int index = validMovesForActivePlayer.indexOf(equalMove);
//        if (index >= 0 && index < validMovesForActivePlayer.size() && isWhite == isWhitePlayersTurn) {
        if (index != -1) {
            performMove(index);
        } else {    // invalid move
            (isWhite ? whitePlayer : blackPlayer).requestMove(this, validMovesForActivePlayer);    //request new move
        }
    }

    /**
     * This method is to be called AFTER the move has been performed.
     */
    private boolean isReversible(Move move) {
	    return !(board.getOnCell(move.getDest()) instanceof Pawn || move.isDoesBeat());
    }

    private void performMove(int index) {
        Move move = validMovesForActivePlayer.get(index);
        board.performMove(move);
        moveHistory.add(move);
        for (ControllerListener cl : listeners) cl.onMovePerformed(move);

	    handlePawnPromotion(move);
	    handleReversibleMoveCount(move);

	    isWhitePlayersTurn = !isWhitePlayersTurn;
        requestNextMove();
    }

	private void handleReversibleMoveCount(Move move) {
		if (isReversible(move)) {
		    reversibleMoveCount++;
		    if (reversibleMoveCount > 50) endGame(null, "50 consecutive moves with no beating and no pawn movement.");

		    Situation situation = new Situation(board);
		    situationCounts.putIfAbsent(situation, 0);
		    int count = situationCounts.get(situation) + 1;
		    situationCounts.put(situation, count);
		    if (count == 3) endGame(null, "The same situation has occurred 3 times during the game.");
	    } else {
		    reversibleMoveCount = 0;

		    situationCounts.clear();
	    }
	}

	private void handlePawnPromotion(Move move) {
		if (board.getOnCell(move.getDest()) instanceof Pawn && move.getDestY() == (isWhitePlayersTurn ? 0 : 7)) {
		    Piece replacementPiece = (isWhitePlayersTurn ? whitePlayer : blackPlayer).onPeasantReachedOtherSide(move.getDest());
		    board.setPiece(move.getDestX(), move.getDestY(), replacementPiece);
		    for (ControllerListener cl : listeners) cl.onPawnPromotion(move.getDest(), replacementPiece);
		}
	}
}
