package Environment;

public abstract class EnvironmentObject {
	protected int x;
	protected int y;
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	protected Environment environmentReference;
	
}
