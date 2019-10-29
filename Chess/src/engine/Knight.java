package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    public Knight(boolean isWhite, int numberOfMoves){
        super(isWhite, numberOfMoves);
    }

    @Override
    public Piece copy() {
        return new Knight(isWhite(), getNumberOfMoves());
    }

    @Override
    public List<Move> listOfPossibleMoves(Board board, Coordinate coordinate) {
        List<Move> result = new ArrayList<>();
        int tempX = coordinate.getX();
        int tempY = coordinate.getY();

        //two steps up||down & one left||right
        for (int i = -1; i <= 1; i += 2) {
            Coordinate left = new Coordinate(tempX - 2, tempY + i);
            Coordinate up = new Coordinate(tempX + i, tempY - 2);
            Coordinate right = new Coordinate(tempX + 2, tempY + i);
            Coordinate down = new Coordinate(tempX + i, tempY + 2);
            List<Coordinate> tempList = new ArrayList<>(Arrays.asList(left, right, up, down));
            tempList.forEach(dest -> {
                int x = dest.getX();
                int y = dest.getY();
                if (Board.isInBounds(x, y)) {
                    Piece piece = board.getOnCell(dest);
                    if (piece == null){
                        result.add(new Move(coordinate, dest));
                    } else if (board.getOnCell(dest).isWhite() != isWhite()) {
                        result.add(new Move(coordinate,dest,false,false,true));
                    }

                }
            });
        }
        return result;
    }

    @Override
    public String toString() {
        String c = (isWhite() ? "W" : "B");
        return c + "knight";
    }
}
