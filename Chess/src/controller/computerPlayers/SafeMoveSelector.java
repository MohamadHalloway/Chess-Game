package controller.computerPlayers;

import engine.*;

import java.util.List;

/**only beats when safe or no rescue**/
public class SafeMoveSelector extends MoveSelectorImpl {

    private static final int SAFE_MOVE_SCORE = 500;
    private static final int SAFE_BEAT_PRICE = 500;
    private static final int NO_RESCUE_BEAT_PRICE = 0;

    private int getFigureValue(Piece piece){
        if(piece instanceof Pawn) return 50;
        if(piece instanceof Bishop) return 100;
        if(piece instanceof Knight) return 100;
        if(piece instanceof Queen) return 200;
        if(piece instanceof Rook) return 100;
        return 50;  //King
    }

    private boolean canMoveAccessFieldCount(Coordinate pos, List<Move> moves){
        for(Move move : moves){
            if(move.getDest().equals(pos)){
                return true;
            }
        }
        return false;
    }

    @Override
    int calculateScore(boolean isWhite, Move move, Board board, List<Move> enemyMoves) {
        int score = 0;

        if(canMoveAccessFieldCount(move.getSource(), enemyMoves)){  //falls eigene Figur geschmissen werden kann
            if(!canMoveAccessFieldCount(move.getDest(), enemyMoves)){
                score += SAFE_MOVE_SCORE;
                if(move.isDoesBeat()){
                    score += SAFE_BEAT_PRICE + getFigureValue(board.getOnCell(move.getDest())) - getFigureValue(board.getOnCell(move.getSource()));
                }
            }else if(move.isDoesBeat()){
                score += NO_RESCUE_BEAT_PRICE + getFigureValue(board.getOnCell(move.getDest())) - getFigureValue(board.getOnCell(move.getSource()));
            }
        }

        return score;
    }
}
