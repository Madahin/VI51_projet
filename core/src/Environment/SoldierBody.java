package Environment;

public class SoldierBody extends AntBody {

	public SoldierBody(Faction f, int fID, Direction d, int initX, int initY, Environment env) {
		super(f, fID, d, initX, initY, env);
		// TODO Auto-generated constructor stub
	}

	public void attack(Perceivable p){
		environmentReference.attack(this, p);
	}
	
}
