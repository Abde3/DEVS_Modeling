package BaseModel;

import Library.DEVSModel.DEVSAtomic;
import Library.DEVSModel.Port;
import Model.NOCModel.NOC;
import Verification.StateRepresentation;

import java.util.Vector;

public class Depacketize extends DEVSAtomic {

    private static final NOC.NodeType NODETYPE = NOC.NodeType.DEPACKETIZER;
    private static final int PACKET_SIZE = 40;
    private static final int DATA_SIZE = 40;

    Port dataPE; // ToPE
    Port dataSwitch; // fromSwitch
    Port commandSwitch;

    private float rho;

    Packet outDataBuffer;

    boolean outQstatus;
    private int sentFlit;
    private int maxFlit;

    public Depacketize(String name) {
        super();
        this.name = NODETYPE.name() + "[" + name.trim() + "]";

        queue = new Vector<>();

        dataPE = new Port(this, "dataPE");
        dataSwitch = new Port(this, "dataSwitch");
        commandSwitch = new Port(this, "commandSwitch");
    }

    public void setState(STATE state) {
        this.state = state;
        StateRepresentation.addState( this.name, String.valueOf(state));

    }


    private enum STATE{ PASSIVE, DEPACKETIZE, WAIT4OK, SEND_OUT_FLIT}
    private Depacketize.STATE state;
    private Vector<Flit> queue;

    @Override
    public void init() {
        setState(STATE.PASSIVE);
        queue = new Vector<>(PACKET_SIZE);
        outQstatus = true;
        sentFlit = 0;
    }

    @Override
    public void deltaExt(Port port, Object o, float v) {

        if ( !((Flit) o).isTail && !((Flit) o).isHeader && ((Flit) o).getData() == 'E' ) {
            System.out.println("RECEIVED " + o + " while in state " + state);
        }

        switch (state) {
            case PASSIVE: {
                if ( port.equals(dataSwitch) &&  queue.size() < PACKET_SIZE / 8) {
                    queue.add((Flit) o);

                } else if ( port.equals(dataSwitch) &&  queue.size() >= PACKET_SIZE / 8) {
                    queue.add((Flit) o);
                    setState(STATE.DEPACKETIZE);

                } else if ( port.equals(commandSwitch) ) {
                    outQstatus = (Boolean) o;
                }
            } break;

            case WAIT4OK: {
                if ( port.equals(dataSwitch) ) {
                    setState(STATE.WAIT4OK);
                }
                else if ( port.equals(commandSwitch) && o.equals("ok")) {
                    outQstatus = true;
                    setState(STATE.SEND_OUT_FLIT);
                }

            } break;

            case DEPACKETIZE: {
                if ( port.equals(commandSwitch) ) {
                    outQstatus = (Boolean) o;
                } else {
                    queue.add((Flit) o);
                }
            } break;

            case SEND_OUT_FLIT: {
                if ( port.equals(commandSwitch) && o.equals("ok") ) { setState(STATE.SEND_OUT_FLIT); }
                if ( port.equals(dataSwitch) ) {
                    queue.add((Flit) o);
                    setState(STATE.SEND_OUT_FLIT);
                }
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

            case DEPACKETIZE: {

                if ( ! outQstatus ) {
                    setState(STATE.WAIT4OK);
                } else {
                    outDataBuffer = new Packet( queue );
                    queue.clear();
                    setState(STATE.SEND_OUT_FLIT);
                }
            } break;

            case SEND_OUT_FLIT: {
                if ( (! outQstatus) && sentFlit < maxFlit) {
                    setState(STATE.WAIT4OK);
                } else if ( sentFlit >= maxFlit && queue.size() >= PACKET_SIZE / 8 ) {
                    setState(STATE.DEPACKETIZE);
                } else if ( sentFlit >= maxFlit && queue.size() < PACKET_SIZE / 8 ) {
                    setState(STATE.PASSIVE);
                    outDataBuffer = null;
                } else if ( outQstatus && sentFlit < maxFlit ) {
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
                output[0] = dataPE;
                output[1] = outDataBuffer;
            }
        }

        return output;
    }

    @Override
    public float getDuration() {


        switch (state)  {
            case WAIT4OK: rho =  Float.POSITIVE_INFINITY;
            case SEND_OUT_FLIT: rho =  5;
            case PASSIVE: rho =  Float.POSITIVE_INFINITY;
            case DEPACKETIZE: rho = 12;
        }

        return rho;
    }
}
