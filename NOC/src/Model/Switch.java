package Model;

import java.util.Random;
import java.util.Vector;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;


// Z Aloi Event B TLA Timed Automata 
// WHANG MOON


/********************************************************************************************************************************
 *
 *
 *                                                           SWITCH
 *
 *                          +----------------------------------------------------------------------+             ------+
 *                          |                          +--------------+                            |    out_cmd_queue0 |
 *                          |                 +....... |   WAIT_PE    <------------------+         +------>            |
 *                          |                 :        |              |                  |         |                   | i times
 *                          |                 :        +--------------+                  |         |    out_cmd_queue1 |
 *                          |                 :                                          |         +------>            |
 *                          |                 :                                          |         |             ------+
 *         +------          |                 :                               +----------+---+     |             ------+
 *         |                |    +------------v-+          +..................>   SEND_PE    |     |                   |
 *         | in_task_queue0 |    |     IDLE     |          :                  |              |     |   out_task_next0  |
 *         |      +--------->    |              +----------:---------+        +----------+---+     +------>            |
 * i times |                |    +------------+-+          :         |                   |         |                   | i times
 *         |                |                 |            :         |                   |         |                   |
 *         | in_task_queue1 |                 |            :         |                   |         |   out_task_next1  |
 *         |      +--------->                 |            :         |                   |         +------>            |
 *         +-----           |                 |            :         |                   |         |             ------+
 *                          |                +v------------++      +-v------------+      |         |
 *                          |                |  SEND_CMD    <......+   SEND_NEXT  |      |         |
 *                          |                |              |      |              <------+         |
 *            in_cmd_PE     |                +--------------+      +--------------+                |      out_task_PE
 *                +--------->                                                                      +------>
 *                          |                                                                      |
 *                          +----------------------------------------------------------------------+
 *
 *
 *******************************************************************************************************************************/



public class Switch extends DEVSAtomic {

	private enum SWITCH_STATE {IDLE, SENDING_TO_PE, SENDING_TO_NEXT, WAITING_PE, SENDING_CMD_TO_QUEUE};

	Vector<Port> v_in_task_queue;
	Vector<Port> v_out_cmd_queue;
	Vector<Port> v_out_task_next;

	Port in_cmd_PE;
	Port out_task_PE;

	Task value_in_task_queue;
	Task value_out_cmd_queue;
	Task value_out_task_PE;
	Task value_out_task_next;
	
	Vector<Task> v_value_out_task_next;
	Queue.COMMAND value_in_cmd_PE;

	SWITCH_STATE 	state;
	NodeCoordinate id;

	float 	rho;

	
	private Random random_generator;
	private int dimension;
	
	public Switch(String name, NodeCoordinate id, int dimension) {
		super();

		int DEGREE = dimension * dimension;

		this.id 			= id;
		this.name 			= name + id;

		this.dimension = dimension;
		random_generator = new Random();
		
		
		this.in_cmd_PE  	= new Port(this, "in_cmd_PE");	
		this.out_task_PE    = new Port(this, "out_task_PE");
		this.v_value_out_task_next = new Vector<>();

		this.addOutPort(out_task_PE);
		this.addInPort(in_cmd_PE);

		
		v_in_task_queue = new Vector<>();
		for (int i = 0; i < DEGREE; i++) {
			Port port_tmp = new Port(this, "in_task_queue-" + NOC_MESH.getDirectionFromIndex(i));
			this.v_in_task_queue.add(i, port_tmp);
			this.addInPort(port_tmp);
		}
		
		v_out_task_next = new Vector<>();
		for (int i = 0; i < DEGREE; i++) {
			Port port_tmp = new Port(this, "out_task_next-" + NOC_MESH.getDirectionFromIndex(i));
			this.v_out_task_next.add(i, port_tmp);
			this.addOutPort(this.v_out_task_next.get(i));
		}
		
		v_out_cmd_queue = new Vector<>();
		for (int i = 0; i < DEGREE; i++) {
			Port cmd_tmp = new Port(this, "out_cmd_queue-" + NOC_MESH.getDirectionFromIndex(i));
			this.v_out_cmd_queue.add(i, cmd_tmp);
			this.addOutPort(cmd_tmp);
		}
		
	} 

	
	@Override
	public void deltaExt(Port port, Object event, float elapsed_time_in_state) {
		
		switch (state) {
		case IDLE:
		{
			if ( v_in_task_queue.contains(port) ) {

				value_in_task_queue = ((Task)event);
				
				if ( value_in_task_queue.getDestination() == id ) {
					value_out_task_PE = value_in_task_queue;
					state = SWITCH_STATE.SENDING_TO_PE;
					Pretty_print.trace( this.name , "IDLE -> SENDING_TO_PE(ρ = 0)");

				} else {
					value_out_task_next = value_in_task_queue;
					state = SWITCH_STATE.SENDING_TO_NEXT;
					Pretty_print.trace( this.name , "IDLE -> SENDING_TO_NEXT(ρ = 0)");
				}

			} else {
				Pretty_print.trace_err( this.name , "RECEIVED UNEXPECTED CMD");
			}
			
			break;
		}
		case SENDING_TO_PE:
		{
			Pretty_print.trace_err( this.name , "BAD STATE");
			break;
		}
		case SENDING_CMD_TO_QUEUE:
		{
			Pretty_print.trace_err( this.name , "BAD STATE");
			break;
		}
		case WAITING_PE:
		{

			if ( v_in_task_queue.contains(port) ) {
				
				Pretty_print.trace_err( this.name , "UNEXPECTED TASK RECEIVED INSTEAD OF CMD");
				
			} else {
				state = SWITCH_STATE.SENDING_CMD_TO_QUEUE;
				Pretty_print.trace( this.name , "WAITING_PE -> SENDING_CMD_TO_QUEUE(ρ = 0)");

			}

			break;
		}
		case SENDING_TO_NEXT:
		{
			Pretty_print.trace_err( this.name , "BAD STATE");
			break;
		}
		default:
			Pretty_print.trace_err( this.name , "BAD STATE");
			break;
		}

	}

	@Override
	public void deltaInt() {


		switch (state) {
		case IDLE:
		{
			break;
		}
		case SENDING_TO_PE:
		{
			state = SWITCH_STATE.WAITING_PE;
			Pretty_print.trace( this.name , "SENDING_TO_PE -> WAITING_PE(ρ = +inf)");
			break;
		}
		case SENDING_CMD_TO_QUEUE:
		{
			state = SWITCH_STATE.IDLE;
			break;
		}
		case WAITING_PE:
		{
			break;
		}
		case SENDING_TO_NEXT: 
		{
			state = SWITCH_STATE.SENDING_CMD_TO_QUEUE;
			Pretty_print.trace( this.name , "SENDING_TO_NEXT -> SENDING_CMD_TO_QUEUE(ρ = 0)");
			break;
		}
		default:
			break;
		}

	}

	@Override
	public float getDuration() {
		switch (state) {
		case IDLE:
			rho = Float.POSITIVE_INFINITY;
			break;

		case SENDING_TO_PE:
			rho = 0F;
			break;

		case WAITING_PE:
			rho = Float.POSITIVE_INFINITY;
			break;

		case SENDING_CMD_TO_QUEUE:
			rho = 0F;
			break;

		case SENDING_TO_NEXT:
			rho = 0F;
			break;

		default:
			Pretty_print.trace_err( this.name , "UNKNOWN STATE (" + state + ")");
			break;
		}

		return rho;
	}

	@Override
	public void init() {
		state = SWITCH_STATE.IDLE;
		Pretty_print.trace( this.name , "STARTING IDLE(ρ = +inf)");
	}

	@Override
	public Object[] lambda() {

		if (state.equals(SWITCH_STATE.SENDING_CMD_TO_QUEUE)) {
			Object[] output = new Object[2];

			output[0] = this.v_out_cmd_queue.get(NOC_MESH.DIRECTION.WEST.ordinal());
			output[1] = Queue.COMMAND.NEXT_TASK;

			Pretty_print.trace( this.name , "ASK NEXT_TASK TO QUEUE-" + this.id);
			return output;

		} else if (state.equals(SWITCH_STATE.SENDING_TO_PE)) {

			Object[] output = new Object[2];

			output[0] = this.out_task_PE;
			output[1] = value_out_task_PE;
			Pretty_print.trace( this.name , "SEND " + value_out_task_PE.getName() +  " TO PE-" + this.id);
			
			return output;

		} else if (state.equals(SWITCH_STATE.SENDING_TO_NEXT)) {

			Object[] output = new Object[2];

			output[0] = this.v_out_task_next.get(NOC_MESH.DIRECTION.EAST.ordinal());
			output[1] = value_out_task_next;
			Pretty_print.trace( this.name , "SEND "  + value_out_task_next.getName() +  " TO NEXT PE: " + output[1]);
			
			return output;

		} else {
			return null;
		}

	}

}














