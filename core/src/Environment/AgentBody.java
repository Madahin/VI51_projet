package Environment;

import java.util.ArrayList;

/**
 * The Class AgentBody is the abstract physical representation of an agent in an environment.
 */
public abstract class AgentBody extends DynamicObject {
	
	/**
	 * Gets what the agent is perceiving.
	 *
	 * @return the list of perceived object
	 */
	public ArrayList<Perceivable> getPerception() {
		return environmentReference.getPerception(this);
	}
	
	/**
	 * Move the body.
	 *
	 * @param d the direction in which the body should move
	 */
	public void move(Direction d){
		environmentReference.move(d, this);
	}
	
	/**
	 * Destroy the body.
	 */
	public void destroy(){
		environmentReference.destroy(this);
	}
}
