package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Refugee extends MyRect{

    public boolean isTerrorist = false;

    Refugee(double x, double y, int randInt) {
        super(ImageManager.refugeesImages[randInt]);
        if (randInt == 3)
            isTerrorist = true;
        setX(x);
        setY(y);
    }

    public void move(double step) {
        setX(getX()-step);
    }
}
