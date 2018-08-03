package Model;

import DEVSModel.*;

import java.util.Vector;

public abstract class NOC extends DEVSCoupled {

    public enum NODE_TYPE {NOC, NODE, QUEUE, SWITCH, QUEUE_SWITCH, PE};

    protected Vector<Vector<NOC_Unit>> model = new Vector<Vector<NOC_Unit>>();

    protected DEVSModel generator;
    protected NODE_TYPE node_type;
    protected int size;

    public NOC( ) {
        super();
        node_type = NODE_TYPE.NOC;
        name = node_type.toString();
    }

    public Vector<Vector<NOC_Unit>> getModel() {
        return model;
    }

    protected void set_generator(DEVSAtomic generator) {
        this.generator = generator;
        subModels.add(generator);
    }

    public DEVSModel getGenerator() {
        return generator;
    }

    protected abstract void build_network();

}
