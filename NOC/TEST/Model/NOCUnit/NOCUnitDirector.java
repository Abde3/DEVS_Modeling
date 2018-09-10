package Model.NOCUnit;


import BaseModel.Generator_Task;
import DEVSModel.DEVSModel;
import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;

import java.util.HashMap;
import java.util.List;

public class NOCUnitDirector {

    private final NocTopology topology;
    private final NocRoutingPolicy routingPolicy;
    private final HashMap<ICoordinate,DEVSModel> generators;

    public NOCUnitDirector(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<ICoordinate,DEVSModel> generators) {
        this.topology = topology;
        this.routingPolicy = routingPolicy;
        this.generators = generators;
    }



    public NOCUnit buildNocUnit(IPoint coordinate) {

        DEVSModel generator = null;
        NOCUnit nocUnit = null;
        AbstractNOCUnitBuilder builder = null;

        try {

            List<String> inPorts  = topology.getInputDirections(coordinate);
            List<String> outPorts = topology.getOutputDirections(coordinate);
            generator =  generators.get(coordinate);

            builder = new NOCUnitBuilder()
                    .withCoordinate(coordinate)
                    .withQueuePerInPortRatio(1)
                    .withQueuePerOutPortRatio(0)
                    .withRoutingPolicy(routingPolicy)
                    .withInPorts(inPorts.toArray(new String[0]))
                    .withOutPorts(outPorts.toArray(new String[0]));

            if( generator != null ) {
                builder = builder.withGenerator(generator);
            }

            nocUnit = builder.build();

        } catch (NOCUnit.ExistingPortException e) {
            e.printStackTrace();
        }

        System.out.println(nocUnit);

        return nocUnit;
    }

}
