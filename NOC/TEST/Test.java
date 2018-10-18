import Model.NOCModel.NOC;
import Model.NOCModel.NOCDirector;


public class Test {

    private static final int SIZE_OF_THE_NETWORK = 2;
    private static final float SIMULATION_TIME = 500;

    public static void main(String[] args){

        NOC MESH_2_DETERMINISTIC = NOCDirector.buildNOCMesh( SIZE_OF_THE_NETWORK );

        DEVSSimulator.Root root = new DEVSSimulator.Root(MESH_2_DETERMINISTIC, SIMULATION_TIME);

		root.startSimulation();

    }

}
