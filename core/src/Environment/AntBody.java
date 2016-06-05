package Environment;

public class AntBody extends AgentBody{
	private final Faction faction;
	private final int factionID;
	private Direction direction;
	
	private int foodCaried;
	
	public AntBody(Faction f, int fID, Direction d, int initX, int initY, Environment env){
		faction = f;
		factionID = fID;
		direction = d;
		x = initX;
		y = initY;
		environmentReference = env;
		foodCaried = 0;
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
	
	public int getFoodCaried(){
		return foodCaried;
	}
	
	public int popFoodCaried(){
		int tmp = foodCaried;
		foodCaried = 0;
		return tmp;
	}
	
	public void setFoodCarried(int n){
		foodCaried = n;
	}
	
	public Faction getFaction(){
		return faction;
	}
	
	public int getFactionID(){
		return factionID;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setDirection(Direction dir){
		direction = dir;
	}
}
