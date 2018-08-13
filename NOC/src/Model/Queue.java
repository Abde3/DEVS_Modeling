package Model;

import java.util.concurrent.ConcurrentLinkedQueue;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;


public class Queue extends DEVSAtomic {

	public enum COMMAND {NEXT_TASK}
	public enum QUEUE_STATE {IDLE, WAITING_CMD, WAITING_TASK, SENDING}

	Port in_task;
	Port in_command;
	Port out_switch;

	Task 		value_in_task;	
	COMMAND 	value_in_command;
	Task 		value_out_switch;

	ConcurrentLinkedQueue<Task> queue; 
	NodeCoordinate coordinate;
	NOC_MESH.DIRECTION direction;

	QUEUE_STATE state;
	float rho;


	public Queue(String name, NodeCoordinate coordinate, NOC_MESH.DIRECTION direction) {
		super();

		this.coordinate = coordinate;
		this.name 		= name + coordinate + '-' + direction;

		this.out_switch = new Port(this, "out_switch");
		this.in_task 	= new Port(this, "in_task");
		this.in_command = new Port(this, "in_command");

		this.direction = direction;

		queue = new ConcurrentLinkedQueue<>();

		this.addOutPort(this.out_switch);
		this.addInPort(this.in_task);
		this.addInPort(this.in_command);
	}


	@Override
	public void init() {
		state = QUEUE_STATE.WAITING_TASK;
//		Pretty_print.trace( this.name , "WAITING_TASK(ρ = +inf)");
	}
	
	
	@Override
	public void deltaExt(Port port, Object event, float elapsed_time_in_state) {
				
		
		switch (state) {
		case IDLE:
		{
			if (port.equals(in_task)) {
				value_in_task = ((Task)event);
				queue.add(value_in_task);
				
				state = QUEUE_STATE.WAITING_CMD;
				Pretty_print.trace( this.name , "RECEIVED " + value_in_task.getName() + " : WAITING_CMD (size: " + queue.size() + ")");

			} else {
				
				state = QUEUE_STATE.WAITING_TASK;
				Pretty_print.trace( this.name , "RECEIVED COMMAND : WAITING TASK (size: " + queue.size() + ")");
				
			}
			
			break;
		}
		case WAITING_CMD:
		{
			if (port.equals(in_task)) {
				
				value_in_task = ((Task)event);
				queue.add(value_in_task);
				
				state = QUEUE_STATE.WAITING_CMD;
				Pretty_print.trace( this.name , "RECEIVED " + value_in_task.getName() + " : WAITING_CMD (size: " + queue.size() + ")");
				
			} else {
				
				state = QUEUE_STATE.SENDING;
				Pretty_print.trace( this.name , "RECEIVED COMMAND : SENDING (size: " + queue.size() + ")");
			
			}
			
			break;
		}
		case SENDING:
		{
			Pretty_print.trace_err( this.name , "RECIEVED TASK WHILE SENDING");
			break;
		}
		case WAITING_TASK:
		{
			if (port.equals(in_task)) {
				value_in_task = ((Task)event);
				queue.add(value_in_task);
				
				state = QUEUE_STATE.SENDING;
				Pretty_print.trace( this.name , "RECEIVED " + value_in_task.getName() + " : SENDING (size: " + queue.size() + ")");

			} else {
				state = QUEUE_STATE.WAITING_TASK;
				Pretty_print.trace( this.name , "RECEIVED CMD : WAITING_TASK (size: " + queue.size() + ")");
			}
			
			break;
		}
		default:
			Pretty_print.trace_err( this.name , "UKNOWN STATE");
			break;
		}
		
	}

	

	@Override
	public Object[] lambda() {
		
		if (state.equals(QUEUE_STATE.SENDING)) {

			Object[] output = new Object[2];

			output[0] = this.out_switch;
			output[1] = queue.poll();

//			Pretty_print.trace( this.name , "SEND " + ((Task) output[1]).getName() + " TO SWITCH " + " (size: " + queue.size() + ")");

			return output;
		} else {
			return null;
		}
		
	}
	
	
	@Override
	public void deltaInt() {

		switch (state) {
		case IDLE:
			break;
		case WAITING_CMD:
			break;
		case SENDING:
		{
			if (queue.size() != 0) {
				state = QUEUE_STATE.WAITING_CMD;	
				Pretty_print.trace( this.name , "SENDING -> WAITING_CMD (size: " + queue.size() + ")");

			} else {
				state = QUEUE_STATE.IDLE;	
				Pretty_print.trace( this.name , "SENDING -> IDLE (size: " + queue.size() + ")");

			}
			break;
		}
		case WAITING_TASK:
			break;
		default:
			Pretty_print.trace_err( this.name , "UKNOWN STATE");
			break;
		}
	}

	@Override
	public float getDuration() {

		switch (state) {
		case IDLE:
			rho = Float.POSITIVE_INFINITY;
			break;
		case WAITING_CMD:
			rho = Float.POSITIVE_INFINITY;
			break;
		case WAITING_TASK:
			rho = Float.POSITIVE_INFINITY;
			break;
		case SENDING:
			rho = 0F;
			break;
		default:
			break;
		}

		return rho;
	}


	public Port getIn_task() {
		return in_task;
	}

	public Port getIn_command() {
		return in_command;
	}

	public Port getOut_switch() {
		return out_switch;
	}

}
