package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.Settings;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane stackPane;

    @FXML
    public void initialize() {
        loadMenu();
    }

    private Settings settings;

    public void loadMenu() {
        FXMLLoader menuScreen = new FXMLLoader(this.getClass().getResource("/fxml/menu-screen.fxml"));
        Pane pane = null;
        try{
            pane = menuScreen.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        setStackPane(pane);

        MenuController menuController;
        menuController = menuScreen.getController();
        menuController.setMainController(this);
    }

    public void setStackPane(Pane pane){
        stackPane.getChildren().clear();
        stackPane.getChildren().add(pane);
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
