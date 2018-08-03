package Model;

import java.util.Arrays;
import java.util.Vector;

import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;
import DEVSModel.Port;

public class QueueSwitch extends DEVSCoupled {

	public  Port in_PE;
	public  Port out_PE;
	public  Vector<Port> v_in_queue;
	public  Vector<Port> v_out_queue;
	private Vector<Queue> v_queue;
	private Switch switch1;


	public QueueSwitch(String name, NodeCoordinate coordinate, int dimension){
		super();
		
		int DEGREE = dimension * dimension;

		this.name = name + coordinate;
		this.v_queue = new Vector<>();

		for (int i = 0; i < DEGREE; i++) {
			Queue queue_tmp = new Queue("queue", coordinate, i);
			this.getSubModels().add(queue_tmp);
			v_queue.add(queue_tmp);
		}
		
		v_in_queue = new Vector<Port>();
		for (int i = 0; i < DEGREE; i++) {
			Port port_tmp = new Port(this, "in_queue-"+i);
			v_in_queue.add(i, port_tmp);
			this.addInPort(port_tmp);			
		}
		
		v_out_queue = new Vector<Port>();
		for (int i = 0; i < DEGREE; i++) {
			Port port_tmp = new Port(this, "out_queue-"+i);
			v_out_queue.add(i, port_tmp);
			this.addOutPort(port_tmp);			
		}

		switch1 = new Switch("switch", coordinate, dimension);
		this.getSubModels().add(switch1);

		out_PE 		= new Port(this, "out_PE");
		in_PE	 	= new Port(this, "in_PE");
		
		this.addInPort(in_PE);
		this.addOutPort(out_PE);
		
		/******** LINKS *************************************************************************************************/
		
		
		this.addEIC(in_PE, switch1.getInPort("in_cmd_PE"));
		for (int i = 0; i < DEGREE; i++) { this.addEIC(v_in_queue.elementAt(i), v_queue.get(i).getIn_task()); }

		
		this.addEOC(switch1.getOutPort("out_task_PE"), out_PE);
		for (int i = 0; i < DEGREE; i++) {
			this.addEOC(switch1.getOutPort("out_task_next-"+i), this.getOutPort("out_queue-"+i));
		}

		
		for (int i = 0; i < DEGREE; i++) {
			this.addIC(this.getSubModel("queue"+coordinate+'-'+i).getOutPort("out_switch"), this.getSubModel("switch"+coordinate).getInPort("in_task_queue-"+i));
			this.addIC(this.getSubModel("switch"+coordinate).getOutPort("out_cmd_queue-"+i), this.getSubModel("queue"+coordinate+'-'+i).getInPort("in_command"));
		}

		this.setSelectPriority();
	}

	@Override
	public void setSelectPriority() {
		
		/****************************************************************************************************/
		/***********************************************************| CONFLICTING SET |****|SELECT|***********/

		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(1)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(2)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(3)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(1),v_queue.get(2)) ), v_queue.get(1));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(1),v_queue.get(3)) ), v_queue.get(1));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(1),switch1) ), v_queue.get(1));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(2),v_queue.get(3)) ), v_queue.get(2));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(2),switch1) ), v_queue.get(2));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(3),switch1) ), v_queue.get(3));

		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(1),v_queue.get(2)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(1),v_queue.get(3)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(1),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(2),v_queue.get(3)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(2),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(0),v_queue.get(3),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(1),v_queue.get(2),v_queue.get(3)) ), v_queue.get(1));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(1),v_queue.get(2),switch1) ), v_queue.get(1));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(1),v_queue.get(3),switch1) ), v_queue.get(1));
		this.selectPriority.put(new Vector<>( Arrays.asList( v_queue.get(2),v_queue.get(3),switch1) ), v_queue.get(2));

		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList( v_queue.get(0),v_queue.get(1),v_queue.get(2),v_queue.get(3)) ), v_queue.get(0));
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList( v_queue.get(0),v_queue.get(1),v_queue.get(2),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList( v_queue.get(0),v_queue.get(1),v_queue.get(3),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList( v_queue.get(0),v_queue.get(2),v_queue.get(3),switch1) ), v_queue.get(0));
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList( v_queue.get(1),v_queue.get(2),v_queue.get(3),switch1) ), v_queue.get(1));

		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList( v_queue.get(0),v_queue.get(1),v_queue.get(2),v_queue.get(3),switch1) ), v_queue.get(0));



		
		/***********************************************************| CONFLICTING SET |****|SELECT|***********/
		/****************************************************************************************************/
	}


}
