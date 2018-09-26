package BaseModel;


import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;
import NocTopology.NOCDirections.IPoint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Switch extends DEVSAtomic {

    private static final NOC.NodeType NODETYPE = NOC.NodeType.SWITCH;

    Vector<Port> inputDataPorts;
    Vector<Port> outputDataPorts;

    HashMap< Port, Vector<Task> > inputDataQueue;       /** Represents the input queue associated to input ports   */

    Vector<Port> inputCmdPorts;                         /** Responsible for getting available buffer information-  */
    Vector<Port> outputCmdPorts;                        /** of neighboring switches and announcing the available-  */
                                                        /** buffer information of the input queues.                */
    float rho;

    public Switch(IPoint coordinate,
                  Vector<String> inputDataPortsNames,
                  Vector<String> outputDataPortsNames,
                  Vector<String> inputCmdPortsNames,
                  Vector<String> outputCmdPortsNames)
    {

	    this.name = NODETYPE + "[" + coordinate.toString().trim() + "]";


        /** Create input data ports from names */
        this.inputDataPorts = inputDataPortsNames.stream().map(
                portName -> new Port(this, portName)
        ).collect( Collectors.toCollection(Vector::new) );

        /** Create output data ports from names */
        this.outputDataPorts = outputDataPortsNames.stream().map(
                portName -> new Port(this, portName)
        ).collect( Collectors.toCollection(Vector::new) );

        /** Create input cmd ports from names */
        this.inputCmdPorts = inputCmdPortsNames.stream().map(
                portName -> new Port(this, portName)
        ).collect( Collectors.toCollection(Vector::new) );

        /** Create output cmd ports from names */
        this.outputCmdPorts = outputCmdPortsNames.stream().map(
                portName -> new Port(this, portName)
        ).collect( Collectors.toCollection(Vector::new) );


        /** Add input data port to the model */
        this.inputDataPorts.forEach(inputPort  -> addInPort(inputPort)  );

        /** Add output data port to the model */
        this.outputDataPorts.forEach(outputPort -> addOutPort(outputPort) );

        /** Add input cmd port to the model */
        this.inputCmdPorts.forEach(inputPort  -> addInPort(inputPort)  );

        /** Add output cmd port to the model */
        this.outputCmdPorts.forEach(outputPort -> addOutPort(outputPort) );

        inputDataQueue = new HashMap<>();
        for (Port p : inputDataPorts) {
            inputDataQueue.computeIfAbsent(p, k -> new Vector<>());
        }
	}




    /********************************************* MODEL REPRESENTATION ********************************************/

    private enum STATE
        {CHECK, ROUTE, SEND_OUT, WAIT4OK, SET_STATUS}   /** Represents all possibles states of the switch model    */

    private static final int CHECK_TIME  = 3;           /** Period for the “check” state                           */
    private static final int ROUTE_TIME  = 10;          /** Period for the “route” state                           */
    private static final int BUFFER_SIZE = 1000;        /** Size of the input buffers                              */
    private static final float SEND_OUT_TIME = 5;
    private static final float SET_STATUS_TIME = 0;
    private static final float WAIT4OK_TIME = Float.POSITIVE_INFINITY;

    private STATE state;                                /** Represents the current state of the switch model       */
    private Port currentPort;                           /** Holds the port of the currently examined input queue   */
    private boolean sendTail;                           /** Represents whether the flits is a tail or not          */
    private Port destPort;                              /** Represents the selected output port                    */
    private IPoint destPoint;                           /** Represents the destination of the current task         */
    private HashMap<IPoint, Port> routeTable;           /** Represents the output port selected for a destPoint    */
    private Map.Entry<STATE, Port> savedState;          /** Represents the state as it was before "setStatus"      */
    private Map<Port, Boolean> extStatus;               /** Represents the queue state of the input queue "i" -    */
                                                        /** connected to each output port "i"                      */
    private Map<Port, Boolean> intStatus;               /** Represents the queue state of the output queue "i" -   */
                                                        /** connected to each output port "i"                      */

    @Override                                           /** The model starts in the “check” state in which each-   */
	public void init() {                                /** input queue is checked for incoming flits periodically */
       state = STATE.CHECK;
       currentPort = inputDataPorts.firstElement();
       sendTail   = false;
       routeTable = new HashMap<>();
       extStatus  = outputDataPorts
               .stream()
               .collect( Collectors.toMap( Function.identity(), Util.NocUtil::alwaysTrue) );
       intStatus  = inputDataPorts
                .stream()
                .collect( Collectors.toMap( Function.identity(), Util.NocUtil::alwaysTrue) );

    }

	@Override
	public void deltaExt(Port port, Object o, float v) {

        if ( inputDataPorts.contains( port ) ) {
            inputDataQueue.get(port).add((Task) o);
            currentPort = port;
        }

	    switch ( state ) {

            case WAIT4OK: {

                if ( inputCmdPorts.contains( port ) ) {
                    Port correspondingPort = outputDataPorts
                            .stream()
                            .filter( outputPort
                                    -> Util.NocUtil.isCorrespondingPort( port.getName(), outputPort.getName() )
                            )
                            .findFirst()
                            .get();

                    extStatus.put( correspondingPort, o.equals("ok") );
                }

            } break;
        }

    }

	@Override
	public void deltaInt() {
        switch (state) {

            case CHECK: {

                if ( inputDataQueue.get(currentPort).isEmpty() ) {
                    currentPort = getNextInputDataPort();
                } else {
                    state = STATE.ROUTE;

                    destPoint = inputDataQueue.get(currentPort).firstElement().getDestination();
                    //destPort  = routeTable.get(destPoint);
                    destPort = outputDataPorts.firstElement();
                    sendTail = true;

                }

            } break;

            case ROUTE: {

                if ( extStatus.get(destPort) ) {
                    state = STATE.SEND_OUT;
                    callToSetStatusState();

                } else {
                    state = STATE.CHECK;
                }

            } break;

            case SEND_OUT: {


                if ( extStatus.get(destPort) ) {
                    if ( sendTail ) {
                        state = STATE.CHECK;
                        sendTail = false;
                        currentPort = getNextInputDataPort();
                    } else {
                        state = STATE.SEND_OUT;
                        callToSetStatusState();
                    }
                } else {
                    state = STATE.WAIT4OK;
                }

            } break;

            case SET_STATUS: {

                Port correspondingPort = outputDataPorts
                        .stream()
                        .filter( outputPort
                                -> Util.NocUtil.isCorrespondingPort( currentPort.getName(), outputPort.getName() )
                        )
                        .findFirst()
                        .get();

                intStatus.put( correspondingPort , false);

                callbackFromSetStatusState();

            } break;

            case WAIT4OK: break;

            default: System.err.println( "Unhandled state in internal transition" );
        }
    }



    @Override
	public Object[] lambda() {
        Object[] output = null;

		switch ( state ) {
            case CHECK: {

            } break;

            case ROUTE: {

            } break;

            case WAIT4OK: {

            } break;

            case SEND_OUT: {
                output = new Object[ 2 ];
                output[0] = destPort;
                output[1] = inputDataQueue.get( currentPort ).lastElement();
                inputDataQueue.get( currentPort ).removeElementAt( inputDataQueue.get( currentPort ).size() - 1 );
            } break;

            case SET_STATUS: {
                output = new Object[ 2 ];

                output[0] =  outputCmdPorts
                        .stream()
                        .filter( outputPort
                                -> Util.NocUtil.isCorrespondingPort( currentPort.getName(), outputPort.getName() )
                        )
                        .findFirst()
                        .get();

                output[1] = inputDataQueue.get( currentPort ).size() < BUFFER_SIZE -1 ? "ok" : "nok";
            } break;
        }

        return output;
	}

	@Override
	public float getDuration() {

		switch (state) {
            case CHECK:         rho = CHECK_TIME;       break;
            case ROUTE:         rho =  ROUTE_TIME;      break;
            case SEND_OUT:      rho =  SEND_OUT_TIME;   break;
            case WAIT4OK:       rho =  WAIT4OK_TIME;    break;
            case SET_STATUS:    rho =  SET_STATUS_TIME; break;
            default:            rho =  -1;              break;
        }

        return rho;
	}

    private Port getNextInputDataPort() {
        Port next = null;
        if ( inputDataPorts.listIterator( inputDataPorts.indexOf( currentPort ) ).hasNext() ) {
            next = inputDataPorts.listIterator( inputDataPorts.indexOf( currentPort ) ).next();
        } else {
            next = inputDataPorts.firstElement();
        }
        return next;
    }

    private void callToSetStatusState() {
        if ( inputDataQueue.get(currentPort).size() == BUFFER_SIZE - 1 ) {
            savedState = new AbstractMap.SimpleEntry<>(state, currentPort);
            state = STATE.SET_STATUS;
        }
    }

    private void callbackFromSetStatusState() {
        state = savedState.getKey();
        currentPort = savedState.getValue();
        savedState = null;
    }

    public String toString() {
        String inports = "";
        String outports = "";

        int i;
        for(i = 0; i < this.inPorts.size(); ++i) {
            inports = inports + ((Port)this.inPorts.get(i)).getName() + "-";
        }

        for(i = 0; i < this.outPorts.size(); ++i) {
            outports = outports + ((Port)this.outPorts.get(i)).getName() + "-";
        }

        return this.getClass().toString() + '@' + Integer.toHexString(this.hashCode()) + " Inports: " + inports + " Outports: " + outports;
    }
}














