package gui;

import controller.*;
import engine.*;
import io.GameReader;
import io.GameWriter;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static gui.Constants.FONT_LABEL;

public class View extends Application implements HumanPlayerInput, ControllerListener {

    //Layout Data
    private static final int GRID_OFFSET_X = 40;
    private static final int GRID_OFFSET_Y = 40;
    private static final int BOARDSIZE = 8;
    private static final int TILESIZE = 70;
    private static final int FIGURESIZE = 60;
    private static final int MENU_WIDTH = 300;
    private static final int MOVETIME = 1500;   //The time the animation takes to move a figure
    private Stage primary;
    private Pane root = new Pane();

    //Figures and Tiles
    private GridTile[][] board = new GridTile[BOARDSIZE][BOARDSIZE];        //first index y, second x
    private FigureTile[][] figures = new FigureTile[BOARDSIZE][BOARDSIZE];  //first y, second x

    //New Game Dialog Data
    private boolean dialogResult;   //true -> new game, false -> abort
    private String whitePlayer;
    private String blackPlayer;

    //Pawn Upgrade Dialog Data
    private Piece returnPiece = null;

    //User selection data
    private boolean isSelectionMode = false;
    private boolean selectionPlayerColor = false;   //true -> white, false -> black
    private Coordinate firstSelectedTile = null;
    private List<Move> validMovesForPlayer = null;

    //Game Data
    private Controller controller = new Controller();

    //FileChooser Data
    private static final FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("Chess Game Format", "*.cgf");

    /**
     * Java FX initialization function, starts the GUI
     *
     * @param primaryStage The main stage, starting point of the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        primary = primaryStage;
        primaryStage.getIcons().add(Figures.BLACK_QUEEN.getImage());

        //GridPane initialization
        GridPane gridChess = new GridPane();
        gridChess.relocate(GRID_OFFSET_X, GRID_OFFSET_Y);
        for (int i = 0; i < 8; i++) {
            ColumnConstraints cc = new ColumnConstraints(TILESIZE);
            RowConstraints rc = new RowConstraints(TILESIZE);
            cc.setFillWidth(true);
            rc.setFillHeight(true);
            gridChess.getColumnConstraints().add(cc);
            gridChess.getRowConstraints().add(rc);
        }
        root.getChildren().add(gridChess);
        for (int y = 0; y < BOARDSIZE; y++) {
            for (int x = 0; x < BOARDSIZE; x++) {
                GridTile gt = new GridTile(((y % 2 == 0 && x % 2 == 0) || (y % 2 == 1 && x % 2 == 1)), x, y, this);     //Oben links startet mit weißem Feld => weiß unten
                board[y][x] = gt;
                gridChess.add(gt, x, y);
            }
        }
        primaryStage.setOnCloseRequest(this::exit);

        Label mhLabel = new Label("Move History:");
        mhLabel.setFont(FONT_LABEL);
        mhLabel.relocate(2 * GRID_OFFSET_X + BOARDSIZE * TILESIZE, (GRID_OFFSET_Y - FONT_LABEL.getSize()) / 2);
        root.getChildren().add(mhLabel);

        //Menu Strip
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-font-size: 15pt ;");
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        MenuItem aboutMI = new MenuItem("About");
        aboutMI.setOnAction(e -> {
            Alert credits = new Alert(Alert.AlertType.INFORMATION);
            credits.initStyle(StageStyle.UTILITY);
            credits.setTitle("About");
            credits.setHeaderText("Credits");
            credits.setContentText("Game made by:\n\nAdrian Samoticha\nMohamad Halloway\nSimon Trapp");
            credits.showAndWait();
        });
        helpMenu.getItems().add(aboutMI);
        MenuItem newGameMI = new MenuItem("New Game");
        newGameMI.setOnAction(e -> setupNewGame(primary, true));
        MenuItem openGameMI = new MenuItem("Load");
        openGameMI.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load game");
            fileChooser.getExtensionFilters().add(ef);
            fileChooser.setSelectedExtensionFilter(ef);
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    GameReader.read(file.getPath(), controller);
                    setupNewGame(primary, false);
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                    showAlert("Game could not be loaded!");
                }
            }
        });
        MenuItem saveGameMI = new MenuItem("Save");
        saveGameMI.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save game");
            fileChooser.getExtensionFilters().add(ef);
            fileChooser.setSelectedExtensionFilter(ef);
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    GameWriter.write(file.getPath(), controller);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    showAlert("Game could not be saved!");
                }
            }
        });
        MenuItem closeMI = new MenuItem("Exit");
        closeMI.setOnAction(e -> exit());
        fileMenu.getItems().addAll(newGameMI, openGameMI, saveGameMI, closeMI);

        //Move History setup
        MoveListView movHistView = new MoveListView(controller.getMoveHistory());
        movHistView.relocate(2 * GRID_OFFSET_X + BOARDSIZE * TILESIZE, GRID_OFFSET_Y);
        movHistView.setPrefSize(MENU_WIDTH - GRID_OFFSET_X, BOARDSIZE * TILESIZE - MENU_WIDTH + GRID_OFFSET_Y);
        root.getChildren().add(movHistView);

        //setup coordinate labels
        setupCoordinateLabels();

        VBox vb = new VBox();
        vb.setFillWidth(true);
        vb.getChildren().addAll(menuBar, root);

        Scene gameScene = new Scene(vb);//, 2 * GRID_OFFSET_X + BOARDSIZE * TILESIZE + MENU_WIDTH, 2 * GRID_OFFSET_Y + BOARDSIZE * TILESIZE + 20);
        primaryStage.setResizable(false);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("SWP 2019 Chess");
        vb.setBackground(new Background(new BackgroundImage(Images.BACKGROUND_IMAGE, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.RIGHT, 0, false, Side.BOTTOM, 0, false), new BackgroundSize(Images.BACKGROUND_IMAGE.getWidth() / 2, Images.BACKGROUND_IMAGE.getHeight() / 2, false, false, false, false))));
        primaryStage.show();

        setupNewGame(primary, true);
    }

    private void exit() {
        boolean answer = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.initOwner(primary);
        alert.setHeaderText("");
        alert.setContentText("Do you want to exit the game?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancel");
        alert.showAndWait();
        if (!alert.getResult().getButtonData().isCancelButton()){
            answer = true;
            alert.close();
        }
        if (answer) {
            primary.close();
        }
    }

    private void exit(Event event) {
        event.consume();
        exit();
    }

    /**
     * prints coordinate labels on the sides of the chessboard
     */
    private void setupCoordinateLabels() {
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for (int i = 0; i < 8; i++) {
            Label labelTopLetter = new Label(String.valueOf(letters[i]));
            labelTopLetter.setFont(FONT_LABEL);
            labelTopLetter.relocate(GRID_OFFSET_X + i * TILESIZE + (TILESIZE - labelTopLetter.getFont().getSize()) / 2, (GRID_OFFSET_Y - labelTopLetter.getFont().getSize()) / 2);

            Label labelBottomLetter = new Label(String.valueOf(letters[i]));
            labelBottomLetter.setFont(FONT_LABEL);
            labelBottomLetter.relocate(GRID_OFFSET_X + i * TILESIZE + (TILESIZE - labelBottomLetter.getFont().getSize()) / 2, BOARDSIZE * TILESIZE + GRID_OFFSET_Y + (GRID_OFFSET_Y - labelBottomLetter.getFont().getSize()) / 2);

            Label labelLeftNumber = new Label(String.valueOf(8 - i));
            labelLeftNumber.setFont(FONT_LABEL);
            labelLeftNumber.relocate((GRID_OFFSET_X - labelLeftNumber.getFont().getSize()) / 2, GRID_OFFSET_Y + i * TILESIZE + (TILESIZE - labelLeftNumber.getFont().getSize()) / 2);

            Label labelRightNumber = new Label(String.valueOf(8 - i));
            labelRightNumber.setFont(FONT_LABEL);
            labelRightNumber.relocate(GRID_OFFSET_X + BOARDSIZE * TILESIZE + (GRID_OFFSET_X - labelRightNumber.getFont().getSize()) / 2, GRID_OFFSET_Y + i * TILESIZE + (TILESIZE - labelRightNumber.getFont().getSize()) / 2);

            root.getChildren().addAll(labelTopLetter, labelBottomLetter, labelLeftNumber, labelRightNumber);
        }
    }

    /**
     * Helper function to calculate the x position for figures on the stage
     *
     * @param x the chessboard coordinate (0-7) of the figure
     * @return the x localization value for the FigureTile on the stage
     */
    private int getFigurePosXForCoord(int x) {
        return GRID_OFFSET_X + x * TILESIZE + (TILESIZE - FIGURESIZE) / 2;
    }

    /**
     * Helper function to calculate the y position for figures on the stage
     *
     * @param y the chessboard coordinate (0-7) of the figure
     * @return the y localization value for the FigureTile on the stage
     */
    private int getFigurePosYForCoord(int y) {
        return GRID_OFFSET_Y + y * TILESIZE + (TILESIZE - FIGURESIZE) / 2;
    }

    /**
     * Adds FigureTiles to the View, does not remove old FigureTiles. For that, clearView() has to be called first
     *
     * @param board The Board, which figures should be represented on the GUI
     */
    private void setupFiguresByBoard(Board board) {
        for (int y = 0; y < BOARDSIZE; y++) {
            for (int x = 0; x < BOARDSIZE; x++) {
                Piece p = board.getOnCell(new Coordinate(x, y));
                if (p != null) {
                    addFigureTile(new FigureTile(Figures.fromFigure(p), x, y, this), x, y);
                }
            }
        }
    }

    /**
     * Adds a FigureTile to the View stage
     *
     * @param ft The FigureTile to be added
     * @param x  The chess board x coordinate (0-7) of the figure
     * @param y  The chess board y coordinate (0-7) of the figure
     */
    private void addFigureTile(FigureTile ft, int x, int y) {
        ft.relocate(getFigurePosXForCoord(x), getFigurePosYForCoord(y));
        ft.setFitHeight(FIGURESIZE);
        ft.setFitWidth(FIGURESIZE);
        root.getChildren().add(ft);
        figures[y][x] = ft;
    }

    /**
     * Event called by GridTiles and FigureTiles of the View when clicked on them
     *
     * @param x chess board x coordinate (0-7) of the Tile
     * @param y chess board y coordinate (0-7) of the Tile
     */
    void onFieldClicked(int x, int y) {
        if (isSelectionMode) {                        //only react in selection mode
            if (firstSelectedTile != null) {          //if start figure was already selected
                if (x == firstSelectedTile.getX() && y == firstSelectedTile.getY()) {
                    firstSelectedTile = null;
                    setUnhighlightAllTiles();
                    highlightAllMovableFigures(validMovesForPlayer);
                } else if (board[y][x].isHighlighted()) {
                    //valid move -> unhighlight tiles, turn off selection mode, offerMove
                    Move selectedMove = new Move(firstSelectedTile, new Coordinate(x, y));
                    setUnhighlightAllTiles();
                    firstSelectedTile = null;
                    isSelectionMode = false;
                    controller.offerMove(selectedMove, selectionPlayerColor);
                }
            } else {                                  //first figure was already selected
                if (figures[y][x] != null && figures[y][x].getFigureType().isWhite() == selectionPlayerColor) {
                    Coordinate coord = new Coordinate(x, y);
                    firstSelectedTile = coord;
                    setUnhighlightAllTiles();
                    board[y][x].setHighlighted(true);
                    highlightAllMovesForFigure(coord);
                }
            }
        }
    }

    /**
     * Starts a new window showing the new game screen.
     * Should not be called by a method except setupNewGame()!
     *
     * @param owner     The owner stage of the new game screen window (primary stage in most cases)
     * @param isNewGame Tells if the new game screen is shown after a loading dialog or really a new game
     */
    private void showNewGameScreen(Stage owner, boolean isNewGame) {
        dialogResult = false;
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        Button startButton = new Button(isNewGame ? "Start" : "Continue");
        startButton.setDefaultButton(true);

        ComboBox<String> whiteCB = new ComboBox<>();
        whiteCB.getItems().addAll(PlayerFactory.playerTypeStrings);
        whiteCB.setValue(PlayerFactory.playerTypeStrings[0]);
        ComboBox<String> blackCB = new ComboBox<>();
        blackCB.getItems().addAll(PlayerFactory.playerTypeStrings);
        blackCB.setValue(PlayerFactory.playerTypeStrings[1]);

        if (isNewGame) {
            Button abortButton = new Button("Abort");
            abortButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            abortButton.setOnAction(e -> stage.close());
            pane.add(abortButton, 0, 2);
            GridPane.setFillWidth(abortButton, true);
        } else {
            stage.setOnCloseRequest(e -> {
                dialogResult = true;
                whitePlayer = whiteCB.getValue();
                blackPlayer = blackCB.getValue();
                stage.close();
            });
        }

        startButton.setOnAction(e -> {
            dialogResult = true;
            whitePlayer = whiteCB.getValue();
            blackPlayer = blackCB.getValue();
            stage.close();
        });

        ImageView wkiv = new ImageView(Figures.WHITE_KING.getImage());
        wkiv.setFitHeight(FIGURESIZE);
        wkiv.setFitWidth(FIGURESIZE);
        ImageView bkiv = new ImageView(Figures.BLACK_KING.getImage());
        bkiv.setFitHeight(FIGURESIZE);
        bkiv.setFitWidth(FIGURESIZE);

        GridPane.setHalignment(blackCB, HPos.CENTER);
        GridPane.setHalignment(whiteCB, HPos.CENTER);
        GridPane.setHalignment(wkiv, HPos.CENTER);
        GridPane.setHalignment(bkiv, HPos.CENTER);

        startButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(startButton, true);

        pane.add(wkiv, 0, 0);
        pane.add(bkiv, 1, 0);
        pane.add(startButton, 1, 2);
        pane.add(whiteCB, 0, 1);
        pane.add(blackCB, 1, 1);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle(isNewGame ? "New game" : "Player selection");
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Shows new game dialog window. Then evaluates dialog result.
     *
     * @param primary   The primary stage of the View/GUI.
     * @param isNewGame True, if this is a new game. False, if this method gets called after a game was loaded.
     */
    private void setupNewGame(Stage primary, boolean isNewGame) {
        showNewGameScreen(primary, true);
        if (dialogResult) {     //true -> user pressed start new game button
            clearView();
            controller.setupNewGame(PlayerFactory.getPlayerByString(whitePlayer, true, this), PlayerFactory.getPlayerByString(blackPlayer, false, this), isNewGame);
            setupFiguresByBoard(controller.getBoard());
            controller.addControllerListener(this);
            controller.start();
        }
    }

    /**
     * Removes all FigureTiles from the View. Removes all highlights and checks from board
     */
    private void clearView() {
        setUnhighlightAllTiles();
        for (int y = 0; y < BOARDSIZE; y++) {
            for (int x = 0; x < BOARDSIZE; x++) {
                removeFigureTile(x, y);
                board[y][x].setChecked(false);
            }
        }
    }

    /**
     * Sets highlight to false for all GridTiles in View
     **/
    private void setUnhighlightAllTiles() {
        for (int y = 0; y < BOARDSIZE; y++) {
            for (int x = 0; x < BOARDSIZE; x++) {
                board[y][x].setHighlighted(false);
            }
        }
    }

    /**
     * Highlights the source coordinates of all moves (= positions of the figures)
     *
     * @param moves The list of valid moves
     */
    private void highlightAllMovableFigures(List<Move> moves) {
        for (Move move : moves) {
            board[move.getSourceY()][move.getSourceX()].setHighlighted(true);
        }
    }

    /**
     * Highlights all valid moves for figure on coordinate
     *
     * @param coord Coordinate of the figure
     */
    private void highlightAllMovesForFigure(Coordinate coord) {
        for (Move move : validMovesForPlayer.stream().filter(move -> move.getSource().equals(coord)).collect(Collectors.toList())) {
            board[move.getDestY()][move.getDestX()].setHighlighted(true);
        }
    }

    /**
     * Removes FigureTile from View.
     *
     * @param x X Coordinate of the FigureTile
     * @param y Y Coordinate of the FigureTile
     */
    private void removeFigureTile(int x, int y) {
        if (figures[y][x] != null) {
            root.getChildren().remove(figures[y][x]);
            figures[y][x] = null;
        }
    }

    /**
     * Event triggered by Controller when a Move was accepted and performed by the Board.
     *
     * @param move The performed Move
     */
    @Override
    public void onMovePerformed(Move move) {
        board[move.getSourceY()][move.getSourceX()].setChecked(false);  //falls checked, dann setze unchecked, da bei move von diesem Feld sowieso leer
        if (move.isEnPassant()) {
            removeFigureTile(move.getDestX(), move.getSourceY());
        }
        if (move.isDoesBeat()) {
            Sounds.playSound(Sounds.FIGURE_BEAT);
        } else {
            Sounds.playSound(Sounds.FIGURE_MOVED);
        }
        if (move.isCastling()) {
            if (move.getDestX() < move.getSourceX()) {    //bei Rochade nach links
                moveFigure(move.getSource(), move.getDest());                       //king
                moveFigure(new Coordinate(0, move.getSourceY()), new Coordinate(3, move.getSourceY()));     //rook
            } else {                      //nach rechts
                moveFigure(move.getSource(), move.getDest());                       //king
                moveFigure(new Coordinate(7, move.getSourceY()), new Coordinate(5, move.getSourceY()));     //rook
            }
        } else {
            if (move.isDoesBeat()) {
                removeFigureTile(move.getDestX(), move.getDestY());
            }
            moveFigure(move.getSource(), move.getDest());
        }
    }

    /**
     * Moves FigureTile in data structure and plays animation, pauses further code execution until animation is finished
     *
     * @param from starting point of animation in chess board coordinates (0-7)
     * @param to   end point of animation in chess board coordinates (0-7)
     */
    private void moveFigure(Coordinate from, Coordinate to) {
        FigureTile toMove = figures[from.getY()][from.getX()];
        figures[from.getY()][from.getX()] = null;
        toMove.setXCoord(to.getX());
        toMove.setYCoord(to.getY());
        figures[to.getY()][to.getX()] = toMove;
        moveFigureTile(toMove, from, to);
    }

    /**
     * Plays an animation on the FigureTile in the from Coordinate
     *
     * @param ft   The FigureTile to be moved
     * @param from starting point of animation in chess board coordinates (0-7)
     * @param to   end point of animation in chess board coordinates (0-7)
     */
    private void moveFigureTile(FigureTile ft, Coordinate from, Coordinate to) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(MOVETIME));
        tt.setNode(ft);
        tt.setByX(getFigurePosXForCoord(to.getX()) - getFigurePosXForCoord(from.getX()));
        tt.setByY(getFigurePosYForCoord(to.getY()) - getFigurePosYForCoord(from.getY()));
        tt.play();
        //Pause the thread until animation finished by showing a new invisible Stage (it doesn't feel right, i know)
        primary.setAlwaysOnTop(true);
        Stage stage = new Stage();
        stage.initOwner(primary);
        stage.setScene(new Scene(new Pane(), 1, 1));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setX(0);
        stage.setY(0);
        stage.setOnShown(e -> {
            PauseTransition delay = new PauseTransition(Duration.millis(MOVETIME));
            delay.setOnFinished(event -> stage.close());
            delay.play();
        });
        stage.showAndWait();
        primary.setAlwaysOnTop(false);
    }

    /**
     * gives visual feedback, when game is finished
     *
     * @param winningPlayer the player who won (null if draw)
     */
    @Override
    public void onGameFinished(Player winningPlayer, String reason) {
        Sounds.playSound(Sounds.GAME_FINISHED);
        String reasonStr = reason.length() == 0 ? "" : String.format("\nReason: %s", reason);
        showAlert(((winningPlayer == null ? "Game ends with a draw!" : (winningPlayer.isWhite() ? "White" : "Black") + " has won!") + reasonStr));
        setupNewGame(primary, true);
    }

    /**
     * Highlights checked king or removes highlight
     *
     * @param isWhite   color of king
     * @param pos       position of king
     * @param isChecked status of the king
     */
    @Override
    public void onCheckedChange(boolean isWhite, Coordinate pos, boolean isChecked) {
        if (pos.getX() >= 0 && pos.getY() >= 0) {
            FigureTile ft = figures[pos.getY()][pos.getX()];
            if (ft != null && ft.getFigureType().equals((isWhite ? Figures.WHITE_KING : Figures.BLACK_KING))) {
                board[pos.getY()][pos.getX()].setChecked(isChecked);
                if (isChecked) {
                    Sounds.playSound(Sounds.CHECK_ALARM);
                }
            }
        }
    }

    @Override
    public void onPawnPromotion(Coordinate pos, Piece piece) {
        figures[pos.getY()][pos.getX()].setFigureType(Figures.fromFigure(piece));
    }

    /**
     * called by player owning playerInput interface to signal a move selection request.
     * when move is selected, view has to offer move to controller (controller.offerMove()) so it can continue the 'loop'
     *
     * @param controller the controller to offer the move to
     * @param player     The player calling this method over PlayerInput interface
     * @param validMoves The list of valid moves for this player
     */
    @Override
    public void requestMove(Controller controller, Player player, List<Move> validMoves) {
        selectionPlayerColor = player.isWhite();
        isSelectionMode = true;
        validMovesForPlayer = validMoves;
        highlightAllMovableFigures(validMoves);
    }

    /**
     * shows new dialog window to ask player to select replacement piece
     *
     * @param pos     Position of Pawn
     * @param isWhite Color of Pawn
     * @return The Piece to replace Pawn with
     */
    @Override
    public Piece onPeasantReachedOtherSide(Coordinate pos, boolean isWhite) {
        Sounds.playSound(Sounds.PAWN_UPGRADE);
        showPawnUpgradeScreen(primary, isWhite);
        return returnPiece;
    }

    /**
     * Classical java main function which just launches the FX Application. Only there for compatibility reasons.
     *
     * @param args program start args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Shows a message box with information icon and message
     *
     * @param message The message to be shown
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }


    /**
     * Shows a dialog window to allow player to select a replacement piece for his pawn on the other side of the board
     *
     * @param owner   the primary stage
     * @param isWhite color of the pawn (also player)
     */
    private void showPawnUpgradeScreen(Stage owner, boolean isWhite) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        VBox vb = new VBox();
        vb.setFillWidth(true);
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        RowConstraints rc = new RowConstraints(TILESIZE);
        rc.setFillHeight(true);
        pane.getRowConstraints().add(rc);
        pane.setHgap(10);
        pane.setVgap(10);

        final ToggleGroup tg = new ToggleGroup();

        RadioButton queenRB = new RadioButton();
        ImageView queenView = new ImageView((isWhite ? Figures.WHITE_QUEEN : Figures.BLACK_QUEEN).getImage());
        queenView.setFitWidth(FIGURESIZE);
        queenView.setFitHeight(FIGURESIZE);
        queenRB.setGraphic(queenView);
        queenRB.setToggleGroup(tg);
        queenRB.setSelected(true);

        RadioButton runnerRB = new RadioButton();
        ImageView runnerView = new ImageView((isWhite ? Figures.WHITE_RUNNER : Figures.BLACK_RUNNER).getImage());
        runnerView.setFitWidth(FIGURESIZE);
        runnerView.setFitHeight(FIGURESIZE);
        runnerRB.setGraphic(runnerView);
        runnerRB.setToggleGroup(tg);

        RadioButton horseRB = new RadioButton();
        ImageView horseView = new ImageView((isWhite ? Figures.WHITE_HORSE : Figures.BLACK_HORSE).getImage());
        horseView.setFitWidth(FIGURESIZE);
        horseView.setFitHeight(FIGURESIZE);
        horseRB.setGraphic(horseView);
        horseRB.setToggleGroup(tg);

        RadioButton rookRB = new RadioButton();
        ImageView rookView = new ImageView((isWhite ? Figures.WHITE_TOWER : Figures.BLACK_TOWER).getImage());
        rookView.setFitWidth(FIGURESIZE);
        rookView.setFitHeight(FIGURESIZE);
        rookRB.setGraphic(rookView);
        rookRB.setToggleGroup(tg);

        vb.getChildren().add(pane);

        Button okButton = new Button("OK");
        okButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        okButton.setOnAction(e -> {
            if (isWhite) {
                if (queenRB.isSelected()) {
                    returnPiece = new Queen(true);
                } else if (runnerRB.isSelected()) {
                    returnPiece = new Bishop(true);
                } else if (horseRB.isSelected()) {
                    returnPiece = new Knight(true);
                } else if (rookRB.isSelected()) {
                    returnPiece = new Rook(true);
                }
            } else {
                if (queenRB.isSelected()) {
                    returnPiece = new Queen(false);
                } else if (runnerRB.isSelected()) {
                    returnPiece = new Bishop(false);
                } else if (horseRB.isSelected()) {
                    returnPiece = new Knight(false);
                } else if (rookRB.isSelected()) {
                    returnPiece = new Rook(false);
                }
            }
            stage.close();
        });

        stage.setOnCloseRequest(e -> {
            if (isWhite) {
                if (queenRB.isSelected()) {
                    returnPiece = new Queen(true);
                } else if (runnerRB.isSelected()) {
                    returnPiece = new Bishop(true);
                } else if (horseRB.isSelected()) {
                    returnPiece = new Knight(true);
                } else if (rookRB.isSelected()) {
                    returnPiece = new Rook(true);
                }
            } else {
                if (queenRB.isSelected()) {
                    returnPiece = new Queen(false);
                } else if (runnerRB.isSelected()) {
                    returnPiece = new Bishop(false);
                } else if (horseRB.isSelected()) {
                    returnPiece = new Knight(false);
                } else if (rookRB.isSelected()) {
                    returnPiece = new Rook(false);
                }
            }
            stage.close();
        });

        pane.add(queenRB, 0, 0);
        pane.add(runnerRB, 1, 0);
        pane.add(horseRB, 2, 0);
        pane.add(rookRB, 3, 0);

        vb.getChildren().add(okButton);

        Scene scene = new Scene(vb);
        stage.setScene(scene);
        stage.setTitle("Pawn promotion");
        stage.setResizable(false);
        stage.showAndWait();
    }
}