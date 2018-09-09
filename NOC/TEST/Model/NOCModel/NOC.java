package Model.NOCModel;

import DEVSModel.*;
import Model.Routing.NocRoutingPolicy;
import Model.NOCUnit.NOCUnitDirector;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;


import java.util.Collection;
import java.util.HashMap;

public abstract class NOC extends DEVSCoupled {

    public enum NodeType {NOC, NODE, QUEUE, SWITCH, QUEUE_SWITCH, PE}


    protected final NodeType nodeType;                            /***** Represent the type of the element ************/
    protected final HashMap<ICoordinate, DEVSModel> generators;   /***** Represent all the generators of the model ****/
    protected final NocTopology topology;                         /***** Represent the topology of the model **********/
    protected final NocRoutingPolicy routingPolicy;               /***** Represent routing policy applied *************/
    protected final int numberOfVirtualChannel;                   /***** Represent the number of virtual channel ******/



    /********************************************* GETTERS AND SETTERS ************************************************/


    protected NOC(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<ICoordinate,DEVSModel> generators ) {
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
        Collection<IPoint> positions = topology.getNocNetwork().getAllPositions();
        positions.stream().forEach( point -> topology.getNocNetwork().addUnitAt( nocUnitDirector.buildNocUnit(point), point ));



    }


}
