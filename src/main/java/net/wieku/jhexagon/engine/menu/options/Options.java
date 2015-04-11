package net.wieku.jhexagon.engine.menu.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.engine.menu.Menu;
import net.wieku.jhexagon.utils.GUIHelper;

/**
 * @author Sebastian Krajewski on 07.04.15.
 */
public class Options implements Screen {

	Stage stage;
	Label name;
	Label message;


	static Table currentSection;

	static Options instance;

	public Options(){
		stage = new Stage(new ScreenViewport());
		name = new Label("SETTINGS", GUIHelper.getLabelStyle(Color.WHITE, 30));

		instance = this;

		stage.addListener(new InputListener() {

			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ESCAPE && currentSection.equals(OptionMenu.instance)) {
					Main.getInstance().setScreen(Menu.getInstance());
				}
				return super.keyDown(event, keycode);
			}

		});

		message = new Label("", GUIHelper.getLabelStyle(new Color(0.9f, 0.9f, 0.9f, 1), 14));
		message.layout();
		stage.addActor(message);

		stage.addActor(name);

		setMenu(currentSection != null ? currentSection : OptionMenu.instance);

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	float delta1;
	@Override
	public void render(float delta) {

		if(CurrentMap.currentText != null){

			if(!CurrentMap.currentText.visible){
				message.setText(CurrentMap.currentText.text);
				message.pack();
				CurrentMap.currentText.visible = true;
			}

			if((delta1 += delta) >= CurrentMap.currentText.duration){
				CurrentMap.currentText = null;
				message.setText("");
				delta1 = 0;
			}
		}

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		currentSection.setBounds(5, 0, Math.min(Gdx.graphics.getWidth(), 512), (float) Gdx.graphics.getHeight() * 2.25f / 3);
		currentSection.layout();
		message.setPosition(Gdx.graphics.getWidth() - 5 - message.getWidth(), 5);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		name.setBounds(5, (float) height * 2.25f / 3, width - 5, (float) height * 0.75f / 3);

	}

	public void setMenu(Table section){
		if(currentSection != null){
			currentSection.setVisible(false);
		}
		currentSection = section;
		if(!stage.getActors().contains(section, true)) stage.addActor(section);
		currentSection.setVisible(true);
		stage.setKeyboardFocus(currentSection);
	}

	public static Options getInstance(){
		return instance;
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}
}
