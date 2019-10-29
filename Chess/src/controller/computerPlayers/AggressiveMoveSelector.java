package controller.computerPlayers;

import engine.*;

import java.util.List;

public class AggressiveMoveSelector extends MoveSelectorImpl {
    private int repeatCounter = 0;

    private static final int KING_MOVE_PENALTY = 170;
    private static final int REPEAT_PENALTY = 120;
    private static final int LOSS_RISK_PENALTY = 75;

    private static final int BEAT_PRICE = 100;
    private static final int ROCHADE_PRICE = 300;
    private static final int PASSANT_PRICE = 70;
    private static final int CHECK_PRICE = 50;

    private int getFigureValue(Piece piece){
        if(piece instanceof Pawn) return 50;
        if(piece instanceof Bishop) return 110;
        if(piece instanceof Knight) return 90;
        if(piece instanceof Queen) return 150;
        if(piece instanceof Rook) return 110;
        return 130;  //King
    }

    private int canMoveAccessFieldCount(Coordinate pos, List<Move> moves){
        int c = 0;
        for(Move move : moves){
            if(move.getDest().equals(pos)){
                c++;
            }
        }
        return c;
    }

   int calculateScore(boolean isWhite, Move move, Board board, List<Move> enemyMoves){
        int figureValue = getFigureValue(board.getOnCell(move.getSource()));
        int score = 150 - figureValue;

        score -= canMoveAccessFieldCount(move.getDest(), enemyMoves) * (LOSS_RISK_PENALTY + figureValue) / 2;

        if(move.isDoesBeat()){
            score += BEAT_PRICE;
        }

        if(move.isEnPassant()){
            score += PASSANT_PRICE;
        }

        if(move.isCastling()){
            score += ROCHADE_PRICE;
        }else if(board.getOnCell(move.getSource()) instanceof King && board.getOnCell(move.getSource()).getNumberOfMoves() == 0){
            score -= KING_MOVE_PENALTY;
        }

        if(lastMove != null && lastMove.getSource().equals(move.getDest()) && lastMove.getDest().equals(move.getSource())){
            score -= (++repeatCounter) * REPEAT_PENALTY;
        }else{
            repeatCounter = 0;
        }

        if(board.getOnCell(move.getDest()) instanceof King && board.getOnCell(move.getDest()).isWhite() != isWhite){
            score += CHECK_PRICE;
        }

        return score;
    }
}
