package engine;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    public Rook(boolean isWhite, int numberOfMoves){
        super(isWhite, numberOfMoves);
    }

    @Override
    public Piece copy() {
        return new Rook(isWhite(), getNumberOfMoves());
    }

    @Override
    public List<Move> listOfPossibleMoves(Board board, Coordinate coordinate) {
        List<Move> result = new ArrayList<>();
        int x = coordinate.getX();
        int y = coordinate.getY();
        int tempX = x + 1;
        int tempY = y;
        while (tempX < 8) {
            Coordinate dest = new Coordinate(tempX, tempY);
            Piece piece = board.getOnCell(dest);
            if (piece == null) {
                result.add(new Move(coordinate, dest));
            } else if (piece.isWhite() != isWhite()) {
                result.add(new Move(coordinate, dest, false, false, true));
                break;
            } else {
                break;
            }
            tempX++;
        }
        tempX = x - 1;
        while (tempX >= 0) {
            Coordinate dest = new Coordinate(tempX, tempY);
            Piece piece = board.getOnCell(dest);
            if (piece == null) {
                result.add(new Move(coordinate, dest));
            } else if (piece.isWhite() != isWhite()) {
                result.add(new Move(coordinate, dest, false, false, true));
                break;
            } else {
                break;
            }
            tempX--;
        }
        tempX = x;
        tempY = y + 1;
        while (tempY < 8) {
            Coordinate dest = new Coordinate(tempX, tempY);
            Piece piece = board.getOnCell(dest);
            if (piece == null) {
                result.add(new Move(coordinate, dest));
            } else if (piece.isWhite() != isWhite()) {
                result.add(new Move(coordinate, dest, false, false, true));
                break;
            } else {
                break;
            }
            tempY++;
        }
        tempY = y - 1;
        while (tempY >= 0) {
            Coordinate dest = new Coordinate(tempX, tempY);
            Piece piece = board.getOnCell(dest);
            if (piece == null) {
                result.add(new Move(coordinate, dest));
            } else if (piece.isWhite() != isWhite()) {
                result.add(new Move(coordinate, dest, false, false, true));
                break;
            } else {
                break;
            }
            tempY--;
        }
        return result;
    }

    @Override
    public String toString() {
        String c = (isWhite() ? "W" : "B");
        return c + "Rook";
    }
}
