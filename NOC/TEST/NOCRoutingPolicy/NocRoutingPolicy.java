package NOCRoutingPolicy;

import NOC.NOC;

public abstract class NocRoutingPolicy {

        public enum RoutingPolicy {DETERMINISTIC, SEMI_DETERMINISTIC, ADAPTATIVE}

        public static NocRoutingPolicy buildRoutingPolicy(RoutingPolicy routingPolicy) throws UnhandledRoutingPolicyException {
            NocRoutingPolicy nocRoutingPolicy = null;
            switch(routingPolicy)
            {
                case ADAPTATIVE: nocRoutingPolicy = new AdaptiveRouting();break;
                case DETERMINISTIC: nocRoutingPolicy = new DeterministicRouting();break;
                case SEMI_DETERMINISTIC: nocRoutingPolicy = new SemiDeterministicRouting();break;
                default: throw new UnhandledRoutingPolicyException("The routing policy : " + routingPolicy + " is not supported yet.");

            }

            return nocRoutingPolicy;
        }

        public abstract Object computeNextStep() throws Exception;
}



