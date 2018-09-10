package NocTopology.NOCDirections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class IPoint {
    private LinkedHashMap<String, Integer> valueOnAxis;

    public IPoint(String[] axisNames, Integer[] values) {

        if (values.length != axisNames.length) {
            System.out.println("ERREUR nb coordinate != axisNames");
        } else {
            valueOnAxis =  IntStream.range(0, values.length).
                    collect(LinkedHashMap::new, (axis, value) -> axis.put(axisNames[value], values[value]), Map::putAll);
        }

    }

    public Integer getValueOnAxis(String axis) {
        return valueOnAxis.get(axis);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        valueOnAxis.forEach( (axis, value) -> {
//            sb.append(axis).append("=").append(value).append(" ");
            sb.append(value).append(", ");

        } );

        sb.delete(sb.length()-2, sb.length());

        return sb.toString();
    }
}
