package Environment;

public class BaseBody extends StaticObject {

	private final int faction;

	public BaseBody(int factionID, int _x, int _y) {
		faction = factionID;
		x = _x;
		y = _y;
	}
	
	public int getFactionID(){
		return faction;
	}
}
