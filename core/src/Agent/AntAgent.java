package Agent;

import java.util.ArrayList;
import java.util.Random;

import Environment.AgentBody;
import Environment.Direction;
import Environment.Perceivable;
import Environment.PheromoneBody;

public class AntAgent extends Agent {

	private boolean isCariingFood;
	
	
	public AntAgent(AgentBody b){
		body = b;
		isCariingFood = false;
	}
	
	@Override
	public void live() {
		ArrayList<Perceivable> perceptions = getPerception();
		// Check for pheromones on the same faction
		/*for(Perceivable p : perceptions){
			if(p.getType() == PheromoneBody.class){
				
			}
		}
		
		if(isCariingFood){
			
		}else{
			
		}*/
		
		wander();
		
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

}
