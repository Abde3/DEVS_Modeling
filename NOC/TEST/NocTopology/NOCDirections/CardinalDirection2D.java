package NocTopology.NOCDirections;

public class CardinalDirection2D implements IDirection {



    @Override
    public String[] getPossibleDirection() {
        return new String[]{"EAST", "SOUTH", "WEST", "NORTH"};
    }
}