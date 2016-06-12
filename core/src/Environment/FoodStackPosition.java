package Environment;

/**
 * The Class FoodStackPosition represent a foodPile for the usage of a GUI
 */
public class FoodStackPosition extends Position {

	/** The size of the foodPile. */
	private final int size;

	/**
	 * Instantiates a new foodStack position.
	 *
	 * @param fp
	 *            the foodPile to be copied
	 */
	public FoodStackPosition(FoodPile fp) {
		super(fp.getX(), fp.getY());
		size = fp.getSize();
	}

	/**
	 * Gets the size of the foodPile.
	 *
	 * @return the size of the foodPile
	 */
	public int getSize() {
		return size;
	}
}
