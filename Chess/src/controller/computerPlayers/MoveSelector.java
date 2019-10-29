package controller.computerPlayers;

import engine.Board;
import engine.Move;

import java.util.List;

public interface MoveSelector {
    Move select(boolean isWhite, List<Move> validMoves, Board board);
}
