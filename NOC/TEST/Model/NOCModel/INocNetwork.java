package Model.NOCModel;


import DEVSModel.DEVSModel;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NOCDirections.IPoint;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class INocNetwork implements Iterable<DEVSModel>  {

    protected HashMap<IPoint, DEVSModel> units;    /***** Represent all the units in the model ***************/
    protected int size;
    protected ICoordinate coordinateSpace;

    public INocNetwork( int size, ICoordinate coordinate ) {
        this.units = new LinkedHashMap<IPoint, DEVSModel>();
        this.size = size;
        this.coordinateSpace = coordinate;

        List<List<Integer>> allCoordinates = combinationsDupl(IntStream.range(0, size).boxed().collect(Collectors.toList()), coordinate.getNuberOfAxis()).collect(Collectors.toList());
        allCoordinates.stream().forEach( coordinatesValues -> addUnitAt(null, coordinate.newPoint( coordinatesValues.toArray(new Integer[0]))) );
    }

    public Collection<DEVSModel> getAllUnits() {
        return units.values();
    }

    public DEVSModel getUnitAt( IPoint position ) {
        return units.get(position);
    }

    public boolean addUnitAt( DEVSModel model, IPoint position ) {
        return null == units.put(position, model);
    }

    public Collection<IPoint> getAllPositions() {
        return units.keySet();
    }

    public DEVSModel getUnitFromPosition( IPoint position, String direction ){
        return null;
    }


    public boolean isValidPoint(IPoint position) {
        return getAllPositions().contains( position );
    }

    public boolean isValidPointOnAxis(String axis, int value) {
        return getAllPositions().stream().anyMatch( point -> point.getValueOnAxis(axis) == value );
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

    public ICoordinate getCoordinateSpace() {
        return coordinateSpace;
    }

    @Override
    public Iterator<DEVSModel> iterator() {
        return null;
    }

    @Override
    public Spliterator<DEVSModel> spliterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super DEVSModel> consumer) {

    }
}
