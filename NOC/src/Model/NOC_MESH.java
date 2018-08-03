package Model;

import DEVSModel.DEVSModel;

import java.util.Vector;

public class NOC_MESH extends NOC {


    public NOC_MESH(int size) {
        super();

        this.size = size;

        for (int i = 0; i < size; i++) {
            model.add(new Vector<>());
        }
    }


    @Override
    protected void build_network() {

        /** GENERATE ALL NOCs UNIT **/
		for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                NOC_Unit NQ_tmp = NOC_Unit_factory.create_NOC_Unit(NOC_factory.Topology.MESH, new NodeCoordinate(i, j));
				this.getSubModels().add(NQ_tmp);
                model.get(i).add( NQ_tmp );
            }
		}

        System.out.println();

        /** LINES EDGES **/
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                NOC_Unit src  = model.get(i).get(j);
                NOC_Unit dest = model.get(i).get(j + 1);

                /** FORWARD LINKS **/
                this.addIC(src.getOutPort("out_NCUnit-0"), dest.getInPort("in_NCUnit-0"));

                /** BACKWARD LINKS **/
                this.addIC(dest.getOutPort("out_NCUnit-1"), src.getInPort("in_NCUnit-1"));
            }
        }


        for (int i = 0; i < size - 1 ; i++) {
            for (int j = 0; j < size; j++) {
                NOC_Unit src  = model.get(i).get(j);
                NOC_Unit dest = model.get(i + 1).get(j);

                /** FORWARD LINKS **/
                this.addIC(src.getOutPort("out_NCUnit-0"), dest.getInPort("in_NCUnit-0"));

                /** BACKWARD LINKS **/
                this.addIC(dest.getOutPort("out_NCUnit-1"), src.getInPort("in_NCUnit-1"));
            }
        }

        this.addIC(getGenerator().getOutPort("out"), model.get(0).get(0).getInPort("in_NCUnit-0"));

    }


    private NodeCoordinate getStartCoordinate() {
        return new NodeCoordinate(0,0);
    }


    public NodeCoordinate getNextNodeCoordinate(NodeCoordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        if (y % 2 == 0) {

            if (x < size-1) {
                return new NodeCoordinate(x + 1, y);
            } else {
                return new NodeCoordinate(x, y + 1);
            }

        } else {
            if (x < size-1) {
                return new NodeCoordinate(x + 1, y);
            } else {
                return new NodeCoordinate(x, y + 1);
            }
        }


    }



    @Override
    public void setSelectPriority() {

    }
}
