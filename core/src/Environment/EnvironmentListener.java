package Environment;

import java.util.ArrayList;
import java.util.EventListener;

import Agent.Agent;

/**
 * The listener interface for receiving environment events.
 * The class that is interested in processing a environment
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addEnvironmentListener<code> method. When
 * the environment event occurs, that object's appropriate
 * method is invoked.
 *
 * @see EnvironmentEvent
 */
public interface EnvironmentListener extends EventListener {
	
	/**
	 * Notify a change in an environment.
	 *
	 * @param basePos the position of each bases
	 * @param foods the position of each food stake
	 * @param newAgentList the new agent list
	 */
	public void environmentChanged(BasePosition basePos[], ArrayList<FoodStackPosition> foods, ArrayList<Agent> newAgentList);
}
