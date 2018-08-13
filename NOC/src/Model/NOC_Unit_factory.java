package Model;


import java.util.ArrayList;

public class NOC_Unit_factory {

    public enum NODE_POSITION {CORNER, EDGE, CENTER}

    /** Constructeur privé */
    private NOC_Unit_factory()
    {}

    /** Instance unique pré-initialisée */
    private static NOC_Unit_factory INSTANCE = new NOC_Unit_factory();

    /** Point d'accès pour l'instance unique du singleton */
    public static NOC_Unit_factory getInstance()  { return INSTANCE; }


    // La création d'une unité en fonction de son type est encapsulée dans la fabrique.
    public static NOC_Unit create_NOC_Unit(NOC_factory.Topology type, NodeCoordinate coordinate)
    {
        NOC_Unit noc = null;

        switch(type)
        {
            case LINEAR: noc = new NOC_LINEAR_UNIT(coordinate); break;
            case MESH: noc = new NOC_MESH_UNIT(coordinate); break;
        }

        return noc;
    }


    public static ArrayList<NOC_MESH.DIRECTION> getAlldirectionsforNode(NodeCoordinate coordinate, int size, NOC_factory.Topology topology) {

        if (topology.equals(NOC_factory.Topology.MESH)) {

            int x = coordinate.getX();
            int y = coordinate.getY();

            ArrayList<NOC_MESH.DIRECTION> directions = new ArrayList<>();

            if (x == 0) {
                directions.add(NOC_MESH.DIRECTION.EAST);
                if (y == 0) {
                    directions.add(NOC_MESH.DIRECTION.SOUTH);
                } else if (y == size-1) {
                    directions.add(NOC_MESH.DIRECTION.NORTH);
                } else {
                    directions.add(NOC_MESH.DIRECTION.NORTH);
                    directions.add(NOC_MESH.DIRECTION.SOUTH);
                }
            } else if (x == size-1) {
                directions.add(NOC_MESH.DIRECTION.WEST);
                if (y == 0) {
                    directions.add(NOC_MESH.DIRECTION.SOUTH);
                } else if (y == size-1) {
                    directions.add(NOC_MESH.DIRECTION.NORTH);
                } else {
                    directions.add(NOC_MESH.DIRECTION.NORTH);
                    directions.add(NOC_MESH.DIRECTION.SOUTH);
                }
            } else {
                directions.add(NOC_MESH.DIRECTION.WEST);
                directions.add(NOC_MESH.DIRECTION.EAST);

                if (y == 0) {
                    directions.add(NOC_MESH.DIRECTION.SOUTH);
                } else if (y == size-1) {
                    directions.add(NOC_MESH.DIRECTION.NORTH);
                } else {
                    directions.add(NOC_MESH.DIRECTION.NORTH);
                    directions.add(NOC_MESH.DIRECTION.SOUTH);
                }
            }

            return directions;
        }

        return null;
    }


    public static NOC_MESH.DIRECTION getOpposite(NOC_MESH.DIRECTION direction) {
        switch (direction) {
            case NORTH: return NOC_MESH.DIRECTION.SOUTH;
            case SOUTH: return NOC_MESH.DIRECTION.NORTH;
            case EAST: return NOC_MESH.DIRECTION.WEST;
            case WEST: return  NOC_MESH.DIRECTION.EAST;
            default: return null;
        }
    }

}
