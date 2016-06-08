package Agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import Environment.AgentBody;
import Environment.AntBody;
import Environment.BaseBody;
import Environment.Direction;
import Environment.FoodPile;
import Environment.Perceivable;
import Environment.PheromoneBody;
import Environment.PheromoneComparator;
import Environment.PheromoneType;

/**
 * Representation of a worker ant.
 */
public class AntAgent extends Agent {

	/** True if the ant carry food. */
	private boolean isCarryingFood;

	/** Delay the pheromone creation tick. */
	private int pheromoneTicks;

	/** The current objective of the ant. */
	private Perceivable currentObjective;

	/**
	 * Instantiates a new ant agent.
	 *
	 * @param b
	 *            the "physical" representation of the ant in the environment
	 */
	public AntAgent(AgentBody b) {
		currentObjective = null;
		body = b;
		isCarryingFood = false;
		pheromoneTicks = 0;
	}

	/** {@inheritDoc} */
	@Override
	public void live() {
		
		// Life expectancy is decreasing
		//((AntBody) body).life--;
		if (((AntBody) body).life <= 0){
			this.destroy();
			return;
		}
		
		// Filling the goal is a priority
		if (currentObjective != null) {
			if (currentObjective.getX() == body.getX() && currentObjective.getY() == body.getY()) {
				currentObjective = null;
			} else {
				goToObject(currentObjective);
				if (isCarryingFood) {
					createPheromone(PheromoneType.Food);
				} else {
					createPheromone(PheromoneType.Base);
				}

				return;
			}
		}

		ArrayList<Perceivable> perceptions = getPerception();

		ArrayList<Perceivable> pheromonesFood = new ArrayList<Perceivable>();
		ArrayList<Perceivable> pheromonesBase = new ArrayList<Perceivable>();
		ArrayList<Perceivable> foods = new ArrayList<Perceivable>();
		ArrayList<Perceivable> bases = new ArrayList<Perceivable>();
		Perceivable onFood = null;
		Perceivable onBase = null;

		// We check for all perceptions if there each kind of elements
		for (Perceivable p : perceptions) {
			// On the same case or not
			if (p.getX() == body.getX() && p.getY() == body.getY()) {

				if (p.getType().equals(FoodPile.class)) { // food

					onFood = p;
				} else if (p.getFactionID() == ((AntBody) body).getFactionID() && p.getType().equals(BaseBody.class)) { // base
					onBase = p;
				}

			} else { // means other cases so we put those element in the lists
				if (p.getType().equals(FoodPile.class)) { // Food
					foods.add(p);
				} else if (p.getType().equals(PheromoneBody.class)
						&& p.getFactionID() == ((AntBody) body).getFactionID()) { // Pheromone
																					// of
																					// faction
					if (p.getPheromoneType() == PheromoneType.Base) { // Base
																		// type
						pheromonesBase.add(p);
					} else { // pheromone type == Food
						pheromonesFood.add(p);
					}
				} else if (p.getFactionID() == ((AntBody) body).getFactionID()) { // Base
					bases.add(p);
				}
			}
		}

		// Now that all perceptions are sort in differents lists
		// We sort the pheromones list in their intensities order.
		PheromoneComparator comparator = new PheromoneComparator();
		Collections.sort(pheromonesFood, comparator);
		Collections.sort(pheromonesBase, comparator);

		// Here are the decision algorithm
		// In the first place we want to know in wich state
		// is the ant
		if (isCarryingFood) { // If the ant have food
			// So she have to return it to the base
			if (onBase != null) { // if we are on a base tile
				// we add the food to the base
				addFoodToBase();
				isCarryingFood = false;

			} else if (!bases.isEmpty()) { // if there is base around
				// we go to the first tile we encounter
				goToObject(bases.get(0));
			} else if (!pheromonesBase.isEmpty()) { // if there base pheromone
													// around
				// we go to that pheromone
				pheromonesVector(pheromonesBase, false);


			} else { // if there is none to do
						// we wander

				wander(((AntBody) body).getDirection());
			}
			// we produce a Food pheromone
			createPheromone(PheromoneType.Food);

		} else { // If the ant looking for the food
					// She have to perceive all the things around her
			if (onFood != null) {
				// We change the state with pick up food
				// there some case were the ant ant to pick the food
				// but in the same time an other ant pick the food
				// so the environment gives the same state. the ant will
				// continue for searching food
				isCarryingFood = pickUpFood();
				isCarryingFood = true;
			} else if (!foods.isEmpty()) { // there is food around
				// If we detect food we want to go to the first
				// item we encounter.
				goToObject(foods.get(0));
			} else if (!pheromonesFood.isEmpty()) { // there is food pheromone
													// around
				// We follow it

				pheromonesVector(pheromonesFood, true);
			
			} else { // we don't know what to do so we wander
				wander(((AntBody) body).getDirection());
			}
			// We produce a Base Pheromone
			createPheromone(PheromoneType.Base);

		}

	}

	/**
	 * Adds the food to the base.
	 */
	public void addFoodToBase() {
		((AntBody) body).addFoodToBase();
		isCarryingFood = false;
	}

	/**
	 * Pick up food.
	 *
	 * @return true, if successful
	 */
	public boolean pickUpFood() {
		return ((AntBody) body).pickUpFood();
	}

	/**
	 * Go to a perceivable object.
	 *
	 * @param obj
	 *            the perceived object
	 */
	public void goToObject(Perceivable obj) {

		int x = obj.getX();
		int y = obj.getY();
		int bodyX = body.getX();
		int bodyY = body.getY();

		if (currentObjective == null)
			currentObjective = obj;

		if (x < bodyX && y < bodyY) {
			move(Direction.SOUTH_WEST);
		} else if (x < bodyX && y > bodyY) {
			move(Direction.NORTH_WEST);
		} else if (x > bodyX && y < bodyY) {
			move(Direction.SOUTH_EAST);
		} else if (x > bodyX && y > bodyY) {
			move(Direction.NORTH_EAST);
		} else if (x == bodyX) {
			if (y > bodyY) {
				move(Direction.NORTH);
			} else if (y < bodyY) {
				move(Direction.SOUTH);
			}
		} else if (y == bodyY) {
			if (x > bodyX) {
				move(Direction.EAST);
			} else if (x < bodyX) {
				move(Direction.WEST);
			}
		}
	}

	/**
	 * Wander in a random direction, but never to far from the last direction.
	 *
	 * @param LastDirection
	 *            the last direction
	 */
	public void wander(Direction LastDirection) {
		Random rand = new Random();

		ArrayList<Direction> possibilities = new ArrayList<Direction>();

		if (LastDirection == Direction.NORTH) {
			possibilities.add(Direction.NORTH_WEST);
			possibilities.add(Direction.NORTH);
			possibilities.add(Direction.NORTH_EAST);
			possibilities.add(Direction.EAST);
			possibilities.add(Direction.WEST);
		} else if (LastDirection == Direction.NORTH_EAST) {
			possibilities.add(Direction.NORTH);
			possibilities.add(Direction.NORTH_EAST);
			possibilities.add(Direction.EAST);
			possibilities.add(Direction.NORTH_WEST);
			possibilities.add(Direction.SOUTH_EAST);
		} else if (LastDirection == Direction.EAST) {
			possibilities.add(Direction.NORTH_EAST);
			possibilities.add(Direction.EAST);
			possibilities.add(Direction.SOUTH_EAST);
			possibilities.add(Direction.NORTH);
			possibilities.add(Direction.SOUTH);
		} else if (LastDirection == Direction.SOUTH_EAST) {
			possibilities.add(Direction.EAST);
			possibilities.add(Direction.SOUTH_EAST);
			possibilities.add(Direction.SOUTH);
			possibilities.add(Direction.NORTH_EAST);
			possibilities.add(Direction.SOUTH_WEST);
		} else if (LastDirection == Direction.SOUTH) {
			possibilities.add(Direction.SOUTH_EAST);
			possibilities.add(Direction.SOUTH);
			possibilities.add(Direction.SOUTH_WEST);
			possibilities.add(Direction.EAST);
			possibilities.add(Direction.WEST);
		} else if (LastDirection == Direction.SOUTH_WEST) {
			possibilities.add(Direction.SOUTH);
			possibilities.add(Direction.SOUTH_WEST);
			possibilities.add(Direction.WEST);
			possibilities.add(Direction.SOUTH_EAST);
			possibilities.add(Direction.NORTH_WEST);
		} else if (LastDirection == Direction.WEST) {
			possibilities.add(Direction.SOUTH_WEST);
			possibilities.add(Direction.WEST);
			possibilities.add(Direction.NORTH_WEST);
			possibilities.add(Direction.NORTH);
			possibilities.add(Direction.SOUTH);
		} else if (LastDirection == Direction.NORTH_WEST) {
			possibilities.add(Direction.WEST);
			possibilities.add(Direction.NORTH_WEST);
			possibilities.add(Direction.NORTH);
			possibilities.add(Direction.NORTH_EAST);
			possibilities.add(Direction.SOUTH_WEST);
		}

		move(possibilities.get(rand.nextInt(possibilities.size())));

	}

	/**
	 * Pheromones vector.
	 * this methods define with all pheromones perceived the right vector to follow for each ants
	 * @param list
	 *            the list of pheromones perceived
	 * @param inv
	 *            the inv if we want to go toward the pheromonesor not
	 *         
	 */
	public void pheromonesVector(ArrayList<Perceivable> list, boolean inv){
		ArrayList<Vector2> vectors = new ArrayList<Vector2>();
		for(Perceivable p : list){

			Vector2 tmpVect = new Vector2(p.getX() - body.getX(), p.getY() - body.getY());
			tmpVect.scl(p.getPheromoneLife());
			tmpVect.nor();
			vectors.add(tmpVect);
		}
		Vector2 finalVect = new Vector2(0.0f, 0.0f);

		for (Vector2 vect : vectors) {
			finalVect.add(vect);
		}

		
		if(inv)
			finalVect.scl(-1.0f);
		
		/*Vector2 finalVect = new Vector2(0.0f , 0.0f);
		Perceivable first = list.get(0);
		Perceivable last = list.get(list.size()-1);
		int diffX;
		int diffY;
		if(!inv){
			diffX = last.getX() - first.getX();
			diffY = last.getY() - first.getY();
		}else{
			diffX = first.getX() - last.getX();
			diffY = first.getY() - last.getY();
		}
		finalVect.x = diffX;
		finalVect.y = diffY;*/
		
		double angle = Math.atan2(finalVect.x, finalVect.y);
		
		if(angle > -Math.PI/8.0d && angle <= Math.PI/8.0d){
			move(Direction.EAST);
		}else if(angle > Math.PI/8.0d && angle <= 3*Math.PI/8.0d ){
			move(Direction.NORTH_EAST);
		}else if(angle > 3*Math.PI/8.0d && angle <= 5*Math.PI/8.0d){
			move(Direction.NORTH);
		}else if(angle > 5*Math.PI/8.0d && angle <= 7*Math.PI/8.0d){
			move(Direction.NORTH_WEST);
		}else if(angle > 7*Math.PI/8.0d && angle <= -7*Math.PI/8.0d){
			move(Direction.WEST);
		}else if(angle > -3*Math.PI/8.0d && angle <= -Math.PI/8.0d){
			move(Direction.SOUTH_EAST);
		}else if(angle > -5*Math.PI/8.0d && angle <= -3*Math.PI/8.0d){
			move(Direction.SOUTH);
		}else if(angle > -7*Math.PI/8.0d && angle <= -5*Math.PI/8.0d){
			move(Direction.SOUTH_WEST);
		}
		
		
		/*if(finalVect.x == 0){
			if(finalVect.y > 0){

				move(Direction.NORTH);
			} else if (finalVect.y < 0) {
				move(Direction.SOUTH);
			}
		} else if (finalVect.x > 0) {
			if (finalVect.y == 0) {
				move(Direction.EAST);
			} else if (finalVect.y > 0) {
				move(Direction.NORTH_EAST);
			} else {
				move(Direction.SOUTH_EAST);
			}
		} else {
			if (finalVect.y == 0) {
				move(Direction.WEST);
			} else if (finalVect.y > 0) {
				move(Direction.NORTH_WEST);
			} else {
				move(Direction.SOUTH_WEST);
			}
		}*/

	}

	/*
	 * public void pheromonesVector(Perceivable first, Perceivable last){ int
	 * diffX = last.getX() - first.getX(); int diffY = last.getY() -
	 * first.getY();
	 * 
	 * if(diffX == 0){ if(diffY > 0){ move(Direction.NORTH); }else if(diffY <
	 * 0){ move(Direction.SOUTH); } }else if(diffX > 0){ if(diffY == 0){
	 * move(Direction.EAST); }else if(diffY > 0){ move(Direction.NORTH_EAST);
	 * }else{ move(Direction.SOUTH_EAST); } }else{ if(diffY == 0){
	 * move(Direction.WEST); }else if(diffY > 0){ move(Direction.NORTH_WEST);
	 * }else{ move(Direction.SOUTH_WEST); } } }
	 */

	/**
	 * Creates a pheromone.
	 *
	 * @param pt
	 *            the type of pheromone to be created
	 */
	public void createPheromone(PheromoneType pt) {

		pheromoneTicks += 1;

		if (pheromoneTicks == 1) {
			((AntBody) this.body).createPheromone(pt);
			pheromoneTicks = 0;
		}
	}

}
