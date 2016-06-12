package Environment;

public class SoldierBody extends AntBody {

	/**
	 * Instantiates a new soldier body.
	 *
	 * @param f
	 *            the faction in which the ant belong
	 * @param fID
	 *            the id of the faction in which the ant belong
	 * @param d
	 *            the direction in which the ant is moving
	 * @param initX
	 *            the initial position in x
	 * @param initY
	 *            the initial position in y
	 * @param env
	 *            the environment
	 */
	public SoldierBody(Faction f, int fID, Direction d, int initX, int initY, Environment env) {
		super(f, fID, d, initX, initY, env);
	}

	/**
	 * Try to attack a perceived entity
	 * 
	 * @param p
	 *            the perceived entity
	 */
	public void attack(Perceivable p) {
		environmentReference.attack(this, p);
	}

}
