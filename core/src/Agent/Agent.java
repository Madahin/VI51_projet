package Agent;

import java.util.List;

import Environment.AgentBody;
import Environment.Direction;
import Environment.Perceivable;

/**
 * An abstract Agent.
 */
public abstract class Agent {

	/** The "physical" representation of the agent in the environment. */
	protected AgentBody body;

	/**
	 * Generate one tick of the agent life.
	 */
	public abstract void live();

	/**
	 * Gets the list of what the agent can perceive.
	 *
	 * @return an arraylist of perceived object
	 */
	protected List<Perceivable> getPerception() {
		return body.getPerception();
	}

	/**
	 * Move the agent body.
	 *
	 * @param d
	 *            the direction in which the agent should move
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

	/**
	 * get the current agent body
	 * 
	 * @return the body
	 */
	public AgentBody getBody() {
		return body;
	}

	/**
	 * set the current agent body
	 * 
	 * @param newBody
	 *            the body to set
	 */
	public void setBody(AgentBody newBody) {
		body = newBody;
	}
}
