package Environment;

import Config.WorldConfig;

public class PheromoneBody extends AgentBody {
	public Faction faction;
	public PheromoneType pheromoneType;
	public int life; 
	
	public PheromoneBody(int x, int y, Faction f, PheromoneType pt, Environment env) {
		// TODO Auto-generated constructor stub
		environmentReference = env;
		this.x = x;
		this.y = y;
		faction = f;
		pheromoneType = pt;
		life = WorldConfig.PHEROMONE_INITIAL_LIFE;
	}
	
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}

}
