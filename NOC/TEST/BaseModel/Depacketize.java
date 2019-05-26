package BaseModel;


import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;

import java.util.Vector;

public class Depacketize extends DEVSAtomic {

    private static final NOC.NodeType NODETYPE = NOC.NodeType.DEPACKETIZER;
    private static final int PACKET_SIZE = Constants.PACKET_SIZE;
    private static final int DATA_SIZE = Constants.DATA_SIZE;

    Port dataPE; // ToPE
    Port dataSwitch; // fromSwitch
    Port commandSwitch;
    Port commandPE;

    private float rho;

    Vector<Flit> outDataBuffer;
    private boolean outPEstatus;

    private enum STATE{ IDLE, DEPACKETIZE, WAIT4OK, SEND_OUT_FLIT}
    private Depacketize.STATE state;
    private Vector<Flit> queue;

    public Depacketize(String name) {
        super();
        this.name = NODETYPE.name() + "[" + name.trim() + "]";

        queue = new Vector<>();
        outDataBuffer = new Vector<>();

        dataPE = new Port(this, "dataPE");
        dataSwitch = new Port(this, "dataSwitch");
        commandSwitch = new Port(this, "commandSwitch");
        commandPE = new Port(this, "commandPE");
    }


    public void setState(STATE state) {
        this.state = state;
    }

    @Override
    public void init() {
        setState(STATE.IDLE);
        queue = new Vector<>(PACKET_SIZE);
        outPEstatus = true;
    }

    @Override
    public void deltaExt(Port port, Object o, float v) {

        switch (state) {
            case IDLE: {
                if ( port.equals(dataSwitch) ) {
                    queue.add((Flit) o);
                    LOG.printThis(this.name, queue.toString());
                    setState(state.DEPACKETIZE);
                }
            } break;

            case DEPACKETIZE: {
                if ( port.equals(dataSwitch)  ) {
                    queue.add((Flit) o);
                    LOG.printThis(this.name, queue.toString());
                }
            } break;

            case WAIT4OK: {
                if ( port.equals(dataPE) ) { state = STATE.WAIT4OK; }
                else if ( port.equals(dataPE) && o.equals("ok")) {
                    outPEstatus = true;
                    state = STATE.SEND_OUT_FLIT;
                }

            } break;

            case SEND_OUT_FLIT: {
                if ( port.equals(dataSwitch) ) {
                    queue.add((Flit) o);
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

                if ( ! queue.isEmpty() ) {
                    outDataBuffer.addAll( queue );
                    queue.clear();
                    setState(state.DEPACKETIZE);
                } else if (! outDataBuffer.isEmpty()) {
                    setState(STATE.SEND_OUT_FLIT);
                }

            } break;

            case SEND_OUT_FLIT: {
                outDataBuffer.removeElementAt(0);
                if ( ! queue.isEmpty() ) {
                    setState(state.DEPACKETIZE);
                } else if (! outDataBuffer.isEmpty()) {
                    setState(STATE.SEND_OUT_FLIT);
                } else if ( queue.isEmpty() && outDataBuffer.isEmpty() ) {
                    setState(state.IDLE);
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
                output[1] = outDataBuffer.firstElement();

                LOG.logThis(this.name, output[1]);

                LOG.printThis(this.name, " SEND OUT " + output[1]);
            }
        }

        return output;
    }

    @Override
    public float getDuration() {


        switch (state)  {
            case IDLE: rho =  Float.POSITIVE_INFINITY;
            case SEND_OUT_FLIT: rho =  5;
            case DEPACKETIZE: rho = 12;
        }

        return rho;
    }
}
