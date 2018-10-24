/*
 * Decompiled with CFR 0_123.
 */
package Library.Messages;


import Library.DEVSSimulator.DEVSProcess;

public class DMSG
extends Message {
    public DMSG() {
        super(4);
    }

    public DMSG(float time) {
        super(4, time);
    }

    public DMSG(float time, DEVSProcess p, DEVSProcess s) {
        super(4);
        this.Type = 4;
        this.Time = time;
        this.Source = p;
        this.Target = s;
    }

    public DMSG(Message msg) {
        super(4);
        this.Type = 4;
        this.Time = msg.getTime();
        this.Source = msg.getSource();
        this.Target = msg.getTarget();
    }

    @Override
    public Message clone() {
        return new DMSG(this.Time, this.Source, this.Target);
    }
}

