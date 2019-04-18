package Model.Routing;

import BaseModel.Switch;
import DEVSModel.Port;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;

import java.util.Map;


public abstract class NocRoutingPolicy {

        public enum RoutingPolicy {DETERMINISTIC, SEMI_DETERMINISTIC, ADAPTIVE}

        public static NocRoutingPolicy buildRoutingPolicy(RoutingPolicy routingPolicy, NocTopology topology) throws UnhandledRoutingPolicyException {
            NocRoutingPolicy nocRoutingPolicy = null;
            switch(routingPolicy)
            {
                case ADAPTIVE: nocRoutingPolicy = new AdaptiveRouting(topology);break;
                case DETERMINISTIC: nocRoutingPolicy = new DeterministicRouting(topology);break;
                case SEMI_DETERMINISTIC: nocRoutingPolicy = new SemiDeterministicRouting(topology);break;
                default: throw new UnhandledRoutingPolicyException("The routing policy : " + routingPolicy + " is not supported yet.");

            }

            return nocRoutingPolicy;
        }

        public abstract Map<IPoint, Port> getRouteTable() ;

        public abstract Port computeNextRouteStep(Switch source, IPoint destination);
}



