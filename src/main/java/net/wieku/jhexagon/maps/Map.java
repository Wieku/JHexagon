package net.wieku.jhexagon.maps;

import net.wieku.jhexagon.api.MapScript;

/**
 * @author Sebastian Krajewski on 28.03.15.
 */
public class Map {

	public MapScript script;
	public MapJson info;

	public Map(MapScript script, MapJson info) {
		this.script = script;
		this.info = info;
	}
}
