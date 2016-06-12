package Environment;

/**
 * The Class DynamicObject represent a dynamic object in an environment.
 */
public abstract class DynamicObject extends EnvironmentObject {

	/**
	 * Sets the position of the object.
	 *
	 * @param x
	 *            the position of the object in x
	 * @param y
	 *            the position of the object in y
	 */
	public abstract void setPosition(int x, int y);

}
