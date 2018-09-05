package NOCUnit;


import NOCRoutingPolicy.NocRoutingPolicy;
import NocTopology.NOCDirections.ICoordinateSystem;
import NocTopology.NocTopology;

public class NOCUnitDirector {

    private final NocTopology topology;
    private final NocRoutingPolicy routingPolicy;

    public NOCUnitDirector(NocTopology topology, NocRoutingPolicy routingPolicy) {
        this.topology = topology;
        this.routingPolicy = routingPolicy;
    }



    public NOCUnit buildNocUnit(ICoordinateSystem coordinate) {
        NOCUnit nocUnit = null;

        try {

            nocUnit = new NOCUnitBuilder()
                    .withInPorts()
                    .withOutPorts(" ", " ")
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
