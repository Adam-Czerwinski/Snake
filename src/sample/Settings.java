package sample;

import javafx.scene.paint.Color;

public class Settings {

    //----------------STAGE SETTINGS---------------------//
    static final String stageTitle = "Snake";
    static final int stageWidth = 620;
    static final int stageHeight = 640;    //+stage top bar


    //---------------------GAME SETTINGS-------------------//
    static final Color color = Color.rgb(194, 145, 183);        //kolor głowy węża
    static boolean inSettings;
    static boolean inGame;
    static boolean isPressed;                          //this boolean is used to let user only once change directory in one loop
    static boolean isPause;

    public Settings(){}
}
