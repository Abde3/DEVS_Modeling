package Model;


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



}
