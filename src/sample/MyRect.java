package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class MyRect extends Rectangle {
    MyRect(Image image) {
        super(1,1);

        setHeight(image.getHeight());
        setWidth(image.getWidth());
        setFill(new ImagePattern(image));
    }
}
