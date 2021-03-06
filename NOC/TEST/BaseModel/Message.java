package BaseModel;

import NocTopology.NOCDirections.IPoint;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.Random;
import java.util.Vector;

public class Message {

    private static final int PACKET_PAYLOAD_SIZE = Constants.PACKET_PAYLOAD_SIZE;

    Vector<Packet> packets = new Vector<>();

    public Message(String data, IPoint destination) {

        int id = new Random().nextInt(20);
        int computation_req = 2;

        for (int i = 0; i < data.length(); i += PACKET_PAYLOAD_SIZE) {
            boolean tail = false, header = false;
            if ( i == 0 ) {
                header = true;
            }
            if( i == data.length() - 1 ) {
                tail = true;
            }

            packets.add( new Packet(id, computation_req, data.substring(i, Math.min(i + PACKET_PAYLOAD_SIZE, data.length())) , destination, header, tail) );
        }

    }

}
