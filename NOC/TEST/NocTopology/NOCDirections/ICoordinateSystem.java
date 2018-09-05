package NocTopology.NOCDirections;


public abstract class ICoordinateSystem <T extends IDirection> {

    protected  T directionSystem;
    final public T getDirectionSystem() {
        return directionSystem;
    }

}
