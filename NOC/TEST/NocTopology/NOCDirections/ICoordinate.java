package NocTopology.NOCDirections;

import java.util.*;
import java.util.stream.Stream;

public class ICoordinate {

    private String[] axisNames;

    public ICoordinate(String ... axisNames ) {
        this.axisNames = axisNames;
    }

    public IPoint newPoint(Integer ... values) {
        return new IPoint(axisNames, values);
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

    public int getNuberOfAxis() {
        return axisNames.length;
    }

}
