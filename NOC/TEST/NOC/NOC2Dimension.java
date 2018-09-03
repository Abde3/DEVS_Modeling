package NOC;

import NOCRoutingPolicy.NocRoutingPolicy;
import NocTopology.NocTopology;

public class NOC2Dimension extends NOC {

    protected NOC2Dimension(int size, NocTopology topology, NocRoutingPolicy routingPolicy){
        super(size, topology, routingPolicy);
    }

    @Override
    public void setSelectPriority() {

    }



}
