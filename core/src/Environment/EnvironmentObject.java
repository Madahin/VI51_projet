package Environment;

/**
 * The Class EnvironmentObject is the "physical" representation of an object in
 * the environment
 */
public abstract class EnvironmentObject implements IEnvironmentObject {

	/** A reference to the environment. */
	protected Environment environmentReference;

	/** The position in x. */
	protected int x;

	/** The position in y. */
	protected int y;

	/** {@inheritDoc} */
	public int getX() {
		return x;
	}

	/** {@inheritDoc} */
	public int getY() {
		return y;
	}
}
