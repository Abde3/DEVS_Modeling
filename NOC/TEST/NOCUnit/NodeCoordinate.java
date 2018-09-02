package NOCUnit;

import java.util.Objects;

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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOnLeftOf(NodeCoordinate coordinate) {
        return  (getX() < coordinate.getX());
    }

    public boolean isOnRightOf(NodeCoordinate coordinate) {
        return  (getX() > coordinate.getX());
    }

    public boolean isUnderOf(NodeCoordinate coordinate) {
        return  (getY() > coordinate.getY());
    }

    public boolean isOnTopOf(NodeCoordinate coordinate) {
        return  (getY() < coordinate.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeCoordinate that = (NodeCoordinate) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "[" + x + ", "+ y + "]";
    }


}
