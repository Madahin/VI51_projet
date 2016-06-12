package Tools;

import com.badlogic.gdx.graphics.Color;

import Config.WorldConfig;

/**
 * The Class ColorUtils contains some function to compute color
 */
public class ColorUtils {

	/** The color of the minimaly sized foodpile. */
	final static Color minFoodColor = new Color(169.0f / 255.0f, 255.0f / 255.0f, 138.0f / 255.0f, 1);

	/** TThe color of the maximaly sized foodpile. */
	final static Color maxFoodColor = new Color(105.0f / 255.0f, 178.0f / 255.0f, 79.0f / 255.0f, 1);

	/** The color of the base pheromone when it's just created. */
	final static Color minBasePheromoneColor = Color.WHITE;

	/** The color of the base pheromone when before dying. */
	final static Color maxBasePheromoneColor = Color.GRAY;

	/** The color of the food pheromone when it's just created. */
	final static Color minFoodPheromoneColor = new Color(85.0f / 255.0f, 164.0f / 255.0f, 242.0f / 255.0f, 1);

	/** The color of the foo pheromone when before dying. */
	final static Color maxFoodPheromoneColor = new Color(15.0f / 255.0f, 69.0f / 255.0f, 122.0f / 255.0f, 1);

	/**
	 * Blend an array of color into one.
	 *
	 * @param colors
	 *            the array of colors to be blended
	 * @return the resulting color
	 */
	public static Color BlendColor(Color... colors) {
		Color res = new Color(0, 0, 0, 1);

		for (Color c : colors) {
			res.r += c.r / colors.length;
			res.g += c.g / colors.length;
			res.b += c.b / colors.length;
		}

		return res;
	}

	/**
	 * Compute the color of a foodPile given the amount of food stocked.
	 *
	 * @param value
	 *            the amount of food stocked
	 * @return the resulting color
	 */
	public static Color FoodColor(int value) {
		float t = (float) value / WorldConfig.MAX_SIZE_FOOD_STACK;
		Color c = new Color(minFoodColor);
		return c.lerp(maxFoodColor, t);
	}

	/**
	 * Compute the color of a base pheromone given the life expectancy of the
	 * pheromone.
	 *
	 * @param value
	 *            the life expectancy of the pheromone
	 * @return the resulting color
	 */
	public static Color BasePheromoneColor(int value) {
		float t = Math.max(0, Math.min(WorldConfig.MAX_PHEROMONE_LIFE, (float) value)) / WorldConfig.MAX_PHEROMONE_LIFE;
		Color c = new Color(minBasePheromoneColor);
		return c.lerp(maxBasePheromoneColor, t);
	}

	/**
	 * Compute the color of a food pheromone given the life expectancy of the
	 * pheromone.
	 *
	 * @param value
	 *            the life expectancy of the pheromone
	 * @return the resulting color
	 */
	public static Color FoodPheromoneColor(int value) {
		float t = Math.max(0, Math.min(WorldConfig.MAX_PHEROMONE_LIFE, (float) value)) / WorldConfig.MAX_PHEROMONE_LIFE;
		Color c = new Color(minFoodPheromoneColor);
		return c.lerp(maxFoodPheromoneColor, t);

	}

}
