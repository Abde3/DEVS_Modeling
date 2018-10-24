/*
 * Decompiled with CFR 0_123.
 */
package Library.Messages;

import Library.DEVSModel.Port;

public class YMSG
extends Message {
    private Port OutPort;
    private Object EventValue;

    public YMSG() {
        super(3);
    }

    public YMSG(Port outport, int value) {
        super(3);
        this.OutPort = outport;
        this.EventValue = value;
    }

    public void setPort(Port outport) {
        this.OutPort = outport;
    }

    public Port getPort() {
        return this.OutPort;
    }

    @Override
    public Message clone() {
        return null;
    }

    public Object getEvent() {
        return this.EventValue;
    }

    public void setEvent(Object ev) {
        this.EventValue = ev;
    }
}

