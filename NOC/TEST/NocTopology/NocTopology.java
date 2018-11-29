package NocTopology;


import Library.DEVSModel.DEVSModel;
import Library.DEVSModel.Port;
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

    public String getAxisFromDirection( String direction ) {
        return directionToTransformations.entrySet().stream()
                .filter( entry -> entry.getKey().getValue().equals(direction) ).findFirst().get().getKey().getKey();
    }

    public String getOppositeDirection( String direction ) {
        return directionToTransformations.entrySet().stream()
                .filter( entry -> entry.getKey().getKey().equals( getAxisFromDirection( direction ) ) )
                .filter( entry -> ! entry.getKey().getValue().equals( direction ) )
                .findFirst()
                .get()
                .getKey()
                .getValue();
    }

    public String getOppositePortType( String type ) {
        return type.replaceFirst( (type.startsWith("IN") ? "IN": "OUT"),  (type.startsWith("IN") ? "OUT": "IN") );
    }

    public Port getCorrespondingPort( Port srcPort ) {

        DEVSModel srcModel = srcPort.getModel();
        IPoint srcPoint    = nocNetwork.getPositionOfUnit( srcModel );

        Map.Entry<Map.Entry<String, String>, Function<Integer, Integer>> res = directionToTransformations.entrySet().stream()
                .filter(entry -> srcPort.getName().contains(entry.getKey().getValue()))
                .findFirst()
                .get();

        IPoint destPoint    = srcPoint.transformedPoint( res.getKey().getKey(), res.getValue() );
        DEVSModel destModel = nocNetwork.getUnitAt( destPoint );
        Port destPort       = null;

        String[] elements = srcPort.getName().split("-", -1);
        String opositePortName = getOppositePortType(elements[0]) + "-" + elements[1] + "-" + getOppositeDirection(elements[2]);

        if ( srcModel.getOutPorts().contains(srcPort) ) {
            destPort = destModel.getInPort( opositePortName );
        } else {
            destPort = destModel.getOutPort( opositePortName );
        }

         return destPort;
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

    public HashMap<String,IPoint> getAllNeighbourPoint(IPoint unit ) {

        HashMap<String,IPoint> neighboursPoint = new HashMap<>();

        List<Map.Entry<Map.Entry<String, String>, Function<Integer, Integer>>> authorizedTransformations =
                directionToTransformations.entrySet().stream()
                        .filter(entry -> getOutputDirections(unit).contains(entry.getKey().getValue()))
                        .collect(Collectors.toList());

        authorizedTransformations.stream()
                .forEach( entry ->
                        neighboursPoint.put( entry.getKey().getValue(), unit.transformedPoint(entry.getKey().getKey(), entry.getValue() ) )
                );

        return neighboursPoint;
    }

    public INocNetwork getNocNetwork() {
        return nocNetwork;
    }


    public String getDirectionToReachPoint(IPoint source, IPoint destination, String selectedAxis) {
        return getOutputDirections( source )
                .stream()
                .filter( direction -> getAxisFromDirection( direction ).equals( selectedAxis ))
                .filter( direction ->
                        Math.abs(directionToTransformations
                                .get(new AbstractMap.SimpleEntry<>(selectedAxis, direction))
                                .apply( source.getValueOnAxis(selectedAxis) ) - destination.getValueOnAxis(selectedAxis))
                                <= Math.abs( source.getValueOnAxis(selectedAxis) - destination.getValueOnAxis(selectedAxis) )
                        )
                .findFirst().get();

    }
}
