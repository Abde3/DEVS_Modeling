package Model.NOCUnit;

import BaseModel.ProcessingElement;
import BaseModel.Queue;
import BaseModel.Switch;
import DEVSModel.DEVSModel;
import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;
import org.omg.CORBA.INV_POLICY;

import java.util.Set;


public abstract class AbstractNOCUnitBuilder<TNOCUnit extends NOCUnit> {

    protected Set<String>       v_in_ports_names;
    protected Set<String>       v_out_ports_names;
    protected Set<Queue>        v_in_queue;
    protected Set<Queue>        v_out_queue;
    protected Switch            aSwitch;
    protected ProcessingElement aProcessingElement;
    protected NocRoutingPolicy  routingPolicy;
    protected IPoint            coordinate;


    public abstract AbstractNOCUnitBuilder withInPorts( String ... inPorts) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withOutPorts(String ... outPorts) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withQueuePerInPortRatio( int queuePerInPortRatio );
    public abstract AbstractNOCUnitBuilder withQueuePerOutPortRatio( int queuePerOutPortRatio );
    public abstract AbstractNOCUnitBuilder withRoutingPolicy(NocRoutingPolicy routingPolicy);
    public abstract AbstractNOCUnitBuilder withCoordinate(IPoint coordinate);
    public abstract AbstractNOCUnitBuilder withGenerator(DEVSModel generator);

    public abstract TNOCUnit build();

}
