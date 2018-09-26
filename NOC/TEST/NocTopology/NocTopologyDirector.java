package NocTopology;

public class NocTopologyDirector {

    public static NocTopology buildMeshTopology(int size) {

        NocTopology meshTopology = new NocTopologyBuilder()
                .withAxisNames("x", "y")
                .withActionForAxis("NORTH", "y", value -> value - 1)
                .withActionForAxis("SOUTH", "y", value -> value + 1)
                .withActionForAxis("EAST",  "x", value -> value + 1)
                .withActionForAxis("WEST",  "x", value -> value - 1)
                .withSize(size)
                .build();

        return meshTopology;

    }
}
