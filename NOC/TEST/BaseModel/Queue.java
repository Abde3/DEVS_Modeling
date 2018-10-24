package BaseModel;

import Library.DEVSModel.DEVSAtomic;
import Library.DEVSModel.Port;
import Model.NOCModel.NOC;
import NocTopology.NOCDirections.IPoint;

import java.util.Vector;
import java.util.stream.Collectors;

public class Queue extends DEVSAtomic {


	private static final NOC.NodeType NODETYPE = NOC.NodeType.QUEUE;

	Vector<Port> inputPorts;
	Vector<Port> outputPorts;


	public Queue(IPoint coordinate, String correspondingPort, int queueNumber, Vector<String> inputPortsNames, Vector<String> outputPortsNames) {

		this.name = NODETYPE + "-" + correspondingPort + "-" + queueNumber + " " + "[" + coordinate.toString().trim() + "]";

		/** Create input ports from names */
		this.inputPorts = inputPortsNames.stream().map(
				portName -> new Port(this, portName)
		).collect( Collectors.toCollection(Vector::new) );


		/** Create output ports from names */
		this.outputPorts = outputPortsNames.stream().map(
				portName -> new Port(this, portName)
		).collect( Collectors.toCollection(Vector::new) );

		/** Add input port to the model */
		this.inputPorts.forEach(  inputPort  -> addInPort(inputPort)  );

		/** Add output port to the model */
		this.outputPorts.forEach( outputPort -> addOutPort(outputPort) );
	}


	@Override
	public void init() { }

	@Override
	public void deltaExt(Port port, Object o, float v) { }

	@Override
	public void deltaInt() { }

	@Override
	public Object[] lambda() {
		return null;
	}

	@Override
	public float getDuration() {
		return 4;
	}

    public String toString() {
        String inports = "";
        String outports = "";

        int i;
        for(i = 0; i < this.inPorts.size(); ++i) {
            inports = inports + this.inPorts.get(i).getName() + "-";
        }

        for(i = 0; i < this.outPorts.size(); ++i) {
            outports = outports + this.outPorts.get(i).getName() + "-";
        }

        return this.getClass().toString() + '@' + Integer.toHexString(this.hashCode()) + " Inports: " + inports + " Outports: " + outports;
    }
}
