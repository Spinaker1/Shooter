package sample;

public class Tank extends MyRect {
    private double sceneHeight;

    Tank(View view) {
        super(ImageManager.tankImage);

        sceneHeight = view.getStage().getScene().getHeight();
        setX(50);
        setY((sceneHeight-getHeight())/2);
    }

    public void move(double step)
    {
        setY(getY()+step);
        if (getY() < 0)
            setY(0.0);
        if (getY() > (sceneHeight-getHeight()))
            setY(sceneHeight-getHeight());
    }
}
