package engine;


import java.io.Serializable;
import java.util.Objects;

/**
 * The coordinate class represents coordinates on the chess board
 *
 * @autohr Mohamad Halloway
 * */
public class Coordinate implements Serializable {

    /**
     * Both x and y will be stored as int from (0 - 7)
     * */

    private int x;
    private int y;

    /**
     * Creates new coordinate according to the board array
     *
     * @param x
     *        x axis on the board (0 - 7)
     * @param y
     *        y axis on the board(0 - 7)
     * */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates new coordinate according to the chess board
     *
     * @param x
     *        x axis on the board (a - h)
     * @param y
     *        y axis on the board(1 - 8)
     * */
    public Coordinate(char x, int y) {
        int xInt = x;
        if (xInt < 97 || xInt > 104){
            throw new IllegalArgumentException("x axis is not in the interval of the board");
        }
        this.x = xInt % 97;
        this.y = 8 - y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return (char) (x + 97) + "" +  (8 - y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
