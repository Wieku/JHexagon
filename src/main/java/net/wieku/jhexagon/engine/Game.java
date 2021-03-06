package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.Wall;
import net.wieku.jhexagon.engine.camera.SkewCamera;
import net.wieku.jhexagon.engine.menu.Menu;
import net.wieku.jhexagon.map.Map;
import net.wieku.jhexagon.resources.ArchiveFileHandle;
import net.wieku.jhexagon.resources.AudioPlayer;
import net.wieku.jhexagon.utils.GUIHelper;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * @author Sebastian Krajewski on 28.03.15.
 */
public class Game implements Screen{

	Map map;
	AudioPlayer audioPlayer;

	ShapeRenderer renderer;
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

	LinkedList<Renderer> renderers = new LinkedList<>();

	private float rumbleX;
	private float rumbleY;
	private float rumbleTime = 0;
	private float currentRumbleTime = 1;
	private float rumblePower = 0;
	private float currentRumblePower = 0;

	int width, height;

	public static float scale = 1f;

	private int inc = 1;

	DecimalFormat timeFormat = new DecimalFormat("0.000");

	public void playSound(Sound sound){
		long id = sound.play();
		sound.setVolume(id, (float) Settings.instance.masterVolume * (float) Settings.instance.effectVolume / 10000f);
	}

	public Game (Map map){
		this.map = map;

		renderer = new ShapeRenderer();

		stage = new Stage(new ScreenViewport());
		stage.getViewport().update(width, height, true);

		fps = new Label("", GUIHelper.getLabelStyle(/*new Color(0, 0, 0, 0.5f),*/ Color.WHITE, 14));
		fps.layout();
		fps.setX(2);
		stage.addActor(fps);

		time = new Label("", GUIHelper.getLabelStyle(/*new Color(0, 0, 0, 0.5f),*/ Color.WHITE, 14));
		time.layout();
		time.setX(2);
		stage.addActor(time);

		message = new Label("", GUIHelper.getLabelStyle(new Color(0.9f, 0.9f, 0.9f, 1), 35));
		message.layout();
		stage.addActor(message);

		try {
			audioPlayer = new AudioPlayer(new ArchiveFileHandle(map.file,map.info.audioFileName));



			levelUp = Gdx.audio.newSound(Gdx.files.internal("assets/sound/levelUp.ogg"));
			sides = Gdx.audio.newSound(Gdx.files.internal("assets/sound/beep.ogg"));
			go = Gdx.audio.newSound(Gdx.files.internal("assets/sound/go.ogg"));
			death = Gdx.audio.newSound(Gdx.files.internal("assets/sound/death.ogg"));
			gameOver = Gdx.audio.newSound(Gdx.files.internal("assets/sound/gameOver.ogg"));

			addRenderer(center);
			addRenderer(wallRenderer);
			addRenderer(player);

			start(map.info.startTimes[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addRenderer(Renderer renderer){
		renderers.add(renderer);
		renderers.sort((o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));
	}


	@Override
	public void show() {

		Main.config.foregroundFPS = 0;
	}

	@Override
	public void render(float delta) {
		updateGame(delta);

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		renderer.setProjectionMatrix(camera.combined);

		renderer.identity();
		renderer.rotate(1, 0, 0, 90);
		renderer.translate(rumbleX, 0, rumbleY);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		background.render(renderer, delta, true);
		renderer.end();

		for(int j = 1; j <= CurrentMap.layers; ++j){
			renderer.identity();
			renderer.translate(rumbleX, -j * CurrentMap.depth, rumbleY);
			renderer.rotate(1, 0, 0, 90);
			renderer.begin(ShapeRenderer.ShapeType.Filled);
			for(Renderer render : renderers){
				render.render(renderer, delta, true);
			}
			renderer.end();
		}

		renderer.identity();
		renderer.rotate(1, 0, 0, 90);
		renderer.translate(rumbleX, 0, rumbleY);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		for(Renderer render : renderers){
			render.render(renderer, delta, false);
		}
		renderer.end();

		message.setPosition((stage.getWidth() - message.getWidth())/2, (stage.getHeight() - message.getHeight()) * 2.5f / 3);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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

		delta0 = delta1 = delta5 = delta4 = delta3 = 0;

		rumblePower = 0;
		rumbleTime = 1;
		currentRumbleTime = 0;

		CurrentMap.currentTime = 0f;
		CurrentMap.reset();
		player.reset();
		audioPlayer.setVolume((float) Settings.instance.masterVolume * (float) Settings.instance.musicVolume / 10000f);
		audioPlayer.play();
		if(startTime != 0)
			audioPlayer.setPosition(startTime);

		map.script.onInit();
		map.script.initColors();
		map.script.initEvents();
		playSound(go);
	}

	public void restart(){
		start(map.info.startTimes[MathUtils.random(0, map.info.startTimes.length - 1)]);
	}

	float fastRotate = 0f;
	float delta0;
	boolean escClick = false;
	Color tmpColor = new Color();
	public void updateGame(float delta){

		updateTimeline(delta);

		if(player.dead){

			if (audioPlayer != null && !audioPlayer.hasEnded()) {
				playSound(death);
				playSound(gameOver);
				rumble(15f, 2f);
				audioPlayer.stop();
			}

			if(Gdx.input.isKeyPressed(Keys.SPACE)){
				restart();
			}

			if(Gdx.input.isKeyPressed(Keys.ESCAPE) && !escClick){
				Main.getInstance().setScreen(Menu.getInstance());
			}

		} else {
			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				player.dead = true;
				escClick = true;
			}
		}

		if(!Gdx.input.isKeyPressed(Keys.ESCAPE)){
			escClick = false;
		}

		updateRotation(delta);

		this.delta0 += delta;
		while (this.delta0 >= (1f / 60)) {
			renderers.forEach(o -> o.update(1f / 60));

			updateText(1f / 60);
			updateSkew(1f / 60);
			updatePulse(1f/60);

			if(currentRumbleTime <= rumbleTime) {
				currentRumblePower = rumblePower * ((rumbleTime - currentRumbleTime) / rumbleTime);

				rumbleX = (MathUtils.random(1.0f) - 0.5f) * 2 * currentRumblePower;
				rumbleY = (MathUtils.random(1.0f) - 0.5f) * 2 * currentRumblePower;

				currentRumbleTime += 1f/60;
			} else {
				rumbleX = 0;
				rumbleY = 0;
			}

			if (!player.dead) {
				Wall.updatePulse();
				background.update(1f / 60);
				tmpColor.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);
				fps.getStyle().fontColor = tmpColor;
				time.getStyle().fontColor = tmpColor;
				message.getStyle().fontColor = tmpColor;

			}

			delta0 -= 0.016666668f;
		}
		if(!player.dead)
			map.script.update(delta);
	}

	public void rumble(float power, float time) {
		rumblePower = power;
		rumbleTime = time;
		currentRumbleTime = 0;
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
		time.setText("Time: " + timeFormat.format(CurrentMap.currentTime) + (player.dead?"\nYou died! Press \"Space\" to restart!":""));

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

			playSound(levelUp);

			CurrentMap.isFastRotation = true;
			CurrentMap.rotationSpeed += (CurrentMap.rotationSpeed > 0 ? CurrentMap.rotationIncrement: -CurrentMap.rotationIncrement );
			CurrentMap.rotationSpeed *= -1;
			CurrentMap.rotationSpeed = Math.min(CurrentMap.rotationSpeedMax, Math.max(-CurrentMap.rotationSpeedMax, CurrentMap.rotationSpeed));

			CurrentMap.mustChangeSides = true;

			delta3 = 0;
		}

		if (CurrentMap.wallTimeline.isEmpty() && CurrentMap.mustChangeSides) {
			CurrentMap.sides = MathUtils.random(CurrentMap.minSides, CurrentMap.maxSides);
			playSound(sides);
			CurrentMap.mustChangeSides = false;
		}

		if (CurrentMap.wallTimeline.isAllSpawned() && !CurrentMap.mustChangeSides) {
			map.script.nextPattern();
		}

	}

	public void updateRotation(float delta) {

		if(player.dead) {
			if(CurrentMap.rotationSpeed < 0){
				CurrentMap.rotationSpeed = Math.min(-0.02f, CurrentMap.rotationSpeed + 0.002f * 60 * delta);
			} else if(CurrentMap.rotationSpeed > 0) {
				CurrentMap.rotationSpeed = Math.max(0.02f, CurrentMap.rotationSpeed - 0.002f * 60 * delta);
			}
		}

		camera.orbit(CurrentMap.rotationSpeed * 360f *  delta + (CurrentMap.rotationSpeed > 0 ? 1 : -1) * (getSmootherStep(0, CurrentMap.fastRotate, fastRotate) / 3.5f) * 17.f * 60 * delta);
		fastRotate = Math.max(0, fastRotate - 60f * delta);
		if(fastRotate == 0) CurrentMap.isFastRotation = false;
	}

	float delta4;
	float delta5;
	public void updatePulse(float delta){

		if(player.dead) return;

		if(delta4 <= 0){
			CurrentMap.beatPulse = CurrentMap.beatPulseMax;
			delta4 = CurrentMap.beatPulseDelay;
		}

		delta4 -= delta;

		if(CurrentMap.beatPulse > CurrentMap.beatPulseMin) scale = CurrentMap.beatPulse -= 1.2f * delta;

		if(delta5 <= 0){

			if((CurrentMap.pulseDir < 0 && CurrentMap.pulse <= CurrentMap.pulseMin) || (CurrentMap.pulseDir > 0 && CurrentMap.pulse >= CurrentMap.pulseMax)){
				CurrentMap.pulseDir *=-1;
				delta5 = CurrentMap.pulseDelayMax;
			}

			CurrentMap.pulse += (CurrentMap.pulseDir > 0 ? CurrentMap.pulseSpeed : -CurrentMap.pulseSpeedR) * 60 * delta;

			CurrentMap.pulse = MathUtils.clamp(CurrentMap.pulse, CurrentMap.pulseMin, CurrentMap.pulseMax);

		}
		//System.out.println(CurrentMap.pulse);
		delta5 -= 60f * delta;

	}

	float getSaturated(float mValue) { return Math.max(0.f, Math.min(1.f, mValue)); }
	float getSmootherStep(float edge0, float edge1, float x)
	{
		x = getSaturated((x - edge0)/(edge1 - edge0));
		return x * x * x * (x * (x * 6 - 15) + 10);
	}


}
