package Environment;

import java.util.Comparator;

/**
 * The Class PheromoneComparator can compare two pheromone with there remaining
 * life
 */
public class PheromoneComparator implements Comparator<Perceivable> {

	/** {@inheritDoc} */
	@Override
	public int compare(Perceivable arg0, Perceivable arg1) {
		if (arg0.getPheromoneLife() < arg1.getPheromoneLife())
			return 1;
		else if (arg0.getPheromoneLife() > arg1.getPheromoneLife())
			return -1;
		else
			return 0;
	}

}
