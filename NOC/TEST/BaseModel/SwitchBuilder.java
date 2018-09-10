package BaseModel;


import NocTopology.NOCDirections.IPoint;

import java.util.Arrays;
import java.util.Vector;

public class SwitchBuilder {
    private Vector<String> inputPortsNames;
    private Vector<String> outputPortsNames;
    private IPoint coordinate;

    public SwitchBuilder withInputPorts(String... inputPorts) {
        this.inputPortsNames = new Vector<>(Arrays.asList(inputPorts));
        return this;
    }

    public SwitchBuilder withOutputPorts(String... outputPorts) {
        this.outputPortsNames = new Vector<>(Arrays.asList(outputPorts));
        return this;
    }

    public SwitchBuilder withCoordinate(IPoint coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public Switch build() {
        return new Switch(coordinate, inputPortsNames, outputPortsNames);
    }
}