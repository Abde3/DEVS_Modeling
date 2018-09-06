package NocTopology;


import NOCUnit.MeshNocNetwork;

public class MeshTopology extends NocTopology {

    protected MeshTopology(int size) {
        this.size = size;
        this.nocNetwork = new MeshNocNetwork(size);
    }

}
