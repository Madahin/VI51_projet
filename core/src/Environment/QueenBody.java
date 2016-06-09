package Environment;

public class QueenBody extends AntBody {

	public QueenBody(Faction f, int fID, Direction d, int initX, int initY, int _Life, Environment env) {
		super(f, fID, d, initX, initY, _Life, env);
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
