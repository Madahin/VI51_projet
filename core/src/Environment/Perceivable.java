package Environment;

public class Perceivable {
	private int x;
	private int y;
	//private Direction direction;
	private Class type;
	
	public Perceivable(EnvironmentObject b){
		x = b.getX();
		y = b.getY();
		type = b.getClass();
		
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	/*public Direction getDirection(){
		return direction;
	}*/
	
	public Class getType(){
		return type; 
	}
	
	
}
