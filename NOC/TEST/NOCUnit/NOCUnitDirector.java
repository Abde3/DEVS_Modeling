package NOCUnit;

public class NOCUnitDirector {
    AbstractNOCUnitBuilder unitBuilder;
    private int nbOutPort;
    private int nbInPort;

    public NOCUnitDirector (AbstractNOCUnitBuilder unitBuilder) {
        this.unitBuilder = unitBuilder;
    }

    NOCUnitDirector withOutPorts(int nbOutPort) {
        this.nbOutPort = nbOutPort;
        return this;
    }

    NOCUnitDirector withInPorts(int nbInPort) {
        this.nbInPort = nbInPort;
        return this;
    }

    public NOCUnit build() {
        NOCUnit unit = unitBuilder.newBuilder();
        unitBuilder.buildInPorts(unit);
        unitBuilder.buildOutPorts(unit);
        unitBuilder.buildSubComponents(unit);

        return unit;
    }

}
