package Environment;

import java.util.Random;

import Config.WorldConfig;

public class FoodPile extends StaticObject{
	private int FoodAmount;
	
	public FoodPile(int x, int y, int min, int max){
		Random rand = new Random();
		FoodAmount = rand.nextInt(max - min) + min;
		this.x = x;
		this.y = y;
	}
	
	public void TakeFood(){
		if(FoodAmount > WorldConfig.ANT_FOOD_CARYING){
			FoodAmount -= WorldConfig.ANT_FOOD_CARYING;
		}else{
			FoodAmount = 0;
		}
	}
	
	public boolean IsEmpty(){
		return FoodAmount <= 0;
	}
	
}
