import DEVSSimulator.Root;
import Model.NOCModel.NOC;
import Model.NOCModel.NOCDirector;


public class Test {

    public static final int   SIZE_OF_THE_NETWORK = 2;
    public static final float SIMULATION_TIME     = 5000;

    public static void main(String[] args) {

        NOC MESH_2_DETERMINISTIC = NOCDirector.buildNOCMesh( SIZE_OF_THE_NETWORK );

        Root root = new Root(MESH_2_DETERMINISTIC, SIMULATION_TIME);
        root.startSimulation();

    /*
        MESH_2_DETERMINISTIC.print_promela_channels(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.print_switches(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.printGenerators1(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.printGenerators2(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.print_initializer();
    */

    }

}
