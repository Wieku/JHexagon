package net.wieku.jhexagon.engine;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Sebastian Krajewski on 05.04.15.
 */
public interface Renderer {
	public void render(ShapeRenderer renderer, float delta, boolean shadow);
	public int getIndex();
	public void update(float delta);
}
