package Environment;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;

/**
 * The Class BasePosition is a representation of a base for the purpose of being
 * used by a GUI.
 */
public class BasePosition extends Position {

	/** The radius of the base. */
	private int radius;

	/** The base color. */
	private Color baseColor;

	/** An opaque version of the base color, for optimisation purpose */
	private Color opaqueBaseColor;

	/** The race of which the base belong to. */
	private Faction race;

	/**
	 * Instantiates a new base position.
	 *
	 * @param c
	 *            the circle graphically representing the base
	 */
	public BasePosition(Circle c) {
		this((int) c.x, (int) c.y);

		Random rand = new Random();

		radius = (int) c.radius;
		// Choose a random semi-transparent color
		baseColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.5f);

		opaqueBaseColor = new Color(baseColor);
		opaqueBaseColor.a = 1f;

		// Choose a random faction
		race = Faction.values()[rand.nextInt(Faction.values().length)];
	}

	/**
	 * Instantiates a new base position.
	 *
	 * @param _x
	 *            the position of the base in x
	 * @param _y
	 *            the position of the base in y
	 */
	public BasePosition(int _x, int _y) {
		super(_x, _y);
	}

	/**
	 * Gets the radius of the base.
	 *
	 * @return the radius of the base
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Gets the color of the base.
	 *
	 * @return the color of the base
	 */
	public Color getColor() {
		return baseColor;
	}

	/**
	 * Get the color of the base (opaque)
	 * 
	 * @return the color of the base (opaque)
	 */
	public Color getOpaqueColor() {
		return opaqueBaseColor;
	}

	/**
	 * Gets the race in which the base belong.
	 *
	 * @return the race in which the base belong.
	 */
	public Faction getRace() {
		return race;
	}

}
