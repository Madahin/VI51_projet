package Tools;

import com.badlogic.gdx.graphics.Color;

import Config.WorldConfig;

public class ColorUtils {
	
	final static Color baseFoodColor = new Color(169.0f / 255.0f, 1, 138.0f / 255.0f, 1);
	final static Color maxFoodColor = new Color(105.0f / 178.0f, 1, 79.0f / 255.0f, 1);

	public static Color BlendColor(Color... colors) {
		Color res = new Color(0, 0, 0, 1);

		for (Color c : colors) {
			res.r += c.r / colors.length;
			res.g += c.g / colors.length;
			res.b += c.b / colors.length;
		}

		return res;
	}
	
	public static Color FoodColor(int value){
		float t = (float)value / WorldConfig.MAX_SIZE_FOOD_STACK;
		Color c = new Color(maxFoodColor);
		return c.lerp(baseFoodColor, t);
	}

}
