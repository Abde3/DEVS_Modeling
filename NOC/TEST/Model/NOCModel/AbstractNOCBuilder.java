package Model.NOCModel;

import DEVSModel.DEVSModel;
import Model.Exceptions.ExistingGeneratorException;
import Model.Routing.NocRoutingPolicy;
import Model.Routing.UnhandledRoutingPolicyException;
import NocTopology.*;
import NocTopology.NOCDirections.ICoordinate;

import java.util.HashMap;

public abstract class AbstractNOCBuilder<TNOC extends NOC> {

    protected HashMap<ICoordinate,DEVSModel> generators;
    protected NocTopology topology;
    protected NocRoutingPolicy routingPolicy;
    protected int numberOfVirtualChannel;
    protected int size;

    public abstract AbstractNOCBuilder withTopology(NocTopology.Topology topology) throws NocTopology.UnhandledTopologyException;
    public abstract AbstractNOCBuilder withRoutingPolicy(NocRoutingPolicy.RoutingPolicy routingPolicy) throws UnhandledRoutingPolicyException;
    public abstract AbstractNOCBuilder withNumberOfVirtualChannel(int numberOfVirtualChannel);
    public abstract AbstractNOCBuilder withAGenerator(DEVSModel generator , ICoordinate coordinate) throws ExistingGeneratorException;
    public abstract AbstractNOCBuilder withSize(int networkSize);

    public abstract TNOC build();



}
