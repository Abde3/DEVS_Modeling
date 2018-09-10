package BaseModel;

import NocTopology.NOCDirections.IPoint;

import java.util.Arrays;
import java.util.Vector;

public class ProcessingElementBuilder {
    private IPoint coordinate;
    private Vector<String> inputPortsNames;
    private Vector<String> outputPortsNames;

    public ProcessingElementBuilder withInputPorts(String... inputPorts) {
        this.inputPortsNames = new Vector<>(Arrays.asList(inputPorts));
        return this;
    }

    public ProcessingElementBuilder withOutputPorts(String ... outputPorts) {
        this.outputPortsNames = new Vector<>(Arrays.asList(outputPorts));
        return this;
    }


    public ProcessingElementBuilder withCoordinate(IPoint coordinate) {
        this.coordinate = coordinate;
        return this;
    }
    public ProcessingElement build() {
        return new ProcessingElement(coordinate, inputPortsNames, outputPortsNames);
    }
}