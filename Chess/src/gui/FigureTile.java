package gui;

import javafx.scene.image.ImageView;

/**
 * UI element, Visual representation of a Piece in a View
 */
class FigureTile extends ImageView {

    private Figures figureType;
    private int x;
    private int y;

    /**
     * @param type The type of Piece to represent, e.g. white horse
     * @param x The x Coordinate on the chess board (0-7), may change later when figure gets moved
     * @param y The x Coordinate on the chess board (0-7), may change later when figure gets moved
     * @param parent The View containing the element
     */
    FigureTile(Figures type, int x, int y, View parent) {
        super(type.getImage());
        figureType = type;
        this.x = x;
        this.y = y;
        setSmooth(true);
        setCache(true);
        setOnMouseClicked(e -> parent.onFieldClicked(this.x, this.y));
    }

    Figures getFigureType() {
        return figureType;
    }

    void setXCoord(int x) {
        this.x = x;
    }

    void setYCoord(int y) {
        this.y = y;
    }

    void setFigureType(Figures type){
        figureType = type;
        setImage(type.getImage());
    }
}
