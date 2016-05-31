package Environment;

public class AntBody extends AgentBody{
	public Faction faction;
	
	public AntBody(Faction f, int initX, int initY, Environment env){
		faction = f;
		x = initX;
		y = initY;
		environmentReference = env;
	}

	public void createPheromone(PheromoneType pt){
		environmentReference.createPheromone(pt, this);
	}
	
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}
}
