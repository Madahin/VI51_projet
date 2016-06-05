package Environment;

import java.util.Random;

import Config.WorldConfig;

public class FoodPile extends StaticObject{
	private int FoodAmount;
	
	public FoodPile(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public FoodPile(int x, int y, int min, int max){
		this(x, y);
		Random rand = new Random();
		FoodAmount = rand.nextInt(max - min) + min;
		
	}
	
	public FoodPile(int x, int y, float percent){
		this(x, y);
		FoodAmount = (int)(WorldConfig.MAX_SIZE_FOOD_STACK * percent);
	}
	
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
	
	public boolean IsEmpty(){
		return FoodAmount <= 0;
	}
	
	public int GetSize(){
		return FoodAmount;
	}
	
}
