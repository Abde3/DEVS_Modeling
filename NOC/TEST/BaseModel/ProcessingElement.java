package BaseModel;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;
import NocTopology.NOCDirections.IPoint;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;

public class ProcessingElement extends DEVSAtomic {

    private static final NOC.NodeType NODETYPE = NOC.NodeType.PE;

	Vector<Port> inputPorts;
	Vector<Port> outputPorts;
    IPoint coordinate;

	public ProcessingElement(IPoint coordinate, Vector<String> inputPortsNames, Vector<String> outputPortsNames) {

		this.name = NODETYPE + "[" + coordinate.toString().trim() + "]";
        this.coordinate = coordinate;

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
    private Flit currentData;
    private float rho;

	@Override
	public void init() {
	    state = STATE.WAITING;
    }

	@Override
	public void deltaExt(Port port, Object o, float v) {
		switch ( state ) {
            case WAITING: {
                 currentData = ((Flit) o) ;

                 LOG.logThis(this.name, currentData);

                 LOG.printThis(this.name,"(WAITING): RECEIVED DATA " + o);

                 state = STATE.PROCESSING;
            } break;

            case PROCESSING: {
                LOG.printThis(this.name,"(PROCESSING): ERROR RECEIVED PACKET " + o);
            } break;

            case SENDING: {
                LOG.printThis(this.name,"(SENDING): ERROR RECEIVED PACKET " + o);
            } break;
        }
	}

	@Override
	public void deltaInt() {
        switch ( state ) {
            case WAITING: {
            } break;

            case PROCESSING: {
                state = STATE.SENDING;
            } break;

            case SENDING: {
                state = STATE.WAITING;
                currentData = null;
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

            }
            break;
        }

        return output;
	}

	@Override
	public float getDuration() {
        switch ( state ) {
            case WAITING:       rho = Float.POSITIVE_INFINITY; break;
            case PROCESSING:    rho = 0; break;
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
