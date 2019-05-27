package Model.NOCModel;

import DEVSModel.DEVSModel;
import Model.Routing.NocRoutingPolicy;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;
import Util.NocUtil;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Vector;

public class NOC2Dimension extends NOC {

    protected NOC2Dimension(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<AbstractMap.SimpleEntry<IPoint,String>, DEVSModel> generators){
        super(topology, routingPolicy, generators);
    }

    @Override
    public void setSelectPriority() {

        NocUtil.combinationsNoDupl( getSubModels() ).forEach(
            sameSizeLists -> sameSizeLists.forEach(
                devsModels -> this.selectPriority.put(
                    new Vector<DEVSModel>( devsModels ), devsModels.get(devsModels.size()-1)
                )
            )
        );

    }

}
