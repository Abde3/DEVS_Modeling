package Model.Routing;


import BaseModel.Switch;
import DEVSModel.Port;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;

import java.util.Map;
import java.util.stream.Collectors;

public class AdaptiveRouting extends NocRoutingPolicy {

    NocTopology topology;

    public AdaptiveRouting(NocTopology topology) {
        this.topology = topology;
    }

    @Override
    public Map<IPoint, Port> getRouteTable() {
        return null;
    }

    public Port computeNextRouteStep(Switch sourceModel, IPoint destination) {
        IPoint source = sourceModel.position;

        return null;
    }


}
