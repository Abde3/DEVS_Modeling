package Model;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;


public class ProcessingElement extends DEVSAtomic {


	public enum STATE {IDLE, PROCESSING, NOTIFY}

	Port 	in_task,
	out_cmd,			
	out_queue;

	Task 			value_in_task;	
	Task 			value_out_next;
	Queue.COMMAND   value_out_cmd;

	STATE state;
	long nbCycleProcessed;
	NodeCoordinate coordinate;
	int dimension;
	float rho;

	
	public ProcessingElement(String name, NodeCoordinate coordinate) {
		super();

		this.name 		= name + '-' + coordinate;

		this.in_task 	= new Port(this, "in_task");
		this.out_queue 	= new Port(this, "out_queue");
		this.out_cmd 	= new Port(this, "out_cmd");

		this.addOutPort(this.out_queue);
		this.addOutPort(this.out_cmd);
		this.addInPort(this.in_task);
	}

	
	
	
	@Override
	public void deltaExt(Port port, Object event, float elapsed_time_in_state) {
		/* THE INPUT EVENT IS A COMMAND FROM THE SWITCH */

		
		if(port.equals(this.in_task)) {
			value_in_task = ((Task)event);
			
			Pretty_print.trace( this.name , "RECEIVED TASK : " + value_in_task);

			if ( state.equals(STATE.IDLE) ) {
				value_out_next = value_in_task;

				if (value_out_next.getDestination() == this.coordinate) {

					value_out_next.setDestination( value_out_next.getDestination() );
					value_out_next.increment_age();
					
					state = STATE.PROCESSING;

					Pretty_print.trace( this.name , "IDLE -> PROCESSING(ρ = "+ value_out_next.getComputation_requirement() + ")");

				} else {
					Pretty_print.trace_err( this.name , "BAD INPUT TASK DESTINATION");
				}

			} else if ( state.equals(STATE.PROCESSING) ) {
				Pretty_print.trace_err( this.name , "SHOULD NOT BE HERE !");
			} else {
				Pretty_print.trace_err( this.name , "UNKNOWN STATE (" + state + ")");
			}

		} else {
			Pretty_print.trace_err( this.name , "BAD PORT");
		}
	}

	@Override
	public void deltaInt() {

		if (state.equals(STATE.IDLE)) {
			Pretty_print.trace_err( this.name , "SHOULD NOT BE HERE");

		} else if (state.equals(STATE.PROCESSING)) {
			
			state = STATE.NOTIFY;
			Pretty_print.trace( this.name , "IDLE -> " + state + "(ρ = 0)");

		} else if (state.equals(STATE.NOTIFY)) {

			state = STATE.IDLE;
			Pretty_print.trace( this.name , "NOTIFY -> " + state + "(ρ = +inf)");

		} else {
			Pretty_print.trace( this.name , "ERROR DELTAINT UNKNOWN STATE");
		}
	}

	@Override
	public float getDuration() {
		
		switch (state) {
			case IDLE:
				rho = Float.POSITIVE_INFINITY;
				break;
	
			case PROCESSING:
				rho = value_out_next.getComputation_requirement();
				break;
				
			case NOTIFY:
				rho = 0F;
				break;
	
			default:
				break;
		}

		return rho;
	}

	@Override
	public void init() {
		state = STATE.IDLE;
		Pretty_print.trace( this.name , "STARTING IDLE(ρ = +inf) ");
	}

	@Override
	public Object[] lambda() {
				
		if (state.equals(STATE.PROCESSING)) {

			if ( value_out_next.is_completed() ) {

				Pretty_print.trace( this.name , "TASK FINISHED: " + value_out_next.getName());
				return null;

			} else {
				Object[] output = new Object[2];
				output[0] = this.out_queue;
				output[1] = value_out_next;

				Pretty_print.trace( this.name , "TASK COMPLETED: " + value_out_next.getName());
				return output;

			}

		} else if (state.equals(STATE.NOTIFY)) {
			Object[] output = new Object[2];
			output[0] = this.out_cmd;
			output[1] = Queue.COMMAND.NEXT_TASK;

			Pretty_print.trace( this.name , "ASK NEXT_TASK TO SWITCH-" + this.coordinate);

			return output;
		} else {
			
			return null;
		}
	}


	public Port getPortIn_task() {
		return in_task;
	}

	public Port getPortOut_cmd() {
		return out_cmd;
	}

	public Port getPortOut_queue() {
		return out_queue;
	}


}
