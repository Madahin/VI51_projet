package Environment;

import java.util.ArrayList;

public abstract class AgentBody extends DynamicObject {
	
	public ArrayList<Perceivable> getPerception() {
		return environmentReference.getPerception(this);
	}
	
	public void move(Direction d){
		environmentReference.move(d, this);
	}
}
