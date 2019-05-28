package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws  IOException{

        primaryStage.setTitle(Settings.stageTitle);
        primaryStage.setHeight(Settings.stageHeight);
        primaryStage.setWidth(Settings.stageWidth);
        primaryStage.setResizable(false);
        FXMLLoader mainScreen = new FXMLLoader(this.getClass().getResource("/fxml/main-screen.fxml"));
        StackPane stackPane = mainScreen.load();
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
