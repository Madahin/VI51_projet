package Tools;

import java.util.Random;

import Config.WorldConfig;

public class SeededRandom {
	private static Random rand;
	private static boolean isInitialized = false;
	
	public static Random getGenerator(){
		if(!isInitialized){
			rand = new Random();
			if(WorldConfig.RANDOM_SEED >= 0){
				rand.setSeed(WorldConfig.RANDOM_SEED);
			}
			isInitialized = true;
		}
		return rand;
	}
}
