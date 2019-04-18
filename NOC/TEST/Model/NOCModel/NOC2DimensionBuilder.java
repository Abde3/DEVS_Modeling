package Model.NOCModel;


import DEVSModel.DEVSModel;
import Model.Exceptions.ExistingGeneratorException;
import Model.Routing.NocRoutingPolicy;
import Model.Routing.UnhandledRoutingPolicyException;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;

import java.util.HashMap;

public class NOC2DimensionBuilder extends AbstractNOCBuilder {


    @Override
    public AbstractNOCBuilder withTopology(NocTopology topology)  {
        this.topology = topology;
        return this;
    }

    @Override
    public AbstractNOCBuilder withRoutingPolicy(NocRoutingPolicy.RoutingPolicy routingPolicy) throws UnhandledRoutingPolicyException {
        if ( topology == null ) {
            System.err.println( "ERROR : The topology have to be set before the Routing Policy!" );
        }
        this.routingPolicy = NocRoutingPolicy.buildRoutingPolicy(routingPolicy, topology);
        return this;
    }

    @Override
    public AbstractNOCBuilder withNumberOfVirtualChannel(int numberOfVirtualChannel) {
        this.numberOfVirtualChannel = numberOfVirtualChannel;
        return this;
    }

    @Override
    public AbstractNOCBuilder withAGenerator(DEVSModel generator, IPoint coordinate) throws ExistingGeneratorException {

        if( generators == null ) { generators = new HashMap<>(); }

        Object hasBeenAdded = generators.putIfAbsent(coordinate, generator);

        if (hasBeenAdded != null) {
            throw new ExistingGeneratorException( "The added generator (ie. name = "
                    + generator.getName()
                    + " ) already exists in the given position (ie. coordinate = "
                    + coordinate
                    + " )");
        }

        return this;
    }

    @Override
    public AbstractNOCBuilder withSize(int networkSize) {
        this.size = networkSize;
        return this;
    }

    @Override
    public NOC2Dimension build() {

        NOC2Dimension instance = new NOC2Dimension( topology, routingPolicy, generators );

        return instance;
    }


}
