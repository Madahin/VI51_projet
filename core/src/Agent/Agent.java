package Agent;

import java.util.ArrayList;

import Environment.AgentBody;
import Environment.Direction;
import Environment.Perceivable;

/**
 * An abstract Agent.
 */
public abstract class Agent {
	
	/** The "physical" representation of the agent in the environment. */
	public AgentBody body;

	/**
	 * Generate one tick of the agent life.
	 */
	public abstract void live();

	/**
	 * Gets the list of what the agent can perceive.
	 *
	 * @return an arraylist of perceived object
	 */
	protected ArrayList<Perceivable> getPerception() {
		return body.getPerception();
	}

	/**
	 * Move the agent body.
	 *
	 * @param d the direction in which the agent should move
	 */
	protected void move(Direction d) {
		body.move(d);
	}

	/**
	 * Destroy the agent.
	 */
	protected void destroy() {
		if (body != null) {
			body.destroy();
			body = null;
		}
	}
}
