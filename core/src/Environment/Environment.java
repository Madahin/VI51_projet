package Environment;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import Agent.Agent;
import Agent.PheromoneAgent;
import Config.WorldConfig;
import Tools.SimplexNoise;
import Environment.FoodPile;
/**
 * The Class Environment.
 */
public class Environment {
	
	/** The width of the world. */
	private int width;
	
	/** The height of the world. */
	private int height;
	
	/** The nb agents in the environment. */
	private int nbAgents;
	
	/** the counter of agent who lived this tick. */
	private int cptAgents;

	/** The stock of food in each base. */
	private int foodInBase[];
	
	/** The number of agent per bases. */
	private int nbAgentPerBases[];

	/** The graphical representation of each bases. */
	private Circle bases[];
	
	/** The bases radius. */
	private int baseRadius;
	
	/** The bases positions. */
	private BasePosition basePositions[];

	/** The listeners. */
	private ArrayList<EnvironmentListener> listeners;
	
	/** The objects in each cell of the world. */
	private ArrayList<EnvironmentObject>[][] objects;
	
	// TODO : a vérifier je suis pas sur de ce que je raconte
	/** The list of newly created agents. */
	private ArrayList<Agent> newAgents;

	/**
	 * Instantiates a new environment.
	 *
	 * @param w the width of the world
	 * @param h the height of the world
	 * @param radius the radius of an ant base
	 */
	@SuppressWarnings("unchecked")
	public Environment(int w, int h, int radius) {
		width = w;
		height = h;
		nbAgents = 0;
		cptAgents = 0;
		newAgents = new ArrayList<Agent>();
		baseRadius = radius;

		listeners = new ArrayList<EnvironmentListener>();

		// Create Bases
		Random rand = new Random();

		bases = new Circle[WorldConfig.BASE_NUMBER];
		basePositions = new BasePosition[WorldConfig.BASE_NUMBER];
		foodInBase = new int[WorldConfig.BASE_NUMBER];

		int n = 0;
		while (n < bases.length) {
			// We place a circle in a random place
			Circle c = new Circle();
			c.radius = baseRadius;
			c.x = rand.nextInt(width - 2 * radius) + radius;
			c.y = rand.nextInt(height - 2 * radius) + radius;

			// We check if the circle overlap with another circle
			boolean overlap = false;
			for (int j = 0; !overlap && j < n; ++j) {
				if (j != n) {
					overlap = Intersector.overlaps(c, bases[j]);
				}
			}

			// If the circle didn't overlap, it's a new base
			if (!overlap) {
				bases[n] = c;
				basePositions[n] = new BasePosition(c);
				foodInBase[n] = WorldConfig.DEFAULT_FOOD_IN_BASE;
				n += 1;
			}
		}
		
		nbAgentPerBases = new int[WorldConfig.BASE_NUMBER];

		objects = new ArrayList[width][height];

		// Simplex noise factor
		// Magically  chosen to look good
		final float d1 = 50;
		final float d2 = 75;
		final float d3 = 150;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				objects[i][j] = new ArrayList<EnvironmentObject>();

				// Applying the factor is like zooming on the noise
				// by adding different level of zoom, we can achieve
				// a good looking environment
				float noise = (float) SimplexNoise.noise(i / d1, j / d1);
				noise += (float) SimplexNoise.noise(i / d2, j / d2);
				noise += (float) SimplexNoise.noise(i / d3, j / d3);

				// This little trick is used to add grain to the noise
				// it create something a lot more messy so it's deactivated by default
				if (!WorldConfig.SMOOTH_FOOD_GENERATION) {
					float g = (float) SimplexNoise.noise(i, j) * 20;
					g = g - (int) g;
					noise += g;
				}

				// by varying FOOD_COVER_INTENSITY we set the quantity of food present on the map
				if (noise > (1f - WorldConfig.FOOD_COVER_INTENSITY)) {
					objects[i][j].add(new FoodPile(i, j, noise));
				}

				// We check if the case is in the base.
				for (int k = 0; k < bases.length; ++k) {
					if (bases[k].contains(i, j)) {
						objects[i][j].add(new BaseBody(k, i, j));
					}
				}
			}
		}
	}

	/**
	 * Adds an environment listener, can technicaly feed multiple GUI.
	 *
	 * @param el the environment listenener
	 */
	public void addListener(EnvironmentListener el) {
		listeners.add(el);
	}

	/**
	 * Gets the list of perceived object in the POV of a body.
	 *
	 * @param b the body who own the POV
	 * @return the list of perceived object
	 */
	public ArrayList<Perceivable> getPerception(AgentBody b) {
		ArrayList<Perceivable> perceptions = new ArrayList<Perceivable>();
		// if there is an ant we just want the other agent
		// around him

		// if its an ant
		if (b instanceof AntBody) {
			// around the body
			// on x
			for (int i = b.getX() - WorldConfig.ANT_FIELD_OF_VIEW; i < b.getX() + WorldConfig.ANT_FIELD_OF_VIEW; i++) {
				// on y
				for (int j = b.getY() - WorldConfig.ANT_FIELD_OF_VIEW; j < b.getY()
						+ WorldConfig.ANT_FIELD_OF_VIEW; j++) {
					// if the case is in the world
					if (i >= 0 && i < width && j >= 0 && j < height) {
						// we run all the object in the case
						for (EnvironmentObject body : objects[i][j]) {
							// we check only the ants and if this is not the
							// self body
							if (/* body instanceof AntBody && */ body != b) {
								perceptions.add(new Perceivable(body));
							}
						}
					}
				}
			}
		}
		return perceptions;
	}

	/**
	 * Move a body in a direction.
	 *
	 * @param d the direction in which the body should move
	 * @param b the body who will move
	 */
	public void move(Direction d, AgentBody b) {
		cptAgents++;
		int vectX = 0, vectY = 0;
		if (d == Direction.NORTH || d == Direction.NORTH_EAST || d == Direction.NORTH_WEST)
			vectY = 1;
		if (d == Direction.SOUTH || d == Direction.SOUTH_EAST || d == Direction.SOUTH_WEST)
			vectY = -1;

		if (d == Direction.EAST || d == Direction.NORTH_EAST || d == Direction.SOUTH_EAST)
			vectX = 1;
		if (d == Direction.WEST || d == Direction.NORTH_WEST || d == Direction.SOUTH_WEST)
			vectX = -1;

		if (b.getX() + vectX >= 0 && b.getX() + vectX < width && b.getY() + vectY >= 0 && b.getY() + vectY < height) {
			objects[b.getX()][b.getY()].remove(b);
			b.setPosition(b.getX() + vectX, b.getY() + vectY);
			objects[b.getX()][b.getY()].add(b);
			((AntBody) b).setDirection(d);
		} else {

			// Alternative 1
			// If a body meet a wall of the world, he goes to the opposite direction
			if ((b.getX() + vectX < 0 && (d == Direction.WEST) || d == Direction.NORTH_WEST
					|| d == Direction.SOUTH_WEST))
				((AntBody) b).setDirection(Direction.EAST);
			else if (b.getX() + vectX >= width
					&& (d == Direction.EAST || d == Direction.NORTH_EAST || d == Direction.SOUTH_EAST))
				((AntBody) b).setDirection(Direction.WEST);
			else if (b.getY() + vectY < 0
					&& (d == Direction.SOUTH || d == Direction.SOUTH_EAST || d == Direction.SOUTH_WEST))
				((AntBody) b).setDirection(Direction.NORTH);
			else if (b.getY() + vectY >= height
					&& (d == Direction.NORTH || d == Direction.NORTH_EAST || d == Direction.NORTH_WEST))
				((AntBody) b).setDirection(Direction.SOUTH);

			// Alternative 2

			// if (b.getX() + vectX < 0 && b.getY() + vectY < 0)
			// ((AntBody) b).direction = Direction.NORTH_EAST;
			// else if (b.getX() + vectX < 0 && b.getY() + vectY >= height)
			// ((AntBody) b).direction = Direction.SOUTH_EAST;
			// else if (b.getX() + vectX >= width && b.getY() + vectY < 0)
			// ((AntBody) b).direction = Direction.NORTH_WEST;
			// else if (b.getX() + vectX >= width && b.getY() + vectY >= height)
			// ((AntBody) b).direction = Direction.SOUTH_WEST;
			// else if ((b.getX() + vectX < 0 && d == Direction.WEST) ||
			// (b.getY() + vectY < 0 && d == Direction.SOUTH_EAST) || (b.getY()
			// + vectY >= height && d == Direction.NORTH_EAST))
			// ((AntBody) b).direction = Direction.EAST;
			// else if ((b.getX() + vectX >= width && d == Direction.EAST) ||
			// (b.getY() + vectY < 0 && d == Direction.SOUTH_WEST) || (b.getY()
			// + vectY >= height && d == Direction.NORTH_WEST))
			// ((AntBody) b).direction = Direction.WEST;
			// else if ((b.getY() + vectY < 0 && d == Direction.SOUTH) ||
			// (b.getX() + vectX < 0 && d == Direction.NORTH_WEST) || (b.getX()
			// + vectX >= width && d == Direction.NORTH_EAST))
			// ((AntBody) b).direction = Direction.NORTH;
			// else if ((b.getY() + vectY >= height && d == Direction.NORTH) ||
			// (b.getX() + vectX < 0 && d == Direction.SOUTH_WEST) || (b.getX()
			// + vectX >= width && d == Direction.SOUTH_EAST))
			// ((AntBody) b).direction = Direction.SOUTH;
		}

		// All agent have moved.
		if (nbAgents == cptAgents) {
			cptAgents = 0;
			// And we notify all listeners.
			notifyListeners();
		}
	}

	
	
	/**
	 * Destroy a body.
	 *
	 * @param b the body to be destroyed
	 */
	public void destroy(AgentBody b) {
		
		//if the agent is an ant he will become food
		if (b.getClass() == AntBody.class){
						
			boolean foodPileExists = false;
			int foodDropped = Math.max(WorldConfig.MIN_SIZE_FOOD_STACK, Math.min( WorldConfig.MAX_SIZE_FOOD_STACK, 
					WorldConfig.DEAD_ANT_FOOD_VALUE + ((AntBody)b).getFoodCaried()));
			for (EnvironmentObject o : objects[b.getX()][b.getY()]) {
				if (o instanceof FoodPile) {
					((FoodPile) o).DropFood(foodDropped);
					foodPileExists = true;
				}
			}
			if(!foodPileExists){
				float percent = (float) (foodDropped / WorldConfig.MAX_SIZE_FOOD_STACK);
				objects[b.getX()][b.getY()].add(new FoodPile(b.getX(),b.getY(),percent));
			}
  			nbAgents--;
  			nbAgentPerBases[((AntBody) b).getFactionID()] -= 1;
			if (nbAgents == cptAgents) {
				cptAgents = 0;
				// And we notify all listeners.
				notifyListeners();
			}
		}
		
		
		objects[b.getX()][b.getY()].remove(b);
	}

	public void clear() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				objects[i][j].clear();
			}
		}
	}
	
	/**
	 * Notify the listeners that a tick as ended.
	 */
	public void notifyListeners() {
		// build food positions
		ArrayList<FoodStackPosition> foods = new ArrayList<FoodStackPosition>();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (EnvironmentObject o : objects[i][j]) {
					if (o instanceof FoodPile) {
						foods.add(new FoodStackPosition((FoodPile) o));
					}
				}
			}
		}

		// notify each listener
		for (EnvironmentListener el : listeners)
			el.environmentChanged(basePositions, foods, newAgents);

		newAgents.clear();
	}

	/**
	 * Creates an ant body.
	 *
	 * @param faction the faction of the ant
	 * @param factionID the faction id of the ant
	 * @param basePosX the position of the base in which the ant will span in x
	 * @param basePosY the position of the base in which the ant will span in  y
	 * @return the ant body
	 */
	public AgentBody createAntBody(Faction faction, int factionID, int basePosX, int basePosY) {
		nbAgents++;
		nbAgentPerBases[factionID] += 1;

		// Spawn the body in a random position inside the base
		Random rand = new Random();
		double r = Math.sqrt(rand.nextDouble());
		double theta = rand.nextDouble() * 2 * Math.PI;

		double dx = baseRadius * r * Math.cos(theta) + basePosX;
		double dy = baseRadius * r * Math.sin(theta) + basePosY;

		int _x = (int) dx, _y = (int) dy;
		
		Direction dir = Direction.values()[rand.nextInt(Direction.values().length)];
		AntBody b = new AntBody(faction, factionID, dir, _x, _y, WorldConfig.LIFE_EXPECTANCY, this);

		objects[_x][_y].add(b);
		return b;
	}
	
	/**
	 * Creates an Queen body.
	 *
	 * @param faction the faction of the ant
	 * @param factionID the faction id of the ant
	 * @param basePosX the position of the base in which the ant will span in x
	 * @param basePosY the position of the base in which the ant will span in  y
	 * @return the Queen body
	 */
	public AgentBody createQueenBody(Faction faction, int factionID, int basePosX, int basePosY) {
		nbAgents++;
		nbAgentPerBases[factionID] += 1;
		
		Direction dir = Direction.NORTH;
		AntBody b = new QueenBody(faction, factionID, dir, basePosX, basePosY, WorldConfig.LIFE_EXPECTANCY, this);

		objects[basePosX][basePosY].add(b);
		return b;
	}

	/**
	 * Creates a pheromone.
	 *
	 * @param pt the type of pheromone that will be created
	 * @param ab the body that will create the pheromone
	 */
	public void createPheromone(PheromoneType pt, AgentBody ab, Direction d) {
		// We want to know if there is a pheromone of the same faction and type
		// on this
		// place
		
		// Spread of the pheromone
		/*int halfValue = WorldConfig.PHEROMONE_INITIAL_LIFE / 3;
		int spread1Value = WorldConfig.PHEROMONE_INITIAL_LIFE / 21;
		int spread2Value = WorldConfig.PHEROMONE_INITIAL_LIFE / 48;
		
		for(int i = ab.getX() - 2 ; i < ab.getX() + 2 ; i ++){
			for(int j = ab.getY() - 2 ; j < ab.getY() + 2 ; j ++){
				
				if (i >= 0 && i < width && j >= 0 && j < height){
					boolean needToCreatePheromone = true;
					for (EnvironmentObject eo : objects[i][j]) {
						if (eo instanceof PheromoneBody) {
							if (((PheromoneBody) eo).factionID == ((AntBody) ab).getFactionID()
									&& ((PheromoneBody) eo).pheromoneType == pt) {
								if(i == ab.getX() && j == ab.getY()){
									((PheromoneBody) eo).life += halfValue;
								}else if(Math.abs(ab.getX() - i) == 1 || Math.abs(ab.getY() - j) == 1){
									((PheromoneBody) eo).life += spread1Value;
								}else{
									((PheromoneBody) eo).life += spread2Value;
								}
								((PheromoneBody) eo).life = Math.max(((PheromoneBody) eo).life, WorldConfig.PHEROMONE_INITIAL_LIFE);
								needToCreatePheromone = false;
								break;
							}
						}
					}
	
					if (needToCreatePheromone) {
						PheromoneBody pb;
						if(i == ab.getX() && j == ab.getY()){
							pb = new PheromoneBody(i, j, ((AntBody) ab).getFaction(), ((AntBody) ab).getFactionID(), pt, this, halfValue);
						}else if(Math.abs(ab.getX() - i) == 1 || Math.abs(ab.getY() - j) == 1){
							pb = new PheromoneBody(i, j, ((AntBody) ab).getFaction(), ((AntBody) ab).getFactionID(), pt, this, spread1Value);
						}else{
							pb = new PheromoneBody(i, j, ((AntBody) ab).getFaction(), ((AntBody) ab).getFactionID(), pt, this, spread2Value);
						}
						
						newAgents.add(new PheromoneAgent(pb));
						objects[i][j].add(pb);
					}
				}
				
			}
		}*/
		
		boolean needToCreatePheromone = true;
		for (EnvironmentObject eo : objects[ab.getX()][ab.getY()]) {
			if (eo instanceof PheromoneBody) {
				if (((PheromoneBody) eo).factionID == ((AntBody) ab).getFactionID()
						&& ((PheromoneBody) eo).pheromoneType == pt) {

					((PheromoneBody) eo).life = WorldConfig.PHEROMONE_INITIAL_LIFE;
					((PheromoneBody) eo).pheromoneDirection = d;
					
					needToCreatePheromone = false;
					break;
				}
			}
		}

		if (needToCreatePheromone) {
			PheromoneBody pb = new PheromoneBody(ab.getX(), ab.getY(), ((AntBody) ab).getFaction(),
												((AntBody) ab).getFactionID(), pt, this, WorldConfig.PHEROMONE_INITIAL_LIFE, d);
			newAgents.add(new PheromoneAgent(pb));
			objects[ab.getX()][ab.getY()].add(pb);
		}
	}

	/**
	 * Pick up food.
	 *
	 * @param b the body that will pick up the food
	 * @return true, if successful
	 */
	public boolean pickUpFood(AgentBody b) {
		boolean isGettingFood = false;
		EnvironmentObject haveToRemove = null;

		for (EnvironmentObject o : objects[b.getX()][b.getY()]) {
			if (o instanceof FoodPile) {
				((AntBody)b).setFoodCarried(((FoodPile) o).TakeFood());
				isGettingFood = true;

				if (((FoodPile) o).IsEmpty()) {

					haveToRemove = o;
				}
			}
		}

		if (haveToRemove != null) {
			objects[b.getX()][b.getY()].remove(haveToRemove);
		}
		cptAgents++;

		if (nbAgents == cptAgents) {
			cptAgents = 0;
			// And we notify all listeners.
			notifyListeners();
		}

		return isGettingFood;
	}

	/**
	 * Adds the food to a base.
	 *
	 * @param b the body that will add the food to it's faction base
	 */
	public void addFoodToBase(AgentBody b) {
		foodInBase[((AntBody) b).getFactionID()] += ((AntBody) b).popFoodCaried();

		cptAgents++;
		if (nbAgents == cptAgents) {
			cptAgents = 0;
			// And we notify all listeners.
			notifyListeners();
		}

	}

	/**
	 * Gets the bases position.
	 *
	 * @return the bases position
	 */
	public BasePosition[] getBasePosition() {
		return basePositions;
	}

	/**
	 * Gets the amount of food in a base.
	 *
	 * @param n the id of the base we sek the amount from
	 * @return the current amount of food in the base
	 */
	public int GetFoodInBase(int n) {
		return foodInBase[n];
	}
	
	/**
	 * Gets the number of agent in a faction.
	 *
	 * @param n the id of a faction
	 * @return the number agent in the faction
	 */
	public int getNbAgent(int n){
		return nbAgentPerBases[n];
	}

	/**
	 * Gets 
	 *
	 * @param b the AntBody
	 * @return boolean if there 's enough food to take from the base
	 */
	public int eat(AntBody b) {
		if (foodInBase[b.getFactionID()] <= 0)
			return 0;
		else if ( foodInBase[b.getFactionID()] < WorldConfig.HUNGER_BAR){
			foodInBase[b.getFactionID()] = 0;
			return WorldConfig.HUNGER_BAR - foodInBase[b.getFactionID()];
		}else{
			foodInBase[b.getFactionID()] -= WorldConfig.HUNGER_BAR;
			return WorldConfig.HUNGER_BAR;
		}
	}

}
