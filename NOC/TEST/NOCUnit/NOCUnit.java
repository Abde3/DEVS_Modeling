package NOCUnit;

import BaseModel.ProcessingElement;
import BaseModel.Queue;
import BaseModel.Switch;
import DEVSModel.DEVSCoupled;
import DEVSModel.Port;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


public class NOCUnit extends DEVSCoupled {

    protected final Set<Port>         v_in_ports;
    protected final Set<Port>         v_out_ports;
    protected final Set<Queue>     v_in_queue;
    protected final Set<Queue>     v_out_queue;
    protected final Switch            aSwitch;
    protected final ProcessingElement aProcessingElement;


    public NOCUnit(Set<String> v_in_ports_names, Set<String> v_out_ports_names,
                   Set<Queue> v_in_queue,        Set<Queue> v_out_queue,
                   Switch aSwitch,                  ProcessingElement aProcessingElement)
    {

        super();

        this.v_in_ports = new HashSet<>();
        this.v_out_ports = new HashSet<>();

        this.v_in_queue = v_in_queue;
        this.v_out_queue = v_out_queue;
        this.aSwitch = aSwitch;
        this.aProcessingElement = aProcessingElement;


        /** Add the input/output ports to the model */
        for (String inPortName  : v_in_ports_names  ) { addInPort(inPortName); }
        for (String outPortName : v_out_ports_names ) { addOutPort(outPortName); }

        /** Add the input queues as submodels */
        for (Queue inQueue : v_in_queue) { this.addSubModel(inQueue); }

        /** Add the output queues as submodels */
        for (Queue outQueue : v_out_queue) { this.addSubModel(outQueue); }

        /** Add the switch as a submodel */
        this.addSubModel(aSwitch);

        /** Add the processing element as a submodel */
        this.addSubModel(aProcessingElement);

    }



    private boolean addPort(boolean isInPort, String portName)  {

        if (isInPort) {

            Port inPort = new Port(this, portName);
            boolean isAdded = v_in_ports.add(inPort);
            this.addInPort(inPort);

            return (this.getInPort(portName) != null && isAdded);

        } else {

            Port outPort = new Port(this, portName);
            boolean isAdded = v_out_ports.add(outPort);
            this.addOutPort(outPort);

            return (this.getOutPort(portName) != null && isAdded);

        }

    }


    private boolean addOutPort(String portName) {
        return addPort(false, portName);
    }


    private boolean addInPort(String portName) {
        return  addPort(true, portName);
    }



    @Override
    public void setSelectPriority() {

    }


    public static class ExistingPortException extends Throwable {
        public ExistingPortException(String message) {
            super(message);
        }
    }
}
