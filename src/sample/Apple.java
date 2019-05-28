package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;

public class Apple extends GameObject{

    public void generateX() {
        this.x = (int) (Math.random() * 30);
    }

    public void generateY() {
        this.y = (int) (Math.random() * 20);
    }

    public void setRectangleX() {
        this.rectangle.setLayoutX(getX() * width);
    }

    public void setRectangleY() {
        this.rectangle.setLayoutY(getY() * height);
    }


    public Apple() {
        rectangle = new Rectangle(width, height);
        rectangle.setFill(new ImagePattern(new Image("pic/apple.png")));
    }
}
