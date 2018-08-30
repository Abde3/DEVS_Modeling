package Model;

import DEVSModel.Port;

public class NOC_MESH_UNIT extends NOC_Unit {

    public NOC_MESH_UNIT(NodeCoordinate coordinate) {
        super(coordinate);

        queueSwitch = new QueueSwitch("QS", coordinate, 2);
        processingElement = new ProcessingElement("PE", coordinate);

        this.getSubModels().add(queueSwitch);
        this.getSubModels().add(processingElement);


        for ( NOC_MESH.DIRECTION direction : NOC_Unit_factory.getAlldirectionsforNode(coordinate, 4, NOC_factory.Topology.MESH)) {
            Port outPort = new Port(this, "out_NCUnit-" + direction);
            v_out_ports.add(outPort);
            this.addOutPort(outPort);

            Port inPort = new Port(this, "in_NCUnit-" + direction);
            v_in_ports.add(inPort);
            this.addInPort(inPort);
        }




        for ( NOC_MESH.DIRECTION direction : NOC_Unit_factory.getAlldirectionsforNode(coordinate, 4, NOC_factory.Topology.MESH)) {
            this.addEIC(this.getInPort("in_NCUnit-"+direction), this.getQueueSwitch().getInPort("in_queue-"+direction));
            this.addEOC(this.getQueueSwitch().getOutPort("out_queue-"+direction), this.getOutPort("out_NCUnit-"+direction));
        }


        // STARTING NODE ADD INPUT FOR GENERATOR
        if(coordinate.getX() == 0 && coordinate.getY() ==0) {
            Port inPort = new Port(this, "in_NCUnit-" + NOC_MESH.DIRECTION.WEST);
            v_in_ports.add(inPort);
            this.addInPort(inPort);

            this.addEIC(this.getInPort("in_NCUnit-"+NOC_MESH.DIRECTION.WEST), this.getQueueSwitch().getInPort("in_queue-"+NOC_MESH.DIRECTION.WEST));

        }


        this.addIC(this.getProcessingElement().getPortOut_cmd(), this.getQueueSwitch().getInPort("in_PE"));
        this.addIC(this.getQueueSwitch().getOutPort("out_PE"), this.getProcessingElement().getPortIn_task());
        this.addIC(this.getProcessingElement().getPortOut_queue(), this.getQueueSwitch().getInPort("in_task_PE"));

        this.setSelectPriority();
    }


}
