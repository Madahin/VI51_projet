package Environment;

import java.util.Comparator;

public class PheromoneComparator implements Comparator<Perceivable> {

	@Override
	public int compare(Perceivable arg0, Perceivable arg1) {
		return arg0.getPheromoneLife() - arg1.getPheromoneLife();
	}

}
