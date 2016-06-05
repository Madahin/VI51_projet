package Environment;

import java.util.ArrayList;
import java.util.EventListener;

import Agent.Agent;

public interface EnvironmentListener extends EventListener {
	public void environmentChanged(BasePosition basePos[], int blackBaseX, int blackBaseY, int redBaseX, int redBaseY, ArrayList<FoodStackPosition> foods, ArrayList<Agent> newAgentList);
}
