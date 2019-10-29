package engine;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    public Bishop(boolean isWhite, int numberOfMoves){
        super(isWhite, numberOfMoves);
    }

    @Override
    public Piece copy() {
        return new Bishop(isWhite(), getNumberOfMoves());
    }

    @Override
    List<Move> listOfPossibleMoves(Board board, Coordinate source) {
        List<Move> result = new ArrayList<>();
        int x = source.getX();
        int y = source.getY();

        //Gehe diagonal in alle 4 richtungen
        for(int off = 1; off < 8; off++){
            int nx = x + off;
            int ny = y + off;
            if(Board.isInBounds(nx, ny)){
                if(board.getOnCell(nx, ny) == null){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny)));
                }else if(board.getOnCell(nx, ny).isWhite() != isWhite()){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny), false, false, true));
                    break;
                }else{
                    break;
                }
            }else{
                break;
            }
        }

        for(int off = 1; off < 8; off++){
            int nx = x + off;
            int ny = y - off;
            if(Board.isInBounds(nx, ny)){
                if(board.getOnCell(nx, ny) == null){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny)));
                }else if(board.getOnCell(nx, ny).isWhite() != isWhite()){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny), false, false, true));
                    break;
                }else{
                    break;
                }
            }else{
                break;
            }
        }

        for(int off = 1; off < 8; off++){
            int nx = x - off;
            int ny = y + off;
            if(Board.isInBounds(nx, ny)){
                if(board.getOnCell(nx, ny) == null){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny)));
                }else if(board.getOnCell(nx, ny).isWhite() != isWhite()){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny), false, false, true));
                    break;
                }else{
                    break;
                }
            }else{
                break;
            }
        }

        for(int off = 1; off < 8; off++){
            int nx = x - off;
            int ny = y - off;
            if(Board.isInBounds(nx, ny)){
                if(board.getOnCell(nx, ny) == null){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny)));
                }else if(board.getOnCell(nx, ny).isWhite() != isWhite()){
                    result.add(new Move(new Coordinate(x, y), new Coordinate(nx, ny), false, false, true));
                    break;
                }else{
                    break;
                }
            }else{
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String c = (isWhite() ? "W" : "B");
        return c + "Bishop";
    }
}
