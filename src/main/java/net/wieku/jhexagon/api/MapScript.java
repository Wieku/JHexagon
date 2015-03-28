package net.wieku.jhexagon.api;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public interface MapScript {

	public void onInit();
	public void initEvents();
	public void nextLevel(int levelNum);
	public void nextPatterns();
	public void update(float delta);

}
