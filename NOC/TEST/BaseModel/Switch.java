package BaseModel;


import DEVSModel.DEVSAtomic;
import DEVSModel.Port;
import Model.NOCModel.NOC;
import Model.Routing.NocRoutingPolicy;
import NocTopology.NOCDirections.IPoint;
import Verification.StateRepresentation;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Switch extends DEVSAtomic {

    private static final NOC.NodeType NODETYPE = NOC.NodeType.SWITCH;

    public IPoint position;
    NocRoutingPolicy routingPolicy;
    Vector<Port> inputDataPorts;
    Vector<Port> outputDataPorts;

    HashMap< Port, Vector<Flit> > inputDataQueue;       /** Represents the input queue associated to input ports   */

    Vector<Port> inputCmdPorts;                         /** Responsible for getting available buffer information-  */
    Vector<Port> outputCmdPorts;                        /** of neighboring switches and announcing the available-  */
                                                        /** buffer information of the input queues.                */
    float rho;

    public Switch(IPoint thisCoordinate,
                  NocRoutingPolicy routingPolicy,
                  Vector<String> inputDataPortsNames,
                  Vector<String> outputDataPortsNames,
                  Vector<String> inputCmdPortsNames,
                  Vector<String> outputCmdPortsNames)
    {

        this.position = thisCoordinate;
        this.routingPolicy = routingPolicy;
        this.name = NODETYPE + "[" + thisCoordinate.toString().trim() + "]";


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
    {CHECK, ROUTE, SEND_OUT, WAIT4OK, SET_STATUS}   /** Represents all possibles states of the switch model       */

    private static final int   ROUTE_TIME  = 10;        /** Period for the “route” state                          */
    private static final int   CHECK_TIME  = 3;         /** Period for the “check” state                          */
    private static final int   BUFFER_SIZE = Constants.BUFFER_SIZE;                      /** Size of the input buffers                             */
    private static final float SEND_OUT_TIME = 5;
    private static final float SET_STATUS_TIME = 0;
    private static final float WAIT4OK_TIME = Float.POSITIVE_INFINITY;

    private STATE state;                                /** Represents the current state of the switch model       */
    private Port currentPort;                           /** Holds the port of the currently examined input queue   */
    private Flit flit;                                  /** Represents the flit to be send out                     */
    private boolean sendTail;                           /** Represents whether the flits is a tail or not          */
    private Port destPort;                              /** Represents the selected output port                    */
    private IPoint destPoint;                           /** Represents the destination of the current task         */
    private Map<IPoint, Port> routeTable;               /** Represents the output port selected for a destPoint    */
    private Map.Entry<STATE, Port> savedState;          /** Represents the state as it was before "setStatus"      */
    private Map<Port, Boolean> extStatus;               /** Represents the queue state of the input queue "i" -    */
                                                        /** connected to each output port "i"                      */
    private Map<Port, Boolean> intStatus;               /** Represents the queue state of the output queue "i" -   */
                                                        /** connected to each output port "i"                      */
    private Map<Port, Boolean> intStatusConsistent;     /** Represents the queue state of the output queue "i" -   */
                                                        /** connected to each output port "i"                      */


    @Override                                           /** The model starts in the “check” state in which each-   */
    public void init() {                                /** input queue is checked for incoming flits periodically */

        changeStateTo( STATE.CHECK );
        currentPort = inputDataPorts.firstElement();
        sendTail   = false;
        routeTable = routingPolicy.getRouteTable();

        extStatus  = outputDataPorts
                .stream()
                .collect( Collectors.toMap( Function.identity(), Util.NocUtil::alwaysTrue) );
        intStatus  = inputDataPorts
                .stream()
                .collect( Collectors.toMap( Function.identity(), Util.NocUtil::alwaysTrue) );
        intStatusConsistent  = inputDataPorts
                .stream()
                .collect( Collectors.toMap( Function.identity(), Util.NocUtil::alwaysTrue) );

    }

    @Override
    public void deltaExt(Port port, Object o, float v) {

        if ( inputDataPorts.contains( port ) ) {
            LOG.printThis(this.name,"IN_TASK[" + (o) + "] - Queue of port : " + port.getModel().getName() + "@" + port.getName() + " = " + inputDataQueue.get(port).size());
            inputDataQueue.get(port).add((Flit) o);
            LOG.logThis(this.name, inputDataQueue.get(port));
            //System.out.println(this.name + " --> " + o);
            if ( inputDataQueue.get(port).size() == BUFFER_SIZE ) {

                intStatusConsistent.put(port, false);
                intStatus.put( port, false);
                currentPort = port;
                callToSetStatusState();
            }

        } else if ( inputCmdPorts.contains( port ) ) {

            Port correspondingPort = outputDataPorts
                    .stream()
                    .filter( outputPort
                            -> Util.NocUtil.isCorrespondingPort( port.getName(), outputPort.getName() )
                    )
                    .findFirst()
                    .get();

            extStatus.put( correspondingPort, o.equals("ok") );

            LOG.printThis(this.name, o + " INPUT COMMAND RECEIVED from " + port.getModel().getName() + " while in state " + state + "!!" + "do not send msg from " + correspondingPort );

        }

        switch ( state ) {

            case CHECK: {

            } break;

            case ROUTE: {

            } break;

            case SEND_OUT: {
                if (! extStatus.get(destPort)) {
                    changeStateTo(STATE.WAIT4OK);
                    LOG.printThis(this.name, "---------- IN WAIT4OK STATE NOW ---------- ");
                }
            } break;

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
                    intStatusConsistent.put( correspondingPort , o.equals("ok") );

                    if (o.equals("ok")) {
                        changeStateTo(STATE.SEND_OUT);
                        LOG.printThis(this.name, "---------- NOT IN WAITING STATE ANYMORE ---------- ");
                    }
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
                    changeStateTo( STATE.ROUTE );

                    destPoint = inputDataQueue.get(currentPort).firstElement().getDestination();
                    destPort  = routingPolicy.computeNextRouteStep(this, destPoint);
                    sendTail = false;
                }

            } break;

            case ROUTE: {

                if ( extStatus.get(destPort) ) {
                    changeStateTo( STATE.SEND_OUT );
                    flit = inputDataQueue.get(currentPort).firstElement();
//                    inputDataQueue.get( currentPort ).removeElementAt(0);
                    LOG.printThis(this.name, "POPOUT[" + flit + "] - Queue of port : " + getName() + "@" + currentPort.getName() + " = " +  inputDataQueue.get(currentPort).size() );

                    //if(this.position.getValueOnAxis("x") == this.position.getValueOnAxis("y") && this.position.getValueOnAxis("x") == 0) {
                    //    extStatus.put(this.getOutPort("PE"), false);
                    //}

                    callToSetStatusState();
                } else {
                    changeStateTo( STATE.CHECK );
                }

            } break;

            case SEND_OUT: {

                inputDataQueue.get( currentPort ).removeElementAt(0);
                if ( extStatus.get(destPort) ) {
                    if ( sendTail  /* || inputDataQueue.get(currentPort).isEmpty() */) {
                        changeStateTo( STATE.CHECK );
                        sendTail = false;
                        currentPort = getNextInputDataPort();
                    } else {

                        flit = null;
                        if (! inputDataQueue.get(currentPort).isEmpty()) {
                            changeStateTo( STATE.SEND_OUT );
                            flit = inputDataQueue.get(currentPort).firstElement();
                            sendTail = flit.isTail;
                            LOG.printThis(this.name, "POPOUT[" + flit + "] - Queue of port : " + getName() + "@" + currentPort.getName() + " = " +  inputDataQueue.get(currentPort).size() );

                            callToSetStatusState();
                        }
                    }
                } else {
                    changeStateTo( STATE.WAIT4OK );
                    LOG.printThis(this.name, "---------- IN WAIT4OK STATE NOW ---------- ");

                }

            } break;

            case SET_STATUS: {

                intStatus.put( currentPort , false);

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
                if(flit==null) {
                    return null;
                }

                output = new Object[ 2 ];
                output[0] = destPort;
                output[1] = flit;

                LOG.printThis(this.name, "lambda sendout : OUT_FLIT[" + ( (Flit) output[1]) + "] through " + destPort);

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

                output[1] = inputDataQueue.get( currentPort ).size() < BUFFER_SIZE ? "ok" : "nok";
                //LOG.printThis(this.name," SEND : " + output[1] );
            } break;
        }

        return output;
    }

    @Override
    public float getDuration() {

        switch (state) {
            case CHECK:         rho =  CHECK_TIME;      break;
            case ROUTE:         rho =  ROUTE_TIME;      break;
            case SEND_OUT:      rho =  SEND_OUT_TIME;   break;
            case WAIT4OK:       rho =  WAIT4OK_TIME;    break;
            case SET_STATUS:    rho =  SET_STATUS_TIME; break;
            default:            rho =  0;              break;
        }

        return rho;
    }

    private Port getNextInputDataPort() {
        return inputDataPorts.get( (inputDataPorts.indexOf( currentPort ) + 1 ) % inputDataPorts.size() );
    }

    private void callToSetStatusState() {
        if ( inputDataQueue.get(currentPort).size() == BUFFER_SIZE || inputDataQueue.get(currentPort).size() == BUFFER_SIZE-1 ) {
            savedState = new AbstractMap.SimpleEntry<>(state, currentPort);
            changeStateTo( STATE.SET_STATUS );
        }
    }

    private void callbackFromSetStatusState() {
        changeStateTo( savedState.getKey() );
        currentPort = savedState.getValue();
        savedState = null;
    }

    private void changeStateTo(STATE key) {
        state = key;
        StateRepresentation.addState( this.name, String.valueOf(state));
    }

    public String toString() {
        String inports = "";
        String outports = "";

        int i;
        for(i = 0; i < this.inPorts.size(); ++i) {
            inports = inports + this.inPorts.get(i).getName() + "-";
        }

        for(i = 0; i < this.outPorts.size(); ++i) {
            outports = outports + this.outPorts.get(i).getName() + "-";
        }

        return this.getClass().toString() + '@' + Integer.toHexString(this.hashCode()) + " Inports: " + inports + " Outports: " + outports;
    }
}














