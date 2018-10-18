package NOCUnit;

import BaseModel.NetworkInterface;
import BaseModel.ProcessingElement;
import BaseModel.Switch;
import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;
import DEVSModel.Port;
import Model.NOCModel.NOC;
import Model.NOCUnit.Type;
import NocTopology.NOCDirections.IPoint;
import Util.NocUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;


public class NOCUnit extends DEVSCoupled {

    static NOC.NodeType NODETYPE = NOC.NodeType.NODE;

    protected final HashMap<Port, Type> v_in_ports;
    protected final HashMap<Port,Type> v_out_ports;
    protected final Switch             aSwitch;
    protected final ProcessingElement  aProcessingElement;
    protected final NetworkInterface aNetworkInterface;


    public NOCUnit(IPoint coordinate,
                   Set<String> v_in_data_ports_names,
                   Set<String> v_out_data_ports_names,
                   Set<String> v_in_cmd_ports_names,
                   Set<String> v_out_cmd_ports_names,
                   NetworkInterface aNetworkInterface,
                   Switch aSwitch,
                   ProcessingElement aProcessingElement)
    {

        super();

        this.name = NODETYPE + "[" + coordinate.toString().trim() + "]";

        this.v_in_ports = new HashMap<>();
        this.v_out_ports = new HashMap<>();

        this.aSwitch = aSwitch;
        this.aNetworkInterface = aNetworkInterface;
        this.aProcessingElement = aProcessingElement;


        /** Add the input/output ports to the model */
        for (String inPortName  : v_in_data_ports_names  ) {
            addDataInPort(inPortName);
        }

        for (String outPortName : v_out_data_ports_names ) {
            addDataOutPort(outPortName);
        }

        for (String inPortName  : v_in_cmd_ports_names  ) {
            addCmdInPort(inPortName);
        }

        for (String outPortName : v_out_cmd_ports_names ) {
            addCmdOutPort(outPortName);
        }


        /** Add the switch as a submodel */
        this.addSubModel(aSwitch);

        /** Add the processing element as a submodel */
        this.addSubModel(aProcessingElement);

        /** Add the network interface element as a submodel */
        this.addSubModel(aNetworkInterface);

        buildEIC();
        buildEOC();
        buildIC();

        this.setSelectPriority();

    }

    private void buildIC() {

        addIC(aSwitch.getOutPort("PE"), aNetworkInterface.getInPort("dataFromSW"));
        addIC(aNetworkInterface.getOutPort("dataToPE"), aProcessingElement.getInPort("in"));
        addIC(aProcessingElement.getOutPort("out"), aNetworkInterface.getInPort("dataFromPE"));
        addIC(aNetworkInterface.getOutPort("dataToSW"), aSwitch.getInPort("PE"));
        addIC(aSwitch.getOutPort("cmdToPE"), aNetworkInterface.getInPort("cmdFromSW"));

    }


    private void buildEIC() {

        this.getInPorts().stream().forEach(
                unitPort -> aSwitch.getInPorts().stream().filter(
                        switchPort -> switchPort.getName().equals(unitPort.getName())
                ).forEach(
                        correspondingPort -> addEIC( unitPort, correspondingPort )
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



    private boolean addPort(boolean isInPort, String portName, Type dataType)  {


        if (isInPort) {

            Port inPort = new Port(this, portName );
            boolean isAdded = ( v_in_ports.put(inPort, dataType) == null);
            this.addInPort(inPort);

            return (this.getInPort(name) != null && isAdded);

        } else {

            Port outPort =  new Port(this, portName);
            boolean isAdded = ( v_out_ports.put(outPort, dataType) == null);
            this.addOutPort(outPort);

            return (this.getOutPort(name) != null && isAdded);

        }

    }

    private boolean addDataOutPort(String portName) {
        boolean isAdded = addPort(false, portName, Type.DATA);
        return isAdded;
    }

    private boolean addDataInPort(String portName) {
        boolean isAdded = addPort(true, portName, Type.DATA);
        return isAdded;
    }

    private boolean addCmdOutPort(String portName) {
        boolean isAdded = addPort(false, portName, Type.COMMAND);
        return isAdded;
    }

    private boolean addCmdInPort(String portName) {
        boolean isAdded = addPort(true, portName, Type.COMMAND);
        return isAdded;
    }


    public List<Port> getInDataPorts() {
        return v_in_ports.entrySet().stream().filter(
                entry ->  entry.getValue() == Type.DATA
        ).map( entry -> entry.getKey() ).collect( Collectors.toList() );
    }

    public List<Port> getOutDataPorts() {
        return v_out_ports.entrySet().stream().filter(
                entry ->  entry.getValue() == Type.DATA
        ).map( entry -> entry.getKey() ).collect( Collectors.toList() );
    }

    public List<Port> getInCmdPorts() {
        return v_in_ports.entrySet().stream().filter(
                entry ->  entry.getValue() == Type.COMMAND
        ).map( entry -> entry.getKey() ).collect( Collectors.toList() );
    }

    public List<Port> getOutCmdPorts() {
        return v_out_ports.entrySet().stream().filter(
                entry ->  entry.getValue() == Type.COMMAND
        ).map( entry -> entry.getKey() ).collect( Collectors.toList() );
    }


    @Override
    public void setSelectPriority() {

        NocUtil.combinationsNoDupl( getSubModels() ).forEach(
                sameSizeLists -> sameSizeLists.forEach(
                        devsModels -> this.selectPriority.put(
                                new Vector<DEVSModel>( devsModels ), devsModels.get( devsModels.size() - 1 )
                        )
                )
        );

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
