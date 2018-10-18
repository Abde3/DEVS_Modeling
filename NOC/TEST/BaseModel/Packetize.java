package BaseModel;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;

import java.util.Vector;

public class Packetize extends DEVSAtomic {

    private static final int PACKET_SIZE = 64;
    private static final NOC.NodeType NODETYPE = NOC.NodeType.PACKETIZER;


    Port dataPE;
    Port dataSwitch;
    Port commandSwitch;

    boolean outQstatus;
    private int sentFlit;
    private int maxFlit;
    private float rho;


    public Packetize(String name) {
        super();
        this.name = NODETYPE.name() + "[" + name.trim() + "]";

        queue = new Vector<>();

        dataPE = new Port(this, "dataPE");
        dataSwitch = new Port(this, "dataSwitch");
        commandSwitch = new Port(this, "commandSwitch");

        this.addInPort(dataPE);
        this.addOutPort(dataSwitch);
        this.addInPort(commandSwitch);
    }


    private enum STATE{ PASSIVE, PACKETIZE, WAIT4OK, SEND_OUT_FLIT}
    private STATE state;
    private Vector<Packet> queue;

    @Override
    public void init() {
        state = STATE.PASSIVE;
        queue = new Vector<>(5);
        outQstatus = true;
        sentFlit = 0;
    }

    @Override
    public void deltaExt(Port port, Object o, float v) {

        switch (state) {
            case PASSIVE: {
                if ( port.equals(dataPE) &&  queue.size() < PACKET_SIZE / 8) {
                    ;

                } else if ( port.equals(dataPE) &&  queue.size() >= PACKET_SIZE / 8) {
                    state = STATE.PACKETIZE;

                } else if ( port.equals(commandSwitch) ) {
                    outQstatus = (Boolean) o;
                }
            } break;

            case WAIT4OK: {
                if ( port.equals(dataPE) ) { state = STATE.WAIT4OK; }
                else if ( port.equals(commandSwitch) && o.equals("ok")) {
                    outQstatus = true;
                    state = STATE.SEND_OUT_FLIT;
                }

            } break;

            case PACKETIZE: {
                    if ( port.equals(commandSwitch) ) {
                        outQstatus = (Boolean) o;
                    }
            } break;

            case SEND_OUT_FLIT: {
                if ( port.equals(commandSwitch) && o.equals("ok") ) { state = STATE.SEND_OUT_FLIT; }
                if ( port.equals(dataPE) ) { state = STATE.SEND_OUT_FLIT; }
            } break;
        }

    }

    @Override
    public void deltaInt() {
        switch (state) {
            case PASSIVE: {

            } break;

            case WAIT4OK: {

            } break;

            case PACKETIZE: {
                if ( ! outQstatus ) {
                    state = STATE.WAIT4OK;
                } else {
                    state = STATE.SEND_OUT_FLIT;
                }
            } break;

            case SEND_OUT_FLIT: {
                if ( (! outQstatus) && sentFlit < maxFlit) {
                    state = STATE.WAIT4OK;
                } else if ( sentFlit >= maxFlit && queue.size() >= PACKET_SIZE / 8 ) {
                    state = STATE.PACKETIZE;
                } else if ( sentFlit >= maxFlit && queue.size() < PACKET_SIZE / 8 ) {
                    state = STATE.PASSIVE;
                } else if ( outQstatus && sentFlit < maxFlit ) {
                    ;
                }
            } break;
        }
    }

    @Override
    public Object[] lambda() {
        return null;
    }

    @Override
    public float getDuration() {
        switch (state)  {
            case WAIT4OK: rho =  Float.POSITIVE_INFINITY;
            case SEND_OUT_FLIT: rho =  1;
            case PASSIVE: rho =  Float.POSITIVE_INFINITY;
            case PACKETIZE: rho = 10;
        }

        return rho;
    }
}
