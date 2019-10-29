package gui;

import engine.Move;
import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

/**
 * A ListView equivalent for displaying the move history of a chess game
 */
class MoveListView extends ScrollPane {

    private VBox vbox;

    /**
     * Constructor initializes MoveListView, gets Move history as parameter and adds listener
     *
     * @param moves The list containing the move history
     */
    MoveListView(ObservableList<Move> moves) {
        super();
        setFitToWidth(true);
        vbox = new VBox();
        moves.addListener((ListChangeListener<Move>) c -> {
            if (c.next()) {
                if (c.wasRemoved()) {
                    vbox.getChildren().clear(); //clear, da kein move undo -> so nur bei list.clear remove
                }
                if (c.wasAdded()) {
                    addNewItem(c.getAddedSubList());
                }
            }
        });
        setContent(vbox);
        vbox.heightProperty().addListener(observable -> setVvalue(1D));
    }

    private void addNewItem(List<? extends Move> addedSubList) {
        for (Move move : addedSubList) {
            MoveListRow mlr = new MoveListRow(vbox.getChildren().size() + 1, move);
            mlr.setOpacity(0.0);
            vbox.getChildren().add(mlr);
            FadeTransition ft = new FadeTransition(Duration.millis(500), mlr);
            ft.setToValue(1.0);
            ft.play();
        }

    }
}
