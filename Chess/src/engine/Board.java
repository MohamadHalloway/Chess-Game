package engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the chess board
 * @author Mohamad Halloway
 * */

public class Board implements Serializable {

    private Piece[][] brett;
    private int totalMoveCount = 0; //Number of moves performed on this board (doesnt count rochade twice).

    public Board() {
        newGame();
    }

    public Board(Piece[][] brett) {
        this.brett = brett;
    }
    /**
     * Move a character on the chessboard also with regard to En Passant and Rochade
     * @param move  the move to perform
     * */
    public void performMove(Move move) {
        Piece movedPiece = brett[move.getSourceY()][move.getSourceX()];
        brett[move.getDestY()][move.getDestX()] = movedPiece;
        brett[move.getSourceY()][move.getSourceX()] = null;
        totalMoveCount++;
        movedPiece.pieceMoved(move, totalMoveCount);
        if (move.isCastling()) {    //rook muss mitbewegt werden
            if (move.getDestX() < 4) {  //Wenn castling nach links
                Piece leftRook = brett[move.getDestY()][0];
                brett[move.getDestY()][3] = leftRook;
                brett[move.getDestY()][0] = null;
                leftRook.pieceMoved(move, totalMoveCount);
            } else {
                Piece rightRook = brett[move.getDestY()][7];
                brett[move.getDestY()][5] = rightRook;
                brett[move.getDestY()][7] = null;
                rightRook.pieceMoved(move, totalMoveCount);
            }
        }else if(move.isEnPassant()){
            setPiece(move.getDestX(), move.getSourceY(), null);
        }
    }

    /**
     * Initialises a new chess board with all new chess figures
     */
    private void newGame() {
        brett = new Piece[8][8];

        //Pawns
        for (int i = 0; i < 8; i++) {
            setPiece(i, 1, new Pawn(false));
            setPiece(i, 6, new Pawn(true));
        }

        //Rooks
        setPiece(0, 0, new Rook(false));
        setPiece(7, 0, new Rook(false));
        setPiece(0, 7, new Rook(true));
        setPiece(7, 7, new Rook(true));

        //Knights
        setPiece(1, 0, new Knight(false));
        setPiece(6, 0, new Knight(false));
        setPiece(1, 7, new Knight(true));
        setPiece(6, 7, new Knight(true));

        //Bishops
        setPiece(2, 0, new Bishop(false));
        setPiece(5, 0, new Bishop(false));
        setPiece(2, 7, new Bishop(true));
        setPiece(5, 7, new Bishop(true));

        //Queens
        setPiece(3, 0, new Queen(false));
        setPiece(3, 7, new Queen(true));

        //Kings
        setPiece(4, 0, new King(false));
        setPiece(4, 7, new King(true));

    }
    /** Returns the chess figure on the specified Coordinate on the chess board
     * @param xy coordinate of the searched figure
     * @return the figure on the specified coordinate on the chess board, or null if
     *         is empty.
     **/
    public Piece getOnCell(Coordinate xy) {
        return brett[xy.getY()][xy.getX()];
    }

    public Piece getOnCell(int x, int y){
        return brett[y][x];
    }

    public void setPiece(int x, int y, Piece piece) {
        brett[y][x] = piece;
    }

    /** Returns the number of performed moves until */
    int getTotalMoveCount(){
        return totalMoveCount;
    }

    /**
     * Simulates a move to check if it prevents the playing player from check
     * @param move the move to be simulated
     * @return true if move is valid
     */
    boolean doesMovePreventCheck(Move move){
        Board simBoard = copy();
        boolean isWhite = simBoard.getOnCell(move.getSource()).isWhite();
        simBoard.performMove(move);
        return !simBoard.isPlayerChecked(isWhite);
    }

    /**
     * creates a copy of this object
     * @return a new instance of this object
     */
    public Board copy(){
        Piece[][] brettCopy = new Piece[8][8];
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(brett[y][x] != null){
                    brettCopy[y][x] = brett[y][x].copy();
                }
            }
        }
        return new Board(brettCopy);
    }

    /**
     * creates a list with all possible moves for a player
     * @param isWhite player color
     * @return the list containing all valid moves for the player specified
     */
    public List<Move> getAllPossibleMovesForPlayer(boolean isWhite) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(getOnCell(x,y) != null && getOnCell(x,y).isWhite() == isWhite){
                    moves.addAll(getOnCell(x,y).listOfValidMoves(this, new Coordinate(x,y)));
                }
            }
        }
        return moves;
    }

    /**
     * @param isWhite color of the player
     * @return true when king of player of defined color is in check
     */
    public boolean isPlayerChecked(boolean isWhite){
        Coordinate kingPos = getKingPosition(isWhite);
        if(!Board.isInBounds(kingPos.getX(), kingPos.getY())){   //Wenn kein Koenig vorhanden
            return true;
        }
        for(int y = 0; y < 8; y++){     //Ueberpruefe fuer alle gegnerischen Figuren, ob sie einen Move ausfuehren koennen, der auf dem Koenig landet
            for(int x = 0; x < 8; x++){
                if(getOnCell(x,y) != null && getOnCell(x,y).isWhite() != isWhite ){
                    List<Move> moves = getOnCell(x,y).listOfPossibleMoves(this, new Coordinate(x,y));
                    for(Move move : moves){
                        if(move.getDest().equals(kingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * searches the position of the players king and returns it
     * @param isWhite player/king color
     * @return the position of the players king
     */
    public Coordinate getKingPosition(boolean isWhite){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(getOnCell(x,y) instanceof King && getOnCell(x,y).isWhite() == isWhite){
                    return new Coordinate(x,y);
                }
            }
        }
        return new Coordinate(-1,-1);   //Wenn kein Koenig da
    }

    /**
     * helper method to tell if a coordinate is in bounds
     * @param x coordinate on board
     * @param y coordinate on board
     * @return true if coordinate is within board
     */
    static boolean isInBounds(int x, int y){
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
