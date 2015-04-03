package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.HColor;
import net.wieku.jhexagon.utils.ShapeRenderer3D;
import net.wieku.jhexagon.utils.ShapeRenderer3D.ShapeType;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class Center {

	Color shadow = new Color();

	Vector2 tmp = new Vector2();
	Vector2 tmp2 = new Vector2();

	public void drawShadow(ShapeRenderer3D renderer, float delta) {

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);

		for(int j=0;j < CurrentMap.layers;++j) {
			renderer.identity();
			renderer.scale(Game.scale, Game.scale, Game.scale);
			renderer.translate(0, -j * CurrentMap.depth, 0);

			renderer.begin(ShapeType.Filled);
			renderer.setColor(this.shadow);

			for (float i = 0; i < CurrentMap.sides; ++i) {

				tmp.set(0, Main.diagonal * 0.05f).rotate(i / CurrentMap.sides * -360f);
				tmp2.set(0, Main.diagonal * 0.05f).rotate((i - 1) / CurrentMap.sides * -360f);

				renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);
				renderer.circle(tmp.x, tmp.y, 4);
				renderer.rectLine(tmp, tmp2, 8);

			}

			renderer.end();

		}

	}

	public void draw(ShapeRenderer3D renderer, float delta) {

		renderer.begin(ShapeType.Filled);

		for (float i = 0; i < CurrentMap.sides; ++i) {

			if(CurrentMap.colors.size() > 0){
				HColor col = CurrentMap.colors.get(CurrentMap.colorOffset);
				renderer.setColor(col.r, col.g, col.b, col.a);
			} else {
				renderer.setColor(Color.WHITE);
			}

			tmp.set(0, Main.diagonal * 0.05f).rotate(i / CurrentMap.sides * -360f);
			tmp2.set(0, Main.diagonal * 0.05f).rotate((i - 1) / CurrentMap.sides * -360f);

			renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);
			renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);

			renderer.circle(tmp.x, tmp.y, 4);
			renderer.rectLine(tmp, tmp2, 8);

		}

		renderer.end();

	}

}
