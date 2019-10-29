package controller.computerPlayers;

import engine.*;

import java.util.List;
import java.util.Random;

public abstract class MoveSelectorImpl implements MoveSelector {
    private static final Random random = new Random();
    private static final int SCORE_TOLERANCE = 40; //if score in scope of bestScore+-tolerance -> move can be selected

    Move lastMove = null;

    abstract int calculateScore(boolean isWhite, Move move, Board board, List<Move> enemyMoves);

    @Override
    public Move select(boolean isWhite, List<Move> validMoves, Board board) {
        Move bestMove = null;
        int bestScore = -1;

        List<Move> enemyMoves = board.getAllPossibleMovesForPlayer(!isWhite);

        for (Move move : validMoves) {
            int score = calculateScore(isWhite, move, board, enemyMoves);
            if(bestMove == null || score > bestScore || Math.abs(bestScore - score) < SCORE_TOLERANCE && random.nextBoolean()){
                bestMove = move;
                bestScore = score;
            }
        }

        lastMove = bestMove;
        return bestMove;
    }
}
