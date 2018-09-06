package NocTopology.NOCDirections;


public class Point2D extends IPoint<Coordinate2D> {

    public static final int numberOfAxis = 2;

    public Point2D(int x, int y) {
        coordinate = new Coordinate2D();

        coordinate.setValueOnAxe("x", x);
        coordinate.setValueOnAxe("y", y);
    }

    public static int getNumberOfAxes() {
        return numberOfAxis;
    }

}
