package controller;

import engine.Coordinate;
import engine.Move;
import engine.Piece;

import java.util.List;

/**Interface to be implemented by GUI or command line programm to ask user for input**/
public interface HumanPlayerInput {

    /**asks the input to ask the user for a move selection,
     * move has to be returned separately by controller.offerMove()!
     **/
    void requestMove(Controller controller, Player player, List<Move> validMoves);

    /**asks Player to select a better replacement piece for peasant at pos**/
    Piece onPeasantReachedOtherSide(Coordinate pos, boolean isWhite);
}
