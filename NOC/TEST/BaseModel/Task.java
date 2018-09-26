package BaseModel;

import NOCUnit.NodeCoordinate;
import NocTopology.NOCDirections.IPoint;

public class Task {
	
	private static final int life_cycle = 3;
	
	private String 	name;
	private int id;
	private int age;
	private IPoint destination;
	private float 	computation_requirement;

	public Task(int id, int computation_requirement, IPoint destination) {
		
		this.id = id;
		this.name = "TASK-" + id;
		this.age = 0;
		this.computation_requirement = computation_requirement;
		this.destination = destination;
	}
	
	public boolean is_completed() {
		return (age == life_cycle);
	}
	
	public int get_id() {
		return id;
	}
	
	public int get_age() {
		return age;
	}
	
	public void increment_age() {
		age += 1;
	}

	public String getName() {
		return name;
	}

	public float getComputation_requirement() {
		return computation_requirement;
	}

	public IPoint getDestination() {
		return destination;
	}

	public void setDestination(IPoint destination) {
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		return "{name: " + name + ", age: " + age + ", dest: " + destination + ", comutation_req: " + computation_requirement + "}";
	}
	
}
