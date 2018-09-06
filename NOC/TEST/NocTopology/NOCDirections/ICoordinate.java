package NocTopology.NOCDirections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Function;

public abstract class ICoordinate {

    private LinkedHashMap<String, Integer> coordinate;
    private HashMap< String, Function<ICoordinate, ICoordinate>> directionToTransformations;

    protected ICoordinate(String... systemAxesName ) {
        coordinate = new LinkedHashMap<>(systemAxesName.length);
        directionToTransformations = new HashMap<>();
        Arrays.stream(systemAxesName).distinct().forEachOrdered(axe -> coordinate.put(axe, 0));
    }

    protected final void bind(String direction, Function<ICoordinate, ICoordinate> pointTransformation) {
        directionToTransformations.put(direction, pointTransformation);
    }

    protected Collection<String> getAxesNames() {
        return coordinate.keySet();
    }

    protected int getValueOnAxe(String axe) {
        return coordinate.get(axe);
    }

    protected void setValueOnAxe(String axe, int value) {
        coordinate.put(axe, value);
    }

    protected ICoordinate incrementValueOnAxe(String axe) {
        coordinate.put(axe, coordinate.get(axe) + 1);
        return this;
    }

    protected ICoordinate decrementValueOnAxe(String axe) {
        coordinate.put(axe, coordinate.get(axe) - 1);
        return this;
    }



}
