package net.wieku.jhexagon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.HColor;
import net.wieku.jhexagon.utils.ShapeRenderer3D;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class Center implements Renderer {

	Color shadow = new Color();

	Vector2 tmp = new Vector2();
	Vector2 tmp2 = new Vector2();

	@Override
	public void render(ShapeRenderer3D renderer, float delta, boolean shadows) {

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);

		for (float i = 0; i < CurrentMap.sides; ++i) {

			if(!shadows)
				if(CurrentMap.colors.size() > 0){
					HColor col = CurrentMap.colors.get(CurrentMap.colorOffset);
					renderer.setColor(col.r, col.g, col.b, col.a);
				} else
					renderer.setColor(Color.WHITE);
			else
				renderer.setColor(shadow);


			tmp.set(0, Main.diagonal * 0.05f * Game.scale).rotate(i / CurrentMap.sides * -360f);
			tmp2.set(0, Main.diagonal * 0.05f * Game.scale).rotate((i - 1) / CurrentMap.sides * -360f);

			if(!shadows){
				renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);
				renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);
			}

			renderer.circle(tmp.x, tmp.y, 4);
			renderer.rectLine(tmp, tmp2, 8);

		}

	}

	@Override
	public void update(float delta){}

	@Override
	public int getIndex(){
		return 2;
	}

}
