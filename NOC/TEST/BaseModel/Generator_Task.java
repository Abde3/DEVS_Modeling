package BaseModel;

import java.util.Random;
import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import NOCUnit.NodeCoordinate;


public class Generator_Task extends DEVSAtomic {

	private enum State {WAIT, GENERATE;}
	private Random random_generator;

	/******************************************************************************************************************/
	private Port 	out;			/**************************** OutPort of the model ********************************/
	private Task 	value_out;		/**************************** Represent the value in the OutPort ******************/

	private State 	state;			/***************************  Represent the state     *****************************/
	private float 	rho;			/***************************  Time elapsed in a state *****************************/


	public Generator_Task(String name) {
		super();
        random_generator = new Random();

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
            int id = random_generator.nextInt(99) + 1;
            int computation_requirement = random_generator.nextInt(4) + 1;
            NodeCoordinate destination = new NodeCoordinate(random_generator.nextInt(3) + 1, random_generator.nextInt(3) + 1);

            value_out = new Task(id, computation_requirement, destination);
			state     = State.GENERATE;
			rho       = 0F;
		} else if (state.equals(State.GENERATE)) {
			state = State.WAIT;
			rho   = random_generator.nextInt(2)+1;
		}

	}

	@Override
	public float getDuration() {
		return rho;
	}

	@Override
	public void init() {
		state = State.WAIT;
		rho   =  0F;
//		System.out.println("STARTING GENERATOR");
	}

	@Override
	public Object[] lambda() {
		Object[] output;

		if (state.equals(State.GENERATE)) {
			output = setOutputLambda(out, value_out);
//			Pretty_print.trace(this.name,  " TASK " + value_out + " created!");
		} else {
			output = null;
		}

		return output;
	}


	private Object[] setOutputLambda(Port port, Object value) {
        Object[] output = new Object[2];

        output[0] = port;
        output[1] = value;

        return output;
    }

}
