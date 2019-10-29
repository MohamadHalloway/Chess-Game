/*
	Author: Adrian Samoticha
 */

package controller.computerPlayers;

import controller.Controller;
import controller.GameStateAnalyzer;
import controller.MoveSimulator;
import controller.Player;
import engine.*;

import java.util.*;

public class ComputerPlayerShallow implements Player {

	private boolean isWhite;
	private Random rnd = new Random();

	public ComputerPlayerShallow(boolean isWhite) {
		this.isWhite = isWhite;
	}

	/**
	 * Evaluates the score of a given move.
	 * @param board The board used to simulate the move.
	 * @param move The move to be evaluated.
	 * @return The evaluated score of the move.
	 */
	private double evaluateMove(Board board, Move move) {
		Board boardCopy = board.copy();
		MoveSimulator.simulateMove(boardCopy, this, move);

		return GameStateAnalyzer.analyzeGameState(boardCopy, isWhite);
	}

	/**
	 * Evaluates a list of moves.
	 * @param board The board used to evaluate the moves.
	 * @param moveList A list containing the moves to be evaluated.
	 * @return A linked hash map where the key is a move from the moveList and the value is the corresponding rating.
	 */
	private LinkedHashMap<Move, Double> evaluateMoves(Board board, List<Move> moveList) {
		LinkedHashMap<Move, Double> r = new LinkedHashMap<>();
		for (Move m : moveList) {
			double e = evaluateMove(board, m);
			r.put(m, e);
		}
		return r;
	}

	/**
	 * Finds the best move evaluation out of a given set of previously evaluated moves.
	 * @param moveEvaluations A linked hash map containing the moves and their evaluations.
	 * @return The score of the best move evaluation.
	 */
	private double findBestMoveEvaluation(LinkedHashMap<Move, Double> moveEvaluations) {
		double r = Double.NEGATIVE_INFINITY;
		for (Map.Entry<Move, Double> e : moveEvaluations.entrySet()) r = Double.max(r, e.getValue());
		return r;
	}

	/**
	 * Generates a list of all the best moves.
	 * @param board The board used to evaluate the moves.
	 * @param moveList A list of moves to pick from.
	 * @return A list of the best moves.
	 */
	@SuppressWarnings("Duplicates")
	private ArrayList<Move> getBestMoves(Board board, List<Move> moveList) {
		LinkedHashMap<Move, Double> moveEvaluations = evaluateMoves(board, moveList);
		double bestMoveEvaluation = findBestMoveEvaluation(moveEvaluations);

		ArrayList<Move> r = new ArrayList<>();
		for (Map.Entry<Move, Double> e : moveEvaluations.entrySet())
			if (Math.abs(e.getValue() - bestMoveEvaluation) < Double.MIN_NORMAL)
				r.add(e.getKey());
		return r;
	}

	/**
	 * Returns the move that is to be played by the computer player.
	 * @param board The chess board for the game.
	 * @param validMoves A list of valid moves to choose from.
	 * @return The chosen move.
	 */
	private Move getChosenMove(Board board, List<Move> validMoves) {
		if (validMoves.isEmpty())
			throw new IllegalArgumentException("Trying to get a computer player move while there are no moves available.");

		ArrayList<Move> bestMoves = getBestMoves(board, validMoves);

		int chosenMove = rnd.nextInt(bestMoves.size());
		return bestMoves.get(chosenMove);
	}

	@Override
	public void requestMove(Controller controller, List<Move> validMoves) {
		Board board = controller.getBoard();
		Move moveToPerform = getChosenMove(board, validMoves);
		controller.offerMove(moveToPerform, isWhite());
	}

	@Override
	public boolean isWhite() {
		return isWhite;
	}

	@Override
	public Piece onPeasantReachedOtherSide(Coordinate pos) {
		return new Queen(isWhite());
	}
}
