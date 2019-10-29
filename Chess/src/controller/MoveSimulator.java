/*
	Author: Adrian Samoticha
 */

package controller;

import engine.Board;
import engine.King;
import engine.Move;
import engine.Pawn;

public class MoveSimulator {

	public static boolean simulateMove(Board board, Player player, Move move) {
		if (board.getOnCell(move.getDest()) instanceof King) return false;

		board.performMove(move);
		boolean isWhitePlayersTurn = player.isWhite();
//		if (board.getOnCell(move.getDest()) instanceof Pawn && board.getOnCell(move.getDest()).isWhite() == isWhitePlayersTurn && (isWhitePlayersTurn ? move.getDestY() == 0 : move.getDestY() == 7)) {
		if (board.getOnCell(move.getDest()) instanceof Pawn && move.getDestY() == (isWhitePlayersTurn ? 0 : 7)) {
			board.setPiece(move.getDestX(), move.getDestY(), player.onPeasantReachedOtherSide(move.getDest()));
		}

		return true;
	}
}
