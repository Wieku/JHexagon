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
public class Background {

	public Background(){

		CurrentMap.colors.add(new HColor(217f/255,162f/255,200f/255, 1, 247f/255,192f/255,230f/255, 1));
		CurrentMap.colors.add(new HColor(1, 1, 1, 1));

	}

	Vector2 tmp = new Vector2();
	Vector2 tmp2 = new Vector2();
	Color tmpC = new Color();

	float delta0;
	float delta1;

	public void draw(ShapeRenderer3D renderer, float delta) {

		renderer.identity();
		renderer.scale(Game.scale, Game.scale, Game.scale);
		renderer.begin(ShapeType.Filled);

		if((delta0 += delta) >= 1f/60){
			CurrentMap.colors.forEach(o -> o.update(1f/60));
			delta0 = 0;
		}

		if((delta1 += delta) >= CurrentMap.colorSwitch){

			++CurrentMap.colorOffset;

			if(CurrentMap.colorOffset == CurrentMap.colors.size()){
				CurrentMap.colorOffset = 0;
			}

			delta1 = 0;
		}

		for(float i = 0; i < CurrentMap.sides; ++i) {

			if(CurrentMap.colors.size() > 0){
				HColor col = CurrentMap.colors.get(((int)i + CurrentMap.colorOffset) % CurrentMap.colors.size());
				tmpC.set(col.r, col.g, col.b, col.a);
				if(i+1==CurrentMap.sides && CurrentMap.sides % 2 ==1)
					tmpC.lerp(Color.GRAY, 0.5f);
				renderer.setColor(tmpC);
			} else {
				renderer.setColor(1, 1, 1, 1);
			}

			tmp.set(MathUtils.sin(i / CurrentMap.sides * MathUtils.PI2), MathUtils.cos(i / CurrentMap.sides * MathUtils.PI2)).scl(Main.diagonal * 20);
			tmp2.set(MathUtils.sin((i - 1) / CurrentMap.sides * MathUtils.PI2), MathUtils.cos((i - 1) / CurrentMap.sides * MathUtils.PI2)).scl(Main.diagonal * 20);

			renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);

		}

		renderer.end();

	}

}
