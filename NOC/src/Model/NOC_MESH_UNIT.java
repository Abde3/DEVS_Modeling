package Model;

import DEVSModel.Port;

public class NOC_MESH_UNIT extends NOC_Unit {

    public NOC_MESH_UNIT(NodeCoordinate coordinate) {
        super(coordinate);

        queueSwitch = new QueueSwitch("QS", coordinate, 2);
        processingElement = new ProcessingElement("PE", coordinate, 1);

        this.getSubModels().add(queueSwitch);
        this.getSubModels().add(processingElement);


        int limit = -1;

        if (node_position == NOC_Unit_factory.NODE_POSITION.CENTER) {
            limit = 4;
        } else if (node_position == NOC_Unit_factory.NODE_POSITION.EDGE) {
            limit = 3;
        } else if (node_position == NOC_Unit_factory.NODE_POSITION.CORNER) {
            limit = 2;
        } else {
            assert false;
            return;
        }


        for (int i = 0; i < limit; i++) {
            v_out_ports.add(new Port(this, "out_NCUnit-"+i));
            this.addOutPort(v_out_ports.get(i));
        }

        for (int i = 0; i < limit; i++) {
            v_in_ports.add(new Port(this, "in_NCUnit-"+i));
            this.addInPort(v_in_ports.get(i));
        }

        for (int i = 0; i < limit; i++) {
            this.addEIC(this.getInPort("in_NCUnit-"+i), this.getQueueSwitch().getInPort("in_queue-"+i));
        }

        for (int i = 0; i < limit; i++) {
            this.addEOC(this.getQueueSwitch().getOutPort("out_queue-"+i), this.getOutPort("out_NCUnit-"+i));
        }

        this.addIC(this.getProcessingElement().getOutPort("out_cmd"), this.getQueueSwitch().getInPort("in_PE"));
        this.addIC(this.getQueueSwitch().getOutPort("out_PE"), this.getProcessingElement().getInPort("in_task"));
        this.addIC(this.getProcessingElement().getOutPort("out_queue"), this.getQueueSwitch().getInPort("in_queue-0"));

        this.setSelectPriority();
    }


}
