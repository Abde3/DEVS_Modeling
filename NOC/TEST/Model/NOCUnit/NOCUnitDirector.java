package Model.NOCUnit;


import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NocTopology;

public class NOCUnitDirector {

    private final NocTopology topology;
    private final NocRoutingPolicy routingPolicy;

    public NOCUnitDirector(NocTopology topology, NocRoutingPolicy routingPolicy) {
        this.topology = topology;
        this.routingPolicy = routingPolicy;
    }



    public NOCUnit buildNocUnit(ICoordinate coordinate) {
        NOCUnit nocUnit = null;

        try {

            nocUnit = new NOCUnitBuilder()
                    .withInPorts()
                    .withOutPorts(" a ", " b ")
                    .withQueuePerInPortRatio(1)
                    .withQueuePerOutPortRatio(0)
                    .withRoutingPolicy(routingPolicy)
                    .build();

        } catch (NOCUnit.ExistingPortException e) {
            e.printStackTrace();
        }


        return nocUnit;
    }

}
