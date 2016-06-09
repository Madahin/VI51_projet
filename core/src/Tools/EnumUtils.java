package Tools;

import com.badlogic.gdx.math.Vector2;

import Environment.Direction;

public class EnumUtils {

	public static Vector2 DirectionToVector(Direction d){
		
		// Optim
		float normValue = 0.70710678118f; // This value represents sqrt(2.0f)/2.0f 
		
		switch (d) {
		case NORTH:
			return new Vector2(0.0f, 1.0f);
			
		case NORTH_EAST:
			return new Vector2(normValue, normValue);
			
		case EAST:
			return new Vector2(1.0f, 0.0f);
			
		case SOUTH_EAST:
			return new Vector2(normValue, -normValue);
			
		case SOUTH:
			return new Vector2(0.0f, -1.0f);
			
		case SOUTH_WEST:
			return new Vector2(-normValue, -normValue);
			
		case WEST:
			return new Vector2(-1.0f, 0.0f);
			
		case NORTH_WEST:
			return new Vector2(-normValue, normValue);
			
			
		default:
			return null;
			
		}
		
	}
	
	public static Direction VectorToDirection(Vector2 v){
		Vector2 vect = new Vector2(v.x, v.y);
		vect.nor();
		double angle = Math.atan2(vect.y, vect.x);
		
		
		if(angle >= -Math.PI/8.0d && angle <= Math.PI/8.0d){
			return Direction.EAST;
		}else if(angle >= Math.PI/8.0d && angle <= 3*Math.PI/8.0d ){
			return Direction.NORTH_EAST;
		}else if(angle >= 3*Math.PI/8.0d && angle <= 5*Math.PI/8.0d){
			return Direction.NORTH;
		}else if(angle >= 5*Math.PI/8.0d && angle <= 7*Math.PI/8.0d){
			return Direction.NORTH_WEST;
		}else if(angle >= -3*Math.PI/8.0d && angle <= -Math.PI/8.0d){
			return Direction.SOUTH_EAST;
		}else if(angle >= -5*Math.PI/8.0d && angle <= -3*Math.PI/8.0d){
			return Direction.SOUTH;
		}else if(angle >= -7*Math.PI/8.0d && angle <= -5*Math.PI/8.0d){
			return Direction.SOUTH_WEST;
		}else if(angle >= 7*Math.PI/8.0d || angle <= -7*Math.PI/8.0d){
			return Direction.WEST;
		}
		
		System.out.println("ZIZI : "+angle);
		return null;
	}
}
