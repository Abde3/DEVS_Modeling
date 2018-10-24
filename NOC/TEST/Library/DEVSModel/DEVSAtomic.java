/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSModel;

public abstract class DEVSAtomic
extends DEVSModel {
    private float ElapsedTime;

    protected DEVSAtomic() {
    }

    public abstract void init();

    public abstract void deltaExt(Port var1, Object var2, float var3);

    public abstract void deltaInt();

    public abstract Object[] lambda();

    public abstract float getDuration();

    public float getElapsedTime() {
        return this.ElapsedTime;
    }

    public void setElapsedTime(float e) {
        this.ElapsedTime = e;
    }

    public void printFinalReport() {
    }

    public void deltaConfluent(Port p, Object ev, float e) {
        this.deltaInt();
        this.deltaExt(p, ev, 0.0f);
    }

    public String toSttring() {
        return this.toString();
    }
}

