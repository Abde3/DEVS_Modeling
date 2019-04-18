package BaseModel;

import NocTopology.NOCDirections.IPoint;

public class Flit {

    private String id;
    private String name;
    private int age;
    private IPoint destination;
    private int computation_requirement;
    private Character data;
	boolean isHeader;
	boolean isTail;

    private Flit(String id,  int age, IPoint destination, int computation_requirement, Character data, boolean isHeader, boolean isTail) {
        this.id = id;
        this.name = "TASK-" + id;
        this.age = age;
        this.destination = destination;
        this.computation_requirement = computation_requirement;
        this.data = data;
        this.isHeader = isHeader;
        this.isTail = isTail;
    }

    public static Flit headerFlit(String id, int computation_requirement, IPoint destination) {
        return new Flit(id, 0, destination, computation_requirement, null, true, false );
    }

    public static Flit tailFlit(String id, int computation_requirement, IPoint destination) {
        return new Flit(id, 0, destination, computation_requirement, null, false, true);
    }

    public static Flit flit(String id, int computation_requirement, Character data, IPoint destination) {
        return new Flit(id, 0, destination, computation_requirement, data, false, false);
    }


	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return (isTail)
                ? ("{name: " + name + " TAIL_FLIT" + "}")
                : ( (isHeader) ? ("{name: " + name + " HEADER_FLIT" + "}") : ("{name: " + name + ", data:" + data + "}") );
	}

    public IPoint getDestination() {
        return destination;
    }

    public void setDestination(IPoint destination) {
        this.destination = destination;
    }

    public Character getData() {
        return data;
    }

    public void updateDestination(IPoint new_destination) {
        destination = new_destination;
    }
}
