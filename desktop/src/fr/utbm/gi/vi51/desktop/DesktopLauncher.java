package fr.utbm.gi.vi51.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Config.WorldConfig;
import UserInterface.Simulator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		if(arg.length >= 1){
			WorldConfig.load(arg[0]);
		}
		
		config.width = WorldConfig.WINDOW_WIDTH;
		config.height = WorldConfig.WINDOW_HEIGHT;
		config.fullscreen = WorldConfig.WINDOW_FULLSCREEN;
		
		new LwjglApplication(new Simulator(), config);
	}
}
