/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSSimulator;


import Library.DEVSModel.DEVSCoupled;
import Library.DEVSModel.DEVSModel;
import Library.DEVSModel.Port;
import Library.Messages.DMSG;
import Library.Messages.Message;
import Library.Messages.XMSG;
import Library.Messages.YMSG;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Coordinator
extends DEVSProcess {
    private DEVSCoupled cmodel;
    private volatile ArrayList<DMSG> DMSGList;
    XMSG XMsgWait = null;

    public Coordinator(DEVSCoupled cm) {
        this.cmodel = cm;
        this.cmodel.setProcess(this);
        this.DMSGList = new ArrayList();
    }

    public Coordinator(DEVSCoupled cm, Vector<DEVSProcess> ch) {
        this.cmodel = cm;
        this.cmodel.setProcess(this);
        this.Child = ch;
        this.DMSGList = new ArrayList();
        int i = 0;
        while (i < this.Child.size()) {
            ((DEVSProcess)this.Child.get(i)).setParent(this);
            ++i;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void receiveDMSG(Message msg) {
        boolean i = false;
        Coordinator coordinator = this;
        synchronized (coordinator) {
            DMSG dmsg = new DMSG(msg);
            this.addMSG(dmsg);
            msg.getSource().setChanged(true);
            if (this.DMSGList.size() == this.getChild().size() && this.sendUp()) {
                int index = this.dmsgIndexSendUp();
                this.TN = this.DMSGList.get(index).getTime();
                DMSG dmsg2 = new DMSG(this.DMSGList.get(index).getTime(), this, this.Parent);
                this.notifyParentP = new NotificationParent(this, dmsg2);
            }
        }
    }

    @Override
    public void receiveIMSG(Message msg) {
        this.notifyChildIn(msg);
    }

    @Override
    public void receiveINMSG(Message msg) {
        int i_dmsg = 0;
        boolean iconfluent = false;
        ArrayList<DEVSModel> DMSGConfluentList = new ArrayList<DEVSModel>();
        DEVSProcess imminentProcess = null;
        DEVSModel immModel = null;
        while (i_dmsg < this.DMSGList.size() && msg.getTime() != this.DMSGList.get(i_dmsg).getTime()) {
            ++i_dmsg;
        }
        if (i_dmsg < this.DMSGList.size()) {
            int ii = 0;
            while (ii < this.DMSGList.size()) {
                if (msg.getTime() == this.DMSGList.get(ii).getTime()) {
                    DMSGConfluentList.add(this.DMSGList.get(ii).getSource().getModel());
                }
                ++ii;
            }
            if (DMSGConfluentList.size() > 1) {
                int iii = 0;
                while (iii < DMSGConfluentList.size()) {
                    Root.getReport().println("CONF: " + DMSGConfluentList.get(iii).getName());
                    ++iii;
                }
                immModel = ((DEVSCoupled)this.getModel()).getImminent(DMSGConfluentList);
                if (immModel != null) {
                    imminentProcess = immModel.getProcess();
                    i_dmsg = this.getNextDMSG(imminentProcess);
                }
            }
            if (imminentProcess != null) {
                DMSG dmsg = this.DMSGList.remove(i_dmsg);
                msg.setSource(this);
                msg.setTarget(dmsg.getSource());
                this.notifyChildP = new NotificationChild(this, msg);
            } else {
                DMSG dmsg = this.DMSGList.remove(i_dmsg);
                msg.setSource(this);
                msg.setTarget(dmsg.getSource());
                this.notifyChildP = new NotificationChild(this, msg);
            }
        }
    }

    @Override
    public void receiveXMSG(Message msg) {
        XMSG xmsg = (XMSG)msg;
        HashMap<Port, Port> eic = this.cmodel.getEIC();
        Port p = eic.get(xmsg.getPort());
        DEVSProcess devsprocess = p.getModel().getProcess();
        xmsg.setTarget(devsprocess);
        xmsg.setTime(msg.getTime());
        xmsg.setPort(p);
        this.TL = msg.getTime();
        this.notifyChildP = new NotificationChild(this, xmsg);
    }

    @Override
    public synchronized void receiveYMSG(Message msg) {
        Port p2;
        XMSG xmsg = null;
        YMSG ymsg2 = null;
        HashMap<Port, Port> eoc = this.cmodel.getEOC();
        HashMap<Port, Vector<Port>> ic = this.cmodel.getIC();
        Port p = eoc.get(((YMSG)msg).getPort());
        if (p != null) {
            ymsg2 = new YMSG();
            ymsg2.setPort(p);
            ymsg2.setSource(this);
            ymsg2.setTarget(this.Parent);
            ymsg2.setEvent(((YMSG)msg).getEvent());
            ymsg2.setTime(msg.getTime());
            this.notifyParentP = new NotificationParent(this, ymsg2);
        }
        if (ic.get(p2 = ((YMSG)msg).getPort()) != null) {
            int i = 0;
            while (i < ic.get(p2).size()) {
                p = ic.get(p2).get(i);
                p.getModel().getProcess().setChanged(false);
                xmsg = new XMSG(p, p.getModel(), msg.getTime());
                xmsg.setSource(this);
                xmsg.setTarget(p.getModel().getProcess());
                xmsg.setEvent(((YMSG)msg).getEvent());
                int j = 0;
                while (j < this.DMSGList.size() && this.DMSGList.get(j).getTarget() != xmsg.getTarget()) {
                    if (this.DMSGList.get(j).getTime() == 0.0f) {
                        j = this.DMSGList.size();
                        continue;
                    }
                    ++j;
                }
                if (this.XMsgWait == null) {
                    this.notifyChildP = new NotificationChild(this, xmsg);
                } else {
                    this.XMsgWait = new XMSG(xmsg.getPort(), p.getModel(), xmsg.getTime());
                    this.XMsgWait.setSource(this);
                    this.XMsgWait.setTarget(xmsg.getTarget());
                    this.XMsgWait.setEvent(xmsg.getEvent());
                }
                ++i;
            }
        }
    }

    @Override
    public synchronized void update(Message msg) {
        switch (msg.getType()) {
            case 0: {
                this.receiveIMSG(msg);
                break;
            }
            case 2: {
                this.receiveINMSG(msg);
                break;
            }
            case 4: {
                this.receiveDMSG(msg);
                break;
            }
            case 1: {
                this.receiveXMSG(msg);
                break;
            }
            case 3: {
                this.receiveYMSG(msg);
                break;
            }
            default: {
                System.err.println("incorrect message");
            }
        }
    }

    private synchronized int dmsgIndexSendUp() {
        int index = 0;
        int i = 0;
        while (i < this.DMSGList.size()) {
            if (this.DMSGList.get(index).getTime() > this.DMSGList.get(i).getTime()) {
                index = i;
            }
            ++i;
        }
        return index;
    }

    public synchronized void addMSG(Message msg) {
        int i = 0;
        boolean found = false;
        while (!found && i < this.DMSGList.size()) {
            if (msg.getSource() != this.DMSGList.get(i).getSource()) {
                ++i;
                continue;
            }
            this.DMSGList.remove(i);
        }
        this.DMSGList.add((DMSG)msg);
    }

    public ArrayList<DMSG> getDMSGList() {
        return this.DMSGList;
    }

    public void addChild(Vector<DEVSProcess> vector) {
        this.Child = vector;
    }

    protected int getNextDMSG(DEVSProcess devsprocess) {
        int i = 0;
        while (i < this.DMSGList.size() && this.DMSGList.get(i).getSource() != devsprocess) {
            ++i;
        }
        return i;
    }

    @Override
    public DEVSModel getModel() {
        return this.cmodel;
    }
}

