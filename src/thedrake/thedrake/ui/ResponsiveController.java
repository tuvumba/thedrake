package thedrake.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static thedrake.ui.TheDrakeApp.createSampleGameState;

public class ResponsiveController implements Initializable{

    @FXML
    private Button btnStartTwo;
    @FXML
    private Button btnStartLAN;
    @FXML
    private Button btnStartAI;
    @FXML
    private Button btnEXIT;
    @FXML
    private VBox vboxMain;

    private Stage stage;

    private Scene scene;

    private Parent root;


    public void startTwoPlayers(ActionEvent event) throws IOException {
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


    public void startLAN(ActionEvent event)
    {

    }

    public void startAI(ActionEvent event)
    {

    }


    public void exit(ActionEvent event)
    {
        ((Stage) vboxMain.getScene().getWindow()).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
