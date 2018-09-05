package NOC;

import DEVSModel.*;
import NOCRoutingPolicy.NocRoutingPolicy;
import NOCUnit.NOCUnitDirector;
import NocTopology.NOCDirections.ICoordinateSystem;
import NocTopology.NocTopology;


import java.util.HashMap;

public abstract class NOC extends DEVSCoupled {

    public enum NodeType {NOC, NODE, QUEUE, SWITCH, QUEUE_SWITCH, PE}


    protected final NodeType nodeType;                            /***** Represent the type of the element ************/
    protected final HashMap<ICoordinateSystem,DEVSModel> generators; /***** Represent all the generators of the model ****/
    protected final NocTopology topology;                         /***** Represent the topology of the model **********/
    protected final NocRoutingPolicy routingPolicy;               /***** Represent routing policy applied *************/
    protected final int numberOfVirtualChannel;                   /***** Represent the number of virtual channel ******/



    /********************************************* GETTERS AND SETTERS ************************************************/


    protected NOC(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<ICoordinateSystem,DEVSModel> generators ) {
        super();

        this.nodeType = NodeType.NOC;
        this.routingPolicy = routingPolicy;
        this.topology = topology;
        this.generators = generators;
        this.numberOfVirtualChannel = 1;

        buildNetwork();
    }

    protected void buildNetwork() {

        NOCUnitDirector nocUnitDirector = new NOCUnitDirector(topology, routingPolicy);



        ICoordinateSystem coordinate = null;
        nocUnitDirector.buildNocUnit(coordinate);



    }


}
