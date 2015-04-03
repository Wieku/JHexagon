package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.Patterns;
import net.wieku.jhexagon.api.Wall;
import net.wieku.jhexagon.engine.camera.SkewCamera;
import net.wieku.jhexagon.maps.Map;
import net.wieku.jhexagon.maps.MapLoader;
import net.wieku.jhexagon.sound.AudioPlayer;
import net.wieku.jhexagon.sound.BeatListener;
import net.wieku.jhexagon.utils.GUIHelper;
import net.wieku.jhexagon.utils.ShapeRenderer3D;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Iterator;

/**
 * @author Sebastian Krajewski on 28.03.15.
 */
public class Game implements Screen{

	Map map;
	AudioPlayer audioPlayer;


	ShapeRenderer3D renderer;
	SkewCamera camera = new SkewCamera();
	Stage stage;

	Background background = new Background();
	Center center = new Center();
	Player player = new Player();
	WallRenderer wallRenderer = new WallRenderer();

	Label fps;
	Label time;
	Label message;

	int width, height;

	public static float scale = 1f;

	private int inc = 1;

	DecimalFormat timeFormat = new DecimalFormat("#.000");

	public Game (Map map){
		this.map = map;

		renderer = new ShapeRenderer3D();

		stage = new Stage(new ScreenViewport());
		stage.getViewport().update(width, height, true);

		fps = new Label("", GUIHelper.getLabelStyle(new Color(0, 0, 0, 0.5f), Color.WHITE, 14));
		fps.layout();
		stage.addActor(fps);

		time = new Label("", GUIHelper.getLabelStyle(new Color(0, 0, 0, 0.5f), Color.WHITE, 14));
		time.layout();
		stage.addActor(time);

		message = new Label("", GUIHelper.getLabelStyle(new Color(0.9f, 0.9f, 0.9f, 1), 35));
		message.layout();
		stage.addActor(message);

		try {
			audioPlayer = new AudioPlayer(new File(MapLoader.TEMP_PATH + map.info.audioTempName));
			audioPlayer.setVolume(0.2f);
			audioPlayer.play();
			audioPlayer.setBeatListener(new BeatListener() {
				@Override
				public void onBeatLow() {
					scale = (!player.dead ? Math.max(scale, 1.2f) : 1f);
				}

				@Override
				public void onBeatHigh() {
				}
			});
			map.script.onInit();
			map.script.initEvents();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void show() {


	}

	float delta;
	float delta0;
	float delta1;
	float delta2;
	float fastRotate = 0f;

	@Override
	public void render(float delta8) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		if(!player.dead){
			CurrentMap.wallTimeline.update(delta8);
			CurrentMap.eventTimeline.update(delta8);
		}


		if(!player.dead)
			CurrentMap.currentTime += Gdx.graphics.getDeltaTime();

		if(!player.dead && (delta1+=Gdx.graphics.getDeltaTime())>=CurrentMap.levelIncrement){

			fastRotate = CurrentMap.fastRotate;

			CurrentMap.isFastRotation = true;
			CurrentMap.rotationSpeed += (CurrentMap.rotationSpeed > 0 ? CurrentMap.rotationIncrement: -CurrentMap.rotationIncrement );
			CurrentMap.rotationSpeed *= -1;
			CurrentMap.rotationSpeed = Math.min(CurrentMap.rotationSpeedMax, Math.max(-CurrentMap.rotationSpeedMax, CurrentMap.rotationSpeed));

			CurrentMap.mustChangeSides = true;

			delta1 = 0;
		}


		this.delta += Gdx.graphics.getDeltaTime();

		while (this.delta >= (1f / 60)) {

			if(CurrentMap.currentText != null){

				if(!CurrentMap.currentText.visible){
					message.setText(CurrentMap.currentText.text.toUpperCase());
					message.pack();
					CurrentMap.currentText.visible = true;
				}

				if((delta2 += 1f / 60) >= CurrentMap.currentText.duration){
					CurrentMap.currentText = null;
					message.setText("");
					message.pack();
					delta2 = 0;
				}
			}

			if (CurrentMap.wallTimeline.isEmpty() && CurrentMap.mustChangeSides) {
				CurrentMap.sides = MathUtils.random(CurrentMap.minSides, CurrentMap.maxSides);
				CurrentMap.wallTimeline.wait(1f);
				CurrentMap.mustChangeSides = false;
			}

			if (CurrentMap.wallTimeline.isAllSpawned() && !CurrentMap.mustChangeSides) {
				map.script.nextPattern();
			}

			if (!player.dead) {
				Wall.updatePulse();
			} else {

				if(CurrentMap.rotationSpeed < 0){
					CurrentMap.rotationSpeed = Math.min(-0.02f, CurrentMap.rotationSpeed + 0.002f);
				} else if(CurrentMap.rotationSpeed > 0) {
					CurrentMap.rotationSpeed = Math.max(0.02f, CurrentMap.rotationSpeed - 0.002f);
				}

				if (audioPlayer != null && !audioPlayer.hasEnded()) {
					audioPlayer.stop();
				}
			}


			camera.orbit(CurrentMap.rotationSpeed * 360f / 60 + (CurrentMap.rotationSpeed > 0 ? 1 : -1) * (getSmootherStep(0, CurrentMap.fastRotate, fastRotate) / 3.5f) * 17.f);
			fastRotate = Math.max(0, fastRotate - 1f);
			if(fastRotate == 0) CurrentMap.isFastRotation = false;



			scale = Math.max(scale - 0.01f, 1f);



			inc = (delta0 == 0 ? 1 : (delta0 == CurrentMap.skewTime ? -1 : inc));
			delta0 += 1f/60 * inc;
			delta0 = Math.min(CurrentMap.skewTime, Math.max(delta0, 0));
			float percent = delta0 / CurrentMap.skewTime;
			CurrentMap.skew = CurrentMap.minSkew + (CurrentMap.maxSkew - CurrentMap.minSkew) * percent;



			fps.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
			fps.pack();

			time.setText("Time: " + timeFormat.format(CurrentMap.currentTime));
			time.pack();

			time.setY(fps.getHeight());

			delta  -= 0.016666668f;
		}

		renderer.setProjectionMatrix(camera.combined);

		background.draw(renderer, Gdx.graphics.getDeltaTime());

		wallRenderer.drawWallsShadow(renderer, CurrentMap.wallTimeline.getObjects());
		center.drawShadow(renderer, Gdx.graphics.getDeltaTime());
		player.drawShadow(renderer, Gdx.graphics.getDeltaTime());

		renderer.identity();
		renderer.translate(0, 0, 0);

		wallRenderer.drawWalls(renderer, CurrentMap.wallTimeline.getObjects());
		renderer.scale(scale, scale, scale);
		center.draw(renderer, Gdx.graphics.getDeltaTime());
		player.draw(renderer, Gdx.graphics.getDeltaTime());

		message.setPosition((stage.getWidth() - message.getWidth())/2, (stage.getHeight() - message.getHeight()) * 2.5f / 3);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

		camera.updateViewport(width, height);

		fps.setPosition(0, 0);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		if(renderer != null) renderer.dispose();
	}

	float getSaturated(float mValue) { return Math.max(0.f, Math.min(1.f, mValue)); }
	float getSmootherStep(float edge0, float edge1, float x)
	{
		x = getSaturated((x - edge0)/(edge1 - edge0));
		return x * x * x * (x * (x * 6 - 15) + 10);
	}


}
