/*
	Author: Adrian Samoticha
 */

package controller;

import engine.*;

@SuppressWarnings("CStyleArrayDeclaration")
public class GameStateAnalyzer {
	private static double pawnEvalWhite[][] = {
		{ 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0 },
		{ 5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0 },
		{ 1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0 },
		{ 0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5 },
		{ 0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0 },
		{ 0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5 },
		{ 0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5 },
		{ 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0 }
	};

	private static double knightEval[][] = {
		{ -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 },
		{ -4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0 },
		{ -3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0 },
		{ -3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0 },
		{ -3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0 },
		{ -3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0 },
		{ -4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0 },
		{ -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 }
	};

	private static double bishopEvalWhite[][] = {
		{ -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 },
		{ -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0 },
		{ -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0 },
		{ -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0 },
		{ -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0 },
		{ -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0 },
		{ -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0 },
		{ -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 }
	};

	private static double rookEvalWhite[][] = {
		{  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0 },
		{  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5 },
		{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5 },
		{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5 },
		{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5 },
		{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5 },
		{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5 },
		{  0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0 }
	};

	private static double queenEval[][] = {
		{ -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 },
		{ -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0 },
		{ -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0 },
		{ -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5 },
		{  0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5 },
		{ -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0 },
		{ -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0 },
		{ -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 }
	};

	private static double kingEvalWhite[][] = {
		{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
		{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
		{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
		{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
		{ -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0 },
		{ -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0 },
		{  2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0 },
		{  2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0 }
	};

	/**
	 * Gets the raw value of a piece, disregarding its current position.
	 * @param piece The piece to get the value for.
	 * @return The value of the piece.
	 */
	private static double getRawPieceValue(Piece piece) {
		return piece instanceof Pawn ? 10 :
			   piece instanceof Rook ? 50 :
			   piece instanceof Knight ? 30 :
			   piece instanceof Bishop ? 30 :
			   piece instanceof Queen ? 90 : 900;
	}

	/**
	 * Gets the value of piece at a given position.
	 * @param piece The piece to check the value for.
	 * @param x The piece's x coordinate.
	 * @param y The piece's y coordinate.
	 * @return The value of the piece.
	 */
	@SuppressWarnings("MethodComplexity")
	private static double getPieceValue(Piece piece, int x, int y) {
		if (piece == null) return 0.0;

		double[][] array = piece instanceof Pawn ? pawnEvalWhite :
			               piece instanceof Knight ? knightEval :
				           piece instanceof Bishop ? bishopEvalWhite :
					       piece instanceof Rook ? rookEvalWhite :
						   piece instanceof Queen ? queenEval : kingEvalWhite;

		boolean w = piece.isWhite();
		boolean doInvert = !w && !(piece instanceof Queen) && !(piece instanceof Knight);
		double r = getRawPieceValue(piece) + array[doInvert ? 7 - y : y][x];
		return w ? r : -r;
	}

	/**
	 * Analyses a game state and returns a value based on which player has an advantage.
	 * @param board The board to check the game state for.
	 * @param isWhite Which player to check the advantage for.
	 * @return A numeric value representing the advantage the given player has in the given game state.
	 */
	public static double analyzeGameState(Board board, boolean isWhite) {
		double r = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board.getOnCell(new Coordinate(i, j));
				double value = getPieceValue(piece, i, j);
				r += value;
			}
		}

		return isWhite ? r : -r;
	}
}
