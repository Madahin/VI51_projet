package Environment;

public class QueenBody extends AntBody {

	/**
	 * Instantiates a new queen body.
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
	public QueenBody(Faction f, int fID, Direction d, int initX, int initY, Environment env) {
		super(f, fID, d, initX, initY, env);
	}

	/**
	 * Spawns ants.
	 */
	public void spawnAnts() {
		environmentReference.spawnAnts(this);
	}
}
