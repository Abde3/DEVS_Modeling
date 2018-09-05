package NocTopology;

import NOC.INocNetwork;
import NocTopology.NOCDirections.ICoordinateSystem;

public abstract class NocTopology {

    public enum Topology {LINEAR, MESH}


    protected INocNetwork nocNetwork;                       /***** Represent all the units in the model *********/
    protected int size;                                     /***** Represent the size of the model **************/


    public abstract ICoordinateSystem getCoordinateSystem();


    public static NocTopology buildTopology(Topology topology, int size) throws UnhandledTopologyException {
        NocTopology nocTopology = null;
        switch(topology)
        {
            case MESH: nocTopology = new MeshTopology(size); break;
            case LINEAR: nocTopology = new LinearTopology(size); break;
            default: throw new UnhandledTopologyException("The topology : " + topology + " is not supported yet.");

        }

        return nocTopology;
    }



    public static class UnhandledTopologyException extends Exception {
        public UnhandledTopologyException(String message) {
            super(message);
        }
    }
}
