package Environment;

public class FoodStackPosition extends Position {

	private final int size;
	
	public FoodStackPosition(FoodPile fp) {
		super(fp.getX(), fp.getY());
		size = fp.GetSize();
	}
	
	public int GetSize(){
		return size;
	}
}
