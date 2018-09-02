package NOCUnit;


import DEVSModel.Port;

import java.util.Vector;

public class NOCUnitBuilder extends AbstractNOCUnitBuilder<NOCUnit> {

    @Override
    NOCUnit newBuilder() {
        return new NOCUnit();
    }

    @Override
    void buildOutPorts(NOCUnit noc, String ... outPortsName) {
        noc.v_out_ports = new Vector<>();

        for ( String portName : outPortsName ) {
            try {
                noc.addOutPort(portName);
            } catch (NOCUnit.ExistingPortException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void buildInPorts(NOCUnit noc, String ... inPortsName) {
        noc.v_in_ports = new Vector<>();

        for ( String portName : inPortsName ) {
            try {
                noc.addInPort(portName);
            } catch (NOCUnit.ExistingPortException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void buildSubComponents(NOCUnit unit) {

        unit.processingElement = new ProcessingElement();
        unit.queueSwitch = new QueueSwitch();

    }


}
