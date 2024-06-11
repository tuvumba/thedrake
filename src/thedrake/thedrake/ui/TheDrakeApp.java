package thedrake.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import thedrake.core.Board;
import thedrake.core.BoardTile;
import thedrake.core.GameState;
import thedrake.core.PositionFactory;
import thedrake.core.StandardDrakeSetup;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader(ResponsiveApp.class.getClassLoader().getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("app.css").toExternalForm());
        primaryStage.setTitle("ResponsiveUI!");
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(3, 1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

}
