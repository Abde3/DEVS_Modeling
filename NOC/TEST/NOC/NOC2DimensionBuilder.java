package NOC;


import NOCRoutingPolicy.NocRoutingPolicy;
import NocTopology.NocTopology;

public class NOC2DimensionBuilder extends AbstractNOCBuilder<NOC2Dimension> {


    @Override
    NOC2Dimension newBuilder(int size, NocTopology topology, NocRoutingPolicy routingPolicy) {
        return new NOC2Dimension(size, topology, routingPolicy);
    }

    @Override
    void buildNocUnits(NOC2Dimension noc) {

    }

    @Override
    void buildLinks(NOC2Dimension noc) {

    }

    @Override
    void buildGenerators(NOC2Dimension noc) {

    }

}
