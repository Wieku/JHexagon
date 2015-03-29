package net.wieku.jhexagon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.wieku.jhexagon.api.Patterns;

import net.wieku.jhexagon.maps.Map;
import net.wieku.jhexagon.maps.MapLoader;
import net.wieku.jhexagon.resources.FontManager;

import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 20.03.15.
 */
public class Main extends Game{

	Vector2 tmp = new Vector2();
	int width, height;

	ArrayList<Map> maps;


	@Override
	public void create() {
		FontManager.init();

		maps = new MapLoader().load();

		setScreen(new net.wieku.jhexagon.engine.Game(maps.get(0)));
	/*	try {
			audioPlayer = new AudioPlayer(new File("/home/wiek/workspaces/idea/JHexagon/src/resources/assets/song.mp3"));
			audioPlayer.setVolume(0.2f);
			audioPlayer.playRepeat();
			audioPlayer.setBeatListener(new BeatListener() {
				@Override
				public void onBeatLow() {
					scale = Math.max(scale, 1.1f);
				}

				@Override
				public void onBeatHigh() {
				}
			});
			//audioPlayer.setPosition(170000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/

		/*Patterns.pMirrorSpiral(MathUtils.random(3, 6), 0);
		Patterns.pBarrageSpiral(MathUtils.random(0, 3), 1, 1);
		Patterns.pBarrageSpiral(MathUtils.random(0, 2), 1.2f, 2);
		Patterns.pBarrageSpiral(2, 0.7f, 1);
		Patterns.pInverseBarrage(0);
		Patterns.pTunnel(MathUtils.random(1, 3));
		Patterns.pMirrorWallStrip(1, 0);
		Patterns.pWallExVortex(0, 1, 1);
		Patterns.pDMBarrageSpiral(MathUtils.random(4, 7), 0.4f, 1);
		Patterns.pRandomBarrage(MathUtils.random(2, 4), 2.25f);*/
		//Patterns.pTunnel(5);
	}

	public static float diagonal = 1600f;

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		//diagonal = tmp.set(width, height).len();
		super.resize(width, height);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}


	public static void main(String[] args) {

		LwjglApplicationConfiguration conf = new LwjglApplicationConfiguration();
		conf.width = 1024;
		conf.height = 768;
		conf.title = "JHexagon";
		conf.foregroundFPS = 0;
		conf.samples = 8;
		conf.vSyncEnabled = false;
		new LwjglApplication(new Main(), conf);

	}

}
