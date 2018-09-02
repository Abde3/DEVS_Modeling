package NOCUnit;

public abstract class AbstractNOCUnitBuilder<TNOCUnit extends NOCUnit> {

    abstract TNOCUnit newBuilder();
    abstract void buildInPorts (TNOCUnit noc, String ... outPortsName);
    abstract void buildOutPorts(TNOCUnit noc, String ... inPortsName);
    abstract void buildSubComponents(TNOCUnit unit);

}
