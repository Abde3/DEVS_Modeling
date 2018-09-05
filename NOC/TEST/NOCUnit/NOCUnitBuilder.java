package NOCUnit;

import BaseModel.ProcessingElement;
import BaseModel.Queue;
import BaseModel.Switch;
import DEVSModel.Port;
import NOCRoutingPolicy.NocRoutingPolicy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

public class NOCUnitBuilder extends AbstractNOCUnitBuilder<NOCUnit> {


    @Override
    public AbstractNOCUnitBuilder withInPorts(String... inPorts) throws NOCUnit.ExistingPortException {

        v_in_ports_names = new HashSet<>(Arrays.asList(inPorts));

        if (v_in_ports_names.size() != inPorts.length) {
            throw new NOCUnit.ExistingPortException("The given list of input ports names contains duplicate") ;
        }

        return this;

    }

    @Override
    public AbstractNOCUnitBuilder withOutPorts(String... outPorts) throws NOCUnit.ExistingPortException {

        v_in_ports_names = new HashSet<>(Arrays.asList(outPorts));

        if (v_in_ports_names.size() != outPorts.length) {
            throw new NOCUnit.ExistingPortException("The given list of output ports names contains duplicate") ;
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withQueuePerInPortRatio(int queuePerInPortRatio) {

        for ( String inPortName : v_in_ports_names) {

            for (int i = 0; i < queuePerInPortRatio; i++) {
                Queue tmpInQueue = new Queue();
                v_in_queue.add(tmpInQueue);
            }
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withQueuePerOutPortRatio(int queuePerOutPortRatio) {

        for ( String outPortName : v_out_ports_names) {

            for (int i = 0; i < queuePerOutPortRatio; i++) {
                Queue tmpOutQueue = new Queue();
                v_out_queue.add(tmpOutQueue);
            }
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withRoutingPolicy(NocRoutingPolicy routingPolicy) {
        this.routingPolicy = routingPolicy;
        return this;
    }


    @Override
    public NOCUnit build() {
        aSwitch = new Switch();
        aProcessingElement = new ProcessingElement();

        NOCUnit nocUnit = new NOCUnit(v_in_ports_names, v_out_ports_names, v_in_queue, v_out_queue, aSwitch, aProcessingElement);

        return nocUnit;
    }


}
