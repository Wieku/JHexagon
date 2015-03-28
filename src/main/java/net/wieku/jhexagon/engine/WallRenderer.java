package net.wieku.jhexagon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.Wall;
import net.wieku.jhexagon.utils.ShapeRenderer3D;
import net.wieku.jhexagon.utils.ShapeRenderer3D.ShapeType;

import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class WallRenderer {

	Color shadow = new Color();

	Vector2 tmp = new Vector2();
	Vector2 tmp2 = new Vector2();
	Vector2 tmp3 = new Vector2();
	Vector2 tmp4 = new Vector2();

	public void drawWallsShadow(ShapeRenderer3D renderer, ArrayList<Wall> walls){

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);

		float angle = 360f / CurrentMap.sides;

		for(int j=0; j< CurrentMap.layers; ++j) {
			renderer.identity();
			renderer.translate(0, -j * CurrentMap.depth, 0);
			renderer.scale(Wall.pulseSpeed, Wall.pulseSpeed, Wall.pulseSpeed);
			renderer.begin(ShapeType.Filled);

			renderer.setColor(shadow);

			for(int i = 0; i < walls.size(); ++i){

				Wall wall = walls.get(i);

				if(!wall.visible) continue;

				//System.out.println((wall.side + 1) / (float)CurrentMap.sides);

				tmp.set(MathUtils.sin(wall.side / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos(wall.side / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, wall.position / tmp.len()));

				tmp2.set(MathUtils.sin(wall.side / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos(wall.side / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, (wall.position + wall.thickness) / tmp2.len()));

				tmp3.set(MathUtils.sin((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, wall.position / tmp3.len()));

				tmp4.set(MathUtils.sin((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, (wall.position + wall.thickness) / tmp4.len()));

				renderer.triangle(tmp.x, tmp.y, tmp2.x, tmp2.y, tmp4.x, tmp4.y);
				renderer.triangle(tmp4.x, tmp4.y, tmp3.x, tmp3.y, tmp.x, tmp.y);

			}

			renderer.end();

		}

		renderer.identity();

	}

	public void drawWalls(ShapeRenderer3D renderer, ArrayList<Wall> walls){

		renderer.scale(Wall.pulseSpeed, Wall.pulseSpeed, Wall.pulseSpeed);

		renderer.begin(ShapeType.Filled);

		renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);

		float angle = 360f / CurrentMap.sides;

		for(int i = 0; i < walls.size(); ++i){

			Wall wall = walls.get(i);

			if(!wall.visible) continue;

			tmp.set(MathUtils.sin(wall.side / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos(wall.side / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, wall.position / tmp.len()));
			tmp2.set(MathUtils.sin(wall.side / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos(wall.side / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, (wall.position + wall.thickness) / tmp2.len()));
			tmp3.set(MathUtils.sin((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, wall.position / tmp3.len()));
			tmp4.set(MathUtils.sin((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2), MathUtils.cos((wall.side + 1) / (float)CurrentMap.sides * MathUtils.PI2)).scl(Math.max(0, (wall.position + wall.thickness) / tmp4.len()));

			renderer.triangle(tmp.x, tmp.y, tmp2.x, tmp2.y, tmp4.x, tmp4.y);
			renderer.triangle(tmp4.x, tmp4.y, tmp3.x, tmp3.y, tmp.x, tmp.y);

		}

		renderer.end();

		renderer.identity();

	}

}
