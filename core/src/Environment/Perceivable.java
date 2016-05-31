package Environment;

import Agent.AntAgent;

public class Perceivable {
	private int x;
	private int y;
	//private Direction direction;
	private Class type;
	private Faction faction;
	private PheromoneType pType;
	
	public Perceivable(EnvironmentObject b){
		x = b.getX();
		y = b.getY();
		type = b.getClass();
		
		if(b instanceof AntBody){
			faction = ((AntBody) b).faction;
		}
		if(b instanceof PheromoneBody){
			faction = ((PheromoneBody) b).faction;
			pType = ((PheromoneBody) b).pheromoneType;
		}
		
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Faction getFaction(){
		return faction;
	}
	
	public PheromoneType getPheromoneType(){
		return pType;
	}
	
	public Class getType(){
		return type; 
	}
	
}
