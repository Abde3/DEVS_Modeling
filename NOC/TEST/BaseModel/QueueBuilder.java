package BaseModel;

import NocTopology.NOCDirections.IPoint;

import java.util.Arrays;
import java.util.Vector;

public class QueueBuilder {
    private IPoint coordinate;
    private String correspondingPort;
    private int numberOfTheQueue;
    private Vector<String> inputPortsNames;
    private Vector<String> outputPortsNames;

    public QueueBuilder withInputPorts(String ... inputPorts) {
        this.inputPortsNames = new Vector<>(Arrays.asList(inputPorts));
        return this;
    }

    public QueueBuilder withOutputPorts(String ...outputPorts) {
        this.outputPortsNames = new Vector<>(Arrays.asList(outputPorts));
        return this;
    }

    public QueueBuilder withCoordinate(IPoint coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public QueueBuilder withCorrespondingPort(String portName) {
        this.correspondingPort = portName;
        return this;
    }

    public QueueBuilder withQueueNumber(int numberOfTheQueue) {
        this.numberOfTheQueue = numberOfTheQueue;
        return this;
    }

    public Queue build() {
        return new Queue(coordinate, correspondingPort, numberOfTheQueue, inputPortsNames, outputPortsNames);
    }
}