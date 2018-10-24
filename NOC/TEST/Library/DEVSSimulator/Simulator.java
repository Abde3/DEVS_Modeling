/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSSimulator;

import Library.DEVSModel.DEVSAtomic;
import Library.DEVSModel.DEVSModel;
import Library.DEVSModel.Port;
import Library.Messages.DMSG;
import Library.Messages.Message;
import Library.Messages.XMSG;
import Library.Messages.YMSG;

public class Simulator
extends DEVSProcess {
    private DEVSAtomic ModelToSimulate;
    Boolean confluent = false;

    public Simulator(DEVSAtomic model) {
        this.Child = null;
        this.ModelToSimulate = model;
        this.ModelToSimulate.setProcess(this);
    }

    public Simulator(DEVSProcess parent) {
        this.Parent = parent;
    }

    public Simulator(DEVSProcess parent, DEVSAtomic model) {
        this.Parent = parent;
        this.Child = null;
        this.ModelToSimulate = model;
        this.ModelToSimulate.setProcess(this);
    }

    @Override
    public void receiveDMSG(Message msg) {
    }

    @Override
    public void receiveIMSG(Message msg) {
        this.ModelToSimulate.init();
        this.TL = msg.getTime();
        this.TN = this.TL + this.ModelToSimulate.getDuration();
        DMSG dmsg = new DMSG(this.TN, this, this.Parent);
        this.notifyParentP = new NotificationParent(this, dmsg);
    }

    @Override
    public void receiveINMSG(Message msg) {
        this.setChanged(false);
        if (this.TN != msg.getTime()) {
            new RuntimeException("Bad Internal transition");
        } else {
            this.TL = msg.getTime();
            Object[] output = this.ModelToSimulate.lambda();
            if (output != null) {
                YMSG ymsg = new YMSG();
                ymsg.setTime(this.TL);
                ymsg.setSource(this);
                ymsg.setTarget(this.Parent);
                ymsg.setPort((Port)output[0]);
                ymsg.setEvent(output[1]);
                Root.getReport().println(String.valueOf(this.ModelToSimulate.getName()) + "(" + ((Port)output[0]).getName() + " = " + output[1] + ")");
                this.notifyParent(ymsg);
            }
            this.ModelToSimulate.deltaInt();
            this.TN = this.TL + this.ModelToSimulate.getDuration();
            DMSG dmsg = new DMSG(this.TN, this, this.Parent);
            this.notifyParentP = new NotificationParent(this, dmsg);
        }
    }

    @Override
    public void receiveXMSG(Message msg) {
        float e = msg.getTime() - this.TL;
        this.setChanged(false);
        if (msg.getTime() != this.TN) {
            if (((XMSG)msg).getEvent() != null) {
                this.ModelToSimulate.deltaExt(((XMSG)msg).getPort(), ((XMSG)msg).getEvent(), e);
                Root.getReport().println(String.valueOf(this.ModelToSimulate.getName()) + "(" + ((XMSG)msg).getPort().getName() + " = " + ((XMSG)msg).getEvent() + ")");
            }
            this.TL = msg.getTime();
            this.TN = this.TL + this.ModelToSimulate.getDuration();
            this.notifyParentP = new NotificationParent(this, new DMSG(this.TN, this, this.Parent));
        } else {
            Root.getReport().println("DELTA CONFLUENT");
            if (((XMSG)msg).getEvent() != null) {
                this.ModelToSimulate.deltaConfluent(((XMSG)msg).getPort(), ((XMSG)msg).getEvent(), e);
                Root.getReport().println(String.valueOf(this.ModelToSimulate.getName()) + "(" + ((XMSG)msg).getPort().getName() + " = " + ((XMSG)msg).getEvent() + ")");
            }
            this.TL = msg.getTime();
            this.TN = this.TL + this.ModelToSimulate.getDuration();
            this.notifyParentP = new NotificationParent(this, new DMSG(this.TN, this, this.Parent));
        }
    }

    @Override
    public void receiveYMSG(Message msg) {
    }

    @Override
    public DEVSModel getModel() {
        return this.ModelToSimulate;
    }

    public void setModel(DEVSAtomic model) {
        this.ModelToSimulate = model;
        this.ModelToSimulate.setProcess(this);
    }

    @Override
    public void update(Message msg) {
        switch (msg.getType()) {
            case 0: {
                this.receiveIMSG(msg);
                break;
            }
            case 1: {
                this.receiveXMSG(msg);
                break;
            }
            case 2: {
                this.receiveINMSG(msg);
                break;
            }
            default: {
                System.err.println("incorrect message");
            }
        }
    }
}

