/*
 * Decompiled with CFR 0_123.
 */
package Library.Messages;

import Library.DEVSModel.DEVSModel;
import Library.DEVSModel.Port;
import Library.DEVSSimulator.DEVSProcess;

public class XMSG
extends Message {
    protected Port InPort;
    protected Object EventValue;

    public XMSG() {
        super(1);
    }

    public XMSG(float time, DEVSProcess s, DEVSProcess t) {
        super(1, time, s, t);
    }

    public XMSG(Port inport, DEVSModel model) {
        super(1);
        this.InPort = inport;
    }

    public XMSG(Port port, DEVSModel model, float time) {
        super(1, time);
        this.InPort = port;
    }

    public XMSG(Port port, DEVSModel model, float time, DEVSProcess source, DEVSProcess target, Object event) {
        super(1, time);
        this.InPort = port;
        this.Source = source;
        this.Target = target;
        this.EventValue = event;
    }

    public void setPort(Port p) {
        this.InPort = p;
    }

    public Port getPort() {
        return this.InPort;
    }

    @Override
    public Message clone() {
        return new XMSG(this.Time, this.Source, this.Target);
    }

    public Object getEvent() {
        return this.EventValue;
    }

    public void setEvent(Object event) {
        this.EventValue = event;
    }

    @Override
    public String toString() {
        return "Port " + this.InPort.getName() + " xmsg at time " + this.Time + " from " + this.Source + " to " + this.Target;
    }
}

