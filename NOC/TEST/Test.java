import DEVSSimulator.Root;
import Model.NOCModel.NOC;
import Model.NOCModel.NOCDirector;


public class Test {

    public static void main(String[] args){
        System.out.println("ooow ");

        NOC MESH_4_DETERMINISTIC = NOCDirector.buildNOCMesh();

        Root root = new Root(MESH_4_DETERMINISTIC, 40);
		root.startSimulation();

    }

}
