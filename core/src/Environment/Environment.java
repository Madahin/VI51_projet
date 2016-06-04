package Environment;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

import Agent.Agent;
import Agent.AntAgent;
import Agent.PheromoneAgent;
import UserInterface.Simulator;

import Config.WorldConfig;

public class Environment {
	private int width;
	private int height;
	private int nbAgents;
	private int cptAgents;
	
	private int foodInBlackBase = 1000;
	private int foodInRedBase = 1000;
	
	// Designing Bases;
	private int blackBaseX, blackBaseY;
	private int redBaseX, redBaseY;
	private int baseRadius;
	
	private int percentageFood;
	
	private ArrayList<EnvironmentListener> listeners;
	private ArrayList<EnvironmentObject>[][] objects;
	private ArrayList<Agent> newAgents;
	
	public Environment(int w, int h,int radius,int _percentageFood){
		width = w;
		height = h;
		nbAgents = 0;
		cptAgents = 0;
		newAgents = new ArrayList<Agent>() ;
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
					objects[i][j].add(new FoodPile(i, j, 100, 100));
				}
				
				// And we check if the case is in the base.
				// Black
				if( (i - blackBaseX) * (i - blackBaseX) + (j - blackBaseY) * (i - blackBaseY) <= baseRadius * baseRadius ){
					objects[i][j].add(new BlackBase(i, j));
				}
				
				// Red
				if( (i - redBaseX) * (i - redBaseX) + (j - redBaseY) * (i - redBaseY) <= baseRadius * baseRadius ){
					objects[i][j].add(new RedBase(i, j));
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
			for(int i = b.getX() - 4 ; i < b.getX() + 4 ; i++){
				// on y
				for(int j = b.getY() - 4 ; j < b.getY() + 4 ; j++){
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
	
	public void destroy(AgentBody b){
		objects[b.getX()][b.getY()].remove(b);
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
			el.environmentChanged(blackBaseX, blackBaseY, redBaseX, redBaseY, foods, newAgents);
		
		newAgents.clear();
	}
	
	private AgentBody createAntBody(Faction faction, int basePosX, int basePosY){
		nbAgents ++;
		// Black Ants are created on the down left of the map
		
		Random rand = new Random();
		double r = Math.sqrt(rand.nextDouble());
		double theta = rand.nextDouble() * 2 * Math.PI;
		
		double dx = baseRadius*r*Math.cos(theta) + basePosX;
		double dy = baseRadius*r*Math.sin(theta) + basePosY;
		
		int _x = (int)dx, _y = (int)dy;
		Direction dir = Direction.values()[rand.nextInt(Direction.values().length)];
		AntBody b = new AntBody(faction, dir, _x, _y, this);
		
		objects[_x][_y].add(b);
		return b;
	}
	
	public AgentBody createBlackAntBody(){
		return createAntBody(Faction.BlackAnt, blackBaseX, blackBaseY);
	}
	
	public AgentBody createRedAntBody(){
		return createAntBody(Faction.RedAnt, redBaseX, redBaseY);
	}
	
	public void createPheromone(PheromoneType pt, AgentBody ab){
		// We want to know if there is a pheromone of the same faction and type on this
		// place
		boolean needToCreatePheromone = true;
		for(EnvironmentObject eo : objects[ab.getX()][ab.getY()]){
			if(eo instanceof PheromoneBody){
				if(((PheromoneBody) eo).faction == ((AntBody) ab).faction && ((PheromoneBody) eo).pheromoneType == pt){

					((PheromoneBody) eo).life += WorldConfig.PHEROMONE_INITIAL_LIFE;
					needToCreatePheromone = false;

				}
			}
		}
		
		if(needToCreatePheromone){
			PheromoneBody pb = new PheromoneBody(ab.getX(), ab.getY(), ((AntBody)ab).faction, pt, this);
			newAgents.add(new PheromoneAgent(pb));
			objects[ab.getX()][ab.getY()].add(pb);
		}
	}
	
	public boolean pickUpFood(AgentBody b){
		boolean isGettingFood = false;
		EnvironmentObject haveToRemove = null;
		
		for(EnvironmentObject o : objects[b.getX()][b.getY()]){
			if(o instanceof FoodPile){
				((FoodPile) o).TakeFood();
				isGettingFood = true;

				if(((FoodPile) o).IsEmpty()){

					haveToRemove = o;
				}
			}
		}
		
		if(haveToRemove != null){
			objects[b.getX()][b.getY()].remove(haveToRemove);
		}
		cptAgents++;
		
		if(nbAgents == cptAgents){
			cptAgents = 0;
			// And we notify all listeners.
			notifyListeners();
		}
		
		return isGettingFood;
	}
	
	public void addFoodToBase(AgentBody b){
		if(((AntBody)b).faction == Faction.BlackAnt){
			foodInBlackBase += 100;
		}else{
			foodInRedBase += 100;
		}
		
		cptAgents++;
		if(nbAgents == cptAgents){
			cptAgents = 0;
			// And we notify all listeners.
			notifyListeners();
		}

	}
	
	public int GetFoodInRedBase(){
		return foodInRedBase;
	}
	
	public int GetFoodInBlackBase(){
		return foodInBlackBase;
	}
	
}
