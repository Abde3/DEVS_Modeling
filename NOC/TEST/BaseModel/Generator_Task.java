package BaseModel;


import Library.DEVSModel.DEVSAtomic;
import Library.DEVSModel.Port;
import NocTopology.NOCDirections.IPoint;
import Verification.StateRepresentation;

import java.util.Random;


public class Generator_Task extends DEVSAtomic {

	public void setState(State state) {
		this.state = state;
		StateRepresentation.addState( this.name, String.valueOf(state));

	}

	private enum State {WAIT, SENDOUT}
	private Random random_generator;

	/******************************************************************************************************************/
	private Port out;			/**************************** OutPort of the model ********************************/
	private Flit 	value_out;

	private State 	state;			/***************************  Represent the state     *****************************/
	private float 	rho;			/***************************  Time elapsed in a state *****************************/

	Packet currentPacket;
	Message currentMessage;
	String data;

	boolean isTailFlit = false;
	int currentFlitIndex = 0;
	int currentPacketIndex = 0;


	public Generator_Task(String name, String data) {
		super();
        random_generator = new Random();
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
            int computation_requirement = random_generator.nextInt(4) + 1;
            IPoint destination = new IPoint (
            			new String[] {"x", "y"},
						new Integer[]{  new Random().nextInt(4),  new Random().nextInt(4)}
					);

            if (currentMessage == null || currentPacketIndex == currentMessage.packets.size() - 1) {
				currentMessage = new Message( data, destination );
				currentPacket = currentMessage.packets.elementAt(currentPacketIndex);
				currentPacketIndex = 0;

				if (currentMessage != null) {
					rho = Float.POSITIVE_INFINITY;
				}

			} else {
				currentPacketIndex++;
				currentPacket = currentMessage.packets.elementAt(currentPacketIndex);

			}

			System.out.println(this.name +  " PACKET " + currentPacket + " created!");

			setState(State.SENDOUT);
			currentFlitIndex = 0;
			value_out = currentPacket.flits.get(currentFlitIndex);
			isTailFlit = false;
			rho = 1F;

		} else if (state.equals(State.SENDOUT)) {

			if (isTailFlit) {

				setState(State.WAIT);
				isTailFlit = false;

				if (currentPacketIndex == currentMessage.packets.size() - 1) {
					rho = Float.POSITIVE_INFINITY;
				} else {
					rho       = 100F;
				}

			} else {

				currentFlitIndex++;
				value_out = currentPacket.flits.get(currentFlitIndex);
				isTailFlit = value_out.isTail;

				setState(State.SENDOUT);
				rho = 1;

			}
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
		isTailFlit = true;
		System.out.println("STARTING GENERATOR");
	}

	@Override
	public Object[] lambda() {
		Object[] output;

		if (state.equals(State.SENDOUT)) {
			output = setOutputLambda(out, value_out);
			System.out.println(this.name +  " Flit " + value_out + " sent!");
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
