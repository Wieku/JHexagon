package net.wieku.jhexagon.engine.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * @author Sebastian Krajewski on 07.04.15.
 */
public interface Element<T> {


	public void onEvent(InputEvent e);
	public void writeValue(T value);
	public T loadValue();
	public void action();
	public void select(boolean state);
}
