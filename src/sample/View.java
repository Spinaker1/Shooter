
package sample;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.control.*;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.*;
import java.util.Optional;

public class View {
    private Stage stage;
    private Pane pane;

    private LinkedList<Bullet> bullets = new LinkedList<>();
    private LinkedList<Refugee> refugees = new LinkedList<>();
    private Tank tank;

    private double sceneWidth;
    private boolean permissionToShot = true;

    private MyText text;
    private int islamization;

    private MyAlert alert;
    private ButtonType buttonTypeOne = new ButtonType("Zagraj ponownie");
    private ButtonType buttonTypeTwo = new ButtonType("Zakończ");

    private CreatingThread creatingThread;
    private MovingThread movingThread;
    private PermissionThread permissionThread;

    private Lock lock;

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
    private Media isisSound;
    private Media mozartSound;
    private Media explosionSound;

    Animation ani;

    View(Stage stage) {
        this.stage = stage;
        loadMusic();
        setupInterface();
        sceneWidth = stage.getScene().getWidth();
    }

    private void loadMusic() {
        isisSound = new Media(new File("src/sounds/isis.mp3").toURI().toString());
        mediaPlayer2 = new MediaPlayer(isisSound);
        mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);

        mozartSound = new Media(new File("src/sounds/mozart.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(mozartSound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        explosionSound = new Media(new File("src/sounds/bomb.mp3").toURI().toString());
        mediaPlayer3 = new MediaPlayer(explosionSound);
        mediaPlayer3.setCycleCount(1);
    }

    private void setupInterface() {
        pane = new Pane();

        islamization = 0;
        text = new MyText(10,30,"Islamizacja Europy " + islamization + "%");
        pane.getChildren().add(text);

        tank = new Tank(this);
        pane.getChildren().add(tank);

        alert = new MyAlert();

        permissionToShot = true;
        lock = new ReentrantLock();

        onClick();

        bullets.clear();
        refugees.clear();

        stage.getScene().setRoot(pane);

        creatingThread = new CreatingThread();
        movingThread = new MovingThread();
        permissionThread = new PermissionThread();

        movingThread.start();
        creatingThread.start();
        permissionThread.start();

        mediaPlayer.play();

        ani = new AnimatedGif(getClass().getResource("explosion.gif").toExternalForm(), 1000);
        ani.setCycleCount(1);
    }

    public void killThreads() {
        movingThread.stop();
        creatingThread.stop();
        permissionThread.stop();
    }

    private void endGame() {
        killThreads();

        mediaPlayer.stop();
        mediaPlayer3.stop();
        mediaPlayer2.play();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            mediaPlayer2.stop();
            setupInterface();
        } else if (result.get() == buttonTypeTwo) {
            Platform.exit();
        }
    }

    private void onClick() {
        stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP)
                tank.move(-5);
            if (event.getCode() == KeyCode.DOWN)
                tank.move(5);
            if (event.getCode() == KeyCode.SPACE && permissionToShot) {
                Bullet bullet = new Bullet(this, tank);
                pane.getChildren().add(bullet);
                bullets.add(bullet);

                permissionToShot = false;
            }
        });
    }

    private class CreatingThread extends Thread {
        private Random rand = new Random();
        private double sceneHeight;
        private double sceneWidth;

        CreatingThread() {
            super();
            Scene scene = getStage().getScene();
            sceneHeight = scene.getHeight();
            sceneWidth = scene.getWidth();
        }

        public void run()
        {
            try {
                sleep(200);
                while (true) {
                    Refugee refugee = new Refugee(sceneWidth, rand.nextInt((int)(sceneHeight-100)), rand.nextInt(4));
                    refugees.add(refugee);
                    Platform.runLater(() ->pane.getChildren().add(refugee));
                    sleep(2000);
                }
            }
            catch(InterruptedException e) {}
        }
    }

    private class MovingThread extends Thread {
        public void run() {
            try {
                while (true) {

                    for (Iterator<Bullet> jter = bullets.iterator(); jter.hasNext();)
                    {
                        Bullet bullet = jter.next();
                        bullet.move(3);

                        if (bullet.getX() > sceneWidth) {
                            jter.remove();
                            Platform.runLater(() ->pane.getChildren().remove(bullet));
                        }
                    }

                    for (Iterator<Refugee> iter = refugees.iterator(); iter.hasNext(); ) {
                        Refugee refugee = iter.next();
                        refugee.move(2);

                        for (Iterator<Bullet> jter = bullets.iterator(); jter.hasNext();) {
                            Bullet bullet = jter.next();
                            if (bullet.getBoundsInParent().intersects(refugee.getBoundsInParent())) {
                                iter.remove();
                                Platform.runLater(() -> pane.getChildren().remove(refugee));
                                jter.remove();
                                Platform.runLater(() -> pane.getChildren().remove(bullet));
                            }
                        }

                        if (refugee.getX() < -refugee.getWidth()) {
                            iter.remove();
                            islamization+=5;
                            Platform.runLater(() -> {
                                pane.getChildren().remove(refugee);
                                text.setText("Islamizacja Europy " + islamization + "%");
                            });
                        }

                        if (refugee.isTerrorist && refugee.getX() < 100) {

                            Platform.runLater(() -> {
                                ani.play();
                                ImageView aniView = ani.getView();
                                aniView.setX(refugee.getX()-50);
                                aniView.setY(refugee.getY()-50);
                                pane.getChildren().add(aniView);

                                mediaPlayer.stop();
                                mediaPlayer3.play();
                            });

                            sleep (3000);
                            Platform.runLater(() -> {
                                endGame();
                            });
                        }
                    }

                    if (islamization >= 100)
                    {
                        Platform.runLater(() -> {
                            endGame();
                        });
                    }
                    sleep(35);
                }
            }
            catch (InterruptedException e) {}
        }
    }

    private class PermissionThread extends Thread {
        public void run() {
            try {
                while (true) {
                    lock.lock();
                    if (permissionToShot == false) {
                        sleep(1000);
                        permissionToShot = true;
                        lock.unlock();
                    }
                    if (lock.tryLock())
                        lock.unlock();
                }
            }
            catch (InterruptedException e) {}
        }
    }

    private class MyAlert extends Alert {
        MyAlert() {
            super(AlertType.WARNING);
            setTitle("Allah Akbar!");
            setHeaderText("Islamizacja Zjednoczonych Emiratów Europejskich zakończyła sie sukcesem! Giń, niewierny psie!");
            setContentText("Co chcesz zrobić?");
            setGraphic(new ImageView(ImageManager.islamSymbolImage));

            getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        }
    }

    private class MyText extends Text {
        MyText(int a, int b,String text) {
            super(a,b,text);
            Font font = new Font("Verdana",30);
            setFont(font);
        }
    }

    public Stage getStage() {
        return stage;
    }
}

