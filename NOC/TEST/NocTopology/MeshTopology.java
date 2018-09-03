package NocTopology;


import NocTopology.NOCDirections.CardinalDirection2D;
import NocTopology.NOCDirections.IDirection;

public class MeshTopology extends NocTopology {


    private IDirection directionSystem;

    protected MeshTopology() {
        directionSystem = new CardinalDirection2D();
    }

    @Override
    public void buildLink() {

    }

    @Override
    public IDirection getDirectionSystem() {
        return directionSystem;
    }


}
