import java.util.Arrays;
import java.util.Vector;

import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;
import DEVSModel.Port;

import Model.*;

public class NOC_Unit extends DEVSCoupled {

	public  Vector<Port> v_in_port;
	public  Vector<Port> v_out_port;
	private QueueSwitch QS;
	private ProcessingElement PE;
	private NodeCoordinate id;
	
	public NOC_Unit(String name, NodeCoordinate coordinate, int dimension){
		
		super();

		this.id = coordinate;
		this.name = name + '-' + coordinate;
		
		QS = new QueueSwitch("QS", coordinate, 2);
		PE = new ProcessingElement("PE", coordinate);
		
		this.getSubModels().add(QS);
		this.getSubModels().add(PE);

		v_in_port = new Vector<Port>();
		v_out_port = new Vector<Port>();


		for ( NOC_MESH.DIRECTION direction : NOC_Unit_factory.getAlldirectionsforNode(coordinate, 4, NOC_factory.Topology.MESH)) {
			Port out_port = new Port(this, "out_port-" + direction);
			v_out_port.add(out_port);
			this.addOutPort(out_port);

			Port inPort = new Port(this, "in_port-" + direction);
			v_in_port.add(inPort);
			this.addInPort(inPort);
		}


		for ( NOC_MESH.DIRECTION direction : NOC_Unit_factory.getAlldirectionsforNode(coordinate, 4, NOC_factory.Topology.MESH)) {
			this.addEIC(this.getInPort("in_port-"+direction), this.getSubModel("QS"+'-'+coordinate).getInPort("in_queue-"+direction));
			this.addEOC(this.getSubModel("QS"+'-'+coordinate).getOutPort("out_queue-"+direction), this.getOutPort("out_NCUnit-"+direction));
		}


		this.addIC(this.getSubModel("PE"+'-'+coordinate).getOutPort("out_cmd"), this.getSubModel("QS"+'-'+coordinate).getInPort("in_PE"));
		this.addIC(this.getSubModel("QS"+'-'+coordinate).getOutPort("out_PE"), this.getSubModel("PE"+'-'+coordinate).getInPort("in_task"));
		this.addIC(this.getSubModel("PE"+'-'+coordinate).getOutPort("out_queue"), this.getSubModel("QS"+'-'+coordinate).getInPort("in_queue-0"));
		
		this.setSelectPriority();
		
	}

	@Override
	public void setSelectPriority() {

		/****************************************************************************************************/
		/***********************************************************| CONFLICTING SET |****|SELECT|***********/
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(  PE,       QS) ), 	QS);
		/***********************************************************| CONFLICTING SET |****|SELECT|***********/
		/****************************************************************************************************/
		
	}
	

}
