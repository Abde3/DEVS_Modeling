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
	
	public NOC_Unit(String name, NodeCoordinate id, int dimension){
		
		super();
		
		int DEGREE = dimension * dimension;
		
		this.id = id;
		this.name = name + '-' + id;
		
		QS = new QueueSwitch("QS", id, 2);
		PE = new ProcessingElement("PE", id, dimension);
		
		this.getSubModels().add(QS);
		this.getSubModels().add(PE);

		v_in_port = new Vector<Port>();
		v_out_port = new Vector<Port>();

		
		for (int i = 0; i < DEGREE; i++) {
			v_out_port.add(i, new Port(this, "out_port-"+i));
			this.addOutPort(v_out_port.get(i));
		}
		
		for (int i = 0; i < DEGREE; i++) {
			v_in_port.add(i, new Port(this, "in_port-"+i));
			this.addInPort(v_in_port.get(i));
		}
		
		for (int i = 0; i < DEGREE; i++) {
			this.addEIC(this.getInPort("in_port-"+i), this.getSubModel("QS"+'-'+id).getInPort("in_queue-"+i));
		}
		
		for (int i = 0; i < DEGREE; i++) {
			this.addEOC(this.getSubModel("QS"+'-'+id).getOutPort("out_queue-"+i), this.getOutPort("out_NCUnit-"+i));
		}
		
		this.addIC(this.getSubModel("PE"+'-'+id).getOutPort("out_cmd"), this.getSubModel("QS"+'-'+id).getInPort("in_PE"));
		this.addIC(this.getSubModel("QS"+'-'+id).getOutPort("out_PE"), this.getSubModel("PE"+'-'+id).getInPort("in_task"));
		this.addIC(this.getSubModel("PE"+'-'+id).getOutPort("out_queue"), this.getSubModel("QS"+'-'+id).getInPort("in_queue-0"));
		
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
