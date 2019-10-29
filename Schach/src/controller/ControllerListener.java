package controller;

import engine.Coordinate;
import engine.Move;
import engine.Piece;

public interface ControllerListener {

    /**event when figure gets moved**/
    void onMovePerformed(Move move);

    /** called when game is finished and winner is determined (or winningPlayer = null) **/
    void onGameFinished(Player winningPlayer, String reason);

    /**
     * event handling when the checked status of the king of one player changes
     * @param isWhite color of king
     * @param isChecked status of the king
     * @param pos position of king
     */
    void onCheckedChange(boolean isWhite, Coordinate pos, boolean isChecked);

    /**
     * event handling when a pawn got promoted, used to signal, that a piece has changed on the board
     * @param pos position, on which pawn reached the other side (y = 0 or y = 7)
     * @param piece the piece, by which the pawn got replaced
     */
    void onPawnPromotion(Coordinate pos, Piece piece);
}
