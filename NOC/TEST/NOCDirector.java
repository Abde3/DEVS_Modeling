public class NOCDirector {
    AbstractNOCBuilder nocBuilder;
    int nocSize;

    public NOCDirector (AbstractNOCBuilder nocBuilder) {
        this.nocBuilder = nocBuilder;
    }

    NOCDirector withSize(int nocSize) {
        this.nocSize = nocSize;
        return this;
    }

    NOCDirector with

    public NOC build() {
        NOC noc = nocBuilder.newBuilder();
        nocBuilder
        nocBuilder.buildNocUnits(noc);
        nocBuilder.buildLinks(noc);

        return noc;
    }

}
