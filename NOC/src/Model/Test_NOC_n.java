package Model;

import DEVSSimulator.Root;


public class Test_NOC_n {



    public static void main(String[] args){

        NocNetwork network = NocNetwork.getInstance();
        NOC MESH_4 = network.create_noc(NOC_factory.Topology.MESH, 4);

        Root root = new Root(MESH_4, 5);
        root.startSimulation();

        network.getPersistance().generate_output();
    }




//	public static void main(String[] args){
//
//		NOC_n MESH_4= new NOC_n(4, NOC_n.Topology.MESH);
//
//		Root root = new Root(MESH_4, 5);
//		root.startSimulation();
//
//        try {
//            Persistance.saveModel(null, MESH_4);
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        }
//    }
}

