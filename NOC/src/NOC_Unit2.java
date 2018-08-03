import java.util.Arrays;
import java.util.Vector;

import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;
import DEVSModel.Port;

public class NOC_Unit2 extends DEVSCoupled {
	@Override
	public void setSelectPriority() {

	}
//
//	public  Vector<Port> v_in_NCUnit;
//	public  Vector<Port> v_out_NCUnit;
//	private QueueSwitch QS;
//	private ProcessingElement PE;
//	private int id;
//
//	public NOC_Unit2(String name, int id, int dimension){
//
//		super();
//
//		int DEGREE = dimension * dimension;
//
//		this.id = id;
//		this.name = name + '-' + id;
//
//		QS = new QueueSwitch("QS", id, 2);
//		PE = new ProcessingElement("PE", id, dimension);
//
//		this.getSubModels().add(QS);
//		this.getSubModels().add(PE);
//
//		v_in_NCUnit  = new Vector<Port>();
//		v_out_NCUnit = new Vector<Port>();
//
//
//		for (int i = 0; i < DEGREE; i++) {
//			v_out_NCUnit.add(i, new Port(this, "out_NCUnit-"+i));
//			this.addOutPort(v_out_NCUnit.get(i));
//		}
//
//		for (int i = 0; i < DEGREE; i++) {
//			v_in_NCUnit.add(i, new Port(this, "in_NCUnit-"+i));
//			this.addInPort(v_in_NCUnit.get(i));
//		}
//
//		for (int i = 0; i < 2; i++) {
//			this.addEIC(this.getInPort("in_NCUnit-"+i), this.getSubModel("QS"+'-'+id).getInPort("in_queue"));
//		}
//
//		for (int i = 0; i < 2; i++) {
//			this.addEOC(this.getSubModel("QS"+'-'+id).getOutPort("out_queue-"+i), this.getOutPort("out_NCUnit-"+i));
//		}
//
//		this.addIC(this.getSubModel("PE"+'-'+id).getOutPort("out_cmd"), this.getSubModel("QS"+'-'+id).getInPort("in_PE"));
//		this.addIC(this.getSubModel("QS"+'-'+id).getOutPort("out_PE"), this.getSubModel("PE"+'-'+id).getInPort("in_task"));
//		this.addIC(this.getSubModel("PE"+'-'+id).getOutPort("out_queue"), this.getSubModel("QS"+'-'+id).getInPort("in_queue"));
//
//		this.setSelectPriority();
//
//	}
//
//	@Override
//	public void setSelectPriority() {
//
//		/****************************************************************************************************/
//		/***********************************************************| CONFLICTING SET |****|SELECT|***********/
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(  PE,       QS) ), 	QS);
//		/***********************************************************| CONFLICTING SET |****|SELECT|***********/
//		/****************************************************************************************************/
//
//	}
//

}
