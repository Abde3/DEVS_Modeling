package BaseModel;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;
import NocTopology.NOCDirections.IPoint;

import java.util.Vector;
import java.util.stream.Collectors;

public class ProcessingElement extends DEVSAtomic {

	private static final NOC.NodeType NODETYPE = NOC.NodeType.PE;

	Vector<Port> inputPorts;
	Vector<Port> outputPorts;

	public ProcessingElement(IPoint coordinate, Vector<String> inputPortsNames, Vector<String> outputPortsNames) {

		this.name = NODETYPE + "[" + coordinate.toString().trim() + "]";

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

	private enum STATE{ WAITING, PROCESSING, SENDING}
    private STATE state;
    private Packet currentPacket;
    private float rho;

	@Override
	public void init() {
	    state = STATE.WAITING;
    }

	@Override
	public void deltaExt(Port port, Object o, float v) {
		switch ( state ) {
            case WAITING: {
                 currentPacket = (Packet) o;
                    System.err.println( " ERROR RECEIVED PACKET " + o);

                state = STATE.PROCESSING;
            } break;

            case PROCESSING: {
                System.err.println( " ERROR RECEIVED PACKET " + o);
            } break;

            case SENDING: {
                System.err.println( " ERROR RECEIVED PACKET " + o);
            } break;
        }
	}

	@Override
	public void deltaInt() {
        switch ( state ) {
            case WAITING: {
            } break;

            case PROCESSING: {
                state = STATE.PROCESSING;
            } break;

            case SENDING: {
                state = STATE.WAITING;
                currentPacket = null;
            } break;
        }
    }

	@Override
	public Object[] lambda() {

        Object[] output = null;

        switch ( state ) {
            case WAITING: {
            }
            break;

            case PROCESSING: {
            }
            break;

            case SENDING: {
                output = new Object[2];
                output[0] = this.getOutPorts().firstElement();
                output[1] = currentPacket;
            }
            break;
        }

        return output;
	}

	@Override
	public float getDuration() {
        switch ( state ) {
            case WAITING:       rho = Float.POSITIVE_INFINITY; break;
            case PROCESSING:    rho = 2; break;
            case SENDING:       rho = 0; break;
        }

        return rho;
	}

    public String toString() {
        String inports = "";
        String outports = "";

        int i;
        for(i = 0; i < this.inPorts.size(); ++i) {
            inports = inports + (this.inPorts.get(i)).getName() + "-";
        }

        for(i = 0; i < this.outPorts.size(); ++i) {
            outports = outports + (this.outPorts.get(i)).getName() + "-";
        }

        return this.getClass().toString() + '@' + Integer.toHexString(this.hashCode()) + " Inports: " + inports + " Outports: " + outports;
    }
}
