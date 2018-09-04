package NOC;

import DEVSModel.*;
import NOCRoutingPolicy.NocRoutingPolicy;
import NocTopology.NocTopology;


import java.util.Vector;

public abstract class NOC extends DEVSCoupled {

    public enum NodeType {NOC, NODE, QUEUE, SWITCH, QUEUE_SWITCH, PE}

    public enum RoutingPolicy {DETERMINISTIC, SEMI_DETERMINISTIC, ADAPTATIVE}


    protected NocNetwork nocNetwork;                        /***** Represent all the units in the model ***************/
    protected Vector<DEVSModel> generators;                 /***** Represent all the generators of the model **********/
    protected NodeType nodeType;                            /***** Represent the type of the element ******************/
    protected NocTopology topology;                         /***** Represent the topology of the model ****************/
    protected NocRoutingPolicy routingPolicy;               /***** Represent routing policy applied *******************/
    protected int numberOfVirtualChannel;                   /***** Represent the number of virtual channel ************/
    protected int size;                                     /***** Represent the size of the model ********************/


    /********************************************* GETTERS AND SETTERS ************************************************/


    protected NOC(int size, NocTopology topology, NocRoutingPolicy routingPolicy) {
        super();

        this.nodeType = NodeType.NOC;
        this.routingPolicy = routingPolicy;
        this.topology = topology;
        this.size = size;
    }

    public void setNocNetwork(NocNetwork nocNetwork) {
        this.nocNetwork = nocNetwork;
    }

    public void setGenerators(Vector<DEVSModel> generators) {
        this.generators = generators;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setTopology(NocTopology topology) {
        this.topology = topology;
    }

    public void setRoutingPolicy(NocRoutingPolicy routingPolicy) {
        this.routingPolicy = routingPolicy;
    }

    public void setNumberOfVirtualChannel(int numberOfVirtualChannel) {
        this.numberOfVirtualChannel = numberOfVirtualChannel;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
