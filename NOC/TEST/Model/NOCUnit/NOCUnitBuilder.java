package Model.NOCUnit;

import BaseModel.*;
import BaseModel.Queue;
import DEVSModel.DEVSModel;
import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;

import java.util.*;
import java.util.stream.Collectors;


public class NOCUnitBuilder extends AbstractNOCUnitBuilder<NOCUnit> {


    @Override
    public AbstractNOCUnitBuilder withInPorts(String... inPorts) throws NOCUnit.ExistingPortException {

        v_in_ports_names = new HashSet<>(Arrays.asList(inPorts));

        if (v_in_ports_names.size() != inPorts.length) {
            throw new NOCUnit.ExistingPortException("The given list of input ports names contains duplicate") ;
        }

        return this;

    }

    @Override
    public AbstractNOCUnitBuilder withOutPorts(String... outPorts) throws NOCUnit.ExistingPortException {

        v_out_ports_names = new HashSet<>(Arrays.asList(outPorts));

        if (v_out_ports_names.size() != outPorts.length) {
            throw new NOCUnit.ExistingPortException("The given list of output ports names contains duplicate") ;
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withQueuePerInPortRatio(int queuePerInPortRatio) {

        v_in_queue = new LinkedHashSet<>();

        for ( String inPortName : v_in_ports_names) {

            for (int i = 0; i < queuePerInPortRatio; i++) {
                Queue tmpInQueue = new QueueBuilder()
                        .withCorrespondingPort(inPortName)
                        .withQueueNumber(i)
                        .withCoordinate(coordinate)
                        .withInputPorts("in")
                        .withOutputPorts("out")
                        .build();

                v_in_queue.add(tmpInQueue);
            }
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withQueuePerOutPortRatio(int queuePerOutPortRatio) {

        v_out_queue = new LinkedHashSet<>();

        for ( String outPortName : v_out_ports_names) {

            for (int i = 0; i < queuePerOutPortRatio; i++) {
                Queue tmpOutQueue = new QueueBuilder().withCoordinate(coordinate).withOutputPorts(outPortName).build();
                v_out_queue.add(tmpOutQueue);
            }
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withRoutingPolicy(NocRoutingPolicy routingPolicy) {
        this.routingPolicy = routingPolicy;
        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withCoordinate(IPoint coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withGenerator(DEVSModel generator) {
        
        return this;
    }


    @Override
    public NOCUnit build() {

        Set<String> switchInputPortsNames  = new HashSet<>();
        Set<String> switchOutputPortsNames = new HashSet<>();

        if ( v_out_queue == null || v_out_queue.isEmpty() ) {
            switchOutputPortsNames.addAll(this.v_out_ports_names);
        } else {
            switchOutputPortsNames.addAll(v_out_queue.stream().map(Queue::getName).collect(Collectors.toCollection( HashSet::new )));
        }
        switchOutputPortsNames.add("PE");

        if ( v_in_queue == null || v_in_queue.isEmpty() ) {
            switchInputPortsNames.addAll(this.v_in_ports_names);
        } else {
            switchInputPortsNames.addAll(v_in_queue.stream().map(Queue::getName).collect(Collectors.toCollection( HashSet::new )));
        }
        switchInputPortsNames.add("PE");


        aSwitch = new SwitchBuilder()
                .withCoordinate(coordinate)
                .withInputPorts(switchInputPortsNames.toArray( new String[0] ))
                .withOutputPorts(switchOutputPortsNames.toArray( new String[0] ))
                .build();

        aProcessingElement = new ProcessingElementBuilder()
                .withCoordinate(coordinate)
                .withInputPorts("in")
                .withOutputPorts("out")
                .build();

        System.out.println( "\n\n\n---------------------" );
        System.out.println( aProcessingElement.toString() );
        System.out.println( aSwitch.toString() );
        System.out.println( "---------------------\n\n\n" );

        NOCUnit nocUnit = new NOCUnit(coordinate, v_in_ports_names, v_out_ports_names, v_in_queue, v_out_queue, aSwitch, aProcessingElement);

        return nocUnit;
    }


}
