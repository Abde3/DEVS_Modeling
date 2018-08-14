package Model;

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

        /** GENERATE ALL NOCs UNIT **/
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                NOC_Unit source = model.get(i).get(j);
                for (DIRECTION direction: NOC_Unit_factory.getAlldirectionsforNode(source.getCoordinate(), size, NOC_factory.Topology.MESH) ) {
                    NodeCoordinate targetCoordinate = getNextNode(source.getCoordinate(), direction);

                    NOC_Unit target = model.get(targetCoordinate.getX()).get(targetCoordinate.getY());

                    System.out.println(source.getName() + "_" + "out_NCUnit-"+direction + " ----> " + target.getName() + "_" + "in_NCUnit-"+NOC_Unit_factory.getOpposite(direction));
                    this.addIC(source.getOutPort("out_NCUnit-"+direction), target.getInPort("in_NCUnit-"+NOC_Unit_factory.getOpposite(direction)));
                }

            }
        }


//        /** LINES EDGES **/
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size - 1; j++) {
//                NOC_Unit src  = model.get(i).get(j);
//                NOC_Unit dest = model.get(i).get(j + 1);
//
//                /** FORWARD LINKS **/
//                this.addIC(src.getOutPort("out_NCUnit-0"), dest.getInPort("in_NCUnit-0"));
////                System.out.println(Util.nodeNumFromCoordinate(src.getCoordinate(), 4) + " ---> "+  Util.nodeNumFromCoordinate(dest.getCoordinate(), 4) );
//
//                /** BACKWARD LINKS **/
//                this.addIC(dest.getOutPort("out_NCUnit-1"), src.getInPort("in_NCUnit-1"));
////                System.out.println(Util.nodeNumFromCoordinate(dest.getCoordinate(), 4) + " ---> "+  Util.nodeNumFromCoordinate(src.getCoordinate(), 4) );
//
//            }
//        }
//
//
//        for (int i = 0; i < size - 1 ; i++) {
//            for (int j = 0; j < size; j++) {
//                NOC_Unit src  = model.get(i).get(j);
//                NOC_Unit dest = model.get(i + 1).get(j);
//
//                /** FORWARD LINKS **/
//                this.addIC(src.getOutPort("out_NCUnit-0"), dest.getInPort("in_NCUnit-0"));
//
//                /** BACKWARD LINKS **/
//                this.addIC(dest.getOutPort("out_NCUnit-1"), src.getInPort("in_NCUnit-1"));
//            }
//        }


        this.addIC(getGenerator().getOutPort("out"), model.get(0).get(0).getInPort("in_NCUnit-"+DIRECTION.WEST));

    }


    private NodeCoordinate getStartCoordinate() {
        return new NodeCoordinate(0,0);
    }


    public NodeCoordinate getNextNode(NodeCoordinate source, DIRECTION direction) {

        NodeCoordinate dest = new NodeCoordinate(0, 0);

        if (direction.equals(DIRECTION.SOUTH)) {
            dest.setX(source.getX());
            dest.setY(source.getY() + 1);
        } else if (direction.equals(DIRECTION.EAST)) {
            dest.setX(source.getX() + 1);
            dest.setY(source.getY());
        } else if (direction.equals(DIRECTION.NORTH)) {
            dest.setX(source.getX());
            dest.setY(source.getY() - 1);
        } else if (direction.equals(DIRECTION.WEST)) {
            dest.setX(source.getX() - 1);
            dest.setY(source.getY());
        } else {
            return null;
        }

        return dest;
    }



    @Override
    public void setSelectPriority() {

    }

    public enum DIRECTION {WEST, SOUTH, EAST ,NORTH}

    public static DIRECTION getDirectionFromIndex(int portIndex) {
        return NOC_MESH.DIRECTION.values()[portIndex%DIRECTION.values().length];
    }
}
