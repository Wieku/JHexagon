package net.wieku.jhexagon.engine.menu.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.wieku.jhexagon.utils.GUIHelper;

/**
 * @author Sebastian Krajewski on 07.04.15.
 */
public abstract class Action extends Table implements Element<Boolean> {

	Label nameLabel;
	String select = "";
	String name;

	public Action(String name){
		this.name = name;
		nameLabel = new Label(name, GUIHelper.getLabelStyle(Color.WHITE, 12));
		add(nameLabel).left().padLeft(2).expandX();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setWidth(Math.min(Gdx.graphics.getWidth(), 512));
		layout();
		super.draw(batch, parentAlpha);
	}

	public void writeValue(Boolean value){}
	public Boolean loadValue(){return false;}

	@Override
	public void onEvent(InputEvent e) {
		if(e.getType() == Type.keyDown && e.getKeyCode() == Keys.ENTER)
			action();
	}

	public void select(boolean state){
		select =  (state?">":"");
		nameLabel.setText(select+name);
	}
}
