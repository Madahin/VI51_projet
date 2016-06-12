package Environment;

/**
 * The Class Position represente an object to be used by the GUI.
 */
public class Position {

	/** The position in x. */
	private int x;

	/** The position in y. */
	private int y;

	/**
	 * Instantiates a new position.
	 *
	 * @param _x
	 *            the _x
	 * @param _y
	 *            the _y
	 */
	public Position(int _x, int _y) {
		x = _x;
		y = _y;
	}

	/**
	 * Gets the position in x.
	 *
	 * @return the position in x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the position in y.
	 *
	 * @return the position in y
	 */
	public int getY() {
		return y;
	}
}
