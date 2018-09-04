package NOC;


import NOCRoutingPolicy.NocRoutingPolicy;
import NOCRoutingPolicy.UnhandledRoutingPolicyException;
import NOCUnit.NOCUnit;
import NOCUnit.NOCUnitBuilder;
import NOCUnit.NOCUnitDirector;
import NocTopology.NocTopology;

public class NOCDirector {
    AbstractNOCBuilder nocBuilder;
    NOCUnitDirector nocUnitDirector;
    NocRoutingPolicy routingPolicy;
    NocTopology topology;
    int nocSize;


    public NOCDirector (AbstractNOCBuilder nocBuilder) {
        this.nocBuilder = nocBuilder;
        nocUnitDirector = new NOCUnitDirector( new NOCUnitBuilder() );
        nocSize = -1;
    }


    public NOCDirector withSize(int nocSize) {
        this.nocSize = nocSize;
        return this;
    }


    public NOCDirector withRoutingPolicy(NOC.RoutingPolicy routingPolicy) {
        try {
            this.routingPolicy = NocRoutingPolicy.buildRoutingPolicy(routingPolicy);
        } catch (UnhandledRoutingPolicyException e) {
            e.printStackTrace();
        }
        return this;
    }


    public NOCDirector withTopology(NocTopology.NocTopology.Topology nocTopology ) {
        try {
            this.topology = NocTopology.buildTopology( nocTopology );
        } catch (NocTopology.UnhandledTopologyException e) {
            e.printStackTrace();
        }
        return this;
    }


    public NOC build() {

        NOC noc = nocBuilder.newBuilder(nocSize, topology, routingPolicy);
        NOCUnit nocUnit = nocUnitDirector.
        nocBuilder.buildNocUnits(noc);
        nocBuilder.buildLinks(noc);

        return noc;
    }


}
