package NOCUnit;

import DEVSModel.DEVSModel;
import NOC.INocNetwork;
import NocTopology.NOCDirections.Coordinate2D;
import NocTopology.NOCDirections.ICoordinateSystem;
import NocTopology.NOCDirections.IDirection;

import java.util.*;

public class MeshNocNetwork extends INocNetwork {

    public MeshNocNetwork(int size) {
        this.units = new LinkedHashMap<>(size*size);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Coordinate2D coordinate = new Coordinate2D(x, y);
                units.put(coordinate, null);
            }
        }
    }

    @Override
    public Collection<DEVSModel> getAllUnits() {
        return units.values();
    }

    @Override
    public DEVSModel getUnitAt(ICoordinateSystem position) {
        return units.get(position);
    }

    @Override
    public DEVSModel getUnitFromPosition(ICoordinateSystem position, IDirection direction) {
        return null;
    }

    @Override
    public boolean addUnitAt(DEVSModel model, ICoordinateSystem position) {
        return ( units.put(position, model) == null );
    }


    public Collection<ICoordinateSystem> getAllPositions() {
        return units.keySet();
    }

    @Override
    public Iterator<DEVSModel> iterator() {
        return null;
    }

}
