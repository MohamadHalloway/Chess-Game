package engine;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    private boolean hasMovedTwoFields = false;
    //en passant only valid directly after enemy piece has moved two pieces -> moveCountLastTwoFieldMove == board.getTotalMoveCount()
    private int moveCountLastTwoFieldMove = -1; //saves the move counter of the board at the last time when this piece has moved two pieces

    Pawn(boolean isWhite) {
        super(isWhite);
    }

    public Pawn(boolean isWhite, int numberOfMoves){
        super(isWhite, numberOfMoves);
    }

    @Override
    public Piece copy() {
        return new Pawn(isWhite(), getNumberOfMoves());
    }

    @Override
    public void pieceMoved(Move move, int moveCount) {
        super.pieceMoved(move, moveCount);
        hasMovedTwoFields = (Math.abs(move.getSourceY() - move.getDestY()) == 2);
        if(hasMovedTwoFields){
            moveCountLastTwoFieldMove = moveCount;
        }
    }

    @Override
    public List<Move> listOfPossibleMoves(Board board, Coordinate source) {
        List<Move> result = new ArrayList<>();
        int x = source.getX();
        int y = source.getY();
        int dir = (board.getOnCell(source).isWhite() ? -1 : 1);     //moving direction of pawn

        //Wenn vor pawn keine figur -> kann dahin bewegen
        if(Board.isInBounds(x, y + dir) && board.getOnCell(x, y + dir) == null){
            //Wenn noch nicht bewegt -> 2 felder springen moeglich
            if(getNumberOfMoves() == 0 && Board.isInBounds(x, y + 2 * dir) && board.getOnCell(x, y + 2 * dir) == null){
                result.add(new Move(source, new Coordinate(x, y + 2 * dir)));
            }
            result.add(new Move(source, new Coordinate(x, y + dir)));
        }

        //Checkt nach links ob enPassant, wenn nicht, ob doesBeat
        if(Board.isInBounds(x - 1, y) && board.getOnCell(x - 1, y) != null && board.getOnCell(x - 1, y).isWhite() != isWhite() && board.getOnCell(x - 1, y) instanceof Pawn && ((Pawn) board.getOnCell(x - 1, y)).hasMovedTwoFields && ((Pawn) board.getOnCell(x - 1, y)).moveCountLastTwoFieldMove == board.getTotalMoveCount()){
            result.add(new Move(source, new Coordinate(x - 1, y + dir), false, true, true));
        }else{
            if(Board.isInBounds(x - 1, y + dir) && board.getOnCell(x - 1, y + dir) != null && board.getOnCell(x - 1, y + dir).isWhite() != isWhite()){
                result.add(new Move(source, new Coordinate(x - 1, y + dir), false, false, true));
            }
        }

        //Checkt nach rechts ob enPassant, wenn nicht, ob doesBeat
        if(Board.isInBounds(x + 1, y) && board.getOnCell(x + 1, y) != null && board.getOnCell(x + 1, y).isWhite() != isWhite() && board.getOnCell(x + 1, y) instanceof Pawn && ((Pawn) board.getOnCell(x + 1, y)).hasMovedTwoFields && ((Pawn) board.getOnCell(x + 1, y)).moveCountLastTwoFieldMove == board.getTotalMoveCount()){
            result.add(new Move(source, new Coordinate(x + 1, y + dir), false, true, true));
        }else{
            if(Board.isInBounds(x + 1, y + dir) && board.getOnCell(x + 1, y + dir) != null && board.getOnCell(x + 1, y + dir).isWhite() != isWhite()){
                result.add(new Move(source, new Coordinate(x + 1, y + dir), false, false, true));
            }
        }

        return result;
    }

    @Override
    public String toString() {
        String c = (isWhite() ? "W" : "B");
        return c + "Pawn ";
    }
}
