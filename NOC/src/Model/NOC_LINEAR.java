package Model;

import DEVSModel.DEVSAtomic;

public class NOC_LINEAR extends NOC {

    @Override
    protected void set_generator(DEVSAtomic generator) {
        this.generator = generator;
    }

    @Override
    protected void build_network() {

    }

    @Override
    public void setSelectPriority() {

    }
}
