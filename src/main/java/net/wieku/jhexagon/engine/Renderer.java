package net.wieku.jhexagon.engine;

import net.wieku.jhexagon.utils.ShapeRenderer3D;

/**
 * @author Sebastian Krajewski on 05.04.15.
 */
public interface Renderer {
	public void render(ShapeRenderer3D renderer, float delta, boolean shadow);
	public int getIndex();
	public void update(float delta);
}
