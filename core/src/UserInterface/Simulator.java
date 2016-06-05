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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Agent.Agent;
import Agent.AntAgent;
import Agent.PheromoneAgent;
import Config.WorldConfig;
import Environment.AntBody;
import Environment.Environment;
import Environment.EnvironmentListener;
import Environment.Faction;
import Environment.FoodStackPosition;
import Environment.PheromoneBody;
import Environment.PheromoneType;
import Tools.ColorUtils;

public class Simulator extends ApplicationAdapter implements EnvironmentListener {

	private ShapeRenderer shapeRenderer;
	private BitmapFont m_font;
	private SpriteBatch m_batch;
	private OrthographicCamera camera;
	
	private Stage stage;
	private Skin skin;
	private TextButton b_START;
	private TextButton b_PAUSE;
	private TextButton b_RESET;
	private boolean SimulatorPaused = false;
	private boolean AgentsInitialised = false;
	

	private int baseRadius = 30;
	private int percentageFood = 5;
	private Environment environment;
	private ArrayList<Agent> agents;
	private Object lockAgentList = new Object();
	private SimulationThread simu;

	private int bbX, bbY, rbX, rbY;
	private ArrayList<FoodStackPosition> foodPiles;
	private ArrayList<Agent> newAgents;
	
	private long elapsedTime;
	private int  fps;
	private int frameThisSec;

	@Override
	public void create() {
		/* Gdx Initializations */
		camera = new OrthographicCamera(500, 500);
		shapeRenderer = new ShapeRenderer();
		m_font = new BitmapFont();
		m_batch = new SpriteBatch();

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
	      
	     skin = new Skin( Gdx.files.internal( "ui/defaultskin.json" ));
	     b_START = new TextButton("Start", skin);
	     b_PAUSE = new TextButton("Pause", skin);
	     b_RESET = new TextButton("Reset", skin);
		 stage.addActor(b_START);
		 stage.addActor(b_PAUSE);
		 stage.addActor(b_RESET);
		 
		 b_START.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		            start();
		        }
		    });
		 
		 b_PAUSE.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		            pause();
		        }
		    });
		 
		 b_RESET.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		            reset();
		        }
		    });
		 
//		 TextField text=new TextField("",skin);
//		 stage.addActor(text);

//		 CheckBox box=new CheckBox("done",skin);
//		 stage.addActor(box);
		 
		 // FPS initialisation
		 elapsedTime = TimeUtils.millis();
		 fps = 0;
		 frameThisSec = 0;
			
	}
	
	public void initializeAgents(){
		
		/* Agents relative Initialization */
		agents = new ArrayList<Agent>();
		foodPiles = new ArrayList<FoodStackPosition>();
		environment = new Environment(WorldConfig.WORLD_WIDTH, WorldConfig.WORLD_HEIGHT, baseRadius, percentageFood);
		newAgents = new ArrayList<Agent>();

		// Each race have 3000 ants at the beginning
		for (int i = 0; i < WorldConfig.ANT_NUMBER; i++) {
			agents.add(new AntAgent(environment.createBlackAntBody()));
			agents.add(new AntAgent(environment.createRedAntBody()));
		}

		environment.addListener(this);
		
		AgentsInitialised = true;
	}
	
	public void deleteAgents(){
		
	}
	
	public void resize (int width, int height) {
		b_START.setPosition(width-170, 10);
		b_PAUSE.setPosition(width-120, 10);
		b_RESET.setPosition(width-60, 10);
	    stage.getViewport().update(width, height, true);
	}

	public void start(){
		SimulatorPaused = false;
		environment.notifyListeners();
	}
	
	public void pause(){
		 SimulatorPaused = true;
	}
	
	public void reset(){
		 SimulatorPaused = true;
	}
	
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

					if (((AntBody) agent.body).faction == Faction.BlackAnt) {
						shapeRenderer.setColor(Color.BLACK);
					} else {
						shapeRenderer.setColor(Color.RED);
					}
					shapeRenderer.rect(agent.body.getX() - WorldConfig.WORLD_WIDTH / 2,
							agent.body.getY() - WorldConfig.WORLD_HEIGHT / 2, 1, 1);
				}

			}
		}

		// Render the blackBase
		shapeRenderer.setColor(104.0f / 255.0f, 114.0f / 255.0f, 117.0f / 255.0f, 128 / 255.0f);
		shapeRenderer.circle(bbX - WorldConfig.WORLD_WIDTH / 2, bbY - WorldConfig.WORLD_HEIGHT / 2, baseRadius);

		// Render the Red Base
		shapeRenderer.setColor(1, 105.0f / 255.0f, 105.0f / 255.0f, 128 / 255.0f);
		shapeRenderer.circle(rbX - WorldConfig.WORLD_WIDTH / 2, rbY - WorldConfig.WORLD_HEIGHT / 2, baseRadius);

		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);

		// Render some text
		m_batch.begin();
		// FPS
		m_font.setColor(Color.YELLOW);
		m_font.draw(m_batch, "" + fps, WorldConfig.WINDOW_WIDTH - 20, WorldConfig.WINDOW_HEIGHT);
		// Red ants info
		m_font.setColor(Color.RED);
		m_font.draw(m_batch, "Food in red base : " + environment.GetFoodInRedBase(), 0, WorldConfig.WINDOW_HEIGHT - 15);
		// Black ants info
		m_font.setColor(Color.BLACK);
		m_font.draw(m_batch, "Food in black base : " + environment.GetFoodInBlackBase(), 0, WorldConfig.WINDOW_HEIGHT);
		m_batch.end();
		
		stage.draw();

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
			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				Gdx.app.exit();
			}
			
			dirVect.nor();
			dirVect.scl(5.0f);
			camera.translate(dirVect);
		}

	}

	@Override
	public void environmentChanged(int blackBaseX, int blackBaseY, int redBaseX, int redBaseY,
			ArrayList<FoodStackPosition> foods, ArrayList<Agent> newAgentList) {
		// When the environment change we render the frame
		
		if (SimulatorPaused)
			return;
		
		bbX = blackBaseX;
		bbY = blackBaseY;
		rbX = redBaseX;
		rbY = redBaseY;
		foodPiles = foods;
		newAgents = new ArrayList<Agent>(newAgentList);
		
		frameThisSec += 1;
		long elapsed = TimeUtils.timeSinceMillis(elapsedTime);
		if(elapsed >= 1000){
			fps = frameThisSec;
			frameThisSec = 0;
			elapsedTime = TimeUtils.millis();
		}
		
		Gdx.graphics.requestRendering();
	}
}
