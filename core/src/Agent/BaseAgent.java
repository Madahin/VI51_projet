package Agent;

import Config.WorldConfig;

public class BaseAgent extends Agent{
	
	private int foodStock;
	
	public BaseAgent() {
		foodStock = WorldConfig.DEFAULT_FOOD_IN_BASE;
	}

	@Override
	public void live() {
		// TODO Auto-generated method stub
		
	}

}
