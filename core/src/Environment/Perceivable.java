package Environment;

public class Perceivable {
	private final int x;
	private final int y;
	// private Direction direction;
	private final Class<? extends EnvironmentObject> type;
	private Faction faction;
	private int factionID;
	private PheromoneType pType;
	private int pheromoneLife;

	public Perceivable(EnvironmentObject b) {
		x = b.getX();
		y = b.getY();
		type = b.getClass();

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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPheromoneLife() {
		return pheromoneLife;
	}

	public Faction getFaction() {
		return faction;
	}
	
	public int getFactionID(){
		return factionID;
	}

	public PheromoneType getPheromoneType() {
		return pType;
	}

	public Class<? extends EnvironmentObject> getType() {
		return type;
	}

}
