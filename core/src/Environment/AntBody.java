package Environment;

public class AntBody extends AgentBody{
	private final Faction faction;
	private Direction direction;
	
	public AntBody(Faction f,Direction d, int initX, int initY, Environment env){
		faction = f;
		direction = d;
		x = initX;
		y = initY;
		environmentReference = env;
	}

	public void createPheromone(PheromoneType pt){
		environmentReference.createPheromone(pt, this);
	}
	
	public boolean pickUpFood(){
		return environmentReference.pickUpFood(this);
	}
	
	public void addFoodToBase(){
		environmentReference.addFoodToBase(this);
	}
	
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}
	
	public Faction getFaction(){
		return faction;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setDirection(Direction dir){
		direction = dir;
	}
}
