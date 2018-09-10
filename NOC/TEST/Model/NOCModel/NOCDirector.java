package Model.NOCModel;



import BaseModel.Generator_Task;
import Model.Exceptions.ExistingGeneratorException;
import Model.Routing.NocRoutingPolicy;
import Model.Routing.UnhandledRoutingPolicyException;
import NocTopology.*;
import NocTopology.NOCDirections.IPoint;

public class NOCDirector {



    public static NOC buildNOCMesh() {

        final NocTopology meshTopology = NocTopologyDirector.buildMeshTopology(4);

        NOC NOCMesh = null;
        try {

            NOCMesh = new NOC2DimensionBuilder()
                    .withSize( 4 )
                    .withTopology( meshTopology )
                    .withRoutingPolicy( NocRoutingPolicy.RoutingPolicy.DETERMINISTIC )
                    .withAGenerator( new Generator_Task("Generator"), meshTopology.getNocNetwork().coordinateSpace.newPoint(0, 0) )
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
