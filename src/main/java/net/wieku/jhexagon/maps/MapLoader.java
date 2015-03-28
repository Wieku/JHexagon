package net.wieku.jhexagon.maps;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.wieku.jhexagon.api.MapScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Sebastian Krajewski on 28.03.15.
 */
public class MapLoader {
	private static final String PLUGIN_PATH = "Maps/";
	private static final String TEMP_PATH = "Maps/.temp/";

	private static Logger log = LoggerFactory.getLogger(MapLoader.class);

	public ArrayList<Map> load() {
		new File(PLUGIN_PATH).mkdirs();
		log.info("Loading maps");
		ArrayList<Map> maps = new ArrayList<>();

		File dir = new File(PLUGIN_PATH);

		File files[] = dir.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				JarFile jar;
				try {
					jar = new JarFile(file);
				} catch (IOException e) {
					log.error("Cannot load " + file.getName());
					e.printStackTrace();
					continue;
				}

				if (jar.getEntry("map.json") == null) {
					log.error("File: " + file.getName() + " doesn't contain map.json!");
					continue;
				}

				MapJson m;
				try {
					Gson gson = new GsonBuilder().create();
					m = gson.fromJson(new InputStreamReader(jar.getInputStream(jar.getEntry("map.json"))), MapJson.class);
				} catch (IOException e) {
					log.error("File map.json in mod " + file.getName() + " has wrong syntax!");
					e.printStackTrace();
					continue;
				}

				ClassLoader loader;
				try {
					loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
					continue;
				}

				Class<?> toLoad;
				try {
					toLoad = loader.loadClass(m.className);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
					continue;
				}

				List<Class<?>> interfaces = Arrays.asList(toLoad.getInterfaces());
				if (!interfaces.contains(MapScript.class)) {
					log.error("Script of " + m.name + "(" + file.getName() + ") Doesn't implement 'MapScript' interface!");
					continue;
				}

				try {

					maps.add(new Map((MapScript) toLoad.newInstance(), m));

					File temp = new File(TEMP_PATH);
					temp.mkdirs();

					m.audioTempName = Files.hash(file, Hashing.sha1()).toString();

					JarEntry entry = jar.getJarEntry(m.audioFileName);

					if(entry == null) {
						log.error("Audiofile of " + m.name + "(" + file.getName() + ") not found!");
						continue;
					}

					InputStream is = jar.getInputStream(entry); // get the input stream
					FileOutputStream fos = new java.io.FileOutputStream(new File(TEMP_PATH+m.audioTempName));
					while (is.available() > 0) {
						fos.write(is.read());
					}
					fos.close();
					is.close();

				} catch (Exception e1) {
					log.error("Script of " + m.name + "(" + file.getName() + ") couldn't contain custom constructor!");
					e1.printStackTrace();
					continue;
				}

				try {
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				log.debug("Map " + m.name + " Has been loaded!");
			}
		}
		log.info("Loaded " + maps.size() + " maps");
		return maps;
	}

}
