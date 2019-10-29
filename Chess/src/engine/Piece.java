package engine;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Piece implements Serializable {
    private boolean isWhite;
    private int numberOfMoves;


    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        numberOfMoves = 0;
    }

    /**
     * Convenience Constructor for copying a figure
     * @param isWhite piece color
     * @param numberOfMoves number of moves already performed by this piece
     */
    public Piece(boolean isWhite, int numberOfMoves){
        this.isWhite = isWhite;
        this.numberOfMoves = numberOfMoves;
    }

    /**
     * checks all possible moves of figure for check prevention
     * @param board the current board of the game
     * @param coordinate current location of the piece on the chess board
     * */

    List<Move> listOfValidMoves(Board board, Coordinate coordinate){
        List<Move> moves = listOfPossibleMoves(board, coordinate);
        return moves.stream().filter(board::doesMovePreventCheck).collect(Collectors.toList());
    }

    /**
     * returns all possible moves of a piece, meaning beating no figures of same color, check for enPassant and Castling rules, does not have to check if move leads to check after performed
     * @param board reference board
     * @param coordinate position of piece on reference board
     * @return list of possible moves
     */
    abstract List<Move> listOfPossibleMoves(Board board, Coordinate coordinate);

    /**
     * creates a copy of this object
     * @return a new instance of the piece
     */
    public abstract Piece copy();

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void pieceMoved(Move move, int moveCount){
        numberOfMoves++;
    }

    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return isWhite == piece.isWhite;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhite);
    }
}
