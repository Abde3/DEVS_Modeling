/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSModel;

import Library.DEVSSimulator.Coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public abstract class DEVSCoupled
extends DEVSModel {
    protected Coordinator coord;
    protected Vector<DEVSModel> subModels;
    private HashMap<Port, Port> EIC;
    private HashMap<Port, Port> EOC;
    private HashMap<Port, Vector<Port>> IC;
    protected HashMap<Vector<DEVSModel>, DEVSModel> selectPriority;

    protected DEVSCoupled(Vector<DEVSModel> submodels) {
        this.subModels = submodels;
        this.EIC = new HashMap();
        this.EOC = new HashMap();
        this.IC = new HashMap();
        this.selectPriority = new HashMap();
    }

    protected DEVSCoupled(HashMap<Port, Port> eic, HashMap<Port, Port> eoc, HashMap<Port, Vector<Port>> ic) {
        this.subModels = new Vector();
        this.EIC = eic;
        this.EOC = eoc;
        this.IC = ic;
    }

    public DEVSCoupled() {
        this.subModels = new Vector();
        this.EIC = new HashMap();
        this.EOC = new HashMap();
        this.IC = new HashMap();
        this.selectPriority = new HashMap();
    }

    public HashMap<Port, Port> getEIC() {
        return this.EIC;
    }

    public HashMap<Port, Port> getEOC() {
        return this.EOC;
    }

    public HashMap<Port, Vector<Port>> getIC() {
        return this.IC;
    }

    public void addEIC(Port p1, Port p2) {
        this.EIC.put(p1, p2);
    }

    public void addEOC(Port p1, Port p2) {
        this.EOC.put(p1, p2);
    }

    public void addIC(Port p1, Port p2) {
        Vector vp = this.IC.get(p1);
        if (vp == null) {
            vp = new Vector();
            vp.add(p2);
        } else {
            vp.add((Port)p2);
        }
        this.IC.put(p1, vp);
    }

    public Vector<DEVSModel> getSubModels() {
        return this.subModels;
    }

    public DEVSModel getSubModel(String submodel) {
        int i = 0;
        while (!this.subModels.get(i).getName().equals(submodel)) {
            ++i;
        }
        return this.subModels.get(i);
    }


    public void addSubModel(DEVSModel submodel) {
        this.subModels.add(submodel);
    }
    @Override
    public String toString() {
        String CoupledModelString = "";
        int i = 0;
        while (i < this.subModels.size()) {
            CoupledModelString = String.valueOf(CoupledModelString) + this.subModels.get(i).getName() + "\n";
            ++i;
        }
        return String.valueOf(this.getName()) + " == \n" + CoupledModelString + "\n Inports:" + this.inPorts.toString() + "\n Outports:" + this.outPorts + "\n EIC: " + this.EIC.toString() + "\n EOC: " + this.EOC.toString() + "\n IC: " + this.IC.toString();
    }

    public abstract void setSelectPriority();

    public DEVSModel getImminent(ArrayList<DEVSModel> DEVSModels) {
        int i_key = 0;
        int i_limit = 0;
        boolean found = false;
        Vector<DEVSModel> vector = null;
        Set<Vector<DEVSModel>> vecKeys = null;
        vecKeys = this.selectPriority.keySet();
        Iterator<Vector<DEVSModel>> iterator = vecKeys.iterator();
        i_limit = vecKeys.size();
        while (i_key < i_limit && !found) {
            vector = iterator.next();
            if (vector.containsAll(DEVSModels) && vector.size() == DEVSModels.size()) {
                found = true;
            }
            ++i_key;
        }
        return this.selectPriority.get(vector);
    }

    @Override
    public void endSim() {
        int i = 0;
        while (i < this.getSubModels().size()) {
            this.getSubModels().get(i).endSim();
            ++i;
        }
    }
}

