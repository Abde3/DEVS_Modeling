/*
 * Decompiled with CFR 0_123.
 */
package Library.Messages;

import Library.DEVSSimulator.DEVSProcess;

public class INMSG
extends Message {
    public INMSG(float time, DEVSProcess s, DEVSProcess t) {
        super(2, time, s, t);
    }

    @Override
    public Message clone() {
        return new INMSG(this.Time, this.Source, this.Target);
    }
}

