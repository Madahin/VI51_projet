package Agent;

import java.util.ArrayList;
import java.util.Random;

import Environment.AgentBody;
import Environment.AntBody;
import Environment.Direction;
import Environment.Perceivable;
import Environment.PheromoneBody;
import Environment.PheromoneType;

public class AntAgent extends Agent {

	private boolean isCariingFood;
	private int pheromoneTicks;
	
	public AntAgent(AgentBody b){
		body = b;
		isCariingFood = false;
		pheromoneTicks = 0;
	}
	
	@Override
	public void live() {
		
		ArrayList<Perceivable> perceptions = getPerception();
		// Check for pheromones on the same faction
		for(Perceivable p : perceptions){
			if(p.getType() == PheromoneBody.class){
				
			}
		}
		
		if(isCariingFood){
			
		}else{
			
			wander();
			createPheromone(PheromoneType.Base);
		}
		
		
	}
	
	public void wander(){
		Random rand = new Random();
		// return a value between 0 and 7.
		// each number means a direction.
		int direction = rand.nextInt(Direction.values().length);
		
		switch(direction){
			case 0:
				move(Direction.EAST);
			break;
			case 1:
				move(Direction.NORTH);
			break;
			case 2:
				move(Direction.NORTH_EAST);
			break;
			case 3:
				move(Direction.NORTH_WEST);
			break;
			case 4:
				move(Direction.SOUTH);
			break;
			case 5:
				move(Direction.SOUTH_EAST);
			break;
			case 6:
				move(Direction.SOUTH_WEST);
			break;
			case 7:
				move(Direction.WEST);
			break;
		}
	}
	
	public void createPheromone(PheromoneType pt){
		pheromoneTicks += 1;
		
		if(pheromoneTicks == 2){
			((AntBody)this.body).createPheromone(pt);
			pheromoneTicks = 0;
		}
	}

}
