package Agent;

import Environment.PheromoneBody;

public class PheromoneAgent extends Agent {

	public PheromoneAgent(PheromoneBody b) {
		// TODO Auto-generated constructor stub
		body = b;
	}
	
	@Override
	public void live() {
		// TODO Auto-generated method stub
		((PheromoneBody) body).life--;
		
		if(((PheromoneBody) body).life == 0){
			destroy();
		}
		
	}

}

	