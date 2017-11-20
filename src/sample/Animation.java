package sample;

import java.awt.image.BufferedImage;
import java.net.URISyntaxException;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Animation extends Transition {

    private ImageView imageView;
    private int count;

    private int lastIndex;

    private Image[] sequence;

    public Animation() {
    }

    public Animation( Image[] sequence, double durationMs) {
        init( sequence, durationMs);
    }

    protected void init( Image[] sequence, double durationMs) {
        this.imageView = new ImageView(sequence[0]);
        this.sequence = sequence;
        this.count = sequence.length;

        setCycleCount(1);
        setCycleDuration(Duration.millis(durationMs));
        setInterpolator(Interpolator.LINEAR);

    }

    protected void interpolate(double k) {

        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            imageView.setImage(sequence[index]);
            lastIndex = index;
        }

    }

    public ImageView getView() {
        return imageView;
    }

}
