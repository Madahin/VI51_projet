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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Agent.Agent;
import Agent.AntAgent;
import Agent.PheromoneAgent;
import Config.WorldConfig;
import Environment.AntBody;
import Environment.BasePosition;
import Environment.Environment;
import Environment.EnvironmentListener;
import Environment.Faction;
import Environment.FoodStackPosition;
import Environment.PheromoneBody;
import Environment.PheromoneType;
import Tools.ColorUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Simulator.
 */
public class Simulator extends ApplicationAdapter implements EnvironmentListener {

	/** The shape renderer. */
	private ShapeRenderer shapeRenderer;
	
	/** The font drawer. */
	private BitmapFont m_font;
	
	/** The batch renderer. */
	private SpriteBatch m_batch;
	
	/** The camera. */
	private OrthographicCamera camera;

	/** The stage. */
	private Stage stage;
	
	/** The skin. */
	private Skin skin;
	
	/** The start button. */
	private TextButton b_START;
	
	/** The pause button. */
	private TextButton b_PAUSE;
	
	/** The reset button. */
	private TextButton b_RESET;
	
	/** True if the simultion must be paused. */
	private boolean SimulatorPaused = false;
	
	/** The Agents are initialised. */
	private boolean AgentsInitialised = false;

	/** The base radius. */
	private int baseRadius = WorldConfig.BASE_RADIUS;
	
	/** The bases. */
	private BasePosition bases[];
	
	/** The environment. */
	private Environment environment;
	
	/** The agents. */
	private ArrayList<Agent> agents;
	
	/** The lock used to synchronized the life of the agents and the renderer. */
	private Object lockAgentList = new Object();
	
	/** The simulation thread. */
	private SimulationThread simu;

	/** The food piles. */
	private ArrayList<FoodStackPosition> foodPiles;
	
	/** The new agents. */
	private ArrayList<Agent> newAgents;

	/** The elapsed time. */
	private long elapsedTime;
	
	/** The fps. */
	private int fps;
	
	/** The number of frame this second. */
	private int frameThisSec;

	/** {@inheritDoc} */
	@Override
	public void create() {
		/* Gdx Initializations */
		camera = new OrthographicCamera(500, 500);
		shapeRenderer = new ShapeRenderer();
		m_font = new BitmapFont();
		m_batch = new SpriteBatch();

		// Agents Init
		initializeAgents();

		/* Thread Init */
		simu = new SimulationThread();
		simu.start();

		// In order to save performance for the application we
		// want to call the render as we wish
		Gdx.graphics.setContinuousRendering(false);
		// Gdx.graphics.requestRendering();

		// Buttons Init
		Gdx.input.setInputProcessor(stage);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));
		b_START = new TextButton("Start", skin);
		b_PAUSE = new TextButton("Pause", skin);
		b_RESET = new TextButton("Reset", skin);
		stage.addActor(b_START);
		stage.addActor(b_PAUSE);
		stage.addActor(b_RESET);

		b_START.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				start();
			}
		});

		b_PAUSE.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pauseSimulator();
			}
		});

		b_RESET.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				reset();
			}
		});

		// TextField text=new TextField("",skin);
		// stage.addActor(text);

		// CheckBox box=new CheckBox("done",skin);
		// stage.addActor(box);

		// FPS initialisation
		elapsedTime = TimeUtils.millis();
		fps = 0;
		frameThisSec = 0;

	}

	/**
	 * Initialize agents.
	 */
	public void initializeAgents() {

		/* Agents relative Initialization */
		agents = new ArrayList<Agent>();
		foodPiles = new ArrayList<FoodStackPosition>();
		environment = new Environment(WorldConfig.WORLD_WIDTH, WorldConfig.WORLD_HEIGHT, baseRadius);
		newAgents = new ArrayList<Agent>();

		bases = environment.getBasePosition();

		// Each race have some ants at the beginning
		for (int n = 0; n < bases.length; ++n) {
			for (int i = 0; i < WorldConfig.ANT_NUMBER; i++) {
				agents.add(new AntAgent(
						environment.createAntBody(bases[n].getRace(), n, bases[n].getX(), bases[n].getY())));
			}
		}

		environment.addListener(this);

		AgentsInitialised = true;
	}

	/**
	 * Delete agents.
	 */
	public void deleteAgents() {

		Iterator<Agent> iter = agents.iterator();
		while (iter.hasNext()) {
			Agent a = iter.next();
			a.body = null;
			iter.remove();
		}
		foodPiles.clear();
		// environment.
	}

	/** {@inheritDoc} */
	public void resize(int width, int height) {
		b_START.setPosition(width - 170, 10);
		b_PAUSE.setPosition(width - 120, 10);
		b_RESET.setPosition(width - 60, 10);
		stage.getViewport().update(width, height, true);
	}

	/**
	 * Start the simulation.
	 */
	public void start() {
		SimulatorPaused = false;
		environment.notifyListeners();
	}

	/**
	 * Pause the simulation.
	 */
	public void pauseSimulator() {
		SimulatorPaused = true;
	}

	/**
	 * Reset the simulation.
	 */
	public void reset() {
		SimulatorPaused = true;
	}

	/** {@inheritDoc} */
	@Override
	public void dispose() {
		simu.isRunning = false;
		stage.dispose();

		try {
			simu.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		shapeRenderer.dispose();
		m_font.dispose();
		m_batch.dispose();
	}

	/** {@inheritDoc} */
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
		shapeRenderer.rect(-WorldConfig.WORLD_WIDTH / 2, -WorldConfig.WORLD_HEIGHT / 2, WorldConfig.WORLD_WIDTH,
				WorldConfig.WORLD_HEIGHT);

		// Display the food
		for (FoodStackPosition p : foodPiles) {
			shapeRenderer.setColor(ColorUtils.FoodColor(p.GetSize()));
			shapeRenderer.rect(p.getX() - WorldConfig.WORLD_WIDTH / 2, p.getY() - WorldConfig.WORLD_HEIGHT / 2, 1, 1);
		}

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
				if (agent instanceof PheromoneAgent && agent.body != null) {
					if (((PheromoneBody) agent.body).pheromoneType == PheromoneType.Base) {
						shapeRenderer.setColor(Color.WHITE);
					} else {
						shapeRenderer.setColor(Color.BLUE);
					}
					shapeRenderer.rect(agent.body.getX() - WorldConfig.WORLD_WIDTH / 2,
							agent.body.getY() - WorldConfig.WORLD_HEIGHT / 2, 1, 1);

				} else if (agent instanceof AntAgent) {

					if (((AntBody) agent.body).getFaction() == Faction.BlackAnt) {
						shapeRenderer.setColor(Color.BLACK);
					} else {
						shapeRenderer.setColor(Color.RED);
					}
					shapeRenderer.rect(agent.body.getX() - WorldConfig.WORLD_WIDTH / 2,
							agent.body.getY() - WorldConfig.WORLD_HEIGHT / 2, 1, 1);
				}

			}
		}

		// Render the bases
		for (int i = 0; i < bases.length; ++i) {
			shapeRenderer.setColor(bases[i].getColor());
			shapeRenderer.circle(bases[i].getX() - WorldConfig.WORLD_WIDTH / 2,
					bases[i].getY() - WorldConfig.WORLD_HEIGHT / 2, baseRadius);
		}

		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);

		// Render some text
		m_batch.begin();
		// FPS
		m_font.setColor(Color.YELLOW);
		m_font.draw(m_batch, "" + fps, WorldConfig.WINDOW_WIDTH - 20, WorldConfig.WINDOW_HEIGHT);
		// ant info
		for (int k = 0; k < bases.length; ++k) {
			Color baseColor = bases[k].getColor();
			m_font.setColor(baseColor.r, baseColor.g, baseColor.b, 1);
			m_font.draw(m_batch, "Food : " + environment.GetFoodInBase(k) + "; Ants : " + environment.getNbAgent(k), 0,
					WorldConfig.WINDOW_HEIGHT - k * 15);
		}
		m_batch.end();

		stage.draw();

	}

	/**
	 * The SimulationThread.
	 */
	public class SimulationThread extends Thread {
		
		/** True if the simulation is running. */
		public boolean isRunning = true;

		/** {@inheritDoc} */
		public void run() {
			while (isRunning) {

				if (SimulatorPaused) {
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

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

		/**
		 * Translate camera.
		 */
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
			if (Gdx.input.isKeyPressed(Keys.O)) {
				camera.zoom *= 1.01f;
			}
			if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
				Gdx.app.exit();
			}

			dirVect.nor();
			dirVect.scl(5.0f);
			camera.translate(dirVect);
		}

	}

	/** {@inheritDoc} */
	@Override
	public void environmentChanged(BasePosition basePos[], ArrayList<FoodStackPosition> foods,
			ArrayList<Agent> newAgentList) {
		// When the environment change we render the frame

		if (SimulatorPaused)
			return;

		foodPiles = foods;
		newAgents = new ArrayList<Agent>(newAgentList);

		// we count the number of update we can make in one second
		frameThisSec += 1;
		long elapsed = TimeUtils.timeSinceMillis(elapsedTime);
		if (elapsed >= 1000) {
			fps = frameThisSec;
			frameThisSec = 0;
			elapsedTime = TimeUtils.millis();
		}

		Gdx.graphics.requestRendering();
	}
}
