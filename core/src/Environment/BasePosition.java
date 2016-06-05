package Environment;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;

public class BasePosition extends Position {

	private int radius;
	private Color baseColor;
	private Faction race;

	public BasePosition(Circle c) {
		this((int) c.x, (int) c.y);
		
		Random rand = new Random();
		
		radius = (int) c.radius;
		baseColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 128 / 255.0f);
		race = Faction.values()[rand.nextInt(Faction.values().length)];
	}

	public BasePosition(int _x, int _y) {
		super(_x, _y);
	}
	
	public int getRadius(){
		return radius;
	}
	
	public Color getColor(){
		return baseColor;
	}
	
	public Faction getRace(){
		return race;
	}

}
