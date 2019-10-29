package gui;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static gui.Constants.COLOR_BLACK_TILE;
import static gui.Constants.COLOR_WHITE_TILE;

/**
 * UI element representing a field on a chess board
 */
class GridTile extends Pane {

    private static final String COLOR_CHECKED = "FE2E2E";
    private static final String COLOR_RECTANGLE = "2E9AFE";
    private boolean white;
    private boolean highlighted;
    private boolean checked;

    /**
     * @param isWhite Tells GridTile to be white or black background
     * @param x       The x Coordinate on the chess board (0-7)
     * @param y       The y Coordinate on the chess board (0-7)
     * @param parent  The View containing the element
     */
    GridTile(boolean isWhite, int x, int y, View parent) {
        super();
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);     //must be done so that resizing in GridPane is possible
        white = isWhite;
        checked = false;
        highlighted = false;
        setBackgroundColor();
        setOnMouseClicked(e -> parent.onFieldClicked(x, y));
    }

    boolean isHighlighted() {
        return highlighted;
    }

    /**
     * sets highlighted and updates background color
     *
     * @param highlighted The value to set highlighted to
     */
    void setHighlighted(boolean highlighted) {
        if (highlighted != this.highlighted) {
            this.highlighted = highlighted;
            setBackgroundColor();
        }
    }

    /**
     * sets the backgroud color according to white and highlighted
     */
    private void setBackgroundColor() {
        if (checked) {
            this.setStyle("-fx-background-color: #" + COLOR_CHECKED + ";");
        } else {
            if (white) {
                this.setStyle("-fx-background-color: #" + COLOR_WHITE_TILE + ";");
            } else {
                this.setStyle("-fx-background-color: #" + COLOR_BLACK_TILE + ";");
            }
        }
        if (highlighted) {
            showRectangle(true);
        } else {
            showRectangle(false);
        }
    }

    private void showRectangle(boolean isFadeIn) {
        if (isFadeIn) {
            Rectangle rect = new Rectangle(4, 4, getWidth() - 8, getHeight() - 8);
            rect.setFill(Color.TRANSPARENT);
            rect.setStyle("-fx-stroke-width: 4;-fx-stroke: #" + COLOR_RECTANGLE + ";");
            rect.setArcHeight(5.0);
            rect.setArcWidth(5.0);
            getChildren().add(rect);
            FadeTransition ft = new FadeTransition(Duration.millis(500), rect);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        } else if (!getChildren().isEmpty()) {
            getChildren().clear();
        }
    }

    void setChecked(boolean checked) {
        if (checked != this.checked) {
            this.checked = checked;
            setBackgroundColor();
        }
    }
}
