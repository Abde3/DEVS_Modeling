import NOC.*;
import NOCUnit.NOCUnitBuilder;
import NOCUnit.NOCUnitDirector;


public class Test {

    public static void main(String[] args){
        System.out.println("ooow");

        NOC noc = new NOCDirector( new NOCMeshBuilder() )
                .withSize(4)
                .build();

    }

}
