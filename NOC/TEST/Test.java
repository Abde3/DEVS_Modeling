import DEVSSimulator.Root;
import NOC.*;
import NOCRoutingPolicy.DeterministicRouting;
import NOCRoutingPolicy.NocRoutingPolicy;
import NOCRoutingPolicy.UnhandledRoutingPolicyException;
import NocTopology.MeshTopology;
import NocTopology.NocTopology;


public class Test {

    public static void main(String[] args){
        System.out.println("ooow");

        NOC noc = new NOCDirector( new NOC2DimensionBuilder() )
                    .withRoutingPolicy( NOC.RoutingPolicy.DETERMINISTIC )
                    .withTopology( NocTopology.NocTopology.Topology.MESH )
                    .withSize( 4 )
                    .build( );

        Root root = new Root(noc, 40);
		root.startSimulation();

    }

}
