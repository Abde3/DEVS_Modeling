package NocTopology;

import Model.NOCModel.INocNetwork;
import NocTopology.NOCDirections.ICoordinate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class NocTopologyBuilder {
    private INocNetwork nocNetwork;
    private HashMap< Map.Entry<String, String>, Function<Integer, Integer>> directionToTransformations;
    private int size;
    private ICoordinate coordinateSpace;


    public NocTopologyBuilder withSize(int size) {
        this.size = size;
        return this;
    }

    public NocTopologyBuilder withAxisNames(String ... axisNames) {
        this.coordinateSpace = new ICoordinate(axisNames);
        return this;
    }

    public NocTopologyBuilder withActionForAxis(String actionName, String axis, Function<Integer, Integer> funct) {
        if (directionToTransformations == null) {
            directionToTransformations = new HashMap<>();
        }

        directionToTransformations.put( new AbstractMap.SimpleEntry(axis, actionName), funct) ;
        return this;
    }

    public NocTopology build() {
        nocNetwork = new INocNetwork(size, coordinateSpace);
        return new NocTopology(nocNetwork, directionToTransformations);
    }


    private static <E> Stream<List<E>> combinationsDupl(List<E> list, int size) {
        if (size == 0) {
            return Stream.of(Collections.emptyList());
        } else {
            return list.stream().flatMap( head -> combinationsDupl(list, size - 1).map(
                    tail -> {
                        List<E> newList = new ArrayList(tail);
                        newList.add(0, head);
                        return newList;
                    }
                    )
            );
        }
    }


    public static class UnhandledTopologyException extends Exception {
        public UnhandledTopologyException(String message) {
            super(message);
        }
    }
}