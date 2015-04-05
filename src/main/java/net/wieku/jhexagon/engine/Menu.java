package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.engine.camera.SkewCamera;
import net.wieku.jhexagon.maps.Map;
import net.wieku.jhexagon.utils.GUIHelper;
import net.wieku.jhexagon.utils.ShapeRenderer3D;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 04.04.15.
 */
public class Menu implements Screen {


	ArrayList<Map> maps;
	ArrayList<MenuMap> actors = new ArrayList<>();
	Stage stage;
	Sound beep;

	SkewCamera camera = new SkewCamera();
	ShapeRenderer3D shapeRenderer = new ShapeRenderer3D();
	Background background = new Background();

	private int mapIndex = 0;
	public static ScrollPane pane;

	static Menu instance;

	public Menu(ArrayList<Map> maps){
		this.maps = maps;
		instance = this;
		beep = Gdx.audio.newSound(Gdx.files.internal("assets/sound/beep.ogg"));

		stage = new Stage(new ScreenViewport());

		Table table = new Table();

		for(Map map : maps){
			MenuMap mp = new MenuMap(map);
			actors.add(mp);
			table.add(mp).top().row();
		}

		if(!actors.isEmpty()) actors.get(0).check(true);

		pane = new ScrollPane(table, GUIHelper.getScrollPaneStyle(Color.BLACK, Color.WHITE));
		pane.setupFadeScrollBars(1f, 1f);
		pane.setSmoothScrolling(false);
		((Table) pane.getChildren().get(0)).top().left();
		pane.setCancelTouchFocus(true);
		stage.addActor(pane);
	}

	@Override
	public void render(float delta) {

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		CurrentMap.skew = 0;
		camera.orbit(180f * delta);
		shapeRenderer.setProjectionMatrix(camera.combined);
		background.draw(shapeRenderer, delta);

		pane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void show() {
		Main.conf.foregroundFPS = 120;

		CurrentMap.reset();
		maps.get(mapIndex).script.initColors();

		Gdx.input.setInputProcessor(new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {

				if(!actors.isEmpty()){
					if(keycode == Keys.UP){

						actors.get(mapIndex).check(false);

						if(--mapIndex < 0) mapIndex = actors.size()-1;

						actors.get(mapIndex).check(true);

						float scr = pane.getScrollHeight() - pane.getScrollY() + Gdx.graphics.getHeight();

						CurrentMap.reset();
						maps.get(mapIndex).script.initColors();

						if(scr < actors.get(mapIndex).getY()+actors.get(mapIndex).getHeight() || actors.get(mapIndex).getY()+actors.get(mapIndex).getHeight() < scr-Gdx.graphics.getHeight() )
							pane.setScrollY(pane.getScrollHeight() + Gdx.graphics.getHeight() - actors.get(mapIndex).getY() - actors.get(mapIndex).getHeight()/2);

					}

					if(keycode == Keys.DOWN){

						actors.get(mapIndex).check(false);
						if(++mapIndex > actors.size()-1) mapIndex = 0;

						actors.get(mapIndex).check(true);

						float scr = pane.getScrollHeight() - pane.getScrollY();

						CurrentMap.reset();
						maps.get(mapIndex).script.initColors();

						if(scr > actors.get(mapIndex).getY()-actors.get(mapIndex).getHeight() || actors.get(mapIndex).getY() > scr + Gdx.graphics.getHeight() )
							pane.setScrollY(pane.getScrollHeight() - actors.get(mapIndex).getY() + actors.get(mapIndex).getHeight()/2);
					}

					if(keycode == Keys.ENTER){
						Gdx.input.setInputProcessor(null);
						Main.getInstance().setScreen(new Game(maps.get(mapIndex)));
					}

					try {
						Method method = pane.getClass().getDeclaredMethod("resetFade");
						method.setAccessible(true);
						method.invoke(pane);
					} catch (Exception e) {}

					beep.play();

				}



				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
		});
	}

	public static Screen getInstance() {
		return instance;
	}
}
