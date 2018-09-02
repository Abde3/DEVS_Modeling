package NOC;

import DEVSModel.*;

import java.util.Vector;

public abstract class NOC extends DEVSCoupled {

    public enum NodeType {NOC, NODE, QUEUE, SWITCH, QUEUE_SWITCH, PE}

    public enum Topology {LINEAR, MESH}

    public enum RoutingPolicy {DETERMINISTIC, SEMI_DETERMINISTIC, ADAPTATIVE}


    protected NocNetwork nocNetwork;                        /***** Represent all the units in the model ***************/
    protected Vector<DEVSModel> generators;                 /***** Represent all the generators of the model **********/
    protected NodeType nodeType;                            /***** Represent the type of the element ******************/
    protected Topology topology;                            /***** Represent the topology of the model ****************/
    protected RoutingPolicy routingPolicy;                  /***** Represent routing policy applied *******************/
    protected int numberOfVirtualChannel;                   /***** Represent the number of virtual channel ************/
    protected int size;                                     /***** Represent the size of the model ********************/


    /********************************************* GETTERS AND SETTERS ************************************************/
    public NocNetwork getNocNetwork() {
        return nocNetwork;
    }

    public void setNocNetwork(NocNetwork nocNetwork) {
        this.nocNetwork = nocNetwork;
    }

    public Vector<DEVSModel> getGenerators() {
        return generators;
    }

    public void setGenerators(Vector<DEVSModel> generators) {
        this.generators = generators;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Topology getTopology() {
        return topology;
    }

    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public RoutingPolicy getRoutingPolicy() {
        return routingPolicy;
    }

    public void setRoutingPolicy(RoutingPolicy routingPolicy) {
        this.routingPolicy = routingPolicy;
    }

    public int getNumberOfVirtualChannel() {
        return numberOfVirtualChannel;
    }

    public void setNumberOfVirtualChannel(int numberOfVirtualChannel) {
        this.numberOfVirtualChannel = numberOfVirtualChannel;
    }



}
