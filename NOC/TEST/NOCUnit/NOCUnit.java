package NOCUnit;

import DEVSModel.DEVSCoupled;
import DEVSModel.Port;

import java.util.Collections;
import java.util.Vector;


public class NOCUnit extends DEVSCoupled {

    protected Vector<Port>      v_in_ports;
    protected Vector<Port>      v_out_ports;
    protected QueueSwitch       queueSwitch;
    protected ProcessingElement processingElement;


    private boolean addPort(boolean isInPort, String portName) throws ExistingPortException {

        if (isInPort) {

            if (v_in_ports.stream().anyMatch(port -> port.getName().equals(portName))) {
                throw new ExistingPortException("Trying to add an existing inPort " + portName + " to model : " + this.name) ;
            }

            Port inPort = new Port(this, portName);
            boolean isAdded = v_in_ports.add(inPort);
            this.addInPort(inPort);

            return (this.getInPort(portName) != null && isAdded);

        } else {

            if (v_out_ports.stream().anyMatch(port -> port.getName().equals(portName))) {
                throw new ExistingPortException("Trying to add an existing outPort " + portName + " to model : " + this.name) ;
            }

            Port outPort = new Port(this, portName);
            boolean isAdded = v_out_ports.add(outPort);
            this.addOutPort(outPort);

            return (this.getOutPort(portName) != null && isAdded);

        }

    }


    protected boolean addOutPort(String portName) throws ExistingPortException {
        return addPort(false, portName);
    }


    protected boolean addInPort(String portName) throws ExistingPortException {
        return  addPort(true, portName);
    }


    @Override
    public void setSelectPriority() {

    }


    public class ExistingPortException extends Throwable {
        public ExistingPortException(String message) {
            super(message);
        }
    }
}
