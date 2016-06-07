package Environment;

import java.util.Random;

import Config.WorldConfig;

/**
 * The Class FoodPile represente a foodPile
 */
public class FoodPile extends StaticObject{
	
	/** The amount of food in the foodPile. */
	private int FoodAmount;
	
	/**
	 * Instantiates a new food pile.
	 *
	 * @param x the position of the foodPile in x
	 * @param y the position of the foodPile in y
	 */
	public FoodPile(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Instantiates a new food pile.
	 *
	 * @param x the position of the foodPile in x
	 * @param y the position of the foodPile in y
	 * @param min the minimum size of the foodPile
	 * @param max the maximum size of the foodPile
	 */
	public FoodPile(int x, int y, int min, int max){
		this(x, y);
		Random rand = new Random();
		FoodAmount = rand.nextInt(max - min) + min;
		
	}
	
	/**
	 * Instantiates a new food pile.
	 *
	 * @param x the position of the foodPile in x
	 * @param y the position of the foodPile in y
	 * @param percent the percentage of MAX_SIZE_FOOD_STACK in the foodPile
	 */
	public FoodPile(int x, int y, float percent){
		this(x, y);
		FoodAmount = (int)(WorldConfig.MAX_SIZE_FOOD_STACK * percent);
	}
	
	/**
	 * Take food from the foodPile.
	 *
	 * @return the amount of food taken
	 */
	public int TakeFood(){
		int foodTaken = 0;
		if(FoodAmount > WorldConfig.ANT_FOOD_CARYING){
			FoodAmount -= WorldConfig.ANT_FOOD_CARYING;
			foodTaken = WorldConfig.ANT_FOOD_CARYING;
		}else{
			foodTaken = FoodAmount;
			FoodAmount = 0;
		}
		
		return foodTaken;
	}
	
	/**
	 * Checks if the foodPile is empty.
	 *
	 * @return true, if successful
	 */
	public boolean IsEmpty(){
		return FoodAmount <= 0;
	}
	
	/**
	 * Gets the size of the foodPile.
	 *
	 * @return the size of the foodPile
	 */
	public int GetSize(){
		return FoodAmount;
	}
	
}
