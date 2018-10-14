package Model.NOCUnit;

import BaseModel.*;
import Model.Routing.NocRoutingPolicy;
import NOCUnit.NOCUnit;
import NocTopology.NOCDirections.IPoint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class NOCUnitBuilder extends AbstractNOCUnitBuilder<NOCUnit> {


    @Override
    public AbstractNOCUnitBuilder withDataInPorts(List<String> inDirections) throws NOCUnit.ExistingPortException {

        v_data_in_ports_names = inDirections
                .stream()
                .map( direction -> Util.NocUtil.directionsToPortsName(direction, true, Type.DATA) )
                .collect( Collectors.toCollection( HashSet::new ) );

        if (v_data_in_ports_names.size() != inDirections.size()) {
            throw new NOCUnit.ExistingPortException("The given list of input ports names contains duplicate") ;
        }

        return this;

    }


    @Override
    public AbstractNOCUnitBuilder withDataOutPorts(List<String> outDirections) throws NOCUnit.ExistingPortException {

        v_data_out_ports_names = outDirections
                .stream()
                .map( direction -> Util.NocUtil.directionsToPortsName(direction, false, Type.DATA) )
                .collect( Collectors.toCollection( HashSet::new ) );

        if (v_data_out_ports_names.size() != outDirections.size()) {
            throw new NOCUnit.ExistingPortException("The given list of output ports names contains duplicate") ;
        }

        return this;
    }


    @Override
    public AbstractNOCUnitBuilder withCmdInPorts(List<String> inDirections) throws NOCUnit.ExistingPortException {
        v_cmd_in_ports_names = inDirections
                .stream()
                .map( direction -> Util.NocUtil.directionsToPortsName(direction, true, Type.COMMAND) )
                .collect( Collectors.toCollection( HashSet::new ) );

        if (v_cmd_in_ports_names.size() != inDirections.size()) {
            throw new NOCUnit.ExistingPortException("The given list of input ports names contains duplicate") ;
        }

        return this;

    }


    @Override
    public AbstractNOCUnitBuilder withCmdOutPorts(List<String> outDirections) throws NOCUnit.ExistingPortException {
        v_cmd_out_ports_names = new HashSet<>(
                Util.NocUtil.directionsToPortsName(outDirections, false, Type.COMMAND)
        );

        if (v_cmd_out_ports_names.size() != outDirections.size()) {
            throw new NOCUnit.ExistingPortException("The given list of output ports names contains duplicate") ;
        }

        return this;
    }

    @Override
    public AbstractNOCUnitBuilder withQueuePerInPortRatio(int queuePerInPortRatio) {
        this.queuePerInPortRatio = queuePerInPortRatio;
        return this;
    }


    @Override
    public AbstractNOCUnitBuilder withQueuePerOutPortRatio(int queuePerOutPortRatio) {
        this.queuePerOutPortRatio = queuePerOutPortRatio;
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
    public NOCUnit build() {

        Set<String> switchDataInputPortsNames  = new HashSet<>( v_data_in_ports_names );
        Set<String> switchDataOutputPortsNames = new HashSet<>( v_data_out_ports_names );
        Set<String> switchCmdInputPortsNames   = new HashSet<>( v_cmd_in_ports_names );
        Set<String> switchCmdOutputPortsNames  = new HashSet<>( v_cmd_out_ports_names );


        switchDataOutputPortsNames.add("PE");
        switchDataInputPortsNames.add("PE");
        switchCmdOutputPortsNames.add("cmdToPE");


        aSwitch = new SwitchBuilder()
                .withCoordinate(coordinate)
                .withDataInputPorts( switchDataInputPortsNames )
                .withDataOutputPorts( switchDataOutputPortsNames )
                .withCmdInputPorts( switchCmdInputPortsNames )
                .withCmdOutputPorts( switchCmdOutputPortsNames )
                .build();

        aNetworkInterface = new NetworkInterface( coordinate.toString() );


        aProcessingElement = new ProcessingElementBuilder()
                .withCoordinate(coordinate)
                .withInputPorts("in")
                .withOutputPorts("out")
                .build();


        System.out.println( "\n\n\n---------------------" );
//        System.out.println( aProcessingElement.toString() );
 //       System.out.println( aSwitch.toString() );
  //      System.out.println( aNetworkInterface.toString() );
        System.out.println( "---------------------\n\n\n" );

        NOCUnit nocUnit = new NOCUnit(coordinate,
                v_data_in_ports_names,
                v_data_out_ports_names,
                v_cmd_in_ports_names,
                v_cmd_out_ports_names,
                aNetworkInterface,
                aSwitch,
                aProcessingElement);

              System.out.println( nocUnit.toString() );


        return nocUnit;
    }


}
