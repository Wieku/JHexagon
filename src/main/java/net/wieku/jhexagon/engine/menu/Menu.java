package net.wieku.jhexagon.engine.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.engine.Background;
import net.wieku.jhexagon.engine.Game;
import net.wieku.jhexagon.engine.Settings;
import net.wieku.jhexagon.engine.camera.SkewCamera;
import net.wieku.jhexagon.engine.menu.options.Options;
import net.wieku.jhexagon.map.Map;
import net.wieku.jhexagon.utils.GUIHelper;

import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 04.04.15.
 */
public class Menu implements Screen {

	public static Options options;
	static Sound beep;

	ArrayList<Map> maps;
	Stage stage;

	Table logo, info, credits;
	Label number, name, description, author, music, creditLabel;

	String[] creditArray = {"Programmed by:", "Sebastian Krajewski", "Lukasz Magiera", "Original ideas by:", "Vittorio Romeo", "Terry Cavanagh", "Music by:", "BOSSFIGHT", "Chipzel"};
	int index = -1;
	float time = 1.5f;
	float toChange = 0f;

	Table conf;

	SkewCamera camera = new SkewCamera();
	ShapeRenderer shapeRenderer;
	Background background = new Background();

	private static int mapIndex = 0;

	static Menu instance;

	public Menu(ArrayList<Map> maps){
		this.maps = maps;
		instance = this;
		beep = Gdx.audio.newSound(Gdx.files.internal("assets/sound/beep.ogg"));
		shapeRenderer = new ShapeRenderer();

		options = new Options();
		stage = new Stage(new ScreenViewport());
		stage.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {

				if(!maps.isEmpty()){

					if(keycode == Keys.LEFT || keycode == Keys.RIGHT){

						if(keycode == Keys.LEFT && --mapIndex < 0) mapIndex = maps.size() - 1;
						if(keycode == Keys.RIGHT && ++mapIndex > maps.size() - 1) mapIndex = 0;

						selectIndex(mapIndex);

						CurrentMap.reset();
						maps.get(mapIndex).script.initColors();

						playBeep();
					}

					if(keycode == Keys.F3){
						playBeep();
						Main.getInstance().setScreen(options);
					}

					if(keycode == Keys.ENTER){
						playBeep();
						Gdx.input.setInputProcessor(null);
						Main.getInstance().setScreen(new Game(maps.get(mapIndex)));
					}

				}

				if(keycode == Keys.ESCAPE){
					playBeep();
					Gdx.app.exit();
				}

				return false;
			}
		});


		info = new Table();
		info.add(number = new Label("", GUIHelper.getLabelStyle(Color.WHITE, 12))).left().row();
		info.add(name = new Label("", GUIHelper.getLabelStyle(Color.WHITE, 18))).left().row();
		info.add(description = new Label("", GUIHelper.getLabelStyle(Color.WHITE, 16))).left().row();
		info.add(author = new Label("", GUIHelper.getLabelStyle(Color.WHITE, 14))).left().row();
		info.add(music = new Label("", GUIHelper.getLabelStyle(Color.WHITE, 14))).left().row();

		info.setPosition(5, 5);
		stage.addActor(info);

		conf = GUIHelper.getTable(Color.BLACK);
		conf.add(new Label("Press F3 to open settings", GUIHelper.getLabelStyle(Color.BLACK, Color.WHITE, 8))).pad(5);
		conf.pack();
		stage.addActor(conf);

		logo = GUIHelper.getTable(Color.BLACK);
		logo.add(new Label("Hexagons!", GUIHelper.getLabelStyle(Color.WHITE, 40))).pad(5).padBottom(0).row();
		logo.add(new Label(Main.version, GUIHelper.getLabelStyle(Color.WHITE, 12))).pad(5).padTop(0).right();
		logo.pack();
		stage.addActor(logo);

		credits = GUIHelper.getTable(Color.BLACK);
		credits.add(creditLabel = new Label("", GUIHelper.getLabelStyle(Color.WHITE, 14))).pad(5).padBottom(10);
		credits.pack();

		stage.addActor(credits);

		if(!maps.isEmpty()) selectIndex(mapIndex);

	}

	@Override
	public void render(float delta) {

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		CurrentMap.skew = 0;
		camera.orbit(90f * delta);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.identity();
		shapeRenderer.rotate(1, 0, 0, 90);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		background.update(delta);
		background.render(shapeRenderer, delta, true);
		shapeRenderer.end();

		if((toChange -= delta) <= 0){

			++index;

			if(index == creditArray.length) index = 0;

			creditLabel.setText(creditArray[index]);
			credits.pack();

			credits.setWidth(Math.max(credits.getWidth(), logo.getWidth()));

			credits.setPosition(Gdx.graphics.getWidth() - 5 - credits.getWidth(), Gdx.graphics.getHeight() - 10 - logo.getHeight() - credits.getHeight());
			toChange = time;
		}

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		conf.setPosition(2, height - 2 - conf.getHeight());
		logo.setPosition(width - 5 - logo.getWidth(), height - 5 - logo.getHeight());
		credits.setPosition(width - 5 - credits.getWidth(), height - 10 - logo.getHeight() - credits.getHeight());
	}

	@Override
	public void show() {
		Main.config.foregroundFPS = 120;

		CurrentMap.reset();
		maps.get(mapIndex).script.initColors();

		Gdx.input.setInputProcessor(stage);
	}


	public void selectIndex(int index){
		Map map = maps.get(index);

		number.setText("[" + (index + 1) + "/" + maps.size() + "] Pack: " + map.info.pack);
		name.setText(map.info.name);
		description.setText(map.info.description);
		author.setText("Author: " + map.info.author);
		music.setText("Music: " + map.info.songName + " by " + map.info.songAuthor);
		info.pack();
		info.setPosition(5, 5);
	}

	public static void playBeep(){
		long id = beep.play();
		beep.setVolume(id, (float) Settings.instance.masterVolume * (float) Settings.instance.effectVolume / 10000f);
	}

	public static Menu getInstance() {
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
