package Agent;

import Environment.PheromoneBody;

/**
 * The Class PheromoneAgent.
 */
public class PheromoneAgent extends Agent {

	private int pheromoneSpread;
	
	private int pheromoneCount;
	/**
	 * Instantiates a new pheromone.
	 *
	 * @param b the "physical" representation of the pheromone in the environment
	 */
	public PheromoneAgent(PheromoneBody b) {
		body = b;
		pheromoneSpread = 100;
		pheromoneCount = 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public void live() {
		pheromoneCount++;
		if(pheromoneCount == pheromoneSpread){
			((PheromoneBody) body).life--;
			pheromoneCount = 0;
		}
		
		if(((PheromoneBody) body).life == 0){
			destroy();
		}
		
	}

}

	