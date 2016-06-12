package Agent;

import java.util.ArrayList;
import java.util.List;

import Environment.AgentBody;
import Environment.AntBody;
import Environment.Perceivable;
import Environment.PheromoneBody;
import Environment.PheromoneType;
import Environment.SoldierBody;

public class SoldierAgent extends AntAgent {

	private boolean enemyAround;

	public SoldierAgent(AgentBody b) {
		super(b);
		enemyAround = true;
	}

	@Override
	public void live() {
		List<Perceivable> perceptions = getPerception();

		// We look for ants in with another Faction ids
		List<Perceivable> enemies = new ArrayList<>();
		List<Perceivable> dangers = new ArrayList<>();
		for (Perceivable p : perceptions) {
			if ((p.getClass().equals(AntBody.class) || p.getClass().equals(SoldierBody.class))
					&& p.getFactionID() != ((SoldierBody) body).getFactionID()) {
				enemies.add(p);
			} else if (p.getClass().equals(PheromoneBody.class) && p.getPheromoneType() == PheromoneType.Danger) {
				dangers.add(p);
			}
		}

		if (!enemies.isEmpty()) {
			enemyAround = true;
			// we attack the first enemy in sight.
			attack(enemies.get(0));
		} else {
			enemyAround = false;
			wander(((SoldierBody) body).getDirection());
		}

		if (enemyAround) {
			createPheromone(PheromoneType.Danger);
		} else {
			createPheromone(PheromoneType.Base);
		}

	}

	public void attack(Perceivable p) {
		((SoldierBody) body).attack(p);
	}

}
