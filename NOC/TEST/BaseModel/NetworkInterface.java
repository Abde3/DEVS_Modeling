package BaseModel;

import DEVSModel.DEVSCoupled;
import DEVSModel.Port;
import Model.NOCModel.NOC;

public class NetworkInterface extends DEVSCoupled {


    private static final NOC.NodeType NODETYPE = NOC.NodeType.NETWORK_INTERFACE;


    Packetize packetizer;
    Depacketize depacketizer;

    Port dataFromPE;
    Port dataToPE;
    Port cmdFromPE;
    Port cmdToPE;

    Port dataFromSW;
    Port dataToSW;
    Port cmdFromSW;
    Port cmdToSW;

    public NetworkInterface(String name) {
        super();

        this.name = NODETYPE.name() + "[" + name.trim() + "]";
        this.packetizer = new Packetize(name);
        this.depacketizer = new Depacketize(name);

        this.addSubModel(packetizer);
        this.addSubModel(depacketizer);


        dataFromPE = new Port(this, "dataFromPE");
        dataToPE   = new Port(this, "dataToPE");
        cmdFromPE  = new Port(this, "cmdFromPE");
        cmdToPE    = new Port(this, "cmdToPE");

        dataFromSW = new Port(this, "dataFromSW");
        dataToSW   = new Port(this, "dataToSW");
        cmdFromSW  = new Port(this, "cmdFromSW");
        cmdToSW    = new Port(this, "cmdToSW");


        addInPort( dataFromPE );
        addOutPort( dataToPE );
        addInPort( cmdFromPE );
        addOutPort( cmdToPE );

        addInPort( dataFromSW );
        addOutPort( dataToSW );
        addInPort( cmdFromSW );
        addOutPort( cmdToSW );


        addEIC( dataFromPE, packetizer.dataPE );
        addEIC( dataFromSW, depacketizer.dataSwitch);

        addEIC( cmdFromSW, packetizer.commandSwitch);
        addEOC( dataToPE, depacketizer.dataPE);

        this.setSelectPriority();

    }

    @Override
    public void setSelectPriority() {

    }
}
