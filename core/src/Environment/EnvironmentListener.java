package Environment;

import java.util.ArrayList;
import java.util.EventListener;

public interface EnvironmentListener extends EventListener {
	public void environmentChanged(int blackBaseX, int blackBaseY, int redBaseX, int redBaseY, ArrayList<Position> foods);
}
