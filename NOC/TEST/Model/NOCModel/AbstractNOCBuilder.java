package Model.NOCModel;

import DEVSModel.DEVSModel;
import Model.Exceptions.ExistingGeneratorException;
import Model.Routing.NocRoutingPolicy;
import Model.Routing.UnhandledRoutingPolicyException;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;
import Verification.StateRepresentation;

import java.util.HashMap;

public abstract class AbstractNOCBuilder<TNOC extends NOC> {

    protected HashMap<IPoint, DEVSModel> generators;
    protected NocTopology topology;
    protected NocRoutingPolicy routingPolicy;
    protected int numberOfVirtualChannel;
    protected int size;
    //protected StateRepresentation watcher;

    public abstract AbstractNOCBuilder withTopology(NocTopology topology) ;
    public abstract AbstractNOCBuilder withRoutingPolicy(NocRoutingPolicy.RoutingPolicy routingPolicy) throws UnhandledRoutingPolicyException;
    public abstract AbstractNOCBuilder withNumberOfVirtualChannel(int numberOfVirtualChannel);
    public abstract AbstractNOCBuilder withAGenerator(DEVSModel generator , IPoint coordinate) throws ExistingGeneratorException;
    public abstract AbstractNOCBuilder withSize(int networkSize);

    public abstract TNOC build();

}
