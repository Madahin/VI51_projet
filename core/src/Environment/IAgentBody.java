package Environment;

import java.util.List;

public interface IAgentBody {
	/**
	 * Gets what the agent is perceiving.
	 *
	 * @return the list of perceived object
	 */
	public List<Perceivable> getPerception();

	/**
	 * Move the body.
	 *
	 * @param d
	 *            the direction in which the body should move
	 */
	public void move(Direction d);

	/**
	 * Destroy the body.
	 */
	public void destroy();

}
