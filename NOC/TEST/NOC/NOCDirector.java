package NOC;


import DEVSModel.DEVSModel;
import NOCRoutingPolicy.NocRoutingPolicy;
import NOCRoutingPolicy.UnhandledRoutingPolicyException;
import NOCUnit.*;
import NocTopology.NocTopology;

import java.util.HashMap;

public class NOCDirector {


    public static NOC buildNOCMesh() {

        NOC NOCMesh = null;
        try {

            NOCMesh = new NOC2DimensionBuilder()
                    .withSize( 4 )
                    .withTopology( NocTopology.Topology.MESH )
                    .withRoutingPolicy( NocRoutingPolicy.RoutingPolicy.DETERMINISTIC )
                    .withAGenerator( null , null )
                    .withNumberOfVirtualChannel(1)
                    .build();

        } catch (NOC2DimensionBuilder.ExistingGeneratorException e) {
            e.printStackTrace();
        } catch (UnhandledRoutingPolicyException e) {
            e.printStackTrace();
        } catch (NocTopology.UnhandledTopologyException e) {
            e.printStackTrace();
        }

        return NOCMesh;
    }


}
