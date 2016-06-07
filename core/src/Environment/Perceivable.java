package Environment;

/**
 * The Class Perceivable represent an object that is perceived by an agent.
 */
public class Perceivable {
	
	/** The position of the object in x. */
	private final int x;
	
	/** The position of the object in y. */
	private final int y;
	
	/** The type of the object. */
	private final Class<? extends EnvironmentObject> type;
	
	/** The faction of the object if it has one. */
	private Faction faction;
	
	/** The faction idof the object if it has one. */
	private int factionID;
	
	/** The type of pheromone if it is one. */
	private PheromoneType pType;
	
	/** The pheromone life if it is one. */
	private int pheromoneLife;

	/**
	 * Instantiates a new perceivable.
	 *
	 * @param b the object from which the perceivable is created
	 */
	public Perceivable(EnvironmentObject b) {
		x = b.getX();
		y = b.getY();
		type = b.getClass();
		
		if(b instanceof BaseBody){
			factionID = ((BaseBody) b).getFactionID();
		}

		if (b instanceof AntBody) {
			faction = ((AntBody) b).getFaction();
			factionID = ((AntBody) b).getFactionID();
		}
		if (b instanceof PheromoneBody) {
			faction = ((PheromoneBody) b).faction;
			factionID = ((PheromoneBody) b).factionID;
			pType = ((PheromoneBody) b).pheromoneType;
			pheromoneLife = ((PheromoneBody) b).life;
		}
		
		

	}

	/**
	 * Gets the position of the object in x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the position of the object in y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the pheromone life.
	 *
	 * @return the pheromone life
	 */
	public int getPheromoneLife() {
		return pheromoneLife;
	}

	/**
	 * Gets the faction.
	 *
	 * @return the faction
	 */
	public Faction getFaction() {
		return faction;
	}
	
	/**
	 * Gets the faction id.
	 *
	 * @return the faction id
	 */
	public int getFactionID(){
		return factionID;
	}

	/**
	 * Gets the pheromone type.
	 *
	 * @return the pheromone type
	 */
	public PheromoneType getPheromoneType() {
		return pType;
	}

	/**
	 * Gets the type of the object.
	 *
	 * @return the type of the object
	 */
	public Class<? extends EnvironmentObject> getType() {
		return type;
	}

}
