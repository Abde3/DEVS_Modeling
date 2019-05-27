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
                    .withAGenerator( new Generator_Task_back(sizeOftheNetwork, "Generator[0]", "01234567891011121314151617",0, 0, 0), meshTopology.getNocNetwork().coordinateSpace.newPoint(2, 0), "WEST" )
                    .withAGenerator( new Generator_Task_back(sizeOftheNetwork, "Generator[1]", "01234567891011121314151617",2, 1, 0), meshTopology.getNocNetwork().coordinateSpace.newPoint(2, 2), "WEST" )
                    .withAGenerator( new Generator_Task_back(sizeOftheNetwork, "Generator[2]", "abcdefghijklmnopqrst",1, 2, 10000), meshTopology.getNocNetwork().coordinateSpace.newPoint(1, 1), "WEST" )
                    .withAGenerator( new Generator_Task_back(sizeOftheNetwork, "Generator[3]", "abcdefghijklmnopqrstuvwxyz",0, 1, 0), meshTopology.getNocNetwork().coordinateSpace.newPoint(1, 2), "WEST" )
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
