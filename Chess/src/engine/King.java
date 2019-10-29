package engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mohamad Halloway
 **/
public class King extends Piece {

    King(boolean isWhite) {
        super(isWhite);
    }

    public King(boolean isWhite, int numberOfMoves){
        super(isWhite, numberOfMoves);
    }

    @Override
    public Piece copy() {
        return new King(isWhite(), getNumberOfMoves());
    }

    private List<Move> getStandardMoves(Board board, Coordinate source){
        List<Move> result = new ArrayList<>();
        int x = source.getX();
        int y = source.getY();
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (i == y && j == x) continue;
                if (Board.isInBounds(j, i)) {
                    Coordinate dest = new Coordinate(j, i);
                    Piece piece = board.getOnCell(dest);
                    if (piece == null) {
                        result.add(new Move(source, dest));
                    } else if (piece.isWhite() != isWhite()) {
                        result.add(new Move(source, dest, false, false, true));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Move> listOfPossibleMoves(Board board, Coordinate source) {
        List<Move> result = new ArrayList<>(getStandardMoves(board, source));

        //IsCastling
        if (getNumberOfMoves() == 0 && !isKingChecked(board, source)) {
            //left Rook
            Piece leftRook = board.getOnCell(0, source.getY());
            if(leftRook instanceof Rook && leftRook.isWhite() == isWhite() && leftRook.getNumberOfMoves() == 0){
                boolean isWayFreeForKing = true;
                for(int i = -1; i > -3; i--){   //checkt, ob der Weg zur Rochade frei von Schach und Figuren ist
                    if(board.getOnCell(source.getX() + i, source.getY()) != null || !board.doesMovePreventCheck(new Move(source, new Coordinate(source.getX() + i, source.getY())))){
                        isWayFreeForKing = false;
                        break;
                    }
                }
                if(isWayFreeForKing && board.getOnCell(1, source.getY()) == null){  //Checks if piece next to left rook is present, it would block the rochade and does not get checked by isWayFreeForKing
                    result.add(new Move(source, new Coordinate(2, source.getY()), true, false, false));
                }
            }

            //right rook
            Piece rightRook = board.getOnCell(7, source.getY());
            if(rightRook instanceof Rook && rightRook.isWhite() == isWhite() && rightRook.getNumberOfMoves() == 0){
                boolean isWayFreeForKing = true;
                for(int i = 1; i < 3; i++){     //checkt, ob der Weg zur Rochade frei von Schach und Figuren ist
                    if(board.getOnCell(source.getX() + i, source.getY()) != null || !board.doesMovePreventCheck(new Move(source, new Coordinate(source.getX() + i, source.getY())))){
                        isWayFreeForKing = false;
                        break;
                    }
                }
                if(isWayFreeForKing){
                    result.add(new Move(source, new Coordinate(6, source.getY()), true, false, false));
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String c = (isWhite() ? "W" : "B");
        return c + "King";
    }

    private boolean isKingChecked(Board board, Coordinate source){
        for(int y = 0; y < 8; y++){     //Ueberpruefe fuer alle gegnerischen Figuren, ob sie einen Move ausfuehren koennen, der auf dem Koenig landet
            for(int x = 0; x < 8; x++){
                if(board.getOnCell(x,y) != null && board.getOnCell(x,y).isWhite() != isWhite() ){
                    List<Move> moves;
                    if(board.getOnCell(x,y) instanceof King){
                        moves = ((King) board.getOnCell(x,y)).getStandardMoves(board, source);
                    }else{
                        moves = board.getOnCell(x,y).listOfPossibleMoves(board, new Coordinate(x,y));
                    }
                    for(Move move : moves){
                        if(move.getDest().equals(source)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
