package engine;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    public Queen(boolean isWhite, int numberOfMoves){
        super(isWhite, numberOfMoves);
    }

    @Override
    public Piece copy() {
        return new Queen(isWhite(), getNumberOfMoves());
    }

    @Override
    public List<Move> listOfPossibleMoves(Board board, Coordinate coordinate) {
        ArrayList<Move> result = new ArrayList<>();
        Bishop bishop = new Bishop(isWhite());
        Rook rook = new Rook(isWhite());
        result.addAll(bishop.listOfPossibleMoves(board,coordinate));
        result.addAll(rook.listOfPossibleMoves(board,coordinate));
        return result;
    }

    @Override
    public String toString() {
        String c = (isWhite() ? "W" : "B");
        return c + "Queen";
    }
}
