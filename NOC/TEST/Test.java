import Library.DEVSSimulator.Root;
import Model.NOCModel.NOC;
import Model.NOCModel.NOCDirector;
import Verification.StateRepresentation;

import java.util.concurrent.TimeUnit;

public class Test {

    private static final int SIZE_OF_THE_NETWORK = 4;
    private static final float SIMULATION_TIME = 50;

    public static void main(String[] args){

        NOC MESH_2_DETERMINISTIC = NOCDirector.buildNOCMesh( SIZE_OF_THE_NETWORK );

        Root root = new Root(MESH_2_DETERMINISTIC, SIMULATION_TIME);
        root.startSimulation();




        try {

            TimeUnit.SECONDS.sleep(10);
            StateRepresentation.writeFile("/home/mofed/Bureau/result/out.txt");
            System.out.println( "OOOOOOOW" );

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
