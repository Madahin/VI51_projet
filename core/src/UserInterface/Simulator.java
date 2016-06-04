package UserInterface;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import Agent.Agent;
import Agent.AntAgent;
import Agent.PheromoneAgent;
import Environment.AntBody;
import Environment.Environment;
import Environment.EnvironmentListener;
import Environment.Faction;
import Environment.PheromoneType;
import Environment.PheromoneBody;
import Environment.Position;
import sun.management.resources.agent;

public class Simulator extends ApplicationAdapter implements EnvironmentListener {

	private ShapeRenderer shapeRenderer;
	private BitmapFont m_font;
	private SpriteBatch m_batch;
	private OrthographicCamera camera;

	private int worldWidth = 500;
	private int worldHeight = 500;
	private int baseRadius = 30;
	private int percentageFood = 5;
	private Environment environment;
	private ArrayList<Agent> agents;
	private Object lockAgentList = new Object();
	private SimulationThread simu;

	private int bbX, bbY, rbX, rbY;
	private ArrayList<Position> foodPiles;
	private ArrayList<Agent> newAgents;

	@Override
	public void create() {
		/* Gdx Initializations */
		camera = new OrthographicCamera(500, 500);
		shapeRenderer = new ShapeRenderer();
		m_font = new BitmapFont();
		m_batch = new SpriteBatch();

		/* Agents relative Initialization */
		agents = new ArrayList<Agent>();
		foodPiles = new ArrayList<Position>();
		environment = new Environment(worldWidth, worldHeight, baseRadius, percentageFood);
		newAgents = new ArrayList<Agent>();

		// Each race have 3000 ants at the beginning
		for (int i = 0; i < 2500; i++) {
			agents.add(new AntAgent(environment.createBlackAntBody()));
			agents.add(new AntAgent(environment.createRedAntBody()));
		}

		environment.addListener(this);

		/* Thread Init */
		simu = new SimulationThread();
		simu.start();

		// In order to save performance for the application we
		// want to call the render as we wish
		Gdx.graphics.setContinuousRendering(false);
		// Gdx.graphics.requestRendering();
	}

	@Override
	public void dispose() {
		simu.isRunning = false;

		try {
			simu.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Filled);
		// Render the Borders
		shapeRenderer.setColor(212.0f / 255.0f, 161.0f / 255.0f, 144.0f / 255.0f, 1);
		shapeRenderer.rect(-worldWidth / 2, -worldHeight / 2, worldWidth, worldHeight);

		// Display the food
		shapeRenderer.setColor(169.0f / 255.0f, 1, 138.0f / 255.0f, 1);
		for (Position p : foodPiles)
			shapeRenderer.rect(p.getX() - worldWidth / 2, p.getY() - worldHeight / 2, 1, 1);

		// We check all agents and display only ants
		synchronized (lockAgentList) {

			int n = 1;
			for (int i = 0; i < agents.size() - n; ++i) {
				if (agents.get(i) instanceof AntAgent) {
					Agent a = agents.get(i);
					agents.set(i, agents.get(agents.size() - n));
					agents.set(agents.size() - n, a);
					n++;
				}
			}

			for (Agent agent : agents) {
				/*if (agent instanceof PheromoneAgent && agent.body != null){
					if (((PheromoneBody)agent.body ).pheromoneType==PheromoneType.Base){
					shapeRenderer.setColor(Color.WHITE);
					}else{
						shapeRenderer.setColor(Color.BLUE);	
					}
					shapeRenderer.rect(agent.body.getX() - worldWidth / 2, agent.body.getY() - worldHeight / 2, 1, 1);
				} else */if (agent instanceof AntAgent) {
					if (((AntBody) agent.body).faction == Faction.BlackAnt) {
						shapeRenderer.setColor(Color.BLACK);
					} else {
						shapeRenderer.setColor(Color.RED);
					}
					shapeRenderer.rect(agent.body.getX() - worldWidth / 2, agent.body.getY() - worldHeight / 2, 1, 1);
				}

			}
		}

		// Render the blackBase
		shapeRenderer.setColor(104.0f / 255.0f, 114.0f / 255.0f, 117.0f / 255.0f, 128 / 255.0f);
		shapeRenderer.circle(bbX - worldWidth / 2, bbY - worldHeight / 2, baseRadius);

		// Render the Red Base
		shapeRenderer.setColor(1, 105.0f / 255.0f, 105.0f / 255.0f, 128 / 255.0f);
		shapeRenderer.circle(rbX - worldWidth / 2, rbY - worldHeight / 2, baseRadius);

		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);

		m_batch.begin();
		m_font.setColor(Color.YELLOW);
		m_font.draw(m_batch, "" + Gdx.graphics.getFramesPerSecond(), 0, 480);
		m_batch.end();

	}

	public class SimulationThread extends Thread {
		public boolean isRunning = true;

		public void run() {
			while (isRunning) {

				translateCamera();

				for (Agent agent : agents) {
					agent.live();
				}

				synchronized (lockAgentList) {
					for (Agent a : newAgents) {
						agents.add(a);
					}
					newAgents.clear();

					// Destroy dying agents
					Iterator<Agent> iter = agents.iterator();
					while (iter.hasNext()) {
						Agent a = iter.next();
						if (a.body == null) {
							iter.remove();
						}
					}
				}

			}
		}

		public void translateCamera() {
			Vector2 dirVect = new Vector2(0, 0);
			if (Gdx.input.isKeyPressed(Keys.Z) || Gdx.input.isKeyPressed(Keys.UP)) {
				dirVect.add(new Vector2(0.0f, 1.0f));
			}
			if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
				dirVect.add(new Vector2(0.0f, -1.0f));
			}
			if (Gdx.input.isKeyPressed(Keys.Q) || Gdx.input.isKeyPressed(Keys.LEFT)) {
				dirVect.add(new Vector2(-1.0f, 0.0f));
			}
			if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
				dirVect.add(new Vector2(1.0f, 0.0f));
			}
			if (Gdx.input.isKeyPressed(Keys.P)) {
				camera.zoom *= 0.99f;
			}
			if (Gdx.input.isKeyPressed(Keys.O)) { // TODO : Why the fuck "minus"
													// doesn't work ?!
				camera.zoom *= 1.01f;
			}
			dirVect.nor();
			dirVect.scl(5.0f);
			camera.translate(dirVect);
		}

	}

	@Override
	public void environmentChanged(int blackBaseX, int blackBaseY, int redBaseX, int redBaseY,
			ArrayList<Position> foods, ArrayList<Agent> newAgentList) {
		// When the environment change we render the frame
		bbX = blackBaseX;
		bbY = blackBaseY;
		rbX = redBaseX;
		rbY = redBaseY;
		foodPiles = foods;
		newAgents = new ArrayList<Agent>(newAgentList);

		Gdx.graphics.requestRendering();
	}
}
