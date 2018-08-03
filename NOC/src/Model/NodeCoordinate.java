package Model;

public class NodeCoordinate {
    private int x;
    private int y;

    public NodeCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    @Override
    public String toString() {
        return "[" + x + ", "+ y + "]";
    }
}
