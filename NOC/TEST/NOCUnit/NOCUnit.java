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
import NocTopology.NocTopology;
import Util.NocUtil;

import java.util.*;
import java.util.stream.Collectors;

import static Util.NocUtil.portsNameToDataType;


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

        // SWITCH > NI > PE

        this.selectPriority.put(new Vector<>( Arrays.asList(aSwitch, aNetworkInterface, aProcessingElement)), aSwitch );
        this.selectPriority.put(new Vector<>( Arrays.asList(aSwitch, aNetworkInterface)), aSwitch );
        this.selectPriority.put(new Vector<>( Arrays.asList(aSwitch, aProcessingElement)), aSwitch );
        this.selectPriority.put(new Vector<>( Arrays.asList(aNetworkInterface, aProcessingElement)), aNetworkInterface );


    }

    public String toString() {
        String CoupledModelString = "{ "
                + subModels.stream().map( DEVSModel::getName ).collect( Collectors.joining( " ; " ))
                + " }\n";

        return this.getName() + " == " + CoupledModelString + "\n Inports:" + this.inPorts.toString() + "\n Outports:" + this.outPorts + "\n EIC: " + this.getEIC().toString() + "\n EOC: " + this.getEOC().toString() + "\n IC: " + this.getIC().toString();
    }

    private int getValueOfCoordinate(IPoint coordinate, int networkSize) {
        return networkSize * coordinate.getValueOnAxis("y")  + coordinate.getValueOnAxis("x");
    }

    public String printAlgo(int networksize) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "\t\td_step {\t\n" +
                "\t\t\tif\n" +
                "\t\t\t:: (curz % "+ networksize +") > (flit.dest % "+ networksize +") -> {cur_out = 3};\n" +
                "\t\t\t:: (curz % "+ networksize +") < (flit.dest % "+ networksize +") -> {cur_out = 2};\n" +
                "\t\t\t:: (curz % "+ networksize +")  == (flit.dest % "+ networksize +") -> \n" +
                "\t\t\t\t\tif\n" +
                "\t\t\t\t\t:: (curz / "+ networksize +") > (flit.dest / "+ networksize +") -> {cur_out = 0};\n" +
                "\t\t\t\t\t:: (curz / "+ networksize +") < (flit.dest / "+ networksize +") -> {cur_out = 1};\n" +
                "\t\t\t\t\t:: (curz / "+ networksize +") == (flit.dest / "+ networksize +") -> { cur_out = 4}\n" +
                "\t\t\t\t\tfi\n" +
                "\t\t\tfi\n" +
                "\t\t}"
        );
        return sb.toString();
    }

    public String getPromelaModel(NocTopology nocTopology, int id, int networkSize, boolean faultyNode) {
        StringBuilder sb = new StringBuilder();

        Optional<Port> EastPort  = this.getOutDataPorts().stream().filter(port -> port.getName().contains("EAST")).findFirst();
        Optional<Port> WestPort  = this.getOutDataPorts().stream().filter(port -> port.getName().contains("WEST")).findFirst();
        Optional<Port> NorthPort = this.getOutDataPorts().stream().filter(port -> port.getName().contains("NORT")).findFirst();
        Optional<Port> SouthPort  = this.getOutDataPorts().stream().filter(port -> port.getName().contains("SOUTH")).findFirst();

        String eastString = "";
        if (EastPort.isPresent()) {
            int from = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit(EastPort.get().getModel()), networkSize );
            int to   = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( nocTopology.getCorrespondingPort(EastPort.get()).getModel()), networkSize);
            eastString = "C" + from + "TO" + to;
        } else {
            eastString = "DUMMY";
        }

        String westString = "";
        if (WestPort.isPresent()) {
            int from = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit(WestPort.get().getModel()) ,networkSize);
            int to   = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( nocTopology.getCorrespondingPort(WestPort.get()).getModel()) ,networkSize);
            westString = "C" + from + "TO" + to;
        } else {
            westString = "DUMMY";
        }


        String northString = "";
        if (NorthPort.isPresent()) {
            int from = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( NorthPort.get().getModel()) , networkSize);
            int to   = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( nocTopology.getCorrespondingPort(NorthPort.get()).getModel()) , networkSize);
            northString = "C" + from + "TO" + to;
        } else {
            northString = "DUMMY";
        }

        String southString = "";
        if (SouthPort.isPresent()) {
            int from = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( SouthPort.get().getModel()) ,networkSize);
            int to   = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( nocTopology.getCorrespondingPort(SouthPort.get()).getModel()),networkSize);
            southString = "C" + from + "TO" + to;
        } else {
            southString = "DUMMY";
        }

        String localString = "LOCAL"+ id ;


        String routeStr = "route("+ id + ", flit, " + northString +  ", " +  southString + ", " + eastString +  ", " + westString + ", LOCAL"+id+");" ;
        int nbInputChannel = this.getInDataPorts().size() ;


        sb.append(
                "\t\tshort curz="+ id +";\n" +
                "\t\tmsg_flit flit;\n" +
                "\t\tshort cur_id = 0;\n" +
                "\t\tshort cur_out = 0;\n" +
                "\t\tshort OUTchannels[" + (nocTopology.getAllDirections().size()+1) +"];\n"
        );

        sb.append(
                "\t\tOUTchannels[0] = " +  northString + ";\n" +
                "\t\tOUTchannels[1] = " +  southString + ";\n" +
                "\t\tOUTchannels[2] = " +  eastString + ";\n" +
                "\t\tOUTchannels[3] = " +  westString + ";\n" +
                "\t\tOUTchannels[4] = " +  localString + ";\n"
        );

        sb.append("\t\tshort INchannels["+ nbInputChannel +"];\n");
        for (int i = 0; i < nbInputChannel; i++) {

            Port portTemp  = this.getInDataPorts().get(i);
            if (portTemp.getName().contains("PE")) continue;

            int to = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( portTemp.getModel()),networkSize );
            int from   = getValueOfCoordinate( nocTopology.getNocNetwork().getPositionOfUnit( nocTopology.getCorrespondingPort(portTemp).getModel()),networkSize);
            String tempStr = "C" + from + "TO" + to;

            sb.append(
                    "\t\tINchannels["+ i +"] = " + tempStr + ";\n"
            );
        }

        sb.append("\t\tinit_ok == true;\n\n");

        sb.append(
                "\t\tEND: CHECK:\n" +
                "\t\tcur_id=(cur_id+1)%"+ nbInputChannel + ";\n"+
                "\t\tif\n" +
                "\t\t:: empty(DATA[INchannels[cur_id]]) -> goto CHECK;\n" +
                "\t\t:: nempty(DATA[INchannels[cur_id]]) -> goto ROUTE;\n" +
                "\t\tfi\n" +
                "\n\t\tROUTE:\n" +
                "\t\tDATA[INchannels[cur_id]]?<flit>;\n" +
                "\t\tassert (flit.type == head);\n"
                + printAlgo(networkSize) +
                "\n\t\tif\n" +
                "\t\t:: full(DATA[OUTchannels[cur_out]]) -> goto CHECK;\n" +
                "\t\t:: nfull(DATA[OUTchannels[cur_out]]) -> goto SEND_OUT;\n" +
                "\t\tfi\n" +
                "\t\tSEND_OUT:\n" +
                "\t\tnfull(DATA[OUTchannels[cur_out]]) ; \n"
        );

        if (faultyNode) {
            sb.append(
                    "\t\tatomic {\n" +
                    "\t\t\tDATA[INchannels[cur_id]]?flit ; \n" +
                    "\t\t\tDATA[OUTchannels[cur_out]]!flit;\n" +
                    "\t\t}\n"
            );
        } else {
            sb.append(
                    "\t\tatomic {\n" +
                            "\t\t\tDATA[INchannels[cur_id]]?flit ; \n" +
                            "\t\t\tif \n" +
                            "\t\t\t:: cur_out != 4 -> DATA[OUTchannels[cur_out]]!flit;\n" +
                            "\t\t\t:: cur_out == 4 -> skip;\n" +
                            "\t\t\tfi\n" +
                            "\t\t}\n"
            );
        }

        sb.append(
                "\t\tif\n" +
                "\t\t::  (flit.type == tail) -> goto CHECK;\n" +
                "\t\t::  (flit.type != tail)  -> goto SEND_OUT;\n" +
                "\t\tfi" +
                "\n"
        );

        return sb.toString();
    }

    public static class ExistingPortException extends Throwable {
        public ExistingPortException(String message) {
            super(message);
        }
    }
}
