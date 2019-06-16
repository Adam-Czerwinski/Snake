package sample;

import javafx.scene.shape.Rectangle;

public class GameObject extends Point2D{


    Rectangle getRectangle() {
        return this.rectangle;
    }      //so i can add rectangle to pane


    Rectangle rectangle;
    final double width = 20.0;
    final double height = 20.0;

    public void setRectangleX() {
        this.rectangle.setLayoutX(getX() * width);
    }

    public void setRectangleY() {
        this.rectangle.setLayoutY(getY() * height);
    }

    GameObject(){}

}
