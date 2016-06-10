package Environment;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Config.WorldConfig;

/**
 * The Class AntBody is the "physical" representation of an ant in an environment.
 */
public class AntBody extends AgentBody{
	
	/** The faction in which the ant belong. */
	private final Faction faction;
	
	/** The id of the faction in which the ant belong. */
	protected final int factionID;
	
	/** The direction in which the ant is moving. */
	protected Direction direction;
	
	/** The quantity of food carried. */
	protected int foodCarried;
	
	/** The life expectancy of the ant. */
	public int life; 
	
	/** The time it takes before losing health/life from hunger. */
	public int hunger; 
	
	public Vector3 behaviourDebug; 
	
	/**
	 * Instantiates a new ant body.
	 *
	 * @param f the faction in which the ant belong
	 * @param fID the id of the faction in which the ant belong
	 * @param d the direction in which the ant is moving
	 * @param initX the initial position in x
	 * @param initY the initial position in y
	 * @param env the environment
	 */
	public AntBody(Faction f, int fID, Direction d, int initX, int initY, Environment env){
		faction = f;
		factionID = fID;
		direction = d;
		x = initX;
		y = initY;
		life = WorldConfig.ANT_INITIAL_LIFE;
		hunger = WorldConfig.HUNGER_BAR;
		environmentReference = env;
		foodCarried = 0;
		behaviourDebug = new Vector3(0.0f, 0.0f, 0.0f);
	}

	/**
	 * Creates a pheromone.
	 *
	 * @param pt the pheromone type
	 */
	public void createPheromone(PheromoneType pt){
		environmentReference.createPheromone(pt, this);
	}
	
	/**
	 * Pick up food.
	 *
	 * @return true, if successful
	 */
	public boolean pickUpFood(){
		return environmentReference.pickUpFood(this);
	}
	
	/**
	 * Adds the food to the base.
	 */
	public void addFoodToBase(){
		environmentReference.addFoodToBase(this);
	}
	
	/** {@inheritDoc} */
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the amount of carried food.
	 *
	 * @return the amount of carried food
	 */
	public int getFoodCaried(){
		return foodCarried;
	}
	
	/**
	 * remove the carried food and return the amount
	 *
	 * @return the amount of carried food
	 */
	public int popFoodCaried(){
		int tmp = foodCarried;
		foodCarried = 0;
		return tmp;
	}
	
	/**
	 * Sets the amount of carried food.
	 *
	 * @param n the new amount of carried food
	 */
	public void setFoodCarried(int n){
		foodCarried = n;
	}
	
	/**
	 * Gets the faction of which the ant belong.
	 *
	 * @return the faction of which the ant belong
	 */
	public Faction getFaction(){
		return faction;
	}
	
	/**
	 * Gets the id of the faction in which the ant belong
	 *
	 * @return the id of the faction in which the ant belong
	 */
	public int getFactionID(){
		return factionID;
	}
	
	/**
	 * Gets the direction in which the ant is moving.
	 *
	 * @return the direction in which the ant is moving
	 */
	public Direction getDirection(){
		return direction;
	}
	
	/**
	 * Sets the direction in which the ant should move.
	 *
	 * @param dir the new direction in which the ant should move
	 */
	public void setDirection(Direction dir){
		direction = dir;
	}
	
	/**
	 * Replenish the hunger.
	 */
	public void eat() {
		hunger = environmentReference.eat(this); 
		
	}
	

}
