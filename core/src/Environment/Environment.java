package Environment;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

import Agent.Agent;
import UserInterface.Simulator;

public class Environment {
	private int width;
	private int height;
	private int nbAgents;
	private int cptAgents;
	
	private ArrayList<EnvironmentListener> listeners;
	private ArrayList<EnvironmentObject>[][] objects;
	private ArrayList<Agent> referenceAgents;
	
	public Environment(int w, int h, ArrayList<Agent> as){
		width = w;
		height = h;
		nbAgents = 0;
		cptAgents = 0;
		referenceAgents = as;
		
		listeners = new ArrayList<EnvironmentListener>();
		
		objects = new ArrayList[width][height];
		for(int i = 0 ; i < width ; i++){
			for(int j = 0 ; j < height ; j++){
				objects[i][j] = new ArrayList<EnvironmentObject>();
			}
		}
	}
	
	public void addListener(EnvironmentListener el){
		listeners.add(el);
	}
	
	public ArrayList<Perceivable> getPerception(AgentBody b){
		ArrayList<Perceivable> perceptions = new ArrayList<Perceivable>();
		// if there is an ant we just want the other agent
		// around him
		
		// if its an ant
		if(b instanceof AntBody){
			// around the body
			// on x
			for(int i = b.getX() - 2 ; i < b.getX() + 2 ; i++){
				// on y
				for(int j = b.getY() - 2 ; j < b.getY() + 2 ; j++){
					// if the case is in the world
					if(i >= 0 && i < width && j >= 0 && j < height){
						// we run all the object in the case
						for(EnvironmentObject body : objects[i][j]){
							// we check only the ants and if this is not the self body
							if(body instanceof AntBody && body != b){
								perceptions.add(new Perceivable((AgentBody)body));
							}
						}
					}
				}
			}
		}
		return perceptions;
	}
	
	public void move(Direction d, AgentBody b){
		cptAgents ++;
		int vectX=0, vectY=0;
		if(d == Direction.NORTH || d == Direction.NORTH_EAST || d == Direction.NORTH_WEST)
			vectY = 1;
		if(d == Direction.SOUTH || d == Direction.SOUTH_EAST || d == Direction.SOUTH_WEST)
			vectY = -1;
		
		if(d == Direction.EAST || d == Direction.NORTH_EAST || d == Direction.SOUTH_EAST)
			vectX = 1;
		if(d == Direction.WEST || d == Direction.NORTH_WEST || d == Direction.SOUTH_WEST)
			vectX = -1;
		
		if(b.getX() + vectX >= 0 && b.getX() + vectX < width && b.getY() + vectY >= 0 && b.getY() + vectY < height){
			objects[b.getX()][b.getY()].remove(b);
			b.setPosition(b.getX() + vectX, b.getY() + vectY);
			objects[b.getX()][b.getY()].add(b);
		}
		
		// All agent have moved.
		if(nbAgents == cptAgents){
			cptAgents = 0;
			// And we notify all listeners.
			notifyListeners();
		}
	}
	
	public void notifyListeners(){
		for(EnvironmentListener el : listeners)
			el.environmentChanged();
	}
	
	public AgentBody createBlackAntBody(){
		nbAgents ++;
		// Black Ants are created on the upper left of the map
		Random rand = new Random();
		int _x, _y;
		
		_x = rand.nextInt(width) / 2;
		_y = rand.nextInt(height) / 2;
		
		AntBody b = new AntBody(Faction.BlackAnt, _x, _y, this);
		objects[_x][_y].add(b);
		return b;
	}
	
	public AgentBody createRedAntBody(){
		nbAgents ++;
		// Red Ants are created on the down right on the map
		Random rand = new Random();
		int _x, _y;
		
		_x = (width / 2) + (rand.nextInt(width) / 2);
		_y = (height / 2) + (rand.nextInt(height) / 2);
		
		AntBody b = new AntBody(Faction.RedAnt, _x, _y, this);
		objects[_x][_y].add(b);
		return b;
	}
	
	
	
}
