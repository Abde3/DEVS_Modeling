import Library.DEVSSimulator.Root;
import Model.NOCModel.NOC;
import Model.NOCModel.NOCDirector;
import Verification.StateRepresentation;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {

    private static final int SIZE_OF_THE_NETWORK = 4;
    private static final float SIMULATION_TIME = 3;

    public static void main(String[] args){


        NOC MESH_2_DETERMINISTIC = NOCDirector.buildNOCMesh( SIZE_OF_THE_NETWORK );

        Root root = new Root(MESH_2_DETERMINISTIC, SIMULATION_TIME);
        root.startSimulation();

        while( root.getCurrentTime() < SIMULATION_TIME) { continue; }

        MESH_2_DETERMINISTIC.print_promela_channels();
        MESH_2_DETERMINISTIC.print_switches();
/*

        List<List<AbstractMap.SimpleEntry<Integer, Integer>>> a = IntStream.rangeClosed(0, 3).mapToObj(
                i -> IntStream.rangeClosed(0, 3).mapToObj(
                        j -> new AbstractMap.SimpleEntry<>(i, j)
                ).collect(Collectors.toList())
        ).collect(Collectors.toList());


        a.stream().forEachOrdered( System.out::println );*/
    }


    public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        List<List<T>> resultLists = new ArrayList<List<T>>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<T>());
            return resultLists;
        } else {
            List<T> firstList = lists.get(0);
            List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (T condition : firstList) {
                for (List<T> remainingList : remainingLists) {
                    ArrayList<T> resultList = new ArrayList<T>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }


}
