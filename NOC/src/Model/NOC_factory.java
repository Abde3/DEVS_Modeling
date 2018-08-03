package Model;

public class NOC_factory {

    public enum Topology {LINEAR, MESH};


    /** Constructeur privé */
    private NOC_factory()
    {}

    /** Instance unique pré-initialisée */
    private static NOC_factory INSTANCE = new NOC_factory();

    /** Point d'accès pour l'instance unique du singleton */
    public static NOC_factory getInstance()  { return INSTANCE; }


    // La création d'une unité en fonction de son type est encapsulée dans la fabrique.
    public static NOC create_NOC(Topology type, int size)
    {
        NOC noc = null;

        switch(type)
        {
            case LINEAR: noc = new NOC_LINEAR(); break;
            case MESH: noc = new NOC_MESH(size); break;
        }

        return noc;
    }



}
