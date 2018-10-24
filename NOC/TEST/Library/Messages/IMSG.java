/*
 * Decompiled with CFR 0_123.
 */
package Library.Messages;


public class IMSG
extends Message {
    public IMSG() {
        super(0);
    }

    public IMSG(float time) {
        super(0, time);
    }

    @Override
    public Message clone() {
        return new IMSG(this.Time);
    }
}

