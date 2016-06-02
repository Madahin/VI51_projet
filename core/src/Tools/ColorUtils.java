package Tools;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {

	public static Color BlendColor(Color... colors) {
		Color res = new Color(0, 0, 0, 1);

		for (Color c : colors) {
			res.r += c.r / colors.length;
			res.g += c.g / colors.length;
			res.b += c.b / colors.length;
		}

		return res;
	}

}
