import DEVSSimulator.Root;
import Model.NOCModel.NOC;
import Model.NOCModel.NOCDirector;


public class Test {

    public static void main(String[] args) {

        /** VERSION 2X2 NOC WITH DEADLOCK **/
    /*
        int   SIZE_OF_THE_NETWORK = 2;
        float SIMULATION_TIME     = 5000;

        NOC MESH_2_DETERMINISTIC = NOCDirector.buildNOCMesh( SIZE_OF_THE_NETWORK );

        Root root = new Root(MESH_2_DETERMINISTIC, SIMULATION_TIME);
        root.startSimulation();
    */


        /** VERSION 3X3 NOC WITH DEADLOCK **/

        int   SIZE_OF_THE_NETWORK = 3;
        float SIMULATION_TIME     = 20000;

        NOC MESH_2_DETERMINISTIC = NOCDirector.buildNOCMesh( SIZE_OF_THE_NETWORK );

        Root root = new Root(MESH_2_DETERMINISTIC, SIMULATION_TIME);
        root.startSimulation();



        /** GENERATION OF THE PROMELA MODEL **/
    /*
        MESH_2_DETERMINISTIC.print_promela_channels(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.print_switches(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.printGenerators1(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.printGenerators2(SIZE_OF_THE_NETWORK);
        MESH_2_DETERMINISTIC.print_initializer();
    */

    }

}
