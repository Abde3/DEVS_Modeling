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
    IPoint coordinate;


    public ProcessingElement(IPoint coordinate, Vector<String> inputPortsNames, Vector<String> outputPortsNames, boolean isDefective) {

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

		/** is this PE defective**/
		this.isDefective = isDefective;

		nbReceivedData = 0;
	}

	private enum STATE{ WAITING, PROCESSING, SENDING, SET_BAD_STATUS, ERROR}
    private STATE state;
    private Flit currentData;
    private float rho;
    private boolean isDefective;
    private int nbReceivedData;

	@Override
	public void init() {
	    state = STATE.WAITING;
    }

	@Override
	public void deltaExt(Port port, Object o, float v) {
        LOG.printThis(this.name, "PE : RECEIVED " + o);
		switch ( state ) {
            case WAITING: {

                currentData = ((Flit) o) ;
                nbReceivedData++;

                LOG.logThis(this.name, currentData);
                LOG.printThis(this.name,"(WAITING): RECEIVED DATA " + o);

                if (isDefective && nbReceivedData == Constants.BUFFER_SIZE) {
                    state = STATE.SET_BAD_STATUS;
                } else {
                     state = STATE.PROCESSING;
                }
            } break;

            case PROCESSING: {
                LOG.printThis(this.name,"(PROCESSING): ERROR RECEIVED PACKET " + o);
            } break;

            case SENDING: {
                LOG.printThis(this.name,"(SENDING): ERROR RECEIVED PACKET " + o);
            } break;

            case SET_BAD_STATUS: {
                LOG.printThis(this.name,"(SET_BAD_STATUS): ERROR RECEIVED PACKET " + o);
            } break;

            case ERROR: {
                LOG.printThis(this.name,"(ERROR): ERROR RECEIVED PACKET " + o);
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

            case SET_BAD_STATUS: {
                state = STATE.ERROR;
                LOG.printThis(this.name, "---------- IN ERROR ---------- ");
                currentData = null;
            } break;

            case ERROR: {
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

            case ERROR: {
            }
            break;

            case SET_BAD_STATUS: {

                output = new Object[ 2 ];
                output[0] = this.getOutPort("out_status");
                output[1] = "nok";
            }
            break;
        }

        return output;
	}

	@Override
	public float getDuration() {
        switch ( state ) {
            case WAITING:        rho = Float.POSITIVE_INFINITY; break;
            case PROCESSING:     rho = 0; break;
            case SENDING:        rho = 0; break;
            case SET_BAD_STATUS: rho = 0; break;
            case ERROR:          rho = Float.POSITIVE_INFINITY; break;
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
