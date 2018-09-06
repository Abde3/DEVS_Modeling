package NOCUnit;

import DEVSModel.DEVSModel;
import Model.NOCModel.INocNetwork;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NOCDirections.Point2D;

import java.util.Collection;
import java.util.Iterator;;

public class MeshNocNetwork extends INocNetwork<Point2D> {

    public MeshNocNetwork(int size) {
        super(size);
    }

    @Override
    public Collection<DEVSModel> getAllUnits() {
        return units.values();
    }

    @Override
    public DEVSModel getUnitAt(ICoordinate position) {
        return units.get(position);
    }


    @Override
    public boolean addUnitAt(DEVSModel model, Point2D position) {
        return ( units.put(position, model) == null );
    }

    @Override
    public DEVSModel getUnitFromPosition(Point2D position, String direction) {
        return null;
    }

    public Collection<Point2D> getAllPositions() {
        return units.keySet();
    }

    @Override
    public Iterator<DEVSModel> iterator() {
        return null;
    }

}
