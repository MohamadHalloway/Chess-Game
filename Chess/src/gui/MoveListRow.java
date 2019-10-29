package gui;

import engine.Move;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import static gui.Constants.FONT_LABEL;

class MoveListRow extends GridPane {

    /**
     * @param index 1 indiziert
     */
    MoveListRow(int index, Move move){
        setHeight(50);
        setHgap(30);
        setPadding(new Insets(10));

        Label indL = new Label(index + ". " + move.toString());
        indL.setFont(FONT_LABEL);
        GridPane.setHalignment(indL, HPos.CENTER);
        GridPane.setValignment(indL, VPos.CENTER);
        add(indL, 0, 0);

        Image img = getMoveImage(move);
        if(img != null){
            ImageView iv = new ImageView(img);
            iv.setFitWidth(getHeight());
            iv.setFitHeight(getHeight());
            GridPane.setHalignment(iv, HPos.CENTER);
            GridPane.setValignment(iv, VPos.CENTER);
            add(iv, 1, 0);
        }
    }

    private Image getMoveImage(Move move){
        if(move.isCastling()){
            return Images.ROCHADE_IMAGE;
        }
        if(move.isEnPassant()){
            return Images.EN_PASSANT_IMAGE;
        }
        if(move.isDoesBeat()){
            return Images.FIGURE_BEAT_IMAGE;
        }
        return null;
    }
}
