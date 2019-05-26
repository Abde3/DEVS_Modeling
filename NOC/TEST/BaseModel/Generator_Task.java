package BaseModel;


import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import NocTopology.NOCDirections.IPoint;
import Verification.StateRepresentation;


import java.util.Random;


public class Generator_Task extends DEVSAtomic {

	private int networkSize;
	private int dest_x;
	private int dest_y;

	public void setState(State state) {
		this.state = state;
		StateRepresentation.addState( this.name, String.valueOf(state));

	}

	private enum State {WAIT, GENERATE, WAIT4OK, SET_STATUS}
	public static Random random_generator = new Random();

	/******************************************************************************************************************/
	private Port out;				/**************************** OutPort of the model ********************************/
	private String 	value_out;

	private State 	state;			/***************************  Represent the state     *****************************/
	private float 	rho;			/***************************  Time elapsed in a state *****************************/


	String data;
	boolean outQstatus;


	public Generator_Task(int networkSize, String name, String data, int dest_x, int dest_y) {
		super();

		this.networkSize = networkSize;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
        this.data = data;

        this.name = name;
        this.out = new Port(this, "out");

        this.addOutPort(this.out);
	}


	@Override
	public void deltaExt(Port port, Object event, float elapsed_time_in_state) {
		/* NO EXTERNAL EVENT */
	}

	
	@Override
	public void deltaInt() {

		if (state.equals(State.WAIT)) {
			value_out = data;
			setState(State.GENERATE);
			rho = 10;

		} else if (state.equals(State.GENERATE)) {
			setState(State.WAIT);
			rho = Float.POSITIVE_INFINITY;
		}

	}

	@Override
	public float getDuration() {
		return rho;
	}

	@Override
	public void init() {
		setState(State.WAIT);
		rho   =  0F;
		outQstatus = true;
		LOG.printThis(this.name,"STARTING GENERATOR");
	}

	@Override
	public Object[] lambda() {
		Object[] output;

		if (state.equals(State.GENERATE)) {
			output = setOutputLambda(out, value_out);
			LOG.printThis(this.name,  " DATA " + value_out + " sent!");
		} else {
			output = null;
		}

		return output;
	}

	private Object[] setOutputLambda(Port port, Object value) {
        Object[] output = new Object[2];

        output[0] = port;
        output[1] = value;

		LOG.logThis(this.name, value);

        return output;
    }

}
