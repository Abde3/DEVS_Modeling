package NOC;

import DEVSModel.DEVSModel;
import NOCRoutingPolicy.NocRoutingPolicy;
import NOCRoutingPolicy.UnhandledRoutingPolicyException;
import NocTopology.*;
import NocTopology.NOCDirections.ICoordinateSystem;

import java.util.HashMap;

public abstract class AbstractNOCBuilder<TNOC extends NOC> {

    protected HashMap<ICoordinateSystem,DEVSModel> generators;
    protected NocTopology topology;
    protected NocRoutingPolicy routingPolicy;
    protected int numberOfVirtualChannel;
    protected int size;

    public abstract AbstractNOCBuilder withTopology(NocTopology.Topology topology) throws NocTopology.UnhandledTopologyException;
    public abstract AbstractNOCBuilder withRoutingPolicy(NocRoutingPolicy.RoutingPolicy routingPolicy) throws UnhandledRoutingPolicyException;
    public abstract AbstractNOCBuilder withNumberOfVirtualChannel(int numberOfVirtualChannel);
    public abstract AbstractNOCBuilder withAGenerator(DEVSModel generator , ICoordinateSystem coordinate) throws NOC2DimensionBuilder.ExistingGeneratorException;
    public abstract AbstractNOCBuilder withSize(int networkSize);

    public abstract TNOC build();



}
