package BaseModel;

import DEVSModel.DEVSCoupled;
import DEVSModel.Port;
import Model.NOCModel.NOC;

import java.util.Arrays;
import java.util.Vector;

public class Generator extends DEVSCoupled {


    private static final NOC.NodeType NODETYPE = NOC.NodeType.GENERATOR;

    NetworkInterface networkInterface;
    Generator_Task generatorTask;

    Port dataToSW;
    Port cmdFromSW;


    public Generator(int networkSize, String name, String data, int dest_x, int dest_y) {
        super();

        this.name = NODETYPE.name() + "[" + name.trim() + "]";
        this.networkInterface = new NetworkInterface(name);
        this.generatorTask = new Generator_Task(networkSize, name, data, dest_x, dest_y);

        this.addSubModel(networkInterface);
        this.addSubModel(generatorTask);

        dataToSW   = new Port(this, "dataToSW");
        cmdFromSW  = new Port(this, "cmdFromSW");

        addOutPort( dataToSW );
        addInPort( cmdFromSW );

        addIC( generatorTask.getOutPort("out"), networkInterface.dataFromPE );
        addEOC( networkInterface.dataToSW, dataToSW);

        addEIC( networkInterface.cmdFromSW, cmdFromSW);

        this.setSelectPriority();

    }

    @Override
    public void setSelectPriority() {
        this.selectPriority.put(new Vector<>( Arrays.asList(generatorTask, networkInterface)), networkInterface );
    }
}
