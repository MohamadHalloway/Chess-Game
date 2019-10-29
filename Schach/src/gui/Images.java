package gui;

import javafx.scene.image.Image;

/**
 * Convenience Class containing all non-figure images used by the view
 */
class Images {
    static final Image ROCHADE_IMAGE = new Image(Images.class.getResourceAsStream("images/rochade.png"));
    static final Image FIGURE_BEAT_IMAGE = new Image(Images.class.getResourceAsStream("images/beatfigure.png"));
    static final Image EN_PASSANT_IMAGE = new Image(Images.class.getResourceAsStream("images/enpassant.png"));
    static final Image BACKGROUND_IMAGE = new Image(Images.class.getResourceAsStream("images/background.png"));
}
