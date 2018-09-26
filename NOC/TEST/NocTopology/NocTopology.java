package NocTopology;


import Model.NOCModel.INocNetwork;
import NocTopology.NOCDirections.IPoint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NocTopology {

    protected INocNetwork nocNetwork;                       /***** Represent all the units in the model *********/
    protected HashMap< Map.Entry<String, String>, Function<Integer, Integer>> directionToTransformations;


    protected NocTopology(INocNetwork nocNetwork,
                       HashMap< Map.Entry<String, String>, Function<Integer, Integer>> directionToTransformations) {
        this.nocNetwork = nocNetwork;
        this.directionToTransformations = directionToTransformations;
    }

    public List<String> getAllDirections() {
        return directionToTransformations.keySet().stream().map(entry -> entry.getValue() ).collect(Collectors.toList());
    }

    public List<String> getInputDirections(IPoint coodinate) {

        ArrayList<String> result = new ArrayList<>();

        for ( Map.Entry<String, String> entry : directionToTransformations.keySet() ) {
            String axis = entry.getKey();
            String direction = entry.getValue();

            Function<Integer, Integer> function = directionToTransformations.get(entry);
            Integer newValueOfAxis = function.apply(coodinate.getValueOnAxis(axis));

            if ( nocNetwork.isValidPointOnAxis( axis, newValueOfAxis) ) {
                result.add(direction);
            }
        }

        return result;
    }

    public List<String> getOutputDirections(IPoint coodinate) {
        ArrayList<String> result = new ArrayList<>();

        for ( Map.Entry<String, String> entry : directionToTransformations.keySet() ) {
            String axis = entry.getKey();
            String direction = entry.getValue();

            Function<Integer, Integer> function = directionToTransformations.get(entry);
            if ( nocNetwork.isValidPointOnAxis( axis, function.apply( coodinate.getValueOnAxis(axis))) ) {
                result.add(direction);
            }
        }

        return result;
    }

    public INocNetwork getNocNetwork() {
        return nocNetwork;
    }



}
