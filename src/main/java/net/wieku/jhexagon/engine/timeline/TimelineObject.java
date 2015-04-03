package net.wieku.jhexagon.engine.timeline;

/**
 * @author Sebastian Krajewski on 03.04.15.
 */
public abstract class TimelineObject {

	public boolean toRemove = false;

	public void setToRemove(boolean remove){
		toRemove = remove;
	}

	public abstract void update(float delta);


}
