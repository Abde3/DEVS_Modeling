import DEVSSimulator.Root;


public class Test_NOC_n {

	public static void main(String[] args){

		NOC_n MESH_4= new NOC_n(4, NOC_n.Topology.MESH);

		Root root = new Root(MESH_4, 50);
		root.startSimulation();		
	}
}

