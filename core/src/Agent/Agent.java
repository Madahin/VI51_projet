package Agent;

import java.util.ArrayList;

import Environment.AgentBody;
import Environment.Direction;
import Environment.Perceivable;

public abstract class Agent {
	public AgentBody body;
	
	public abstract void live();
	
	protected ArrayList<Perceivable> getPerception(){
		return body.getPerception();
	}
	
	protected void move(Direction d){
		body.move(d);
	}
}
