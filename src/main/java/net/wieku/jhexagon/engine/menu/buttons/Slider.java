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
public abstract class Slider extends Table implements Element<Integer> {

	Label nameLabel;
	Label valueLabel;
	String name;
	String select = "";
	int value, min, max, jump;

	public Slider(String name, int min, int max, int jump, int def){
		super();
		nameLabel = new Label(name, GUIHelper.getLabelStyle(Color.WHITE, 12));

		this.name = name;
		this.jump = jump;
		this.min = min;
		this.max = max;
		value = loadValue();
		if(value < min || value > max) value = def;

		valueLabel = new Label(Integer.toString(value), GUIHelper.getLabelStyle(Color.WHITE, 12));

		add(nameLabel).left().padLeft(2).expandX();
		add(valueLabel).padRight(2);
	}

	public void action(){}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setWidth(Math.min(Gdx.graphics.getWidth(), 512));
		layout();
		super.draw(batch, parentAlpha);
	}

	public abstract void writeValue(Integer value);
	public abstract Integer loadValue();

	public void onEvent(InputEvent e){
		if(e.getType() == Type.keyDown){
			if(e.getKeyCode() == Keys.LEFT){
				value = Math.max(min, value - jump);
				valueLabel.setText(Integer.toString(value));
				writeValue(value);
				Menu.playBeep();
			}

			if(e.getKeyCode() == Keys.RIGHT){
				value = Math.min(max, value + jump);
				valueLabel.setText(Integer.toString(value));
				writeValue(value);
				Menu.playBeep();
			}
		}
	}

	public void select(boolean state){
		select =  (state?">":"");
		nameLabel.setText(select+name);
	}
}
