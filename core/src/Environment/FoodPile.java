package Environment;

import java.util.Random;

import Config.WorldConfig;

/**
 * The Class FoodPile represente a foodPile
 */
public class FoodPile extends StaticObject {

	/** The amount of food in the foodPile. */
	private int foodAmount;

	/**
	 * Instantiates a new food pile.
	 *
	 * @param x
	 *            the position of the foodPile in x
	 * @param y
	 *            the position of the foodPile in y
	 */
	public FoodPile(int _x, int _y) {
		x = _x;
		y = _y;
	}

	/**
	 * Instantiates a new food pile.
	 *
	 * @param x
	 *            the position of the foodPile in x
	 * @param y
	 *            the position of the foodPile in y
	 * @param min
	 *            the minimum size of the foodPile
	 * @param max
	 *            the maximum size of the foodPile
	 */
	public FoodPile(int _x, int _y, int min, int max) {
		this(_x, _y);
		Random rand = new Random();
		foodAmount = rand.nextInt(max - min) + min;

	}

	/**
	 * Instantiates a new food pile.
	 *
	 * @param x
	 *            the position of the foodPile in x
	 * @param y
	 *            the position of the foodPile in y
	 * @param percent
	 *            the percentage of MAX_SIZE_FOOD_STACK in the foodPile
	 */
	public FoodPile(int _x, int _y, float percent) {
		this(_x, _y);
		foodAmount = (int) (WorldConfig.MAX_SIZE_FOOD_STACK * percent);
	}

	/**
	 * Take food from the foodPile.
	 *
	 * @return the amount of food taken
	 */
	public int takeFood() {
		int foodTaken = 0;
		if (foodAmount > WorldConfig.ANT_FOOD_CARYING) {
			foodAmount -= WorldConfig.ANT_FOOD_CARYING;
			foodTaken = WorldConfig.ANT_FOOD_CARYING;
		} else {
			foodTaken = foodAmount;
			foodAmount = 0;
		}

		return foodTaken;
	}

	/**
	 * Drops food in the pile.
	 */
	public void dropFood(int Quantity) {
		foodAmount += Quantity;
	}

	/**
	 * Checks if the foodPile is empty.
	 *
	 * @return true, if successful
	 */
	public boolean isEmpty() {
		return foodAmount <= 0;
	}

	/**
	 * Gets the size of the foodPile.
	 *
	 * @return the size of the foodPile
	 */
	public int getSize() {
		return foodAmount;
	}

}
