import BaseModel.Generator_Task;
import DEVSSimulator.Root;
import NOC.*;
import NOCUnit.NodeCoordinate;
import NocTopology.NocTopology;


public class Test {

    public static void main(String[] args){
        System.out.println("ooow");

        NOC noc = new NOCDirector( new NOC2DimensionBuilder() )
                    .withRoutingPolicy( NOC.RoutingPolicy.DETERMINISTIC )
                    .withTopology( NocTopology.Topology.MESH )
                    .withSize( 4 )
                    .withAGeneratorAt( new Generator_Task("random"), new NodeCoordinate(0, 0))
                    .build( );

        Root root = new Root(noc, 40);
		root.startSimulation();

    }

}
