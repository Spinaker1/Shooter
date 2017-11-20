package sample;

import javafx.scene.image.Image;

/**
 * Created by User on 2017-09-23.
 */
public class ImageManager {
    public static Image tankImage;
    public static Image bulletImage;
    public static Image refugeesImages[] = new Image[4];
    public static Image islamSymbolImage;

    static void loadImages() {
        tankImage = new Image("file:src/images/tank.png");
        bulletImage = new Image("file:src/images/bullet.png");

        islamSymbolImage = new Image("file:src/images/islam.png");

        for (int i=0;i<=3;i++) {
            refugeesImages[i] = new Image("file:src/images/refugee"+i+".png");
        }
    }
}
