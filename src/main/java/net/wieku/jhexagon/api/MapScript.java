package net.wieku.jhexagon.api;

import java.util.Random;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public interface MapScript {

	Random random = new Random();

	public void onInit();
	public void initEvents();
	public void nextLevel(int levelNum);
	public void nextPattern();
	public void update(float delta);

	default <T> void shuffle(T[] array) {

		Random random = new Random();
		random.setSeed(System.nanoTime());

		for(int i = array.length; i >= 2; --i){
			int j = random.nextInt(i);

			T val = array[i];

			array[i] = array[j];
			array[j] = val;

		}

	}

}
