package NocTopology;


import NOCUnit.MeshNocNetwork;
import NocTopology.NOCDirections.Coordinate2D;
import NocTopology.NOCDirections.ICoordinateSystem;
import NocTopology.NOCDirections.IDirection;

public class MeshTopology extends NocTopology {


    protected MeshTopology(int size) {
        this.size = size;
        this.nocNetwork = new MeshNocNetwork(size);


    }

    @Override
    public ICoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }


}
