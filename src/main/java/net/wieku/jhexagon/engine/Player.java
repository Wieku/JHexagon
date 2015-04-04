package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.Wall;
import net.wieku.jhexagon.utils.ShapeRenderer3D;
import net.wieku.jhexagon.utils.ShapeRenderer3D.ShapeType;

/**
 * @author Sebastian Krajewski on 22.03.15.
 */
public class Player {

	float rot;
	Vector2 tmp = new Vector2();
	Vector2 tmp2 = new Vector2();
	Vector2 tmp3 = new Vector2();
	Vector2 tmp4 = new Vector2();
	Vector2 lCh = new Vector2();
	Vector2 rCh = new Vector2();
	public boolean dead = false;
	int dir = 0;
	Color shadow = new Color();

	public void drawShadow(ShapeRenderer3D renderer, float delta) {

		this.delta += delta;

		while (this.delta >= (1f / 60)) {

			float oldRot = rot;

			if(!dead)
				if(Gdx.input.isKeyPressed(Keys.LEFT)){
					rot += (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)? 300f : 600f) / 60f;
					dir = -1;
				} else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
					rot -= (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)? 300f : 600f) / 60f;
					dir = 1;
				} else {
					dir = 0;
				}

			rot = (rot<0?rot+360f:(rot>360f?rot-360f:rot));

			tmp4.set(0, 0.067f * Main.diagonal).rotate(-rot);

			lCh.set(0, 0.067f * Main.diagonal).rotate(-(rot+1));
			rCh.set(0, 0.067f * Main.diagonal).rotate(-(rot-1));


			for(Wall wall : CurrentMap.wallTimeline.getObjects()){

				if((dir == -1 && Intersector.isPointInPolygon(wall.vecs, lCh)) || (dir == 1 && Intersector.isPointInPolygon(wall.vecs, rCh))){

					rot = oldRot;
					tmp4.set(0, 0.067f * Main.diagonal).rotate(-rot);
					lCh.set(0, 0.067f * Main.diagonal).rotate(-(rot+3));
					rCh.set(0, 0.067f * Main.diagonal).rotate(-(rot-3));

				}

				if(Intersector.isPointInPolygon(wall.vecs, tmp4)){
					dead = true;
				}
			}

			tmp.set(0, 0.067f * Main.diagonal).rotate(-rot);

			tmp2.set(0, 0.06f * Main.diagonal).rotate(-rot - 6);
			tmp3.set(0, 0.06f * Main.diagonal).rotate(-rot+6);
			lCh.set(0, 0.067f * Main.diagonal).rotate(-(rot+3));
			rCh.set(0, 0.067f * Main.diagonal).rotate(-(rot-3));

			this.delta  -= 0.016666668f;
		}

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);

		for(int j=0;j< CurrentMap.layers;++j){
			renderer.identity();
			renderer.scale(Game.scale, Game.scale, Game.scale);
			renderer.translate(0, -j * CurrentMap.depth, 0);

			renderer.begin(ShapeType.Filled);

			renderer.setColor(shadow);

			renderer.triangle(tmp3.x, tmp3.y, tmp.x, tmp.y, tmp2.x, tmp2.y);

			renderer.end();
		}
	}

	float delta;

	public void draw(ShapeRenderer3D renderer, float delta) {

		renderer.begin(ShapeType.Filled);

		renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);

		renderer.triangle(tmp3.x, tmp3.y, tmp.x, tmp.y, tmp2.x, tmp2.y);

		renderer.end();
	}

	public void reset(){
		dead = false;
	}

}
