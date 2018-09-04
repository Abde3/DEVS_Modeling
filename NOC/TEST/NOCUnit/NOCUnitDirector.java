package NOCUnit;

public class NOCUnitDirector {
    AbstractNOCUnitBuilder unitBuilder;

    public NOCUnitDirector (AbstractNOCUnitBuilder unitBuilder) {
        this.unitBuilder = unitBuilder;
    }

    public NOCUnit build() {

        NOCUnit unit = unitBuilder.newBuilder();
        unitBuilder.buildInPorts(unit);
        unitBuilder.buildOutPorts(unit);
        unitBuilder.buildSubComponents(unit);

        return unit;
    }

}
