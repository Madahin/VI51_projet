package Environment;

import java.util.List;

/**
 * The Class AgentBody is the abstract physical representation of an agent in an environment.
 */
public abstract class AgentBody extends DynamicObject implements IAgentBody {
	
	/** {@inheritDoc} */
	public List<Perceivable> getPerception() {
		return environmentReference.getPerception(this);
	}
	
	/** {@inheritDoc} */
	public void move(Direction d){
		environmentReference.move(d, this);
	}
	
	/** {@inheritDoc} */
	public void destroy(){
		environmentReference.destroy(this);
	}
}
