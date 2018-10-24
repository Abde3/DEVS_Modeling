package Model.NOCModel;


import Library.DEVSModel.DEVSModel;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NOCDirections.IPoint;
import Util.NocUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class INocNetwork implements Iterable<DEVSModel>  {

    protected TreeMap<IPoint, DEVSModel> units;    /***** Represent all the units in the model ***************/
    protected int size;
    protected ICoordinate coordinateSpace;

    public INocNetwork( int size, ICoordinate coordinate ) {
        this.units = new TreeMap<IPoint, DEVSModel>( coordinate.getComparator() );
        this.size = size;
        this.coordinateSpace = coordinate;

        List<List<Integer>> allCoordinates = NocUtil.combinationsDupl(IntStream.range(0, size).boxed().collect(Collectors.toList()), coordinate.getNuberOfAxis()).collect(Collectors.toList());
        boolean isOk = allCoordinates
                .stream()
                .map( coordinatesValues ->
                        addUnitAt(null, coordinate.newPoint( coordinatesValues.toArray(new Integer[0])))
                    )
                .reduce(Boolean::logicalAnd)
                .get();
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

    public boolean isValidPoint(IPoint position) {
        return getAllPositions().contains( position );
    }

    public boolean isValidPointOnAxis(String axis, int value) {
        return getAllPositions().stream().anyMatch( point -> point.getValueOnAxis(axis) == value );
    }

    public IPoint getPositionOfUnit ( DEVSModel unit ) {
        return units.entrySet().stream().filter( entry -> entry.getValue().equals( unit ) ).findFirst().get().getKey();
    }

    public ICoordinate getCoordinateSpace() {
        return coordinateSpace;
    }

    @Override
    public Iterator<DEVSModel> iterator() {
        return units.values().iterator();
    }

    @Override
    public Spliterator<DEVSModel> spliterator() {

        return units.values().spliterator();
    }

    @Override
    public void forEach(Consumer<? super DEVSModel> consumer) {
        units.values().forEach( consumer );
    }
}
