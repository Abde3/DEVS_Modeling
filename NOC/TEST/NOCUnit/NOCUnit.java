package NOCUnit;

import BaseModel.ProcessingElement;
import BaseModel.Queue;
import BaseModel.Switch;
import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;
import DEVSModel.Port;
import Model.NOCModel.NOC;
import NocTopology.NOCDirections.IPoint;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class NOCUnit extends DEVSCoupled {

    static NOC.NodeType NODETYPE = NOC.NodeType.NODE;

    protected final Set<Port>         v_in_ports;
    protected final Set<Port>         v_out_ports;
    protected final Set<Queue>        v_in_queue;
    protected final Set<Queue>        v_out_queue;
    protected final Switch            aSwitch;
    protected final ProcessingElement aProcessingElement;


    public NOCUnit(IPoint coordinate, Set<String> v_in_ports_names, Set<String> v_out_ports_names,
                   Set<Queue> v_in_queue, Set<Queue> v_out_queue,
                   Switch aSwitch, ProcessingElement aProcessingElement)
    {

        super();

        this.name = NODETYPE + "[" + coordinate.toString().trim() + "]";

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

        buildEIC();
        buildEOC();
        buildIC();
    }

    private void buildIC() {

        this.aSwitch.getInPorts().stream().forEach(
                switchPort -> v_in_queue.stream().forEach(
                        queue -> queue.getOutPorts().stream().filter(
                                queuePort -> queue.getName().contains(switchPort.getName())
                        ).forEach(
                                correspondingPort -> addIC(correspondingPort, switchPort)
                        )
                )
        );

        addIC(aSwitch.getOutPort("PE"), aProcessingElement.getInPort("in"));
        addIC(aProcessingElement.getOutPort("out"), aSwitch.getInPort("PE"));
    }


    private void buildEIC() {

        this.getInPorts().stream().forEach(
                unitPort -> v_in_queue.stream().forEach(
                        queue -> queue.getInPorts().stream().filter(
                                queuePort -> queue.getName().contains(unitPort.getName())
                        ).forEach( correspondingPort -> addEIC(unitPort, correspondingPort))
                )
        );

    }


    private void buildEOC() {

        this.getOutPorts().stream().forEach(
                unitPort -> aSwitch.getOutPorts().stream().filter(
                        switchPort -> switchPort.getName().contains(unitPort.getName())
                ).forEach(
                        correspondingPort -> addEOC( correspondingPort, unitPort )
                )
        );

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


    public String toString() {
        String CoupledModelString = "{ "
                + subModels.stream().map( DEVSModel::getName ).collect( Collectors.joining( " ; " ))
                + " }\n";

        return this.getName() + " == " + CoupledModelString + "\n Inports:" + this.inPorts.toString() + "\n Outports:" + this.outPorts + "\n EIC: " + this.getEIC().toString() + "\n EOC: " + this.getEOC().toString() + "\n IC: " + this.getIC().toString();
    }

    public static class ExistingPortException extends Throwable {
        public ExistingPortException(String message) {
            super(message);
        }
    }
}
