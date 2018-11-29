package Model.NOCModel;


import Library.DEVSModel.DEVSCoupled;
import Library.DEVSModel.DEVSModel;
import Model.Routing.NocRoutingPolicy;
import Model.NOCUnit.NOCUnitDirector;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;
import NocTopology.NocTopology;
import Util.NocUtil;


import java.util.*;

import static Util.NocUtil.portsNameToDataType;


public abstract class NOC extends DEVSCoupled {


    public enum NodeType {NOC, NODE, QUEUE, SWITCH, PE, NETWORK_INTERFACE, PACKETIZER, DEPACKETIZER}


    protected final NodeType nodeType;                       /***** Represent the type of the element ************/
    protected final HashMap<IPoint, DEVSModel> generators;   /***** Represent all the generators of the model ****/
    protected final NocTopology topology;                    /***** Represent the topology of the model **********/
    protected final NocRoutingPolicy routingPolicy;          /***** Represent routing policy applied *************/
    protected final int numberOfVirtualChannel;              /***** Represent the number of virtual channel ******/



    /********************************************* GETTERS AND SETTERS ************************************************/


    protected NOC(NocTopology topology, NocRoutingPolicy routingPolicy, HashMap<IPoint,DEVSModel> generators ) {
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
                    topology.getNocNetwork().getUnitAt( coordinate ),
                    devsModel
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

    protected void addGenerator(DEVSModel unit, DEVSModel generator) {
        addSubModel(generator);
        addIC(generator.getOutPorts().firstElement(), unit.getInPorts().firstElement());
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

    public void print_promela_channels() {
        topology.getNocNetwork().getAllUnits().forEach(
            srcModel -> srcModel.getOutPorts().forEach(
                port -> {
                    System.out.println(
                        "chan "
                        + portsNameToDataType(port.getName())
                        + topology.getNocNetwork().getPositionOfUnit(port.getModel()).toSimpleString()
                        + "TO"
                        + topology.getNocNetwork().getPositionOfUnit(topology.getCorrespondingPort(port).getModel()).toSimpleString()
                        + "= [QSZ] of { byte };"
                    );
                }
            )
        );
    }

    public void print_switches() {
        topology.getNocNetwork().forEach(
                unit-> System.out.println(
                        "proctype NODE"
                        + topology.getNocNetwork().getPositionOfUnit(unit).toSimpleString()
                        + "()"
                        + " { \n"
                        + "\tmtype state = CHECK"
                        + "\n}"
                )
        );
    }


}
