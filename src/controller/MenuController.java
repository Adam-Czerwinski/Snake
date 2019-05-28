package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import sample.GameLogic;

import java.io.IOException;

public class MenuController {


    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void onActionPlayButton() {
        FXMLLoader gameScreen = new FXMLLoader(this.getClass().getResource("/fxml/game-screen.fxml"));
        Pane pane = null;
        try{
            pane = gameScreen.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        mainController.setStackPane(pane);

        GameController gameController;
        gameController = gameScreen.getController();

        GameLogic gameLogic = new GameLogic();
        gameLogic.setGameController(gameController);
        gameLogic.setMainController(mainController);
        gameLogic.newGame();
    }

    @FXML
    void onActionExitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Quit", new ButtonType("quit"), new ButtonType("cancel"));
        alert.setHeaderText(null);
        alert.setContentText("Do you want do exit?");
        alert.showAndWait();
        if (alert.getResult() == alert.getButtonTypes().get(0)) {
            Platform.exit();
        }
    }


}
