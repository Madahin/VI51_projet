package Agent;

import Environment.PheromoneBody;

/**
 * The Class PheromoneAgent.
 */
public class PheromoneAgent extends Agent {

	/**
	 * Instantiates a new pheromone.
	 *
	 * @param b the "physical" representation of the pheromone in the environment
	 */
	public PheromoneAgent(PheromoneBody b) {
		body = b;
	}
	
	/** {@inheritDoc} */
	@Override
	public void live() {
		((PheromoneBody) body).life--;
		
		if(((PheromoneBody) body).life == 0){
			destroy();
		}
		
	}

}

	