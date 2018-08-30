package Model;

import DEVSModel.Port;
import javafx.geometry.Point2D;


import java.util.*;

public class Util {


    private static final int BASE_GRAPH_X_COORDINATE = 10;
    private static final int BASE_GRAPH_Y_COORDINATE = 10;
    private static final int GRAPH_NODE_OFFSET = 200 ;
    private static final int GRAPH_NODE_WIDTH = 150 ;
    private static final int GRAPH_NODE_HEIGHT = 100 ;


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

        return coordinate.getY() * rowSize + coordinate.getX();
    }

    public static NodeCoordinate nodeCoordinateFromNodeName(String nodeName) {

        String s = nodeName.replaceAll("NODE\\[(.*)\\]", "$1");
        Scanner scan = new Scanner(s).useDelimiter(", ");
        int X = scan.nextInt();
        int Y = scan.nextInt();

        return new NodeCoordinate(X, Y);
    }


    public static NodeCoordinate nodeCoordinateFromElementName(String elementName) {

        String s = elementName.replaceAll(".*\\[(.*)\\]", "$1");
        Scanner scan = new Scanner(s).useDelimiter(", ");
        int X = scan.nextInt();
        int Y = scan.nextInt();

        return new NodeCoordinate(X, Y);
    }

    public static String getDirectionFromElementName( String elementName) {
        if ( elementName == null ) return "";
        return elementName.replaceAll(".*-((WEST)|(EAST)|(NORTH)|(SOUTH)).*", "$1");
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


    private static boolean isAtTop(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX() && dest.getY() == src.getY()+1);
    }

    private static boolean isAtRight(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX()+1 && dest.getY() == src.getY());
    }

    private static boolean isAtBottom(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX() && dest.getY() == src.getY()-1);
    }

    private static boolean isAtLeft(NodeCoordinate src, NodeCoordinate dest) {
        return (src.getX() == dest.getX()-1 && dest.getY() == src.getY());
    }

    public static AbstractMap.SimpleEntry<Integer, Integer> getConnectorFromConnectionString(String connectionValue) {

        String s = connectionValue.replaceAll("//@nodes\\.([0-9]+)/@connectors\\.([0-9]+)", "$1, $2");
        Scanner scan = new Scanner(s).useDelimiter(", ");
        Integer nodeNum = scan.nextInt();
        Integer connectorNum = scan.nextInt();

        return  new AbstractMap.SimpleEntry<>(nodeNum, connectorNum);

    }

    public static NodeCoordinate getCoordinateFromNodeNum(int srcNodeNum) {


        int X = Math.floorMod(srcNodeNum, 4);
        int Y = Math.floorDiv(srcNodeNum ,4);

        return new NodeCoordinate(X, Y);

    }

    public static List<Point2D> getGraphJointsPosition(int srcNodeNum, int srcNumPort, int destNodeNum, int destNumPort) {

        NodeCoordinate srcCoordinate = getCoordinateFromNodeNum(srcNodeNum);
        NodeCoordinate destCoordinate = getCoordinateFromNodeNum(destNodeNum);

        NodeCoordinate graphSrcCoordinate = getNodeGraphPosition(srcCoordinate);
        NodeCoordinate graphDestCoordinate = getNodeGraphPosition(destCoordinate);

        ArrayList<Point2D> joints = new ArrayList<>();

        if(isAtTop(srcCoordinate, destCoordinate)) {
            Point2D start   = new Point2D(graphSrcCoordinate.getX()  + (srcNumPort+1) * (100 / (2+srcNumPort)), graphSrcCoordinate.getY() + GRAPH_NODE_HEIGHT + 20 );
            Point2D end     = new Point2D(graphDestCoordinate.getX() + (srcNumPort+1) * (100 / (2+srcNumPort)), graphDestCoordinate.getY() - 15);
            joints.add(start);
            joints.add(end);
        } else if (isAtRight(srcCoordinate, destCoordinate)) {
            Point2D start = new Point2D(graphSrcCoordinate.getX() - 15 , graphSrcCoordinate.getY() + (srcNumPort+1) * (100 / (2+srcNumPort)) );
            Point2D end   = new Point2D(graphDestCoordinate.getX() + (GRAPH_NODE_WIDTH) + 20, graphDestCoordinate.getY() + (srcNumPort+1) * (100 / (2+srcNumPort)) );
            joints.add(start);
            joints.add(end);
        } else if (isAtBottom(srcCoordinate, destCoordinate)) {
            Point2D start = new Point2D(graphSrcCoordinate.getX() + (srcNumPort+1) * (100 / (2+srcNumPort)) , graphSrcCoordinate.getY() - 15);
            Point2D end   = new Point2D(graphDestCoordinate.getX() + (srcNumPort+1) * (100 / (2+srcNumPort)), graphDestCoordinate.getY() + GRAPH_NODE_HEIGHT + 20 );
            joints.add(start);
            joints.add(end);
        } else if (isAtLeft(srcCoordinate, destCoordinate)) {
             Point2D start = new Point2D(graphSrcCoordinate.getX()  + (GRAPH_NODE_WIDTH) + 20, graphSrcCoordinate.getY() + (srcNumPort+1) * (100 / (2+srcNumPort)) );
             Point2D end   = new Point2D(graphDestCoordinate.getX() - 15, graphDestCoordinate.getY() + (srcNumPort+1) * (100 / (2+srcNumPort)) );
             joints.add(start);
             joints.add(end);
        } else {
            return joints;
        }

        return joints;
    }


    public static NodeCoordinate getNodeGraphPosition(NodeCoordinate coordinate) {

        int X = BASE_GRAPH_X_COORDINATE + coordinate.getX() * GRAPH_NODE_OFFSET;
        int Y = BASE_GRAPH_Y_COORDINATE + coordinate.getY() * GRAPH_NODE_OFFSET;

        return new NodeCoordinate(X, Y);

    }
}
