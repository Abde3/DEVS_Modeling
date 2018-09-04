package BaseModel;

import DEVSModel.DEVSAtomic;
import DEVSModel.Port;

import java.util.Random;

public class ProcessingElement extends DEVSAtomic {

	@Override
	public void init() { }

	@Override
	public void deltaExt(Port port, Object o, float v) { }

	@Override
	public void deltaInt() { }

	@Override
	public Object[] lambda() {
		return null;
	}

	@Override
	public float getDuration() {
		return 0;
	}
}
