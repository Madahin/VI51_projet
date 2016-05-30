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
	
	// Designing Bases;
	private int blackBaseX, blackBaseY;
	private int redBaseX, redBaseY;
	private int baseRadius;
	
	private int percentageFood;
	
	private ArrayList<EnvironmentListener> listeners;
	private ArrayList<EnvironmentObject>[][] objects;
	private ArrayList<Agent> referenceAgents;
	
	public Environment(int w, int h,int radius,int _percentageFood,  ArrayList<Agent> as){
		width = w;
		height = h;
		nbAgents = 0;
		cptAgents = 0;
		referenceAgents = as;
		baseRadius = radius;
		percentageFood = _percentageFood;
		
		listeners = new ArrayList<EnvironmentListener>();
		
		// Create Bases
		Random rand = new Random();
		blackBaseX = rand.nextInt(width/4) +  baseRadius;
		blackBaseY = rand.nextInt(height/4) + baseRadius;
		
		redBaseX = rand.nextInt(width/4) + (3*width/4) - baseRadius;
		redBaseY = rand.nextInt(height/4) + (3*width/4) - baseRadius;
		
		objects = new ArrayList[width][height];
		for(int i = 0 ; i < width ; i++){
			for(int j = 0 ; j < height ; j++){
				objects[i][j] = new ArrayList<EnvironmentObject>();
				
				int test = rand.nextInt(100);
				if(test <= percentageFood){
					objects[i][j].add(new FoodPile(i, j, 100, 500));
				}
				
				// And we check if the case is in the base.
				// Black
				if( (i - blackBaseX) * (i - blackBaseX) + (j - blackBaseY) * (i - blackBaseY) <= baseRadius * baseRadius ){
					objects[i][j].add(new BlackBase());
				}
				
				// Red
				if( (i - redBaseX) * (i - redBaseX) + (j - redBaseY) * (i - redBaseY) <= baseRadius * baseRadius ){
					objects[i][j].add(new RedBase());
				}
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
							if(/*body instanceof AntBody &&*/ body != b){
								perceptions.add(new Perceivable(body));
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
		// build food positions
		ArrayList<Position> foods = new ArrayList<Position>();
		
		for(int i = 0 ; i < width ; i++){
			for(int j = 0 ; j < height ; j++){
				for(EnvironmentObject o : objects[i][j]){
					if( o instanceof FoodPile){
						foods.add(new Position(o.getX(), o.getY()));
					}
				}
			}
		}
		
		for(EnvironmentListener el : listeners)
			el.environmentChanged(blackBaseX, blackBaseY, redBaseX, redBaseY, foods);
	}
	
	public AgentBody createBlackAntBody(){
		nbAgents ++;
		// Black Ants are created on the down left of the map
		
		Random rand = new Random();
		double r = Math.sqrt(rand.nextDouble());
		double theta = rand.nextDouble() * 2 * Math.PI;
		
		double dx = baseRadius*r*Math.cos(theta) + blackBaseX;
		double dy = baseRadius*r*Math.sin(theta) + blackBaseY;
		
		int _x = (int)dx, _y = (int)dy;
		AntBody b = new AntBody(Faction.BlackAnt, _x, _y, this);
		
		objects[_x][_y].add(b);
		return b;
	}
	
	public AgentBody createRedAntBody(){
		nbAgents ++;
		// Red Ants are created on the down right on the map

		Random rand = new Random();
		double r = Math.sqrt(rand.nextDouble());
		double theta = rand.nextDouble() * 2 * Math.PI;
		
		double dx = baseRadius*r*Math.cos(theta) + redBaseX;
		double dy = baseRadius*r*Math.sin(theta) + redBaseY;
		
		int _x = (int)dx, _y = (int)dy;
		AntBody b = new AntBody(Faction.RedAnt, _x, _y, this);
		
		objects[_x][_y].add(b);
		return b;
	}
	
	
	
}
