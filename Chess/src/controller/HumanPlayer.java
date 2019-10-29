package controller;

import engine.Coordinate;
import engine.Move;
import engine.Piece;

import java.util.List;

public class HumanPlayer implements Player {

    private boolean isWhite;
    private HumanPlayerInput input;

    public HumanPlayer(boolean isWhite, HumanPlayerInput input){
        this.isWhite = isWhite;
        this.input = input;
    }

    @Override
    public void requestMove(Controller controller, List<Move> validMoves) {
        input.requestMove(controller, this, validMoves);
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public Piece onPeasantReachedOtherSide(Coordinate pos) {
        return input.onPeasantReachedOtherSide(pos, isWhite);
    }
}
