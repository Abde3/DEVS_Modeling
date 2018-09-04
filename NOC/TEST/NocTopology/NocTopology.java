package NocTopology;

import NocTopology.NOCDirections.IDirection;

public abstract class NocTopology {

    public enum Topology {LINEAR, MESH}


    public static NocTopology buildTopology(Topology topology) throws UnhandledTopologyException {
        NocTopology nocTopology = null;
        switch(topology)
        {
            case MESH: nocTopology = new MeshTopology();break;
            case LINEAR: nocTopology = new LinearTopology();break;
            default: throw new UnhandledTopologyException("The topology : " + topology + " is not supported yet.");

        }

        return nocTopology;
    }

    public abstract void buildNetwork();
    public abstract void buildLink();
    public abstract IDirection getDirectionSystem();

    public static class UnhandledTopologyException extends Exception {
        public UnhandledTopologyException(String message) {
            super(message);
        }

    }
}
