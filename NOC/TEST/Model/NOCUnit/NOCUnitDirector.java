package Model.NOCUnit;


import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;

import java.util.List;

public class NOCUnitDirector {

    private final NocTopology topology;
    private final NocRoutingPolicy routingPolicy;

    public NOCUnitDirector(NocTopology topology, NocRoutingPolicy routingPolicy) {
        this.topology = topology;
        this.routingPolicy = routingPolicy;
    }

    public NOCUnit buildNocUnit(IPoint coordinate) {

        NOCUnit nocUnit = null;
        AbstractNOCUnitBuilder builder = null;

        try {

            List<String> inDirections_data  = topology.getInputDirections(coordinate);
            List<String> inDirections_cmd  = topology.getInputDirections(coordinate);
            List<String> outDirections_data = topology.getOutputDirections(coordinate);
            List<String> outDirections_cmd = topology.getOutputDirections(coordinate);

            inDirections_cmd.add("PE");

            builder = new NOCUnitBuilder()
                    .withDataInPorts( inDirections_data )
                    .withDataOutPorts( outDirections_data )
                    .withCmdInPorts( inDirections_cmd )
                    .withCmdOutPorts( outDirections_cmd )
                    .withCoordinate( coordinate )
                    .withQueuePerInPortRatio( 1 )
                    .withQueuePerOutPortRatio( 0 )
                    .withRoutingPolicy( routingPolicy );

            nocUnit = builder.build();

        } catch (NOCUnit.ExistingPortException e) {
            e.printStackTrace();
        }

        return nocUnit;
    }

}
