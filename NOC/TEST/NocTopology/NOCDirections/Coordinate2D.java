package NocTopology.NOCDirections;


public class Coordinate2D extends ICoordinateSystem {

    int x, y;

    public Coordinate2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}