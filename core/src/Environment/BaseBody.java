package Environment;

/**
 * The Class BaseBody is the "physical" representation of a base tile in an environment.
 */
public class BaseBody extends StaticObject {

	/** The faction in which the base belong. */
	private final int faction;

	/**
	 * Instantiates a new base body.
	 *
	 * @param factionID the faction in which the base belong
	 * @param _x the position of the base in x
	 * @param _y the position of the base in _y
	 */
	public BaseBody(int factionID, int _x, int _y) {
		faction = factionID;
		x = _x;
		y = _y;
	}
	
	/**
	 * Gets the faction id.
	 *
	 * @return the faction in which the base belong.
	 */
	public int getFactionID(){
		return faction;
	}
}
