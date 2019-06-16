package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.Directions;

public class Snake extends GameObject{

    private Directions direction = Directions.UP;

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public Directions getDirection() {
        return direction;
    }

    public Snake() {
        rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.RED);
        rectangle.setArcHeight(12);
        rectangle.setArcWidth(12);
    }

}
