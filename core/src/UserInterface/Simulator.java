package UserInterface;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import Agent.Agent;
import Agent.AntAgent;
import Environment.AntBody;
import Environment.Environment;
import Environment.EnvironmentListener;
import Environment.Faction;

public class Simulator extends ApplicationAdapter implements EnvironmentListener {
	
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	
	int worldWidth = 1000;
	int worldHeight = 1000;
	Environment environment;
	ArrayList<Agent> agents;
	
	SimulationThread simu;
	
	
	@Override
	public void create () {
		/* Gdx Initializations */
		camera = new OrthographicCamera(500, 500);
		shapeRenderer = new ShapeRenderer();
		
		/* Agents relative Initialization */
		agents = new ArrayList<Agent>();
		environment = new Environment(worldWidth, worldHeight, agents);
		
		// Each race have 3000 ants at the beginning
		for(int i = 0 ; i < 3000 ; i++){
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
		Gdx.graphics.requestRendering();
	}

	@Override
	public void dispose () {
		simu.isRunning = false;
		
		try {
			simu.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
			// Render the Borders
			shapeRenderer.setColor(212.0f/255.0f, 161.0f/255.0f, 144.0f/255.0f, 1);
			shapeRenderer.rect(-worldWidth/2, -worldHeight/2, worldWidth, worldHeight);
		
			// We check all agents and display only ants
			for(Agent agent : agents){
				if(agent instanceof AntAgent){
					if( ((AntBody)agent.body).faction == Faction.BlackAnt){
						shapeRenderer.setColor(Color.BLACK);
					}else{
						shapeRenderer.setColor(Color.RED);
					}
					shapeRenderer.rect(agent.body.getX() - worldWidth/2, agent.body.getY() - worldHeight/2, 1, 1);
				}
			}
			
			
		shapeRenderer.end();
		
	}
	
	public class SimulationThread extends Thread{
		public boolean isRunning = true;
		
		public void run(){
			while(isRunning){
			
				translateCamera();
				
				for(Agent agent : agents){
					agent.live();
				}
				
			}
		}
		
		public void translateCamera(){
			Vector2 dirVect = new Vector2(0, 0);
			if(Gdx.input.isKeyPressed(Keys.Z) ||
			   Gdx.input.isKeyPressed(Keys.UP)){
				dirVect.add(new Vector2(0.0f, 1.0f));
			}
			if(Gdx.input.isKeyPressed(Keys.S) ||
			   Gdx.input.isKeyPressed(Keys.DOWN)){
				dirVect.add(new Vector2(0.0f, -1.0f));
			}
			if(Gdx.input.isKeyPressed(Keys.Q) ||
			   Gdx.input.isKeyPressed(Keys.LEFT)){
				dirVect.add(new Vector2(-1.0f, 0.0f));
			}
			if(Gdx.input.isKeyPressed(Keys.D) ||
			   Gdx.input.isKeyPressed(Keys.RIGHT)){
				dirVect.add(new Vector2(1.0f, 0.0f));
			}
			if(Gdx.input.isKeyPressed(Keys.P)){
				camera.zoom *= 0.99f;
			}
			if(Gdx.input.isKeyPressed(Keys.O)){ // TODO : Why the fuck "minus" doesn't work ?!
				camera.zoom *= 1.01f;
			}
			dirVect.nor();
			dirVect.scl(5.0f);
			camera.translate(dirVect);
		}
		
	}

	@Override
	public void environmentChanged() {
		// When the environment change we render the frame
		Gdx.graphics.requestRendering();
	}
}
