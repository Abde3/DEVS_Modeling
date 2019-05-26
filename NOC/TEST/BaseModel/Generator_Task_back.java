package BaseModel;


import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import NocTopology.NOCDirections.IPoint;
import Verification.StateRepresentation;

import java.util.Random;


public class Generator_Task_back extends DEVSAtomic {

	private int networkSize;
	private int dest_x;
	private int dest_y;

	public void setState(State state) {
		this.state = state;
		StateRepresentation.addState( this.name, String.valueOf(state));

	}

	private enum State {WAIT, SENDOUT}
	public static Random random_generator = new Random();

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


	public Generator_Task_back(int networkSize, String name, String data, int dest_x, int dest_y) {
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
            int computation_requirement = random_generator.nextInt(4) + 1;
            IPoint destination = new IPoint (
            			new String[] {"x", "y"},
						new Integer[]{  (dest_x < 0) ? (new Random().nextInt(networkSize)) : dest_x,  dest_y < 0 ? (new Random().nextInt(networkSize)) : dest_y}
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

			LOG.printThis(this.name, " PACKET " + currentPacket + " created!");

			setState(State.SENDOUT);
			currentFlitIndex = 0;
			value_out = currentPacket.flits.get(currentFlitIndex);
			isTailFlit = false;
			rho = 15F;

		} else if (state.equals(State.SENDOUT)) {

			if (isTailFlit) {

				setState(State.WAIT);
				isTailFlit = false;

				if (currentPacketIndex == currentMessage.packets.size() - 1) {
					rho = Float.POSITIVE_INFINITY;
				} else {
					rho = 150F;
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
		LOG.printThis(this.name,"STARTING GENERATOR");
	}

	@Override
	public Object[] lambda() {
		Object[] output;

		if (state.equals(State.SENDOUT)) {
			output = setOutputLambda(out, value_out);
			LOG.printThis(this.name,  " Flit " + value_out + " sent!");
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
