package controller;

import engine.*;

import java.io.Serializable;
import java.util.Arrays;

public class Situation implements Serializable {
    @SuppressWarnings("MethodComplexity")
    private static int pieceToInt(Piece piece) {
        if (piece == null) return 0;

        int r = piece instanceof Bishop ? 1 :
                piece instanceof King ? 3 :
                        piece instanceof Knight ? 4 :
                                piece instanceof Pawn ? 5 :
                                        piece instanceof Queen ? 6 : 7; // Rook

        if (piece.isWhite()) r |= 0xf0;
        return r;
    }

    private int[] brd;

    Situation(Board board) {
        brd = new int[64];

        for (int i = 0; i < 64; i++)
            brd[i] = pieceToInt(board.getOnCell(i >>> 3, i & 7));
    }

    @SuppressWarnings("ObjectComparison")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Situation situation = (Situation) o;
        return Arrays.equals(brd, situation.brd);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(brd);
    }
}
