package Model.NOCModel;


import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;
import DEVSModel.Port;
import Model.NOCUnit.Type;
import Model.Routing.NocRoutingPolicy;
import Model.NOCUnit.NOCUnitDirector;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;
import Util.NocUtil;
import javafx.util.Pair;


import java.util.*;
import java.util.stream.Collectors;


public abstract class NOC extends DEVSCoupled {


    public enum NodeType {NOC, NODE, QUEUE, SWITCH, PE, NETWORK_INTERFACE, PACKETIZER, DEPACKETIZER, GENERATOR}


    protected final NodeType nodeType;                       /***** Represent the type of the element ************/
    protected final HashMap<AbstractMap.SimpleEntry<IPoint,String>, DEVSModel> generators;   /***** Represent all the generators of the model ****/
    protected final NocTopology topology;                    /***** Represent the topology of the model **********/
    protected final NocRoutingPolicy routingPolicy;          /***** Represent routing policy applied *************/
    protected final int numberOfVirtualChannel;              /***** Represent the number of virtual channel ******/



    /********************************************* GETTERS AND SETTERS ************************************************/


    protected NOC(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<AbstractMap.SimpleEntry<IPoint,String>,DEVSModel> generators ) {
        super();

        this.nodeType = NodeType.NOC;
        this.routingPolicy = routingPolicy;
        this.topology = topology;
        this.generators = generators;
        this.numberOfVirtualChannel = 1;

        buildNetwork();

        this.setSelectPriority();
    }

    protected void buildNetwork() {

        NOCUnitDirector nocUnitDirector = new NOCUnitDirector(topology, routingPolicy);

        Collection<IPoint> positions = topology.getNocNetwork().getAllPositions();

        positions.stream().forEach(
                point -> topology.getNocNetwork().addUnitAt( nocUnitDirector.buildNocUnit(point), point )
        );

        generators.forEach((coordinate, devsModel) ->
                addGenerator(
                    topology.getNocNetwork().getUnitAt( coordinate.getKey() ),
                    devsModel,
                    coordinate.getValue()
                )
        );

        topology.getNocNetwork().forEach(this::addSubModel);

        buildIC();
    }

    protected void buildIC() {
        topology.getNocNetwork().getAllUnits().stream().forEach(
            srcModel -> srcModel.getOutPorts().forEach(
                port -> {
                    addIC(port , topology.getCorrespondingPort(port));
                }
            )
        );
    }

    protected void addGenerator(DEVSModel unit, DEVSModel generator, String fromDirection) {
        addSubModel(generator);
        addIC(generator.getOutPorts().firstElement(), unit.getInPorts().stream().filter(port -> port.getName().contains(fromDirection)).findFirst().get());

    }

    @Override
    public void setSelectPriority() {

        NocUtil.combinationsNoDupl( getSubModels() ).forEach(
            sameSizeLists -> sameSizeLists.forEach(
                devsModels -> this.selectPriority.put(
                        new Vector<DEVSModel>( devsModels ), devsModels.get( devsModels.size() - 1)
                )
            )
        );

    }

    private int getValueOfCoordinate(IPoint coordinate, int networkSize) {
        return networkSize * coordinate.getValueOnAxis("y")  + coordinate.getValueOnAxis("x");
    }


    public void print_promela_channels(int networkSize) {

        int max = 0;
        Vector<String> channels = new Vector<>();

        System.out.println("#define QSZ 13");

            for (DEVSModel srcModel : topology.getNocNetwork().getAllUnits()) {
                for (Port port : srcModel.getOutPorts()) {
                    if ( NocUtil.portsNameToDataType( port.getName()) != Type.DATA ) continue;
                    int from = getValueOfCoordinate(topology.getNocNetwork().getPositionOfUnit(port.getModel()), networkSize);
                    int to   = getValueOfCoordinate(topology.getNocNetwork().getPositionOfUnit(topology.getCorrespondingPort(port).getModel()), networkSize);
                    channels.add( "#define C" + from + "TO" + to);
                }
            }

            Collections.sort( channels );
            channels.stream().forEachOrdered( s -> System.out.println( s + " " + channels.indexOf(s)) );

            int i = 0;
            for (DEVSModel srcModel : topology.getNocNetwork().getAllUnits()) {
                System.out.println("#define LOCAL"+ getValueOfCoordinate( topology.getNocNetwork().getPositionOfUnit(srcModel) , networkSize) + " " + (channels.size() + i) );
                i++;
            }

            System.out.println("#define DUMMY " + (channels.size() + i) );
            i++;

            System.out.println("mtype{ head, payload, tail};");
            System.out.println("typedef msg_flit {\n" +
                    "mtype type;\n" +
                    "\tshort  data;\n" +
                    "\tshort dest\n" +
                    "};");
            System.out.println("chan DATA["+ (channels.size() + i)  +"] = [QSZ] of { msg_flit };\n");
            System.out.println("bool init_ok = false;\n" +
                    "\n" +
                    "byte nb_msg_GEN1 = 0;\n" +
                    "byte nb_msg_GEN2 = 0;\n" +
                    "\n" +
                    "short i = 0;\n" +
                    "short j = 0;");
    }


    public void print_switches(int networkSize) {
        for (DEVSModel unit : topology.getNocNetwork().getAllNodes().stream().sorted(Comparator.comparing(m -> getValueOfCoordinate( topology.getNocNetwork().getPositionOfUnit(m), networkSize ))).collect(Collectors.toList())) {

            int cur_node_id = Integer.parseInt(String.valueOf(getValueOfCoordinate( topology.getNocNetwork().getPositionOfUnit(unit) , networkSize)));

            System.out.println(
                    "active proctype NODE"
                            + getValueOfCoordinate( topology.getNocNetwork().getPositionOfUnit(unit), networkSize )
                            + "()"
                            + " { \n"
                            + ((NOCUnit) unit).getPromelaModel( topology, cur_node_id, networkSize, (cur_node_id==0))
                            + "\n}"
            );
        }



    }


    public void printGenerators1(int networkSize) {

        String s = "";

        s += "active proctype GEN1() {\n" +
                "\t\t\tinit_ok == true;\n" +
                "\t\n" +
                "\t\t\tmsg_flit header_flit; header_flit.type = head;  header_flit.dest = 3;\n" +
                "\t\t\tmsg_flit tail_flit; tail_flit.type = tail; tail_flit.dest = 3;\n" +
                "\t\t\tmsg_flit payload_flit; payload_flit.type = payload; payload_flit.data = 7; payload_flit.dest = 3;\n" +
                "\t\t\t\n" +
                "\t\t\tSENDING:\n" +
                "\t\t\t\n" +
                "\t\t\tdo\n" +
                "\t\t\t:: (i==0) ->DATA[C0TO1]!header_flit; i++;\n" +
                "\t\t\t:: (i> 0 && i<3)  -> DATA[C0TO1]!payload_flit;  i++;\n" +
                "\t\t\t:: (i==3) ->DATA[C0TO1]!tail_flit; i++; break;\n" +
                "\t\t\tod\n" +
                "\t\n" +
                "\t\t\tnb_msg_GEN1++;\n" +
                "\t\n" +
                "\t\t\tif \n" +
                "\t\t\t:: (nb_msg_GEN1 < 10) -> i=0; goto SENDING;\n" +
                "\t\t\t:: (nb_msg_GEN1 == 10) -> skip;\n" +
                "\t\t\tfi\n" +
                "}";

        System.out.println(s);
    }

    public void printGenerators2(int networkSize) {

        String s = "";

        s += "active proctype GEN2() {\n" +
                "\t\t\tinit_ok == true;\n" +
                "\t\n" +
                "\t\t\tmsg_flit header_flit; header_flit.type = head;  header_flit.dest = 0;\n" +
                "\t\t\tmsg_flit tail_flit; tail_flit.type = tail; tail_flit.dest = 0;\n" +
                "\t\t\tmsg_flit payload_flit; payload_flit.type = payload; payload_flit.data = 7; payload_flit.dest = 0;\n" +
                "\t\t\t\n" +
                "\t\t                SENDING:\n" +
                "\t\t\t\n" +
                "\t\t\tdo\n" +
                "\t\t\t:: (j==0) ->DATA[C2TO3]!header_flit; j++;\n" +
                "\t\t\t:: (j> 0 && j<3)  -> DATA[C2TO3]!payload_flit;  j++;\n" +
                "\t\t\t:: (j==3) ->DATA[C2TO3]!tail_flit; j++; break;\n" +
                "\t\t\tod\n" +
                "\t\n" +
                "\t\t\tnb_msg_GEN2++;\n" +
                "\t\n" +
                "\t\t\tif \n" +
                "\t\t\t:: (nb_msg_GEN2 < 10) -> j = 0; goto SENDING;\n" +
                "\t\t\t:: (nb_msg_GEN2 == 10) -> skip;\n" +
                "\t\t\tfi\n" +
                "\t\n" +
                "}";

        System.out.println(s);
    }

    public void print_initializer() {
        String s = "";

        s += "active proctype hh() {\n" +
                "\t\t\t\n" +
                "\t\t/* {\n" +
                "\t\t\tmsg_flit header_flit; header_flit.type = head;  header_flit.dest = 0;\n" +
                "\t\t\tmsg_flit tail_flit; tail_flit.type = tail; tail_flit.dest = 0;\n" +
                "\t\t\tmsg_flit payload_flit; payload_flit.type = payload; payload_flit.data = 7; payload_flit.dest = 0;\n" +
                "\t\t\t\n" +
                "\t\t\tj=0;\n" +
                "\t\t\tSENDING:\n" +
                "\t\t\tdo\n" +
                "\t\t\t:: (j==0) ->DATA[LOCAL0]!header_flit; j++;\n" +
                "\t\t\t:: (j> 0 && j<3)  -> DATA[LOCAL0]!payload_flit;  j++;\n" +
                "\t\t\t:: (j==3) ->DATA[LOCAL0]!tail_flit; j++; break;\n" +
                "\t\t\tod\n" +
                "\t\n" +
                "\t\t\tnb_msg_GEN2++;\n" +
                "\t\n" +
                "\t\t\tif \n" +
                "\t\t\t:: (nb_msg_GEN2 < 3) -> j = 0; goto SENDING;\n" +
                "\t\t\t:: (nb_msg_GEN2 == 3) -> j = 0; skip;\n" +
                "\t\t\tfi\n" +
                "\t\t\t j = 0;\n" +
                "\t\t}*/\n" +
                "\t\t\n" +
                "\t\t\tinit_ok = true;\n" +
                "}";

        System.out.println(s);
    }

}
