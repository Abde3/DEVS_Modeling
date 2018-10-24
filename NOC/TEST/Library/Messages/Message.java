/*
 * Decompiled with CFR 0_123.
 */
package Library.Messages;


import Library.DEVSSimulator.DEVSProcess;

public abstract class Message {
    public static final int IMSG = 0;
    public static final int XMSG = 1;
    public static final int INMSG = 2;
    public static final int YMSG = 3;
    public static final int DMSG = 4;
    protected int Type;
    protected float Time;
    protected DEVSProcess Source;
    protected DEVSProcess Target;

    public Message(int type) {
        this.Type = type;
    }

    public Message(int type, float time) {
        this.Type = type;
        this.Time = time;
    }

    public Message(int type, float time, DEVSProcess s, DEVSProcess t) {
        this.Type = type;
        this.Time = time;
        this.Source = s;
        this.Target = t;
    }

    public int getType() {
        return this.Type;
    }

    public void setTime(float time) {
        this.Time = time;
    }

    public float getTime() {
        return this.Time;
    }

    public DEVSProcess getSource() {
        return this.Source;
    }

    public void setSource(DEVSProcess s) {
        this.Source = s;
    }

    public DEVSProcess getTarget() {
        return this.Target;
    }

    public void setTarget(DEVSProcess t) {
        this.Target = t;
    }

    public String toString() {
        char msg = '\u0000';
        switch (this.Type) {
            case 0: {
                msg = 'i';
                break;
            }
            case 1: {
                msg = 'x';
                break;
            }
            case 2: {
                msg = '*';
                break;
            }
            case 3: {
                msg = 'y';
                break;
            }
            case 4: {
                msg = 'd';
                break;
            }
            default: {
                System.err.println("error message");
            }
        }
        return String.valueOf(msg) + "msg" + " at time " + this.Time + " from " + this.Source.toStringA() + " to " + this.Target.toStringA();
    }

    public abstract Message clone();
}

