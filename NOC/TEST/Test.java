import BaseModel.Generator_Task;
import DEVSSimulator.Root;
import NOC.*;
import NOCUnit.NodeCoordinate;
import NocTopology.NocTopology;

import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Test {

    public static void main(String[] args){
        System.out.println("ooow ");

        NOC MESH_4_DETERMINISTIC = NOCDirector.buildNOCMesh();

        Root root = new Root(MESH_4_DETERMINISTIC, 40);
		root.startSimulation();

    }

}
