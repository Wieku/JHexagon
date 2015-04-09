package net.wieku.jhexagon;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.wieku.jhexagon.engine.Settings;
import net.wieku.jhexagon.engine.menu.Menu;
import net.wieku.jhexagon.map.Map;
import net.wieku.jhexagon.map.MapLoader;
import net.wieku.jhexagon.resources.FontManager;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 20.03.15.
 */
public class Main extends Game{

	public static LwjglApplicationConfiguration config;
	int width, height;
	ArrayList<Map> maps;
	public static float diagonal = 1600f;
	static Main instance = new Main();
	static LwjglApplication app;
	private Main(){

	}

	public static Main getInstance(){
		return instance;
	}

	static boolean restarted = false;

	@Override
	public void create() {
		FontManager.init();
		maps = MapLoader.load();
		new Menu(maps);
		setScreen(restarted ? Menu.options : Menu.getInstance());

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
		if(maps != null)
			maps.forEach(m->MapLoader.closeJar(m.file));

		if(Settings.instance != null){
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			try {
				Files.write(gson.toJson(Settings.instance), new File("settings.json"), Charsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	public static void main(String[] args) {

		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

		try {
			Gson gson = new GsonBuilder().create();
			File file = new File("settings.json");
			if(!file.exists())
				Settings.instance = new Settings();
			else
				Settings.instance = gson.fromJson(new FileReader(file), Settings.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		config.title = "JHexagon";
		config.foregroundFPS = 120;
		config.addIcon("assets/icon.png", FileType.Internal);
		config.samples = Settings.instance.msaa;
		config.vSyncEnabled = Settings.instance.vSync;
		app = new LwjglApplication(instance, config);

	}

}
