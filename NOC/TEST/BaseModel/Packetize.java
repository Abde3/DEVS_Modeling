package BaseModel;


import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;

import java.util.Vector;

public class Packetize extends DEVSAtomic {

    private static final int PACKET_SIZE = Constants.PACKET_SIZE;
    private static final int MAX_FLIT = Constants.MAX_FLIT;
    private static final NOC.NodeType NODETYPE = NOC.NodeType.PACKETIZER;


    Port dataPE;
    Port dataSwitch;
    Port commandSwitch;

    boolean outQstatus;
    private int sentFlit;
    private float rho;


    public Packetize(String name) {
        super();
        this.name = NODETYPE.name() + "[" + name.trim() + "]";

        inputQueue = new Vector<>();

        dataPE = new Port(this, "dataPE");
        dataSwitch = new Port(this, "dataSwitch");
        commandSwitch = new Port(this, "commandSwitch");


        this.addInPort(dataPE);
        this.addOutPort(dataSwitch);
        this.addInPort(commandSwitch);
    }


    private enum STATE{ PASSIVE, PACKETIZE, WAIT4OK, SEND_OUT_FLIT}
    private STATE state;
    private Vector<Flit> inputQueue;
    private Packet PacketToSend;

    @Override
    public void init() {
        state = STATE.PASSIVE;
        inputQueue = new Vector<>(5);
        outQstatus = true;
        sentFlit = 0;
    }

    @Override
    public void deltaExt(Port port, Object o, float v) {

        LOG.printThis(this.name, " "  +  o);
        if ( port.equals(dataPE) ) {
            inputQueue.add((Flit) o);
        }

        switch (state) {
            case PASSIVE: {
                if ( port.equals(dataPE) && inputQueue.size() < PACKET_SIZE / 8 ) {
                    LOG.printThis(this.name,inputQueue.toString());
                    state = STATE.PASSIVE;
                } else if ( port.equals(dataPE) &&  inputQueue.size() >= PACKET_SIZE / 8) {
                    state = STATE.PACKETIZE;
                } else if ( port.equals(commandSwitch) ) {
                    outQstatus = o.equals("ok");
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
                        outQstatus = o.equals("ok");
                    }
            } break;

            case SEND_OUT_FLIT: {
                if ( port.equals(commandSwitch) && o.equals("ok") ) {
                    outQstatus = true;
                    state = STATE.SEND_OUT_FLIT; }
                if ( port.equals(dataPE) ) {
                    state = STATE.SEND_OUT_FLIT;
                }
            } break;
        }
    }

    @Override
    public void deltaInt() {
        switch (state) {
            case PASSIVE: {
                /* No internal  */
            } break;

            case WAIT4OK: {
                /* No internal  */
            } break;

            case PACKETIZE: {
                if ( ! outQstatus ) {
                    state = STATE.WAIT4OK;
                } else {
                    state = STATE.SEND_OUT_FLIT;
                }
            } break;

            case SEND_OUT_FLIT: {
                sentFlit++;
                inputQueue.removeElementAt(0);
                if ( (! outQstatus) && sentFlit < MAX_FLIT) {
                    state = STATE.WAIT4OK;
                } else if ( sentFlit >= MAX_FLIT && inputQueue.size() >= PACKET_SIZE / 8 ) {
                    state = STATE.PACKETIZE;
                } else if ( sentFlit >= MAX_FLIT && inputQueue.size() < PACKET_SIZE / 8 ) {
                    state = STATE.PASSIVE;
                    sentFlit=0;
                } else if ( outQstatus && sentFlit < MAX_FLIT ) {
                    state = STATE.SEND_OUT_FLIT;
                }
            } break;
        }
    }

    @Override
    public Object[] lambda() {
        Object[] output = null;

        switch (state) {
            case SEND_OUT_FLIT: {
                output = new Object[ 2 ];
                output[0] = dataSwitch;
                output[1] = inputQueue.firstElement();
            }
        }

        return output;
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
