package Model;

import DEVSModel.Port;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Util {


    public static NOC_Unit_factory.NODE_POSITION getNodePosition(NodeCoordinate coordinate, int rowSize, NOC_factory.Topology topology) {

        int i = coordinate.getX();
        int j = coordinate.getY();

        if (topology == NOC_factory.Topology.MESH) {

            if ( (i == j) && (i == 0 || i == rowSize-1) ) {
                return NOC_Unit_factory.NODE_POSITION.CORNER;
            } else if ( (i + j == rowSize-1 ) && (i == 0 || i == rowSize-1) ) {
                return NOC_Unit_factory.NODE_POSITION.CORNER;
            } else if ( i == 0 || i == rowSize-1 || j == 0 || j == rowSize-1) {
                return NOC_Unit_factory.NODE_POSITION.EDGE;
            } else {
                return NOC_Unit_factory.NODE_POSITION.CENTER;
            }

        } else {
            return null;
        }
    }



    public static int nodeNumFromCoordinate(NodeCoordinate coordinate, int rowSize) {

        return coordinate.getX() * rowSize + coordinate.getY();
    }

    public static NodeCoordinate nodeCoordinateFromNodeName(String nodeName) {

        String s = nodeName.replaceAll("NODE\\[(.*)\\]", "$1");
        Scanner scan = new Scanner(s).useDelimiter(", ");
        int X = scan.nextInt();
        int Y = scan.nextInt();

        return new NodeCoordinate(X, Y);
    }

    public static Port getSource(Port inPort) {
        Port srcPort = null;

        for ( Map.Entry<Port, Vector<Port>> couple : NocNetwork.getInstance().getNocInstance().getIC().entrySet() ) {

            Port in = couple.getKey();
            Vector<Port> outs = couple.getValue();

            if( outs.contains(inPort) ) {
                srcPort = in;
                break;
            }
        }

        return srcPort;
    }

    public static String getConnectorString(NodeCoordinate src, NodeCoordinate dest) {
        String type = "";
        String outputDirection = "";

        if (isAtBottom(src, dest)) {
            outputDirection = "top";
        } else if (isAtRight(src, dest)) {
            outputDirection = "left";
        } else if (isAtTop(src, dest)) {
            outputDirection = "bottom";
        } else if (isAtLeft(src, dest)) {
            outputDirection = "right";
        } else {
            outputDirection = "ERROR";
        }

        return outputDirection;
    }


    private static boolean isAtLeft(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX() && dest.getY() == src.getY()+1);
    }

    private static boolean isAtRight(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX() && dest.getY() == src.getY()-1);
    }

    private static boolean isAtBottom(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX()+1 && dest.getY() == src.getY());
    }

    private static boolean isAtTop(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX()-1 && dest.getY() == src.getY());
    }

    public static AbstractMap.SimpleEntry<Integer, Integer> getConnectorFromConnectionString(String connectionValue) {

        String s = connectionValue.replaceAll("//@nodes\\.([0-9]+)/@connectors\\.([0-9]+)", "$1, $2");
        Scanner scan = new Scanner(s).useDelimiter(", ");
        Integer nodeNum = scan.nextInt();
        Integer connectorNum = scan.nextInt();

        return  new AbstractMap.SimpleEntry<>(nodeNum, connectorNum);

    }

    public static NodeCoordinate getCoordinateFromNodeNum(int srcNodeNum, int srcNumPort) {

        int X = Math.floorDiv(srcNodeNum, 4);
        int Y = Math.floorMod(srcNodeNum ,4);

        X = X * 200 + 500 ;
        Y = Y * 200 + 500 ;

        return new NodeCoordinate(X, Y);


    }
}
