package controller;

import engine.Coordinate;
import engine.Move;
import engine.Piece;

import java.util.List;

public interface Player {

    /**gets called by Controller to start the move selection process. Has to call back by Controller.offerMove()**/
    void requestMove(Controller controller, List<Move> validMoves);

    /**lets the caller know if the player is white or black**/
    boolean isWhite();

    /**asks Player to select a better replacement piece for peasant at pos**/
    Piece onPeasantReachedOtherSide(Coordinate pos);
}
