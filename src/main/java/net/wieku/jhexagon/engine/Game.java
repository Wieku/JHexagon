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
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.Patterns;
import net.wieku.jhexagon.api.Wall;
import net.wieku.jhexagon.engine.camera.SkewCamera;
import net.wieku.jhexagon.maps.Map;
import net.wieku.jhexagon.maps.MapLoader;
import net.wieku.jhexagon.sound.AudioPlayer;
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

		try {
			audioPlayer = new AudioPlayer(new File(MapLoader.TEMP_PATH + map.info.audioTempName));
			audioPlayer.setVolume(0.2f);
			audioPlayer.play();
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

	float fastRotate = 0f;

	@Override
	public void render(float delta8) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		CurrentMap.currentTime += Gdx.graphics.getDeltaTime();

		if((delta1+=Gdx.graphics.getDeltaTime())>=CurrentMap.levelIncrement){

			fastRotate = CurrentMap.fastRotate;

			CurrentMap.isFastRotation = true;
			CurrentMap.rotationSpeed += (CurrentMap.rotationSpeed > 0 ? CurrentMap.rotationIncrement: -CurrentMap.rotationIncrement );
			CurrentMap.rotationSpeed *= -1;
			CurrentMap.rotationSpeed = Math.min(CurrentMap.rotationSpeedMax, Math.max(-CurrentMap.rotationSpeedMax, CurrentMap.rotationSpeed));

			delta1 = 0;
		}

		this.delta += Gdx.graphics.getDeltaTime();

		while (this.delta >= (1f / 60)) {

			Iterator<Wall> i = CurrentMap.wallObjects.iterator();

			while(i.hasNext()){
				Wall wall = i.next();

				wall.update(1f/60);

				if(wall.toRemove)
					i.remove();

			}


			Wall.updatePulse();

			if(CurrentMap.wallObjects.isEmpty()){
				CurrentMap.sides = MathUtils.random(CurrentMap.minSides, CurrentMap.maxSides);


				Patterns.t_wait(200);
				Patterns.pMirrorSpiral(MathUtils.random(3, 6), 0);
				Patterns.pBarrageSpiral(MathUtils.random(0, 3), 1, 1);
				Patterns.pBarrageSpiral(MathUtils.random(0, 2), 1.2f, 2);
				Patterns.pBarrageSpiral(2, 0.7f, 1);
				Patterns.pInverseBarrage(0);
				Patterns.pTunnel(MathUtils.random(1, 3));
				Patterns.pMirrorWallStrip(1, 0);
				Patterns.pWallExVortex(0, 1, 1);
				Patterns.pDMBarrageSpiral(MathUtils.random(4, 7), 0.4f, 1);
				Patterns.pRandomBarrage(MathUtils.random(2, 4), 2.25f);

			}

			camera.orbit(CurrentMap.rotationSpeed * 360f / 60 + (CurrentMap.rotationSpeed > 0 ? 1 : -1) * (getSmootherStep(0, CurrentMap.fastRotate, fastRotate) / 3.5f) * 17.f);

			fastRotate -= 0.5f;
			fastRotate = Math.max(0, fastRotate);
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

		wallRenderer.drawWallsShadow(renderer, CurrentMap.wallObjects);
		center.drawShadow(renderer, Gdx.graphics.getDeltaTime());
		player.drawShadow(renderer, Gdx.graphics.getDeltaTime());

		renderer.identity();
		renderer.translate(0, 0, 0);

		wallRenderer.drawWalls(renderer, CurrentMap.wallObjects);
		renderer.scale(scale, scale, scale);
		center.draw(renderer, Gdx.graphics.getDeltaTime());
		player.draw(renderer, Gdx.graphics.getDeltaTime());


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
		if(renderer != null) renderer.dispose();
	}

	float getSaturated(float mValue) { return Math.max(0.f, Math.min(1.f, mValue)); }
	float getSmootherStep(float edge0, float edge1, float x)
	{
		x = getSaturated((x - edge0)/(edge1 - edge0));
		return x * x * x * (x * (x * 6 - 15) + 10);
	}


}
