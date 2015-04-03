package net.wieku.jhexagon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.wieku.jhexagon.maps.Map;
import net.wieku.jhexagon.maps.MapLoader;
import net.wieku.jhexagon.resources.FontManager;

import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 20.03.15.
 */
public class Main extends Game{

	int width, height;
	ArrayList<Map> maps;
	public static float diagonal = 1600f;

	@Override
	public void create() {
		FontManager.init();
		maps = new MapLoader().load();
		setScreen(new net.wieku.jhexagon.engine.Game(maps.get(0)));
	}


	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
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
