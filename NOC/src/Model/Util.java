package Model;

public class Util {


    public static NOC_Unit_factory.NODE_POSITION getNodePosition(NodeCoordinate coordinate, int rowSize, NOC_factory.Topology topology) {

        int i = coordinate.getX();
        int j = coordinate.getY();

        if (topology == NOC_factory.Topology.MESH) {

            if ( (i == j) && (i == 0 || i == rowSize-1) ) {
                return NOC_Unit_factory.NODE_POSITION.CORNER;
            } else if ( (i + j == rowSize-1 ) && (i == 0 || i == rowSize-1) ) {
                return NOC_Unit_factory.NODE_POSITION.CORNER;
            } else if ( i == 0 || i == rowSize || j == 0 || j == rowSize) {
                return NOC_Unit_factory.NODE_POSITION.EDGE;
            } else {
                return NOC_Unit_factory.NODE_POSITION.CENTER;
            }

        } else {
            return null;
        }
    }


}
