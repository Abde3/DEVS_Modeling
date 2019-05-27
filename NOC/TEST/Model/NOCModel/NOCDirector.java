package Model.NOCModel;



import BaseModel.Generator_Task_back;
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
                    .withAGenerator( new Generator_Task_back(sizeOftheNetwork, "Generator[1, 0]", "01234567891011121314",1, 1, 2000), meshTopology.getNocNetwork().coordinateSpace.newPoint(1, 0), "WEST" )
                    .withAGenerator( new Generator_Task_back(sizeOftheNetwork, "Generator[1, 1]", "abcdefghijklmnopqrst",0, 0, 0), meshTopology.getNocNetwork().coordinateSpace.newPoint(1, 1), "WEST" )
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
