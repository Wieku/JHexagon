package net.wieku.jhexagon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.HColor;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class Background implements Renderer{

	public Background(){}

	Vector2 tmp = new Vector2();
	Vector2 tmp2 = new Vector2();
	Color tmpC = new Color();

	float delta1;

	@Override
	public void render(ShapeRenderer renderer, float delta, boolean shadows) {

		if(shadows)
			for(float i = 0; i < CurrentMap.sides; ++i) {

				if(CurrentMap.colors.size() > 0){
					HColor col = CurrentMap.colors.get(((int)i + CurrentMap.colorOffset) % CurrentMap.colors.size());
					tmpC.set(col.r, col.g, col.b, col.a);
					if(i+1==CurrentMap.sides && CurrentMap.sides % 2 == 1)
						tmpC.lerp(Color.GRAY, 0.5f);
					renderer.setColor(tmpC);
				} else {
					renderer.setColor(1, 1, 1, 1);
				}

				tmp.set(0, Main.diagonal * 2).rotate(i / CurrentMap.sides * -360f);
				tmp2.set(0, Main.diagonal * 2).rotate((i - 1) / CurrentMap.sides * -360f);

				renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);

			}

	}

	@Override
	public void update(float delta){
		CurrentMap.colors.forEach(o -> o.update(delta));

		if((delta1 += delta) >= CurrentMap.colorSwitch){

			++CurrentMap.colorOffset;

			if(CurrentMap.colorOffset == CurrentMap.colors.size()){
				CurrentMap.colorOffset = 0;
			}

			delta1 = 0;
		}
	}

	@Override
	public int getIndex(){
		return 0;
	}

	/*
	public void render(ShapeRenderer3D renderer, float delta, boolean shadows) {

		renderer.identity();
		renderer.scale(Game.scale, Game.scale, Game.scale);
		renderer.begin(ShapeType.Filled);

		CurrentMap.colors.forEach(o -> o.update(delta));

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
				if(i+1==CurrentMap.sides && CurrentMap.sides % 2 == 1)
					tmpC.lerp(Color.GRAY, 0.5f);
				renderer.setColor(tmpC);
			} else {
				renderer.setColor(1, 1, 1, 1);
			}

			tmp.set(0, Main.diagonal * 20).rotate(i / CurrentMap.sides * -360f);
			tmp2.set(0, Main.diagonal * 20).rotate((i - 1) / CurrentMap.sides * -360f);

			renderer.triangle(0, 0, tmp.x, tmp.y, tmp2.x, tmp2.y);

		}

		renderer.end();

	}
	 */
}
