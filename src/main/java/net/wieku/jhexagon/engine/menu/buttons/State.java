package net.wieku.jhexagon.engine.menu.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.wieku.jhexagon.engine.menu.Menu;
import net.wieku.jhexagon.utils.GUIHelper;

/**
 * @author Sebastian Krajewski on 07.04.15.
 */
public abstract class State extends Table implements Element<Boolean> {

	Label nameLabel;
	Label valueLabel;
	String name;
	boolean value;
	String select = "";

	public State(String name, boolean def){
		super();

		nameLabel = new Label(name, GUIHelper.getLabelStyle(Color.WHITE, 12));

		this.name = name;
		value = loadValue();
		//if(value < min || value > max) value = def;

		valueLabel = new Label((value?"ON":"OFF"), GUIHelper.getLabelStyle(Color.WHITE, 12));

		add(nameLabel).left().padLeft(2).expandX();
		add(valueLabel).right().padRight(2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setWidth(Math.min(Gdx.graphics.getWidth(), 512));
		layout();
		super.draw(batch, parentAlpha);
	}

	public void action(){}

	public abstract void writeValue(Boolean value);
	public abstract Boolean loadValue();

	public void onEvent(InputEvent e){
		if(e.getType() == Type.keyDown){
			if(e.getKeyCode() == Keys.ENTER){
				value = !value;
				valueLabel.setText(value?"ON":"OFF");
				Menu.playBeep();
				writeValue(value);
			}
		}
	}

	public void select(boolean state){
		select =  (state?">":"");
		nameLabel.setText(select+name);
	}
}
