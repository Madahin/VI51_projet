package Environment;

public class PheromoneBody extends AgentBody {
	public Faction faction;
	public PheromoneType pheromoneType;
	public int life; 
	
	public PheromoneBody(int x, int y, Faction f, PheromoneType pt) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		faction = f;
		pheromoneType = pt;
		life = 100;
	}
	
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}

}
