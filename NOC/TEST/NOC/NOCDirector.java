package NOC;

import NOCUnit.AbstractNOCUnitBuilder;

public class NOCDirector {
    AbstractNOCBuilder nocBuilder;
    int nocSize;


    public NOCDirector (AbstractNOCBuilder nocBuilder) {
        this.nocBuilder = nocBuilder;
    }


    public NOCDirector withSize(int nocSize) {
        this.nocSize = nocSize;
        return this;
    }


    public NOC build() {
        NOC noc = nocBuilder.newBuilder();
        nocBuilder.buildNocUnits(noc);
        nocBuilder.buildLinks(noc);

        return noc;
    }

}
