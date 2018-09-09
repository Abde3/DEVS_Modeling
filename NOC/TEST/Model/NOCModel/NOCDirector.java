package Model.NOCModel;



import Model.Exceptions.ExistingGeneratorException;
import Model.Routing.NocRoutingPolicy;
import Model.Routing.UnhandledRoutingPolicyException;
import NocTopology.*;

public class NOCDirector {


    public static NOC buildNOCMesh() {

        NOC NOCMesh = null;
        try {

            NOCMesh = new NOC2DimensionBuilder()
                    .withSize( 4 )
                    .withTopology( NocTopologyDirector.buildMeshTopology( 4) )
                    .withRoutingPolicy( NocRoutingPolicy.RoutingPolicy.DETERMINISTIC )
                    .withAGenerator( null , null )
                    .withNumberOfVirtualChannel(1)
                    .build();

        } catch (UnhandledRoutingPolicyException e) {
            e.printStackTrace();
        } catch (ExistingGeneratorException e) {
            e.printStackTrace();
        }

        return NOCMesh;
    }


}
