package net.wieku.jhexagon.maps;

import net.wieku.jhexagon.api.MapScript;

import java.util.zip.ZipFile;

/**
 * @author Sebastian Krajewski on 28.03.15.
 */
public class Map {

	public MapScript script;
	public MapJson info;
	public ZipFile file;

	public Map(MapScript script, MapJson info, ZipFile file) {
		this.script = script;
		this.info = info;
		this.file = file;
	}
}
