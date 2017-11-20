package sample;

import javafx.scene.shape.Rectangle;

public class Bullet extends MyRect{
    private double sceneWidth;

    Bullet(View view, Tank tank) {
        super(ImageManager.bulletImage);

        sceneWidth = view.getStage().getScene().getWidth();
        setX(tank.getX() + tank.getWidth() - 20);
        setY(tank.getY() + tank.getHeight() / 2 - 40);
    }

    public void move(double step){
        setX(getX()+step);
    }
}
