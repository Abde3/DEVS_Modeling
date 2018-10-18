package BaseModel;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;

import java.util.Vector;

public class Depacketize extends DEVSAtomic {

    private static final NOC.NodeType NODETYPE = NOC.NodeType.DEPACKETIZER;
    private static final int DATA_SIZE = 64;

    Port dataPE; // ToPE
    Port dataSwitch; // fromSwitch
    Port commandSwitch;



    Packet outDataBuffer;
    boolean outQstatus;
    private int sentFlit;
    private int maxFlit;

    public Depacketize(String name) {
        super();
        this.name = NODETYPE.name() + "[" + name.trim() + "]";

        incomingFlitQueue = new Vector<>();

        dataPE = new Port(this, "dataPE");
        dataSwitch = new Port(this, "dataSwitch");
        commandSwitch = new Port(this, "commandSwitch");
    }

    private enum STATE{ IDLE, DEPACKETIZE, TRANSMISSION}
    private STATE state;
    private Vector<Flit> incomingFlitQueue;

    @Override
    public void init() {
        state = STATE.IDLE;
        outQstatus = true;
        sentFlit = 0;
        outDataBuffer = null;
    }

    @Override
    public void deltaExt(Port port, Object o, float v) {

        switch (state) {
            case IDLE: {
                if ( port.equals(dataSwitch) ) {
                    state = STATE.DEPACKETIZE;
                }
            } break;

            case DEPACKETIZE: {
                    if ( ! incomingFlitQueue.isEmpty() ) {
                        state = STATE.DEPACKETIZE;
                    } else if (  outDataBuffer != null) {
                        state = STATE.TRANSMISSION;
                    }
            } break;

            case TRANSMISSION: {
                if ( outDataBuffer != null) {
                    state = STATE.TRANSMISSION;
                }
            } break;
        }

    }

    @Override
    public void deltaInt() {
        switch (state) {
            case IDLE: {

            } break;

            case DEPACKETIZE: {
                if ( ! incomingFlitQueue.isEmpty() ) {
                    state = STATE.DEPACKETIZE;
                    outDataBuffer = null;
                } else if (  outDataBuffer != null) {
                    state = STATE.TRANSMISSION;
                }
            } break;

            case TRANSMISSION: {
                if ( outDataBuffer != null) {
                    state = STATE.TRANSMISSION;
                }
            } break;
        }
    }

    @Override
    public Object[] lambda() {

        Object[] output = null;

        switch (state) {
            case TRANSMISSION: {
                output = new Object[ 2 ];
                output[0] = dataPE;
                output[1] = outDataBuffer;
            }
        }

        return output;
    }

    @Override
    public float getDuration() {
        return 2;
    }
}
