package BaseModel;

public class Constants {

    static int PACKET_SIZE = 64;
    static int DATA_SIZE = 40;
    static int BUFFER_SIZE = 13;
    static int PACKET_PAYLOAD_SIZE = BUFFER_SIZE-2;
    static int MAX_FLIT = PACKET_SIZE/8;

}
