package NOC;

import NOCRoutingPolicy.NocRoutingPolicy;
import NocTopology.NocTopology;

public abstract class AbstractNOCBuilder<TNOC extends NOC> {

    abstract TNOC newBuilder(int size, NocTopology topology, NocRoutingPolicy routingPolicy);

    abstract void buildGenerators(TNOC noc);
    abstract void buildNocUnits(TNOC noc);
    abstract void buildLinks(TNOC noc);

}
