package BaseModel;

import NocTopology.NOCDirections.IPoint;

import java.util.Vector;

public class Packet {

	Vector<Flit> flits = new Vector<>();
    boolean header;
    boolean tail;

	public Packet(int id, int computation_requirement, String data, IPoint destination, boolean header, boolean tail) {

	    this.header = header;
	    this.tail = tail;

		flits.add( Flit.headerFlit( String.valueOf( id ), computation_requirement, destination ) );

        for ( Character c : data.toCharArray() ) {
            flits.add( Flit.flit( String.valueOf( id ), computation_requirement, c, destination ) );
        }

        flits.add( Flit.tailFlit( String.valueOf( id ),computation_requirement, destination ) );
	}

	public Packet(Flit headerFlit, Flit[] flits, Flit tailFlit) {
        this.flits.add( headerFlit );

        for ( Flit f : flits ) {
            this.flits.add( f );
        }

        this.flits.add( tailFlit );
    }

    public Packet(Vector<Flit> queue) {

        this.flits.add( queue.get(0) );

        for ( Flit f : queue.subList(1, queue.size()-1) ) {
            this.flits.add( f );
        }

        this.flits.add( queue.lastElement() );
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

    public float getComputation_requirement() {
        return 1;
    }

}
