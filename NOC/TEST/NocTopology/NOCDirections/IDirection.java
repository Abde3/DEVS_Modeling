package NocTopology.NOCDirections;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class IDirection {

    LinkedHashSet<String> directions ;

    public IDirection(String ... directions) {
        this.directions = new LinkedHashSet(Arrays.asList(directions));
    }

    final public String[] getDirectionsSet() {
        return directions.toArray(new String[directions.size()]);
    }

}
