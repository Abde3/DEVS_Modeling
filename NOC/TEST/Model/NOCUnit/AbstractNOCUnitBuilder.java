package Model.NOCUnit;

import BaseModel.NetworkInterface;
import BaseModel.ProcessingElement;
import BaseModel.Switch;
import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;

import java.util.List;
import java.util.Set;


public abstract class AbstractNOCUnitBuilder<TNOCUnit extends NOCUnit> {

    protected Set<String>       v_data_in_ports_names;
    protected Set<String>       v_data_out_ports_names;
    protected Set<String>       v_cmd_in_ports_names;
    protected Set<String>       v_cmd_out_ports_names;
    protected int               queuePerInPortRatio;
    protected int               queuePerOutPortRatio;
    protected Switch            aSwitch;
    protected ProcessingElement aProcessingElement;
    protected NetworkInterface aNetworkInterface;
    protected NocRoutingPolicy  routingPolicy;
    protected IPoint            coordinate;


    public abstract AbstractNOCUnitBuilder withDataInPorts(List<String> inDirections) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withDataOutPorts(List<String> outDirections) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withCmdInPorts(List<String> inDirections) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withCmdOutPorts(List<String> outDirections) throws NOCUnit.ExistingPortException;
    public abstract AbstractNOCUnitBuilder withQueuePerInPortRatio( int queuePerInPortRatio );
    public abstract AbstractNOCUnitBuilder withQueuePerOutPortRatio( int queuePerOutPortRatio );
    public abstract AbstractNOCUnitBuilder withRoutingPolicy(NocRoutingPolicy routingPolicy);
    public abstract AbstractNOCUnitBuilder withCoordinate(IPoint coordinate);


    public abstract TNOCUnit build();

}
