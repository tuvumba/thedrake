package thedrake.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import thedrake.core.*;
import thedrake.ui.BoardView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static thedrake.ui.TheDrakeApp.createSampleGameState;

public class gameController implements Initializable {

    @FXML
    BorderPane mainBorderPane;

    @FXML
    Label labelWinner;

    @FXML
    Label labelTeamToMove;

    @FXML
    GridPane stackBlue;

    @FXML
    GridPane stackOrange;

    @FXML
    Button buttonRestart;

    private BoardView game;

    private Stage stage;

    private Scene scene;

    private Parent root;

    public void setBoardView(BoardView view) {
        mainBorderPane.setCenter(view);
        game = view;
        refreshStacks(game.playingSide().get());

        game.playingSide().addListener((observable, oldValue, newValue) ->
        {
            refreshStacks(newValue);
            labelTeamToMove.setText(newValue.name() + " moves!");
            if (newValue.name().equals("ORANGE")) {
                labelTeamToMove.setStyle("-fx-text-fill: #d56e09;");
            } else {
                labelTeamToMove.setStyle("-fx-text-fill: #1f78ff;");
            }
        });

        game.gameResultProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == GameResult.VICTORY) {
                labelWinner.setVisible(true);
                buttonRestart.setVisible(true);
                labelTeamToMove.setVisible(false);
                if (game.playingSide().get() == PlayingSide.BLUE) {
                    labelWinner.setText("BLUE won!");
                    labelWinner.setStyle("-fx-text-fill: #1f78ff;");
                } else {
                    labelWinner.setText("ORANGE won!");
                    labelWinner.setStyle("-fx-text-fill: #d56e09;");


                }
            }
        });
    }

    public void resign(ActionEvent event)
    {
        game.resign();
    }

    public void restartTwoPlayers(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gameTwoPlayers.fxml"));
        root = fxmlLoader.load();
        gameController controller = fxmlLoader.getController();
        controller.setBoardView(new BoardView(createSampleGameState()));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("game.css").toExternalForm());
        stage.setTitle("The Drake");
        stage.setMinHeight(750);
        stage.setMinWidth(850);
        stage.setScene(scene);
        stage.show();
    }


    private void refreshStacks(PlayingSide onMove)
    {
        stackBlue.getChildren().clear();
        List<Troop> blueTroops = game.stackBlueProperty().get();
        for(int i = 0; i < blueTroops.size(); i++)
        {
            TileView toBeInserted = new TileView(TilePos.OFF_BOARD,new TroopTile(blueTroops.get(i), PlayingSide.BLUE, TroopFace.AVERS),
                    game, i == 0 && onMove == PlayingSide.BLUE);

            if(!toBeInserted.clickable)
            {
                Lighting lighting = new Lighting();
                lighting.setDiffuseConstant(0.5);
                lighting.setLight(new Light.Distant(45, 45, Color.LIGHTGRAY));

                toBeInserted.setEffect(lighting);
            }
            stackBlue.add(toBeInserted, i,0);
        }

        stackOrange.getChildren().clear();
        List<Troop> orangeTroops = game.stackOrangeProperty().get();
        for(int i = 0; i < orangeTroops.size(); i++)
        {
            TileView toBeInserted = new TileView(TilePos.OFF_BOARD,new TroopTile(orangeTroops.get(i), PlayingSide.ORANGE, TroopFace.AVERS),
                    game, i == 0 && onMove == PlayingSide.ORANGE);
            if(!toBeInserted.clickable)
            {
                Lighting lighting = new Lighting();
                lighting.setDiffuseConstant(0.5);
                lighting.setLight(new Light.Distant(45, 45, Color.LIGHTGRAY));

                toBeInserted.setEffect(lighting);
            }
            stackOrange.add(toBeInserted, i,0);
        }
    }



    public void exitToMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("menu.fxml"));
        root = fxmlLoader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("app.css").toExternalForm());
        stage.setTitle("The Drake");
        stage.setMinHeight(750);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonRestart.setVisible(false);
        labelWinner.setVisible(false);
        labelTeamToMove.setText("BLUE moves!");
        labelTeamToMove.setStyle("-fx-text-fill: #1f78ff;");
    }
}
