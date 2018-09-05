package NOC;

import DEVSModel.DEVSModel;
import NOCRoutingPolicy.NocRoutingPolicy;
import NOCUnit.NOCUnitDirector;
import NocTopology.NOCDirections.ICoordinateSystem;
import NocTopology.NOCDirections.IDirection;
import NocTopology.NocTopology;

import java.util.HashMap;

public class NOC2Dimension extends NOC {

    protected NOC2Dimension(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<ICoordinateSystem,DEVSModel> generators){
        super(topology, routingPolicy, generators);

    }


    @Override
    public void setSelectPriority() {

    }



}
