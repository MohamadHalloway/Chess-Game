package controller.computerPlayers;

import controller.Controller;
import controller.Player;
import engine.*;

import java.util.List;

public class ComputerPlayerSimon implements Player {

    private final boolean isWhite;
    private MoveSelector moveSelector;

    public ComputerPlayerSimon(boolean isWhite, MoveSelector moveSelector){
        this.isWhite = isWhite;
        this.moveSelector = moveSelector;
    }

    @Override
    public void requestMove(Controller controller, List<Move> validMoves) {
        controller.offerMove(moveSelector.select(isWhite(), validMoves, controller.getBoard()), isWhite());
    }


    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public Piece onPeasantReachedOtherSide(Coordinate pos) {
        return new Queen(isWhite);
    }
}
