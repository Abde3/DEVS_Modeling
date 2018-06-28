import java.util.Random;
import DEVSModel.DEVSAtomic;
import DEVSModel.Port;



public class Generator_Task extends DEVSAtomic {

	private enum State {WAIT, GENERATE};

	private Port 	out;
	private Task 	value_out;

	private State 	state;
	private float 	rho;
	
	private Random random_generator;

	public Generator_Task(String name) {
		super();
		
		this.name = name;
		this.out = new Port(this, "out");

		this.addOutPort(this.out);		

		random_generator = new Random();
		
	}


	@Override
	public void deltaExt(Port port, Object event, float elapsed_time_in_state) {
		/* NO EXTERNAL EVENT */
	}

	
	@Override
	public void deltaInt() {

		if (state.equals(State.WAIT)) {
			value_out = new Task(random_generator.nextInt(99)+1, random_generator.nextInt(4), random_generator.nextInt(3)+1);
			state     = State.GENERATE;
			rho       = 0F;
		} else if (state.equals(State.GENERATE)) {
			state = State.WAIT;
			rho   =  random_generator.nextInt(5)+1;
		}

	}

	@Override
	public float getDuration() {
		return rho;
	}

	@Override
	public void init() {
		state = State.WAIT;
		rho   =  1F;
		System.out.println("STARTING GENERATOR");

	}

	@Override
	public Object[] lambda() {
		Object[] output;

		if (state.equals(State.GENERATE)) {
			output = new Object[2];

			output[0] = this.out;
			output[1] = value_out;
			System.out.println(this.name + " TASK " + value_out + " created!");
		} else {
			output = null;
		}

		return output;
	}

}
