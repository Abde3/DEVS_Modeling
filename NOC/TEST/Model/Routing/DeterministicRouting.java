package Model.Routing;

import Model.Routing.NocRoutingPolicy;

public class DeterministicRouting extends NocRoutingPolicy {

    protected DeterministicRouting() {
    }

    public Object computeNextStep() throws Exception {
        throw new Exception("Not implemented yet !");
    }

}