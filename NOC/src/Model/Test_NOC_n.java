package Model;

import DEVSSimulator.Root;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Vector;


public class Test_NOC_n {



    public static void main(String[] args){

        NocNetwork network = NocNetwork.getInstance();
        NOC MESH_4 = network.create_noc(NOC_factory.Topology.MESH, 4);

        Root root = new Root(MESH_4, 100);
        root.startSimulation();

        network.getPersistance().saveNodes(MESH_4);
        network.getPersistance().generate_output();

        //readSimulaionSequence("/home/mofed/IdeaProjects/DEVS Modeling/NOC.NOC/output/out_test_read");
    }



    public static Vector<AbstractMap.SimpleEntry<Double, Vector<AbstractMap.SimpleEntry<String, String>>>> readSimulaionSequence(String fileName) {

        Vector<AbstractMap.SimpleEntry<Double, Vector<AbstractMap.SimpleEntry<String, String>>>> sequences= new Vector<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            double currentTime = 0;

            while ((line = br.readLine()) != null) {

                Vector<AbstractMap.SimpleEntry<String, String>> sequence = new Vector<>();

                if (line.startsWith("time:")) {
//                    System.out.println(line);
                    currentTime = Double.parseDouble(line.substring(5));
                    continue;
                }

                do{
                    if(line.startsWith("--NODE")) {

                        if (line.matches(".*in_.*")) {
                            System.out.println("-" + line);

                            //System.out.println(line.replaceAll("--(.*)\\(.*", "$1"));
                            sequence.add(new AbstractMap.SimpleEntry<String, String>("in",line.replaceAll("--(.*)\\(.*", "$1")));
                        } else if (line.matches(".*out_.*")) {
                            System.out.println("-->" + line);
                            sequence.add(new AbstractMap.SimpleEntry<String, String>("out",line.replaceAll("--(.*)\\(.*", "$1")));
                        }

                    }
                } while ((line = br.readLine()) != null && !line.startsWith("time:") );


                if (! sequence.isEmpty()) {
                    sequences.add(new AbstractMap.SimpleEntry<>(currentTime, sequence));
                }

                // check why we got out
                if (line != null && line.startsWith("time:")) {
                    System.out.println(line);
                    currentTime = Double.parseDouble(line.substring(5));
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sequences;
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

