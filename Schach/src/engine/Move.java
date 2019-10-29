package engine;


import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {

    private Coordinate source;
    private Coordinate dest;

    private boolean isCastling;
    private boolean enPassant;
    private boolean doesBeat;

    public Move(Coordinate source, Coordinate dest) {
        this.source = source;
        this.dest = dest;
    }

    public Move(Coordinate source, Coordinate dest, boolean isCastling, boolean enPassant, boolean doesBeat) {
        this.source = source;
        this.dest = dest;
        this.isCastling = isCastling;
        this.enPassant = enPassant;
        this.doesBeat = doesBeat;
    }

    public Coordinate getDest() {
        return dest;
    }

    public Coordinate getSource() {
        return source;
    }

    public int getSourceX() {
        return source.getX();
    }

    public int getSourceY() {
        return source.getY();
    }

    public int getDestX() {
        return dest.getX();
    }

    public int getDestY() {
        return dest.getY();
    }

    public boolean isCastling() {
        return isCastling;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public boolean isDoesBeat() {
        return doesBeat;
    }

    @Override
    public String toString() {
        return source + " ➜ " + dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return source.equals(move.source) &&
                dest.equals(move.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, dest);
    }
}
