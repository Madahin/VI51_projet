package Environment;

import java.util.Random;

public class FoodPile extends StaticObject{
	public int FoodAmount;
	
	public FoodPile(int x, int y, int min, int max){
		Random rand = new Random();
		FoodAmount = /*rand.nextInt(max - min) +*/ min;
		this.x = x;
		this.y = y;
	}
	
}
