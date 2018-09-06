package Model.NOCModel;

import DEVSModel.DEVSModel;
import Model.Routing.NocRoutingPolicy;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NocTopology;


import java.util.HashMap;

public class NOC2Dimension extends NOC {

    protected NOC2Dimension(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<ICoordinate,DEVSModel> generators){
        super(topology, routingPolicy, generators);

    }


    @Override
    public void setSelectPriority() {

    }



}
