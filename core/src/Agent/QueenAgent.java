package Agent;

import Environment.AgentBody;
import Environment.QueenBody;

public class QueenAgent extends AntAgent {

	public QueenAgent(AgentBody b) {
		super(b);
	}

	/** {@inheritDoc} */
	@Override
	public void live() {
		((QueenBody) body).spawnAnts();

	}
}
