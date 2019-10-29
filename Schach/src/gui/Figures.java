package gui;

import engine.*;
import javafx.scene.image.Image;

/**
 * Enum containing all chess figures and their image representations
 */
public enum Figures {
    BlACK_PEASANT, BLACK_HORSE, BLACK_TOWER, BLACK_RUNNER, BLACK_KING, BLACK_QUEEN,
    WHITE_PEASANT, WHITE_HORSE, WHITE_TOWER, WHITE_RUNNER, WHITE_KING, WHITE_QUEEN;

    private static final Image BLACK_PEASANT_IMAGE = new Image(Figures.class.getResourceAsStream("images/peasant_black.png"));
    private static final Image BLACK_HORSE_IMAGE = new Image(Figures.class.getResourceAsStream("images/horse_black.png"));
    private static final Image BLACK_TOWER_IMAGE = new Image(Figures.class.getResourceAsStream("images/tower_black.png"));
    private static final Image BLACK_RUNNER_IMAGE = new Image(Figures.class.getResourceAsStream("images/runner_black.png"));
    private static final Image BLACK_KING_IMAGE = new Image(Figures.class.getResourceAsStream("images/king_black.png"));
    private static final Image BLACK_QUEEN_IMAGE = new Image(Figures.class.getResourceAsStream("images/queen_black.png"));
    private static final Image WHITE_PEASANT_IMAGE = new Image(Figures.class.getResourceAsStream("images/peasant_white.png"));
    private static final Image WHITE_HORSE_IMAGE = new Image(Figures.class.getResourceAsStream("images/horse_white.png"));
    private static final Image WHITE_TOWER_IMAGE = new Image(Figures.class.getResourceAsStream("images/tower_white.png"));
    private static final Image WHITE_RUNNER_IMAGE = new Image(Figures.class.getResourceAsStream("images/runner_white.png"));
    private static final Image WHITE_KING_IMAGE = new Image(Figures.class.getResourceAsStream("images/king_white.png"));
    private static final Image WHITE_QUEEN_IMAGE = new Image(Figures.class.getResourceAsStream("images/queen_white.png"));

    /**
     * @return The Image for the given figure
     */
    public Image getImage() {
        switch (this) {
            case BlACK_PEASANT:
                return BLACK_PEASANT_IMAGE;
            case BLACK_HORSE:
                return BLACK_HORSE_IMAGE;
            case BLACK_TOWER:
                return BLACK_TOWER_IMAGE;
            case BLACK_RUNNER:
                return BLACK_RUNNER_IMAGE;
            case BLACK_KING:
                return BLACK_KING_IMAGE;
            case BLACK_QUEEN:
                return BLACK_QUEEN_IMAGE;
            case WHITE_PEASANT:
                return WHITE_PEASANT_IMAGE;
            case WHITE_HORSE:
                return WHITE_HORSE_IMAGE;
            case WHITE_TOWER:
                return WHITE_TOWER_IMAGE;
            case WHITE_RUNNER:
                return WHITE_RUNNER_IMAGE;
            case WHITE_KING:
                return WHITE_KING_IMAGE;
            case WHITE_QUEEN:
                return WHITE_QUEEN_IMAGE;
            default:
                return null;
        }
    }

    /**
     * @return A boolean value representing, if the figure is white or not (black)
     */
    public boolean isWhite(){
        return this.ordinal() > 5;
    }

    /**
     * Converts Piece to Enum value
     * @param piece The piece to be converted to the Enum equivalent
     * @return the Enum equivalent of given Piece
     */
    public static Figures fromFigure(Piece piece){
        if(piece == null){
            return null;
        }
        if(piece.isWhite()){
            if(piece instanceof Bishop){
                return WHITE_RUNNER;
            }else if(piece instanceof Rook){
                return WHITE_TOWER;
            }else if(piece instanceof King){
                return WHITE_KING;
            }else if(piece instanceof Knight){
                return WHITE_HORSE;
            }else if(piece instanceof Pawn){
                return WHITE_PEASANT;
            }else if(piece instanceof Queen){
                return WHITE_QUEEN;
            }
        }else{
            if(piece instanceof Bishop){
                return BLACK_RUNNER;
            }else if(piece instanceof Rook){
                return BLACK_TOWER;
            }else if(piece instanceof King){
                return BLACK_KING;
            }else if(piece instanceof Knight){
                return BLACK_HORSE;
            }else if(piece instanceof Pawn){
                return BlACK_PEASANT;
            }else if(piece instanceof Queen){
                return BLACK_QUEEN;
            }
        }
        return null;
    }
}
