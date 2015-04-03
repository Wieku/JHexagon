package net.wieku.jhexagon.api;

import java.util.Random;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public interface MapScript {

	public void onInit();
	public void initEvents();
	public void nextLevel(int levelNum);
	public void nextPattern();
	public void update(float delta);

	default <T> void shuffle(T[] ar) {
		Random rnd = new Random();
		rnd.setSeed(System.nanoTime());
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			T a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}

	}

}
