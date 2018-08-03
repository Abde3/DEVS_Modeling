package Model;

public class NocNetwork {

    private NOC_factory.Topology topology;
    private int size;
    private Persistance persistance;

    /** Constructeur privé */
    private NocNetwork()
    {
        persistance = new Persistance();

    }

    /** Instance unique pré-initialisée */
    private static NocNetwork INSTANCE = new NocNetwork();

    /** Point d'accès pour l'instance unique du singleton */
    public static NocNetwork getInstance()  { return INSTANCE; }


    public NOC create_noc(NOC_factory.Topology topology, int size) {
        INSTANCE.size = size;
        INSTANCE.topology = topology;

        NOC nocInstance = NOC_factory.create_NOC( topology, size );
        nocInstance.set_generator(new Generator_Task("Simple Generator"));
        nocInstance.build_network();

        return nocInstance;
    }



    public Persistance getPersistance() {
        return persistance;
    }

}
