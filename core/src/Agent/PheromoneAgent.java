package Agent;

import Environment.PheromoneBody;

/**
 * The Class PheromoneAgent.
 */
public class PheromoneAgent extends Agent {

	/**
	 * Instantiates a new pheromone.
	 *
	 * @param b
	 *            the "physical" representation of the pheromone in the
	 *            environment
	 * @param d
	 *            the direction in which the direction is toward
	 */
	public PheromoneAgent(PheromoneBody b) {
		super();
		body = b;
	}

	/** {@inheritDoc} */
	@Override
	public void live() {
		((PheromoneBody) body).life--;

		if (((PheromoneBody) body).life == 0) {
			destroy();
		}

	}

}
