package Model.NOCModel;



import BaseModel.Generator_Task;
import Model.Exceptions.ExistingGeneratorException;
import Model.Routing.NocRoutingPolicy;
import Model.Routing.UnhandledRoutingPolicyException;
import NocTopology.NocTopology;
import NocTopology.NocTopologyDirector;

public class NOCDirector {



    public static NOC buildNOCMesh(int sizeOftheNetwork) {

        final NocTopology meshTopology = NocTopologyDirector.buildMeshTopology(sizeOftheNetwork);

        NOC NOCMesh = null;
        try {

            NOCMesh = new NOC2DimensionBuilder()
                    .withSize( sizeOftheNetwork )
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
