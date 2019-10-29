/*
	Author: Adrian Samoticha
 */

package controller.computerPlayers;

import controller.Controller;
import controller.Player;
import engine.*;

import java.util.List;
import java.util.Random;

public class ComputerPlayerRandom implements Player {

	private boolean isWhite;
	private Random rnd = new Random();

	public ComputerPlayerRandom(boolean isWhite) {
		this.isWhite = isWhite;
	}

	/**
	 * Returns the move that is to be played by the computer player.
	 * @param validMoves A list of valid moves.
	 * @return The chosen move.
	 */
	private Move getChosenMove(List<Move> validMoves) {
		if (validMoves.isEmpty())
			throw new IllegalStateException("Trying to get a computer player move while there are no moves available.");

		int chosenMove = rnd.nextInt(validMoves.size());
		return validMoves.get(chosenMove);
	}

	@Override
	public void requestMove(Controller controller, List<Move> validMoves) {
		Board board = controller.getBoard();
		Move moveToPerform = getChosenMove(validMoves);
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
