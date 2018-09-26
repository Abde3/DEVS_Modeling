package NocTopology.NOCDirections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class IPoint implements Comparable<IPoint>{
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPoint iPoint = (IPoint) o;
        boolean equals =  iPoint.valueOnAxis.equals(this.valueOnAxis) ;

        return equals ;
    }


    @Override
    public int hashCode() {
        return Objects.hash(valueOnAxis);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        valueOnAxis.forEach( (axis, value) -> {
            sb.append(value).append(", ");

        } );

        sb.delete(sb.length()-2, sb.length());

        return sb.toString();
    }

    @Override
    public int compareTo(IPoint point) {
        for (Map.Entry<String, Integer> entry: valueOnAxis.entrySet() ) {
            if (entry.getValue().equals(point.getValueOnAxis(entry.getKey()))) {
                continue;
            }
            else {
                return entry.getValue().compareTo(point.getValueOnAxis(entry.getKey()));
            }
        }

        return 0;
    }
}
