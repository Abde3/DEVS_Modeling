
public abstract class AbstractNOCBuilder<TNOC extends NOC> {

    abstract TNOC newBuilder();
    abstract void buildGenerators(TNOC noc);
    abstract void buildNocUnits(TNOC noc);
    abstract void buildLinks(TNOC noc);

}
