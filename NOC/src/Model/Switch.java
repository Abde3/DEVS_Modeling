package Model;

import java.util.ArrayList;
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

    private String lastEntryPort;

    private enum SWITCH_STATE {IDLE, SENDING_TO_PE, SENDING_TO_NEXT, WAITING_PE, SENDING_CMD_TO_QUEUE};
	boolean PEIsBusy = false;


	Vector<Port> v_in_task_queue;
	Vector<Port> v_out_cmd_queue;
	Vector<Port> v_out_task_next;

	Port in_cmd_PE;
	Port out_task_PE;
	Port in_task_PE;

	Task value_in_task_queue;
	Task value_out_cmd_queue;
	Task value_out_task_PE;
	Task value_out_task_next;
	
	Vector<Task> v_value_out_task_next;
	Queue.COMMAND value_in_cmd_PE;

	SWITCH_STATE 	state;
	NodeCoordinate id;
    ArrayList<NOC_MESH.DIRECTION> directionsPossible;

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
        this.in_task_PE     = new Port(this, "in_task_PE");
        this.out_task_PE    = new Port(this, "out_task_PE");
		this.v_value_out_task_next = new Vector<>();

		this.addOutPort(out_task_PE);
		this.addInPort(in_cmd_PE);
		this.addInPort(in_task_PE);

		
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

        directionsPossible = NOC_Unit_factory.getAlldirectionsforNode(Util.nodeCoordinateFromElementName(getName()), 4, NOC_factory.Topology.MESH);

	} 

	
	@Override
	public void deltaExt(Port port, Object event, float elapsed_time_in_state) {


		if (port.equals(in_task_PE)) {
			System.err.println("ooook - " + state + "  received " + event.toString());

		}

		switch (state) {
		case IDLE:
		{
			if ( v_in_task_queue.contains(port) ) {

				value_in_task_queue = ((Task)event);
				
				if ( value_in_task_queue.getDestination().equals(id) ) {
					value_out_task_PE = value_in_task_queue;
					state = SWITCH_STATE.SENDING_TO_PE;
					PEIsBusy = true;
					Pretty_print.trace( this.name , "IDLE -> SENDING_TO_PE(ρ = 0)");

				} else {
					value_out_task_next = value_in_task_queue;
					state = SWITCH_STATE.SENDING_TO_NEXT;
					PEIsBusy = false;
					Pretty_print.trace( this.name , "IDLE -> SENDING_TO_NEXT(ρ = 0)");
					lastEntryPort = port.getName();
				}

			} else if (port.equals(in_task_PE)) {

                Pretty_print.trace_err( this.name , "IN tASK PE");
				value_out_task_next = value_in_task_queue;
				state = SWITCH_STATE.SENDING_TO_NEXT;
				PEIsBusy = false;
				Pretty_print.trace( this.name , "IDLE -> SENDING_TO_NEXT(ρ = 0)");

            } else {
				//Pretty_print.trace_err( this.name , "RECEIVED UNEXPECTED CMD");
				state = SWITCH_STATE.SENDING_CMD_TO_QUEUE;
				Pretty_print.trace( this.name , "WAITING_PE -> SENDING_CMD_TO_QUEUE(ρ = 0)");
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

				value_in_task_queue = ((Task)event);

				if ( value_in_task_queue.getDestination().equals(id) ) {

					Pretty_print.trace_err( this.name , "TASK RECEIVED FOR PE WHILE BUSY");

				} else {
					value_out_task_next = value_in_task_queue;
					state = SWITCH_STATE.SENDING_TO_NEXT;
					Pretty_print.trace( this.name , "IDLE -> SENDING_TO_NEXT(ρ = 0)");
					lastEntryPort = port.getName();
				}


			} else if (port.equals(in_task_PE)) {

				value_in_task_queue = ((Task)event);
				// Pretty_print.trace_err( this.name , "OOOOOOOOOOW");

				value_out_task_next = value_in_task_queue;
				state = SWITCH_STATE.SENDING_TO_NEXT;
				Pretty_print.trace( this.name , "IDLE -> SENDING_TO_NEXT(ρ = 0)");
				PEIsBusy = false;

			} else {
				state = SWITCH_STATE.SENDING_CMD_TO_QUEUE;
				Pretty_print.trace( this.name , "WAITING_PE -> SENDING_CMD_TO_QUEUE(ρ = 0)");
			}

			break;
		}
		case SENDING_TO_NEXT:
		{
			if (! port.equals(in_cmd_PE)) {
				Pretty_print.trace_err( this.name , "BAD STATE");
			}
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
                PEIsBusy = true;
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
			rho = 1F;
			break;

		case WAITING_PE:
			rho = Float.POSITIVE_INFINITY;
			break;

		case SENDING_CMD_TO_QUEUE:
			rho = 1F;
			break;

		case SENDING_TO_NEXT:
			rho = 1F;
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
		PEIsBusy = false;
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

            output[0] = this.v_out_task_next.get(getOutputDirection_X_Y());
			output[1] = value_out_task_next;
			Pretty_print.trace( this.name , "SEND "  + value_out_task_next.getName() +  " TO NEXT PE: " + output[1]);
			
			return output;

		} else {
			return null;
		}
	}

    private int getOutputDirection_random_notLast() {

        int direction;

        do {
            direction = directionsPossible.get(new Random().nextInt(directionsPossible.size())).ordinal();
        } while ( direction == NOC_MESH.DIRECTION.valueOf(Util.getDirectionFromElementName(lastEntryPort)).ordinal());

        return direction;
    }


    private int getOutputDirection_X_Y() {

        int direction = -1;

        do {
            direction = computeNextStepX_Y().ordinal();
        } while ( direction == -1 ||
				(lastEntryPort != null && direction == NOC_MESH.DIRECTION.valueOf(Util.getDirectionFromElementName(lastEntryPort)).ordinal()));

        return direction;
    }

    private NOC_MESH.DIRECTION computeNextStepX_Y() {
	    NodeCoordinate source = this.id;
	    NodeCoordinate target = value_out_task_next.getDestination();

        NOC_MESH.DIRECTION nextDirectionStep = null;

	    if( target.isOnLeftOf(source) ) {
            nextDirectionStep = NOC_MESH.DIRECTION.WEST;
        } else if ( target.isOnRightOf(source) ) {
            nextDirectionStep = NOC_MESH.DIRECTION.EAST;
        } else {
            if( target.isOnTopOf(source) ) {
                nextDirectionStep =  NOC_MESH.DIRECTION.NORTH;
            } else if ( target.isUnderOf(source) ) {
                nextDirectionStep = NOC_MESH.DIRECTION.SOUTH;
            } else {
                System.out.println("ERROR IN SWITCHING LOGIC ! TASK SHOULD BE SEND TO PE");
            }
        }

        if (directionsPossible.indexOf(nextDirectionStep) == -1) {
            System.out.println("ERROR IN SWITCHING LOGIC ! Comptuted step is not possible");
            nextDirectionStep = null;
        }

        // System.out.println(source + " --> " + target + " : " + nextDirectionStep);

	    return nextDirectionStep;
    }

}














