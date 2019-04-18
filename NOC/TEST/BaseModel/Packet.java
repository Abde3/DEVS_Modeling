package BaseModel;

import NocTopology.NOCDirections.IPoint;
import java.util.Vector;

public class Packet {

	Vector<Flit> flits = new Vector<>();
	IPoint destination;
    boolean header;
    boolean tail;

	public Packet(int id, int computation_requirement, String data, IPoint destination, boolean header, boolean tail) {

	    this.destination = destination;
	    this.header = header;
	    this.tail = tail;

		flits.add( Flit.headerFlit( String.valueOf( id ), computation_requirement, destination ) );

        for ( Character c : data.toCharArray() ) {
            flits.add( Flit.flit( String.valueOf( id ), computation_requirement, c, destination ) );
        }

        flits.add( Flit.tailFlit( String.valueOf( id ),computation_requirement, destination ) );
	}

	public Packet(Flit headerFlit, Flit[] flits, Flit tailFlit) {
        this.destination = headerFlit.getDestination();

        this.flits.add( headerFlit );

        for ( Flit f : flits ) {
            this.flits.add( f );
        }

        this.flits.add( tailFlit );
    }


    public Packet(Vector<Flit> queue) {
        this.destination = queue.firstElement().getDestination();

        for ( Flit f : queue.subList(0, queue.size()) ) {
            this.flits.add( f );
        }

    }


    public Packet() {
        this.flits = new Vector<>();
    }

    @Override
    public String toString() {
        return
            "Packet{" +
                    "flits=" + flits +
                    '}';

    }

    public void updateDestination(IPoint destination) {
	    flits.forEach( flit -> flit.setDestination(destination) );
        this.destination = destination;

    }

    public IPoint getDestination() {
        return this.destination;
    }

    public float getComputation_requirement() {
        return 1;
    }

}
