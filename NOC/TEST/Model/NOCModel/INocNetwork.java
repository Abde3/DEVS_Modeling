package Model.NOCModel;


import DEVSModel.DEVSModel;
import NocTopology.NOCDirections.ICoordinate;
import NocTopology.NOCDirections.IPoint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public abstract class INocNetwork<T extends IPoint> implements Iterable<DEVSModel>  {

    protected HashMap<T, DEVSModel> units;    /***** Represent all the units in the model ***************/

    protected INocNetwork( int size ) {
        this.units = new LinkedHashMap<>(size*size);

        Stream<List<Integer>> allPositionsStream = combinationsDupl(IntStream.range(0, size).boxed().collect(Collectors.toList()), numberOfAxis);

    }


    abstract public Collection<DEVSModel>   getAllUnits();
    abstract public DEVSModel               getUnitAt( ICoordinate position );
    abstract public boolean                 addUnitAt( DEVSModel model, T position );
    abstract public Collection<T>           getAllPositions();
    abstract public DEVSModel               getUnitFromPosition( T position, String direction );


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


}
