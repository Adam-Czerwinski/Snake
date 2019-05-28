package sample;

import controller.GameController;
import controller.MainController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    //GUI SETTINGS
    private IntegerProperty maximumApplesOnScreen = new SimpleIntegerProperty();         //maximum apples on the screen at one moment
    private IntegerProperty snakePace = new SimpleIntegerProperty();                           //pace of the game: less -> faster
    private IntegerProperty appleGeneratorPace = new SimpleIntegerProperty();             //how fast apple will be generated: less -> faster
    private BooleanProperty isBorders = new SimpleBooleanProperty();
    private StringProperty status = new SimpleStringProperty();

    //NEW GAME
    private final int posY = 10;                                      //posY
    private int posX = 14;                                            //first posX, its increasing in loop

    //CONTROLLERS
    private GameController gameController;
    private MainController mainController;
    private Group group;

    //LOOPS
    private Timeline gameLoop;                          //game loop
    private Timeline appleGenerator;                    //apple generator loop

    //GAME LOGIC
    private List<Snake> snake;
    private List<Apple> apples;
    private List<Point2D> applesCoordinates = new ArrayList<>();
    private List<Point2D> snakeCoordinates = new ArrayList<>();


    private void setApples(List<Apple> apples) {
        this.apples = apples;
    }

    //it is adding new element of prevX and prevY, depending on snake size.
    private void setSnake(List<Snake> snake) {
        this.snake = snake;
        for (int i = 0; i < this.snake.size(); i++)       //size != capacity
        {
            snakeCoordinates.add(new Point2D(0, 0));
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void newGame() {
        //default when new game
        Settings.inGame = false;
        Settings.isPause = false;
        Settings.isPressed = false;
        Settings.inSettings = true;

        status.bindBidirectional(gameController.statusTextProperty());
        maximumApplesOnScreen.bind(gameController.MaxApplesOnTheScreenSlider());
        snakePace.bind(gameController.snakeSpeedSliderIntegerProperty());
        appleGeneratorPace.bind(gameController.AppleGeneratorSpeedSlider());
        isBorders.bind(gameController.checkboxBorders());

        status.setValue("In settings");

        //----------------CREATING FIRST APPLE
        List<Apple> apples = generateFirstApple();

        //------------------CREATING FIRST SNAKE
        List<Snake> snake = new ArrayList<>();
        int startingSnakeSize = 3;
        for (int i = 0; i < startingSnakeSize; i++, posX++) {
            snake.add(new Snake());
            snake.get(i).setX(posX);
            snake.get(i).setY(posY);
            snake.get(i).setRectangleX();     //automatic update depending on x position
            snake.get(i).setRectangleY();     //automatic update depending on y position

            gameController.paneGameAdd(snake.get(i).getRectangle());
        }
        setSnake(snake);
        setApples(apples);
        snake.get(0).getRectangle().setFill(Settings.color);

        //KEYBOARD INTERACTION
        controlSnake();

        gameController.setGameLogic(this);
    }

    private List<Apple> generateFirstApple() {
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        do {
            apples.get(0).generateX();
            apples.get(0).generateY();
        }
        while ((apples.get(0).getX() == posX || apples.get(0).getX() == (posX + 1) || apples.get(0).getX() == (posX + 2)) && apples.get(0).getY() == posY);
        apples.get(0).setRectangleX();
        apples.get(0).setRectangleY();
        applesCoordinates.add(new Point2D(apples.get(0).getX(), apples.get(0).getY()));
        gameController.paneGameAdd(apples.get(0).getRectangle());
        return apples;
    }

    private void GameLoop() {
        //--------------GAME LOOP
        gameLoop = new Timeline(
                new KeyFrame(
                        Duration.millis(snakePace.getValue()),
                        event -> {
                            if (Settings.inGame && !Settings.isPause) {
                                Settings.isPressed = false;
                                if (snake.get(0).getDirection() == Directions.UP) {
                                    moveUp();
                                } else if (snake.get(0).getDirection() == Directions.RIGHT) {
                                    moveRight();
                                } else if (snake.get(0).getDirection() == Directions.DOWN) {
                                    moveDown();
                                } else if (snake.get(0).getDirection() == Directions.LEFT) {
                                    moveLeft();
                                }
                                moveRest();
                                update();
                                updateStatus();
                            }
                        }
                )
        );//////END OF GAME LOOP
        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();

        appleGenerator = new Timeline(
                new KeyFrame(
                        Duration.millis(appleGeneratorPace.getValue()),
                        event -> {
                            if (Settings.inGame && !Settings.isPause && apples.size() < maximumApplesOnScreen.getValue()) {
                                generateNewApple();
                            }
                        }
                )
        );//END OF APPLE GENERATOR LOOP
        appleGenerator.setCycleCount(Animation.INDEFINITE);
        appleGenerator.play();

    }

    private void update() {
        if (collisionSnake() || (collisionBorder() && isBorders.get())) {
            quitGame();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Game over : (");

            alert.setOnHidden(evt -> mainController.loadMenu());

            alert.show();
        }

        if (collisionBorder() && !isBorders.get()) {
            outOfBorders();
        }

        int index = collisionApple();
        if (index >= 0) {
            increaseScore();
            addSnake();
            generateAppleCoordinates(index);
        }
    }

    private void outOfBorders() {
        if (snake.get(0).getX() < 0) {
            snake.get(0).setX(snake.get(0).getX() + 30);
            snake.get(0).setRectangleX();
        } else if (snake.get(0).getX() > 29) {
            snake.get(0).setX(snake.get(0).getX() - 30);
            snake.get(0).setRectangleX();
        }
        if (snake.get(0).getY() < 0) {
            snake.get(0).setY(snake.get(0).getY() + 20);
            snake.get(0).setRectangleY();
        } else if (snake.get(0).getY() > 19) {
            snake.get(0).setY(snake.get(0).getY() - 20);
            snake.get(0).setRectangleY();
        }
    }

    private void updateStatus() {
        if (Settings.isPause && Settings.inGame && !Settings.inSettings) {
            gameLoop.pause();
            appleGenerator.pause();
            status.setValue("Paused");
        }
        if (Settings.inGame && !Settings.isPause && !Settings.inSettings) {
            gameLoop.play();
            appleGenerator.play();
            status.setValue("In game");
        }
        if (!Settings.inGame && !Settings.inSettings) {
            gameLoop.stop();
            appleGenerator.stop();
            status.setValue("Game over");
        }

    }

    private int collisionApple() {
        int i = 0;
        for (Apple apple : apples) {
            if (snake.get(0).getX() == apple.getX() &&
                    snake.get(0).getY() == apple.getY()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private boolean collisionSnake() {
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).getX() == snake.get(i).getX() &&
                    snake.get(0).getY() == snake.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean collisionBorder() {
        return snake.get(0).getX() < 0 || snake.get(0).getX() > 29 ||
                snake.get(0).getY() < 0 || snake.get(0).getY() > 19;

    }

    private void generateNewApple() {
        apples.add(new Apple());
        int pom;
        do {
            pom = 0;
            apples.get(apples.size() - 1).generateX();
            apples.get(apples.size() - 1).generateY();
            for (Point2D appleCoord : applesCoordinates) {
                for (Point2D snakeCoord : snakeCoordinates) {
                    if ((appleCoord.getX() == apples.get(apples.size() - 1).getX() &&
                            appleCoord.getY() == apples.get(apples.size() - 1).getY())
                            || (apples.get(apples.size() - 1).getX() == snakeCoord.getX() &&
                            apples.get(apples.size() - 1).getY() == snakeCoord.getY())) {
                        pom = 1;
                        break;
                    }
                }
                if (pom == 1)
                    break;
            }
        } while (pom >= 1);

        apples.get(apples.size() - 1).setRectangleX();
        apples.get(apples.size() - 1).setRectangleY();
        applesCoordinates.add(new Point2D(apples.get(apples.size() - 1).getX(), apples.get(apples.size() - 1).getY()));

        gameController.paneGameAdd(apples.get(apples.size() - 1).getRectangle());
    }

    private void generateAppleCoordinates(int index) {
        int pom;
        do {
            pom = 0;
            apples.get(index).generateX();
            apples.get(index).generateY();
            for (Point2D appleCoord : applesCoordinates) {
                for (Point2D snakeCoord : snakeCoordinates) {
                    if ((appleCoord.getX() == apples.get(index).getX() && appleCoord.getY() == apples.get(index).getY())
                            || (apples.get(index).getX() == snakeCoord.getX() && apples.get(index).getY() == snakeCoord.getY())) {
                        pom = 1;
                        break;
                    }
                }
                if (pom == 1)
                    break;
            }
        } while (pom >= 1);

        apples.get(index).setRectangleX();
        apples.get(index).setRectangleY();
        applesCoordinates.get(index).setX(apples.get(index).getX());
        applesCoordinates.get(index).setY(apples.get(index).getY());
    }

    private void addSnake() {

        snakeCoordinates.add(new Point2D(snakeCoordinates.get(snakeCoordinates.size() - 1).getX(), snakeCoordinates.get(snakeCoordinates.size() - 1).getY()));

        snake.add(new Snake());
        snake.get(snake.size() - 1).setX(snakeCoordinates.get(snakeCoordinates.size() - 1).getX());
        snake.get(snake.size() - 1).setY(snakeCoordinates.get(snakeCoordinates.size() - 1).getY());

        snake.get(snake.size() - 1).setRectangleX();
        snake.get(snake.size() - 1).setRectangleY();
        gameController.paneGameAdd(snake.get(snake.size() - 1).getRectangle());
    }

    private void increaseScore() {
        gameController.increaseScore();
    }

    private void startGame() {
        Settings.inSettings = false;
        Settings.inGame = true;
        GameLoop();
    }

    private void pauseGame() {
        Settings.isPause = true;
        updateStatus();
    }

    private void unpauseGame() {
        Settings.isPause = false;
        updateStatus();
    }

    private void quitGame() {
        Settings.inGame = false;
        updateStatus();
    }

    private void controlSnake() {
        //------CREATING OBJECT WHICH WILL CONTAIN KEYBOARD EVENTS
        group = new Group();
        group.setFocusTraversable(true);
        group.requestFocus();
        group.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && Settings.inSettings) {
                gameController.setDisableAppleGeneratorSpeedSlider(true);
                gameController.setDisableMaxApplesOnTheScreenSlider(true);
                gameController.setDisableSnakeSpeedSlider(true);
                gameController.setDisableCheckboxBorders(true);
                startGame();
            }
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                pauseGame();
                keyEventEscape();
                unpauseGame();
            }
            if (keyEvent.getCode() == KeyCode.SPACE && !Settings.inSettings) {
                if (!Settings.isPause) {
                    pauseGame();
                } else {
                    unpauseGame();
                }
            }
            if (!Settings.isPressed && (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.UP
                    || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.DOWN) && !Settings.inSettings) {
                if (keyEvent.getCode() == KeyCode.LEFT && snake.get(0).getDirection() != Directions.RIGHT) {
                    keyEventMove(Directions.LEFT);
                } else if (keyEvent.getCode() == KeyCode.UP && snake.get(0).getDirection() != Directions.DOWN) {
                    keyEventMove(Directions.UP);
                } else if (keyEvent.getCode() == KeyCode.RIGHT && snake.get(0).getDirection() != Directions.LEFT) {
                    keyEventMove(Directions.RIGHT);
                } else if (keyEvent.getCode() == KeyCode.DOWN && snake.get(0).getDirection() != Directions.UP) {
                    keyEventMove(Directions.DOWN);
                }
                Settings.isPressed = true;
            }
        });
        gameController.paneGameAdd(group);
    }

    private void keyEventMove(Directions direction) {
        snake.get(0).setDirection(direction);
    }

    private void keyEventEscape() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back to menu");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to back to menu?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            quitGame();
            mainController.loadMenu();
        }
    }

    private void moveLeft() {
        saveLastPositionSnake();
        snake.get(0).setX(snake.get(0).getX() - 1);
        snake.get(0).setRectangleX();

    }

    private void moveDown() {
        saveLastPositionSnake();
        snake.get(0).setY(snake.get(0).getY() + 1);
        snake.get(0).setRectangleY();
    }

    private void moveRight() {
        saveLastPositionSnake();
        snake.get(0).setX(snake.get(0).getX() + 1);
        snake.get(0).setRectangleX();
    }

    private void moveUp() {
        saveLastPositionSnake();
        snake.get(0).setY(snake.get(0).getY() - 1);
        snake.get(0).setRectangleY();
    }

    private void saveLastPositionSnake() {
        snakeCoordinates.get(0).setX(snake.get(0).getX());
        snakeCoordinates.get(0).setY(snake.get(0).getY());
    }

    private void moveRest() {
        for (int i = 1; i < snake.size(); i++)
        {
            snakeCoordinates.get(i).setX(snake.get(i).getX());
            snakeCoordinates.get(i).setY(snake.get(i).getY());
            snake.get(i).setX(snakeCoordinates.get(i - 1).getX());
            snake.get(i).setY(snakeCoordinates.get(i - 1).getY());
            snake.get(i).setRectangleX();
            snake.get(i).setRectangleY();
        }
    }

    public Group getGroup() {
        return group;
    }
}
