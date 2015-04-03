package net.wieku.jhexagon.engine.timeline;

/**
 * @author Sebastian Krajewski on 03.04.15.
 */
public abstract class TimelineRunnable extends TimelineObject{

	public abstract void run();

	@Override
	public void update(float delta) {
		run();
		setToRemove(true);
	}
}
