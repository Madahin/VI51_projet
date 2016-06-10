package Environment;

public class QueenBody extends AntBody {

	public QueenBody(Faction f, int fID, Direction d, int initX, int initY, Environment env) {
		super(f, fID, d, initX, initY, env);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Spawns ants.
	 *
	 */
	public void spawnAnts(){
		environmentReference.spawnAnts(this);
	}
}
