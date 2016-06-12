package UserInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.badlogic.gdx.math.Vector3;
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
import Agent.QueenAgent;
import Config.WorldConfig;
import Environment.AntBody;
import Environment.BasePosition;
import Environment.Environment;
import Environment.EnvironmentListener;
import Environment.FoodStackPosition;
import Environment.PheromoneBody;
import Environment.PheromoneType;
import Tools.ColorUtils;

/**
 * The Class Simulator.
 */
public class Simulator extends ApplicationAdapter implements EnvironmentListener {

	/** The shape renderer. */
	private ShapeRenderer shapeRenderer;

	/** The font renderer. */
	private BitmapFont fontRenderer;

	/** The sprite batch. */
	private SpriteBatch spriteBatch;

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

	/** The zoom in buttp,. */
	private TextButton b_ZOOM_IN;

	/** The zoom out button. */
	private TextButton b_ZOOM_OUT;

	/** The up button. */
	private TextButton b_UP;

	/** The down button. */
	private TextButton b_DOWN;

	/** The left button. */
	private TextButton b_LEFT;

	/** The right button. */
	private TextButton b_RIGHT;

	/** true if the simulator should be paused. */
	private boolean SimulatorPaused = false;

	/** true if the environment is initialised. */
	private boolean EnvironmentInitialised = false;

	/** true if we are in debug mode. */
	private boolean debug = true;

	// TODO : Change this to a good 'ol struct
	private Vector3[] vectDebugInfo;

	/** The base positions. */
	private BasePosition bases[];

	/** The environment. */
	private Environment environment;

	/** The agents. */
	private List<Agent> agents;

	/** The lock for synchronisation. */
	private Object lockAgentList = new Object();

	/** The simulation thread. */
	private SimulationThread simu;

	/** The food pile positions. */
	private List<FoodStackPosition> foodPiles;

	/** The new agents. */
	private List<Agent> newAgents;

	/** The elapsed time since the last tick. */
	private long elapsedTime;

	/** The fps. */
	private int fps;

	/** The number of frame computed this second. */
	private int frameThisSec;

	/** {@inheritDoc} */
	@Override
	public void create() {
		/* Gdx Initializations */
		camera = new OrthographicCamera(500, 500);
		shapeRenderer = new ShapeRenderer();
		fontRenderer = new BitmapFont();
		spriteBatch = new SpriteBatch();

		// Environment Init
		initializeEnvironment();

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
		b_ZOOM_IN = new TextButton(" + ", skin);
		b_ZOOM_IN.setWidth(30);
		b_ZOOM_OUT = new TextButton("  -  ", skin);
		b_ZOOM_OUT.setWidth(30);
		b_UP = new TextButton("   up  ", skin);
		b_DOWN = new TextButton("down", skin);
		b_LEFT = new TextButton(" left ", skin);
		b_RIGHT = new TextButton("right", skin);
		stage.addActor(b_START);
		stage.addActor(b_PAUSE);
		stage.addActor(b_RESET);
		stage.addActor(b_ZOOM_IN);
		stage.addActor(b_ZOOM_OUT);
		stage.addActor(b_UP);
		stage.addActor(b_DOWN);
		stage.addActor(b_LEFT);
		stage.addActor(b_RIGHT);

		b_START.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startSimulator();
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
				resetSimulator();
			}
		});

		b_ZOOM_IN.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				camera.zoom *= 0.99f;
			}
		});

		b_ZOOM_OUT.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				camera.zoom *= 1.01f;
			}
		});

		b_UP.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				camera.translate(new Vector2(0.0f, 5.0f));
			}
		});

		b_DOWN.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				camera.translate(new Vector2(0.0f, -5.0f));
			}
		});

		b_LEFT.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				camera.translate(new Vector2(-0.5f, 0.0f));
			}
		});

		b_RIGHT.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				camera.translate(new Vector2(0.5f, 0.0f));
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

		// info debug init
		vectDebugInfo = new Vector3[WorldConfig.BASE_NUMBER];
		for (int i = 0; i < WorldConfig.BASE_NUMBER; i++) {
			vectDebugInfo[i] = new Vector3(0.0f, 0.0f, 0.0f);
		}
	}

	/**
	 * Initialize environment.
	 */
	public void initializeEnvironment() {

		/* Agents relative Initialization */
		agents = new ArrayList<Agent>();
		foodPiles = new ArrayList<FoodStackPosition>();
		environment = new Environment(WorldConfig.WORLD_WIDTH, WorldConfig.WORLD_HEIGHT);
		newAgents = new ArrayList<Agent>();

		bases = environment.getBasePosition();

		// Each race have some ants at the beginning
		for (int n = 0; n < bases.length; ++n) {
			agents.add(new QueenAgent(
					environment.createQueenBody(bases[n].getRace(), n, bases[n].getX(), bases[n].getY())));
			for (int i = 0; i < WorldConfig.ANT_NUMBER; i++) {
				agents.add(new AntAgent(
						environment.createAntBody(bases[n].getRace(), n, bases[n].getX(), bases[n].getY())));
			}
		}

		environment.addListener(this);

		EnvironmentInitialised = true;
	}

	/**
	 * Clear environment.
	 */
	public void clearEnvironment() {
		synchronized (lockAgentList) {
			Iterator<Agent> iter = agents.iterator();
			while (iter.hasNext()) {
				Agent a = iter.next();
				a.setBody(null);
			}
			agents.clear();

			iter = newAgents.iterator();
			while (iter.hasNext()) {
				Agent a = iter.next();
				a.setBody(null);
			}
			newAgents.clear();
		}

		foodPiles.clear();
		environment.clear();

		for (int i = 0; i < bases.length; i++) {
			bases[i] = null;
		}

		System.gc();
		EnvironmentInitialised = false;
	}

	/** {@inheritDoc} */
	public void resize(int width, int height) {
		b_START.setPosition(width - 170, 10);
		b_PAUSE.setPosition(width - 120, 10);
		b_RESET.setPosition(width - 60, 10);
		b_ZOOM_IN.setPosition(10, 40);
		b_ZOOM_OUT.setPosition(10, 10);
		b_UP.setPosition(95, 40);
		b_DOWN.setPosition(95, 10);
		b_LEFT.setPosition(50, 10);
		b_RIGHT.setPosition(150, 10);
		stage.getViewport().update(width, height, true);
	}

	/**
	 * Start the simulation.
	 */
	public void startSimulator() {
		if (EnvironmentInitialised && SimulatorPaused) {
			SimulatorPaused = false;
			environment.notifyListeners();
		} else if (!EnvironmentInitialised) {
			initializeEnvironment();
			simu = new SimulationThread();
			simu.start();
		}
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
	public void resetSimulator() {
		if (EnvironmentInitialised) {
			simu.isRunning = false;
			try {
				simu.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clearEnvironment();
			SimulatorPaused = false;

		}
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
		fontRenderer.dispose();
		spriteBatch.dispose();
	}

	/** {@inheritDoc} */
	@Override
	public void render() {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!EnvironmentInitialised) {
			stage.draw();
			return;
		}

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
			shapeRenderer.setColor(ColorUtils.FoodColor(p.getSize()));
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
				if (agent instanceof PheromoneAgent && agent.getBody() != null) {
					if (((PheromoneBody) agent.getBody()).pheromoneType == PheromoneType.Base) {
						shapeRenderer.setColor(ColorUtils.BasePheromoneColor(((PheromoneBody) agent.getBody()).life));
					} else {
						shapeRenderer.setColor(ColorUtils.FoodPheromoneColor(((PheromoneBody) agent.getBody()).life));
					}
					shapeRenderer.rect(agent.getBody().getX() - WorldConfig.WORLD_WIDTH / 2,
							agent.getBody().getY() - WorldConfig.WORLD_HEIGHT / 2, 1, 1);

				} else if (agent instanceof AntAgent && agent.getBody() != null) {
					shapeRenderer.setColor(bases[((AntBody) agent.getBody()).getFactionID()].getOpaqueColor());
					shapeRenderer.rect(agent.getBody().getX() - WorldConfig.WORLD_WIDTH / 2,
							agent.getBody().getY() - WorldConfig.WORLD_HEIGHT / 2, 1, 1);
				}

			}
		}

		// Render the bases
		for (int i = 0; i < bases.length; ++i) {
			shapeRenderer.setColor(bases[i].getColor());
			shapeRenderer.circle(bases[i].getX() - WorldConfig.WORLD_WIDTH / 2,
					bases[i].getY() - WorldConfig.WORLD_HEIGHT / 2, WorldConfig.BASE_RADIUS);
		}

		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);

		// Render some text
		spriteBatch.begin();
		// FPS
		fontRenderer.setColor(Color.YELLOW);
		fontRenderer.draw(spriteBatch, "" + fps, WorldConfig.WINDOW_WIDTH - 20, WorldConfig.WINDOW_HEIGHT);
		// ant info
		for (int k = 0; k < bases.length; ++k) {
			Color baseColor = bases[k].getColor();
			fontRenderer.setColor(baseColor.r, baseColor.g, baseColor.b, 1);
			if (!debug)
				fontRenderer.draw(spriteBatch,
						"Food : " + environment.getFoodInBase(k) + "; Ants : " + environment.getNbAgent(k), 0,
						WorldConfig.WINDOW_HEIGHT - k * 15);
			else {

				fontRenderer.draw(spriteBatch,
						"Food : " + environment.getFoodInBase(k) + "; Ants : " + environment.getNbAgent(k)
								+ "; Wander : " + (int) vectDebugInfo[k].x + "; ToFood : " + (int) vectDebugInfo[k].y
								+ "; ToBase : " + (int) vectDebugInfo[k].z,
						0, WorldConfig.WINDOW_HEIGHT - k * 15);
			}
		}
		spriteBatch.end();

		stage.draw();

	}

	/**
	 * The Class representing a simulation thread.
	 */
	public class SimulationThread extends Thread {

		/** The is running. */
		public boolean isRunning = true;

		/** {@inheritDoc} */
		public void run() {
			while (isRunning) {

				if (SimulatorPaused || !EnvironmentInitialised) {
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
						if (a.getBody() == null) {
							iter.remove();
						}
					}
				}

				// We count the debug info
				for (int i = 0; i < WorldConfig.BASE_NUMBER; i++) {
					environment.nbWanderBeh[i] = 0;
					environment.nbFindFoodBeh[i] = 0;
					environment.nbGoHomeBeh[i] = 0;
				}

				for (Agent agent : agents) {
					if (agent instanceof AntAgent && agent.getBody() != null) {

						environment.nbWanderBeh[((AntBody) agent.getBody())
								.getFactionID()] += ((AntBody) agent.getBody()).behaviourDebug.x;
						environment.nbFindFoodBeh[((AntBody) agent.getBody())
								.getFactionID()] += ((AntBody) agent.getBody()).behaviourDebug.y;
						environment.nbGoHomeBeh[((AntBody) agent.getBody())
								.getFactionID()] += ((AntBody) agent.getBody()).behaviourDebug.z;
					}
				}
				for (int i = 0; i < WorldConfig.BASE_NUMBER; i++)
					vectDebugInfo[i].set(environment.nbWanderBeh[i], environment.nbFindFoodBeh[i],
							environment.nbGoHomeBeh[i]);

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

	/**
	 * Environment changed.
	 *
	 * @param basePos
	 *            the base positions
	 * @param foods
	 *            the food positions
	 * @param newAgentList
	 *            the new agent list
	 */
	@Override
	public void environmentChanged(BasePosition basePos[], List<FoodStackPosition> foods, List<Agent> newAgentList) {
		// When the environment change we render the frame

		if (SimulatorPaused)
			return;

		foodPiles = foods;
		newAgents = new ArrayList<Agent>(newAgentList);

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
