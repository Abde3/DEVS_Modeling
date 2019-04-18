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
                    .withAGenerator( new Generator_Task(sizeOftheNetwork, "Generator[0, 0]", "0123456789101112131415",1, 1), meshTopology.getNocNetwork().coordinateSpace.newPoint(1, 0) )
                    .withAGenerator( new Generator_Task(sizeOftheNetwork, "Generator[1, 1]", "abcdefghijklmnopqrstuv",0, 0), meshTopology.getNocNetwork().coordinateSpace.newPoint(1, 1) )
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
