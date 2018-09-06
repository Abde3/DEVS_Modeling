package NocTopology.NOCDirections;


public class Coordinate2D extends ICoordinate {


    public Coordinate2D( ) {
        super( "x", "y" );
        bind("NORTH", coordinate2D -> super.incrementValueOnAxe("y"));
        bind("SOUTH", coordinate2D -> super.decrementValueOnAxe("y"));
        bind("EAST",  coordinate2D -> super.incrementValueOnAxe("x"));
        bind("WEST",  coordinate2D -> super.decrementValueOnAxe("x"));
    }

}