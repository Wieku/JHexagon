package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.wieku.jhexagon.api.CurrentMap;
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
	Sound levelUp;
	Sound sides;
	Sound go;
	Sound death;
	Sound gameOver;


	int width, height;

	public static float scale = 1f;

	private int inc = 1;

	DecimalFormat timeFormat = new DecimalFormat("0.000");

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

			levelUp = Gdx.audio.newSound(Gdx.files.internal("assets/sound/levelUp.ogg"));
			sides = Gdx.audio.newSound(Gdx.files.internal("assets/sound/beep.ogg"));
			go = Gdx.audio.newSound(Gdx.files.internal("assets/sound/go.ogg"));
			death = Gdx.audio.newSound(Gdx.files.internal("assets/sound/death.ogg"));
			gameOver = Gdx.audio.newSound(Gdx.files.internal("assets/sound/gameOver.ogg"));


			start(map.info.startTimes[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void show() {


	}

	@Override
	public void render(float delta) {
		updateGame(delta);

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

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

	public void start(float startTime){

		delta0 = delta1 = delta2 = delta3 = 0;

		CurrentMap.currentTime = 0f;
		CurrentMap.reset();

		audioPlayer.setVolume(0.2f);
		audioPlayer.play();
		if(startTime != 0)
			audioPlayer.setPosition(startTime);

		camera.reset();

		map.script.onInit();
		map.script.initEvents();
		go.play();
	}

	public void restart(){
		start(map.info.startTimes[MathUtils.random(0, map.info.startTimes.length - 1)]);
	}

	float fastRotate = 0f;
	float delta0;
	public void updateGame(float delta){

		if(player.dead){

			if (audioPlayer != null && !audioPlayer.hasEnded()) {
				death.play();
				gameOver.play();
				camera.rumble(5f, 4f);
				audioPlayer.stop();
			}

			if(Gdx.input.isKeyPressed(Keys.SPACE)){
				player.reset();
				restart();
			}

		}


		updateTimeline(delta);

		this.delta0 += delta;
		while (this.delta0 >= (1f / 60)) {

			updateText(1f / 60);
			updateRotation(1f / 60);
			updateSkew(1f / 60);

			if (!player.dead) {
				Wall.updatePulse();
			}

			scale = Math.max(scale - 0.01f, 1f);

			delta0 -= 0.016666668f;
		}

		map.script.update(delta);
	}

	float delta1;
	public void updateText(float delta) {
		if(CurrentMap.currentText != null){

			if(!CurrentMap.currentText.visible){
				message.setText(CurrentMap.currentText.text.toUpperCase());
				CurrentMap.currentText.visible = true;
			}

			if((delta1 += delta) >= CurrentMap.currentText.duration){
				CurrentMap.currentText = null;
				message.setText("");
				delta1 = 0;
			}
		}

		fps.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		time.setText("Time: " + timeFormat.format(CurrentMap.currentTime));

		fps.pack();
		time.pack();
		message.pack();

		time.setY(fps.getHeight());
	}

	float delta2;
	public void updateSkew(float delta) {
		inc = (delta2 == 0 ? 1 : (delta2 == CurrentMap.skewTime ? -1 : inc));
		delta2 += delta * inc;
		delta2 = Math.min(CurrentMap.skewTime, Math.max(delta2, 0));
		float percent = delta2 / CurrentMap.skewTime;
		CurrentMap.skew = CurrentMap.minSkew + (CurrentMap.maxSkew - CurrentMap.minSkew) * percent;
	}

	float delta3;
	public void updateTimeline(float delta) {

		if(!player.dead){
			CurrentMap.wallTimeline.update(delta);
			CurrentMap.eventTimeline.update(delta);
			CurrentMap.currentTime += delta;
		}

		if(!player.dead && (delta3 +=delta)>=CurrentMap.levelIncrement){

			fastRotate = CurrentMap.fastRotate;

			levelUp.play();

			CurrentMap.isFastRotation = true;
			CurrentMap.rotationSpeed += (CurrentMap.rotationSpeed > 0 ? CurrentMap.rotationIncrement: -CurrentMap.rotationIncrement );
			CurrentMap.rotationSpeed *= -1;
			CurrentMap.rotationSpeed = Math.min(CurrentMap.rotationSpeedMax, Math.max(-CurrentMap.rotationSpeedMax, CurrentMap.rotationSpeed));

			CurrentMap.mustChangeSides = true;

			delta3 = 0;
		}

		if (CurrentMap.wallTimeline.isEmpty() && CurrentMap.mustChangeSides) {
			CurrentMap.sides = MathUtils.random(CurrentMap.minSides, CurrentMap.maxSides);
			sides.play();
			CurrentMap.mustChangeSides = false;
		}

		if (CurrentMap.wallTimeline.isAllSpawned() && !CurrentMap.mustChangeSides) {
			map.script.nextPattern();
		}

	}

	public void updateRotation(float delta) {

		if(player.dead) {
			if(CurrentMap.rotationSpeed < 0){
				CurrentMap.rotationSpeed = Math.min(-0.02f, CurrentMap.rotationSpeed + 0.002f);
			} else if(CurrentMap.rotationSpeed > 0) {
				CurrentMap.rotationSpeed = Math.max(0.02f, CurrentMap.rotationSpeed - 0.002f);
			}
		}

		camera.orbit(CurrentMap.rotationSpeed * 360f / 60 + (CurrentMap.rotationSpeed > 0 ? 1 : -1) * (getSmootherStep(0, CurrentMap.fastRotate, fastRotate) / 3.5f) * 17.f);
		fastRotate = Math.max(0, fastRotate - 1f);
		if(fastRotate == 0) CurrentMap.isFastRotation = false;
	}

	float getSaturated(float mValue) { return Math.max(0.f, Math.min(1.f, mValue)); }
	float getSmootherStep(float edge0, float edge1, float x)
	{
		x = getSaturated((x - edge0)/(edge1 - edge0));
		return x * x * x * (x * (x * 6 - 15) + 10);
	}


}
