/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSSimulator;


import Library.DEVSModel.DEVSModel;
import Library.Messages.Message;

import java.util.Vector;

public abstract class DEVSProcess {
    protected float TL;
    protected float TN = Float.MAX_VALUE;
    protected DEVSProcess Parent;
    protected Vector<DEVSProcess> Child = new Vector<DEVSProcess>();
    protected int child_notified = this.Child.size();
    private volatile boolean hasChanged = false;
    public NotificationParent notifyParentP;
    public NotificationChild notifyChildP;

    public abstract void receiveYMSG(Message var1);

    public abstract void receiveDMSG(Message var1);

    public abstract void receiveXMSG(Message var1);

    public abstract void receiveIMSG(Message var1);

    public abstract void receiveINMSG(Message var1);

    public abstract void update(Message var1);

    public void addChild(DEVSProcess ch) {
        this.Child.add(ch);
    }

    public synchronized void notifyParent(Message msg) {
        this.Parent.update(msg);
    }

    public void setChanged(boolean changed) {
        this.hasChanged = changed;
    }

    public boolean getChanged() {
        return this.hasChanged;
    }

    public void notifyChild(Message msg) {
        msg.getTarget().update(msg);
    }

    public void notifyChildIn(Message msg) {
        msg.setSource(this);
        int i = 0;
        while (i < this.Child.size()) {
            msg.setTarget(this.Child.get(i));
            this.Child.get(i).update(msg);
            ++i;
        }
    }

    public boolean sendUp() {
        int i = 0;
        boolean sendup = true;
        while (sendup && i < this.Child.size()) {
            if (this.Child.get(i).getChanged()) {
                ++i;
                continue;
            }
            sendup = false;
        }
        if (sendup && this.Child.size() == i) {
            return true;
        }
        return false;
    }

    public void setParent(DEVSProcess p) {
        this.Parent = p;
    }

    public Vector<DEVSProcess> getChild() {
        return this.Child;
    }

    public DEVSProcess getParent() {
        return this.Parent;
    }

    public String toString() {
        return "process(" + this.getModel().getName() + ")";
    }

    public String toStringA() {
        String name = this.getModel() != null ? this.getModel().getName() : "";
        if (this != null) {
            return String.valueOf(this.getClass().getSimpleName()) + '@' + Integer.toHexString(this.hashCode()) + "(" + name + ")";
        }
        return null;
    }

    public abstract DEVSModel getModel();

    public class NotificationChild
    extends Thread {
        public Message msg;
        final /* synthetic */ DEVSProcess this$0;

        public NotificationChild(DEVSProcess dEVSProcess) {
            this.this$0 = dEVSProcess;
        }

        public NotificationChild(DEVSProcess dEVSProcess, Message msg) {
            this.this$0 = dEVSProcess;
            this.msg = msg;
            if (msg.getType() == 2) {
                dEVSProcess.child_notified = 0;
            }
            this.start();
        }

        @Override
        public synchronized void run() {
            this.this$0.notifyChild(this.msg);
        }
    }

    public class NotificationParent
    extends Thread {
        public Message msg;
        final /* synthetic */ DEVSProcess this$0;

        public NotificationParent(DEVSProcess dEVSProcess) {
            this.this$0 = dEVSProcess;
        }

        public NotificationParent(DEVSProcess dEVSProcess, Message msg) {
            this.this$0 = dEVSProcess;
            this.msg = msg;
            this.start();
        }

        @Override
        public void run() {
            this.this$0.notifyParent(this.msg);
        }
    }

}

