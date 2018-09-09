package Model.NOCUnit;


import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;

public class NOCUnitDirector {

    private final NocTopology topology;
    private final NocRoutingPolicy routingPolicy;

    public NOCUnitDirector(NocTopology topology, NocRoutingPolicy routingPolicy) {
        this.topology = topology;
        this.routingPolicy = routingPolicy;
    }



    public NOCUnit buildNocUnit(IPoint coordinate) {
        NOCUnit nocUnit = null;

        try {

            String[] inPorts  = topology.getInputDirections(coordinate).toArray(new String[0]);
            String[] outPorts = topology.getOutputDirections(coordinate).toArray(new String[0]);

            nocUnit = new NOCUnitBuilder()
                    .withInPorts(inPorts)
                    .withOutPorts(outPorts)
                    .withCoordinate(coordinate)
                    .withQueuePerInPortRatio(1)
                    .withQueuePerOutPortRatio(0)
                    .withRoutingPolicy(routingPolicy)
                    .build();

        } catch (NOCUnit.ExistingPortException e) {
            e.printStackTrace();
        }

        System.out.println(nocUnit);

        return nocUnit;
    }

}
