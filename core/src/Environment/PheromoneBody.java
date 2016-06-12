package Environment;

import com.badlogic.gdx.math.Vector2;

/**
 * The Class PheromoneBody.
 */
public class PheromoneBody extends AgentBody {

	/** The faction from which the pheromone is emitted. */
	public Faction faction;

	/** The faction id from which the pheromone is emitted. */
	public int factionID;

	/** The pheromone type. */
	public PheromoneType pheromoneType;

	/** The life of the pheromone. */
	public int life;

	/** the direction of the pheromone */
	// public Direction pheromoneDirection;
	public Vector2 pheromoneDirection;

	/**
	 * Instantiates a new pheromone body.
	 *
	 * @param x
	 *            the position of the pheromone in x
	 * @param y
	 *            the position of the pheromone in y
	 * @param f
	 *            the faction of the pheromone
	 * @param fID
	 *            the faction ID of the pheromone
	 * @param pt
	 *            the pheromone type
	 * @param env
	 *            the environment
	 * @param _life
	 *            the life of the pheromone
	 */
	public PheromoneBody(int _x, int _y, Faction f, int fID, PheromoneType pt, Environment env, int _life, Vector2 d) {
		environmentReference = env;
		x = _x;
		y = _y;
		faction = f;
		factionID = fID;
		pheromoneType = pt;
		life = _life;
		pheromoneDirection = d;
	}

	/** {@inheritDoc} */
	@Override
	public void setPosition(int _x, int _y) {
		x = _x;
		y = _y;
	}

}
