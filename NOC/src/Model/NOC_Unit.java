package Model;

import DEVSModel.DEVSCoupled;
import DEVSModel.Port;

import java.util.Vector;

public abstract class NOC_Unit extends DEVSCoupled {

    protected Vector<Port> v_in_ports;
    protected Vector<Port> v_out_ports;
    protected QueueSwitch queueSwitch;
    protected ProcessingElement processingElement;
    protected NOC.NODE_TYPE node_type;
    protected NOC_Unit_factory.NODE_POSITION node_position;
    protected NodeCoordinate coordinate;

    public NOC_Unit(NodeCoordinate coordinate) {
        super();
        this.coordinate = coordinate;

        node_type = NOC.NODE_TYPE.NODE;
        node_position = Util.getNodePosition(coordinate, 4, NOC_factory.Topology.MESH);
        name = node_type.toString() + coordinate ;

        v_in_ports = new Vector<>();
        v_out_ports = new Vector<>();
    }

    public QueueSwitch getQueueSwitch() {
        return queueSwitch;
    }

    public ProcessingElement getProcessingElement() {
        return processingElement;
    }


    public Vector<Port> get_in_ports() {
        return v_in_ports;
    }

    public Vector<Port> get_out_ports() {
        return v_out_ports;
    }

    public NOC.NODE_TYPE getNode_type() {
        return node_type;
    }

    public NOC_Unit_factory.NODE_POSITION getNode_position() {
        return node_position;
    }

    public NodeCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setSelectPriority() {

    }


}
