package Environment;

public class BaseBody extends StaticObject {

	private final int faction;

	public BaseBody(int factionID) {
		faction = factionID;
	}
	
	public int getFactionID(){
		return faction;
	}
}
