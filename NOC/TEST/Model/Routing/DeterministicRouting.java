package Model.Routing;

import BaseModel.Switch;
import DEVSModel.Port;
import Model.NOCUnit.Type;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;
import Util.NocUtil;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeterministicRouting extends NocRoutingPolicy {

    NocTopology topology;

    protected DeterministicRouting(NocTopology topology) {
        this.topology = topology;
    }

    @Override
    public Map<IPoint, Port> getRouteTable() {

        Map<IPoint, Port> a = topology.getNocNetwork().getAllPositions()
                .stream()
                .collect(Collectors.toMap(entry -> entry, entry -> new Port(null)));


        return null;
    }

    public Port computeNextRouteStep(Switch sourceModel, IPoint destination) {
        IPoint source = sourceModel.position;

        Optional<String> selectedAxis = source.getAllAxisName()
                .stream()
                .filter(axis -> source.getValueOnAxis(axis) != destination.getValueOnAxis(axis))
                .reduce((axis1, axis2) -> axis1);

        Port outputPort = null;

        if (selectedAxis.isPresent()) {
            outputPort = sourceModel.getOutPort(NocUtil.directionsToPortsName(
                    topology.getDirectionToReachPoint(source, destination, selectedAxis.get()),
                    false,
                    Type.DATA));
        } else {
            outputPort = sourceModel.outPorts
                    .stream()
                    .filter( port -> port.getName().toUpperCase().contains("PE") )
                    .findFirst()
                    .get();
        }

        return outputPort;
    }

}
