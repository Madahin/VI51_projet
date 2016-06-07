package Environment;

/**
 * The Class EnvironmentObject is the "physical" representation of an object in the environment
 */
public abstract class EnvironmentObject {
	
	/** The position in x. */
	protected int x;
	
	/** The position in y. */
	protected int y;
	
	/**
	 * Gets the position in x.
	 *
	 * @return the position in x
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the position in y.
	 *
	 * @return the position in y
	 */
	public int getY(){
		return y;
	}
	
	/** A reference to the environment. */
	protected Environment environmentReference;
	
}
