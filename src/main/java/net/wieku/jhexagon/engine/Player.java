package net.wieku.jhexagon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
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
	Color shadow = new Color();

	public void drawShadow(ShapeRenderer3D renderer, float delta) {

		this.delta += delta;

		while (this.delta >= (1f / 60)) {

			if(Gdx.input.isKeyPressed(Keys.LEFT)){
				rot += (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)? 5f : 10f);
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
				rot -= (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)? 5f : 10f);
			}

			rot = (rot<0?rot+360f:(rot>360f?rot-360f:rot));

			this.delta  -= 0.016666668f;
		}

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);

		for(int j=0;j< CurrentMap.layers;++j){
			renderer.identity();
			renderer.scale(Game.scale, Game.scale, Game.scale);
			renderer.translate(0, -j * CurrentMap.depth, 0);

			renderer.begin(ShapeType.Filled);

			renderer.setColor(shadow);

			tmp.set(MathUtils.sin(rot / 360f * MathUtils.PI2), MathUtils.cos(rot / 360f * MathUtils.PI2)).scl(0.057f * Main.diagonal);

			tmp2.set(MathUtils.sin((rot - 6f) / 360f  * MathUtils.PI2), MathUtils.cos((rot - 5f) / 360f  * MathUtils.PI2)).scl(0.05f * Main.diagonal);

			tmp3.set(MathUtils.sin((rot + 6f) / 360f  * MathUtils.PI2), MathUtils.cos((rot + 5f) / 360f  * MathUtils.PI2)).scl(0.05f * Main.diagonal);

			renderer.triangle(tmp3.x, tmp3.y, tmp.x, tmp.y, tmp2.x, tmp2.y);

			renderer.end();
		}
	}

	float delta;

	public void draw(ShapeRenderer3D renderer, float delta) {

		renderer.begin(ShapeType.Filled);

		renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);

		tmp.set(MathUtils.sin(rot / 360f * MathUtils.PI2), MathUtils.cos(rot / 360f * MathUtils.PI2)).scl(0.057f * Main.diagonal);

		tmp2.set(MathUtils.sin((rot - 6f) / 360f  * MathUtils.PI2), MathUtils.cos((rot - 5f) / 360f  * MathUtils.PI2)).scl(0.05f * Main.diagonal);

		tmp3.set(MathUtils.sin((rot + 6f) / 360f  * MathUtils.PI2), MathUtils.cos((rot + 5f) / 360f  * MathUtils.PI2)).scl(0.05f * Main.diagonal);

		renderer.triangle(tmp3.x, tmp3.y, tmp.x, tmp.y, tmp2.x, tmp2.y);

		renderer.end();
	}

}
