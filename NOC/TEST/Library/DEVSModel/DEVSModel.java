/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSModel;


import Library.DEVSSimulator.DEVSProcess;

import java.util.Vector;

public abstract class DEVSModel {
    public Vector<Port> inPorts = new Vector();
    public Vector<Port> outPorts = new Vector();
    public DEVSProcess modelProcess;
    protected String name = "";

    protected DEVSModel() {
    }

    public void addInPort(Port p) {
        this.inPorts.add(p);
    }

    public void addOutPort(Port p) {
        this.outPorts.add(p);
    }

    public Port getInPort(String inport) {
        int i = 0;
        while (i < this.getInPorts().size()) {
            if (this.getInPorts().get(i).getName().equals(inport)) {
                return this.getInPorts().get(i);
            }
            ++i;
        }
        return null;
    }

    public Port getOutPort(String outport) {
        int i = 0;
        while (i < this.getOutPorts().size()) {
            if (this.getOutPorts().get(i).getName().equals(outport)) {
                return this.getOutPorts().get(i);
            }
            ++i;
        }
        return null;
    }

    public Vector<Port> getInPorts() {
        return this.inPorts;
    }

    public Vector<Port> getOutPorts() {
        return this.outPorts;
    }

    public DEVSProcess getProcess() {
        return this.modelProcess;
    }

    public void setProcess(DEVSProcess p) {
        this.modelProcess = p;
    }

    public String toString() {
        String inports = "";
        String outports = "";
        int i = 0;
        while (i < this.inPorts.size()) {
            inports = String.valueOf(inports) + this.inPorts.get(0).getName() + "-";
            ++i;
        }
        i = 0;
        while (i < this.outPorts.size()) {
            outports = String.valueOf(outports) + this.outPorts.get(0).getName() + "-";
            ++i;
        }
        return String.valueOf(this.getClass().toString()) + '@' + Integer.toHexString(this.hashCode()) + " Inports: " + inports + " Outports: " + outports;
    }

    public String getName() {
        return this.name;
    }

    public void endSim() {
    }
}

