package NOCUnit;

import BaseModel.ProcessingElement;
import BaseModel.Queue;
import BaseModel.Switch;
import NOCRoutingPolicy.NocRoutingPolicy;
import java.util.Set;


public abstract class AbstractNOCUnitBuilder<TNOCUnit extends NOCUnit> {

    protected Set<String>       v_in_ports_names;
    protected Set<String>       v_out_ports_names;
    protected Set<Queue>        v_in_queue;
    protected Set<Queue>        v_out_queue;
    protected Switch            aSwitch;
    protected ProcessingElement aProcessingElement;
    protected NocRoutingPolicy  routingPolicy;


    public abstract AbstractNOCUnitBuilder withInPorts( String ... inPorts) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withOutPorts(String ... outPorts) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withQueuePerInPortRatio( int queuePerInPortRatio );
    public abstract AbstractNOCUnitBuilder withQueuePerOutPortRatio( int queuePerOutPortRatio );
    public abstract AbstractNOCUnitBuilder withRoutingPolicy(NocRoutingPolicy routingPolicy);
    public abstract TNOCUnit build();



}
