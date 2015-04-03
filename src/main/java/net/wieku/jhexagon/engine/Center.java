package net.wieku.jhexagon.engine;

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

			for (float i = 0; i < CurrentMap.sides; ++i) {

				renderer.setColor(this.shadow);

				tmp.set(MathUtils.sin(i / CurrentMap.sides * MathUtils.PI2), MathUtils.cos(i / CurrentMap.sides * MathUtils.PI2)).scl(0.05f * Main.diagonal);

				tmp2.set(MathUtils.sin((i - 1) / CurrentMap.sides * MathUtils.PI2), MathUtils.cos((i - 1) / CurrentMap.sides * MathUtils.PI2)).scl(0.05f * Main.diagonal);

				renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);

				renderer.setColor(this.shadow);

				renderer.rectLine(tmp, tmp2, 2);

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

			tmp.set(MathUtils.sin(i / CurrentMap.sides * MathUtils.PI2), MathUtils.cos(i / CurrentMap.sides * MathUtils.PI2)).scl(0.05f * Main.diagonal);

			tmp2.set(MathUtils.sin((i - 1) / CurrentMap.sides * MathUtils.PI2), MathUtils.cos((i - 1) / CurrentMap.sides * MathUtils.PI2)).scl(0.05f * Main.diagonal);

			renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);

			renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);

			renderer.rectLine(tmp, tmp2, 2);

		}

		renderer.end();

	}

}
