package controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.GameLogic;


public class GameController {
    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    private GameLogic gameLogic;
    @FXML
    private Pane paneGame;
    @FXML
    private Label score;
    @FXML
    private Label status;
    @FXML
    private Slider snakeSpeedSlider;
    @FXML
    private Slider AppleGeneratorSpeedSlider;
    @FXML
    private Slider MaxApplesOnTheScreenSlider;
    @FXML
    private CheckBox checkboxBorders;

    public void increaseScore(){
        int x = Integer.parseInt(score.getText()) + 1;
        score.setText(Integer.toString(x));
    }

    public StringProperty statusTextProperty(){ return status.textProperty(); }
    public DoubleProperty snakeSpeedSliderIntegerProperty(){return snakeSpeedSlider.valueProperty();}
    public DoubleProperty AppleGeneratorSpeedSlider(){return AppleGeneratorSpeedSlider.valueProperty();}
    public DoubleProperty MaxApplesOnTheScreenSlider(){return MaxApplesOnTheScreenSlider.valueProperty();}
    public BooleanProperty checkboxBorders(){return checkboxBorders.selectedProperty();}

    public void setDisableSnakeSpeedSlider(boolean b){snakeSpeedSlider.setDisable(b);}
    public void setDisableAppleGeneratorSpeedSlider(boolean b){AppleGeneratorSpeedSlider.setDisable(b);}
    public void setDisableMaxApplesOnTheScreenSlider(boolean b){MaxApplesOnTheScreenSlider.setDisable(b);}
    public void setDisableCheckboxBorders(boolean b){checkboxBorders.setDisable(b);}

    public void paneGameAdd(Node node){
        paneGame.getChildren().add(node);
    }

    @FXML
    public void initialize(){
        status.setText("");
        paneGame.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        paneGame.setOnMousePressed(mouseEvent -> gameLogic.getGroup().requestFocus());
    }


}



