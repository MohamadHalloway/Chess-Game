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

public class ComputerPlayerDeep implements Player {

	private boolean isWhite;
	private Random rnd = new Random();
	private int minDepth;
	private int maxDepth;
	private ComputerPlayerShallow virtualOpponent;
	private final double infty = 1000000.0;

	public ComputerPlayerDeep(boolean isWhite, int minDepth, int maxDepth) {
		this.isWhite = isWhite;
		this.minDepth = minDepth;

		virtualOpponent = new ComputerPlayerShallow(!isWhite);
		this.maxDepth = maxDepth;
	}

	/**
	 * Performs the minimax-algorithm with alpha-beta pruning.
	 * @param board The board used to simulate the moves.
	 * @param depth The depth of the search tree.
	 * @param alpha Let this be negative inifity for the initial call.
	 * @param beta Let this be positive inifnity for the initial call.
	 * @param maximizingPlayer Is the currently evalutated player trying to maximize the score? (Let it be false for the
	 *                         initial call)
	 * @return The best score to be achieved.
	 */
	private double alphabeta(Board board, int depth, double alpha, double beta, boolean maximizingPlayer) {
		boolean isWhite = maximizingPlayer == this.isWhite;

		if (depth == 0)
			return GameStateAnalyzer.analyzeGameState(board, this.isWhite);

		List<Move> moveList = board.getAllPossibleMovesForPlayer(isWhite);

		if (maximizingPlayer) {
			double value = -infty;
			for (Move move : moveList) {
				Board child = board.copy();
				MoveSimulator.simulateMove(child, this, move);
				value = Math.max(value, alphabeta(child, depth - 1, alpha, beta, false));
				if (alpha >= beta) break;
			}

			return value;
		}

		double value = infty;
		for (Move move  : moveList) {
			Board child = board.copy();
			MoveSimulator.simulateMove(child, virtualOpponent, move);
			value = Math.min(value, alphabeta(child, depth - 1, alpha, beta, true));
			if (alpha >= beta) break;
		}

		return value;
	}

	/**
	 * Evaluates the score of a given move.
	 * @param board The board used to simulate the move.
	 * @param move The move to be evaluated.
	 * @param depth The depth of the search tree.
	 * @return The evaluated score of the move.
	 */
	private double evaluateMove(Board board, Move move, int depth) {
		Board boardCopy = board.copy();
		MoveSimulator.simulateMove(boardCopy, this, move);

		// Let the maximizing player be false, since a move has already been performed, therefore it is now the
		// opponents' turn.
		return alphabeta(boardCopy, depth - 1, -infty, infty, false);
	}

	/**
	 * A class used to perform the move evaluation on multiple threads.
	 */
	class MoveEvaluator implements Runnable {
		private Thread t = new Thread(this, "");

		private LinkedHashMap<Move, Double> moveEvaluations;
		private Move move;
		private Board board;
		private int depth;

		MoveEvaluator(LinkedHashMap<Move, Double> moveEvaluations, Move move, Board board, int depth) {
			this.moveEvaluations = moveEvaluations;
			this.move = move;
			this.board = board;
			this.depth = depth;
		}

		@Override
		public void run() {
			double e = evaluateMove(board, move, depth);
			moveEvaluations.put(move, e);
		}

		void start() {
			t.start();
		}
	}

	/**
	 * Evaluates a list of moves.
	 * @param board The board used to evaluate the moves.
	 * @param moveList A list containing the moves to be evaluated.
	 * @param depth The depth of the search tree.
	 * @return A linked hash map where the key is a move from the moveList and the value is the corresponding rating.
	 */
	private LinkedHashMap<Move, Double> evaluateMoves(Board board, List<Move> moveList, int depth) {
		LinkedHashMap<Move, Double> r = new LinkedHashMap<>();
		ArrayList<MoveEvaluator> threads = new ArrayList<>();
		for (Move m : moveList) {
			MoveEvaluator t = new MoveEvaluator(r, m, board, depth);
			threads.add(t);
			t.start();
		}

		for (MoveEvaluator t : threads) {
			//noinspection StatementWithEmptyBody
			while (t.t.isAlive());
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
	 * @param depth The depth of the search tree.
	 * @return A list of the best moves.
	 */
	@SuppressWarnings("Duplicates")
	private ArrayList<Move> getBestMoves(Board board, List<Move> moveList, int depth) {
		LinkedHashMap<Move, Double> moveEvaluations = evaluateMoves(board, moveList, depth);
		double bestMoveEvaluation = findBestMoveEvaluation(moveEvaluations);

		ArrayList<Move> r = new ArrayList<>();
		for (Map.Entry<Move, Double> e : moveEvaluations.entrySet())
			if (Math.abs(e.getValue() - bestMoveEvaluation) < Double.MIN_NORMAL)
				r.add(e.getKey());

		return r.size() == 1 | depth == maxDepth ? r : getBestMoves(board, r, depth + 1);
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

		ArrayList<Move> bestMoves = getBestMoves(board, validMoves, minDepth);

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
