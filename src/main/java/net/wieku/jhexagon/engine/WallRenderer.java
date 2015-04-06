package net.wieku.jhexagon.engine;

import com.badlogic.gdx.graphics.Color;
import net.wieku.jhexagon.api.CurrentMap;
import net.wieku.jhexagon.api.Wall;
import net.wieku.jhexagon.utils.ShapeRenderer3D;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class WallRenderer implements Renderer {
	String h = "»";
	Color shadow = new Color();

	@Override
	public void render(ShapeRenderer3D renderer, float delta, boolean shadows){

		if(!shadows)
			renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);
		else
			renderer.setColor(shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f));

		for(Wall wall : CurrentMap.wallTimeline.getObjects()){

			if(!wall.visible) continue;

			renderer.triangle(wall.tmp.x, wall.tmp.y, wall.tmp2.x, wall.tmp2.y, wall.tmp4.x, wall.tmp4.y);
			renderer.triangle(wall.tmp4.x, wall.tmp4.y, wall.tmp3.x, wall.tmp3.y, wall.tmp.x, wall.tmp.y);

		}

	}

	@Override
	public void update(float delta){}

	@Override
	public int getIndex(){
		return 1;
	}

	/*
	public void drawWallsShadow(ShapeRenderer3D renderer, ArrayList<Wall> walls){

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);

		for(int j = 0; j< CurrentMap.layers; ++j) {
			renderer.identity();
			renderer.translate(0, -j * CurrentMap.depth, 0);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(shadow);

			for(Wall wall : walls){

				if(!wall.visible) continue;

				renderer.triangle(wall.tmp.x, wall.tmp.y, wall.tmp2.x, wall.tmp2.y, wall.tmp4.x, wall.tmp4.y);
				renderer.triangle(wall.tmp4.x, wall.tmp4.y, wall.tmp3.x, wall.tmp3.y, wall.tmp.x, wall.tmp.y);

			}

			renderer.end();

		}

		renderer.identity();

	}

	public void render(ShapeRenderer3D renderer, float delta, boolean shadows){

		shadow.set(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a).lerp(Color.BLACK, 0.4f);
		//renderer.begin(ShapeType.Filled);
		renderer.setColor(CurrentMap.walls.r, CurrentMap.walls.g, CurrentMap.walls.b, CurrentMap.walls.a);

		for(Wall wall : walls){

			if(!wall.visible) continue;

			renderer.triangle(wall.tmp.x, wall.tmp.y, wall.tmp2.x, wall.tmp2.y, wall.tmp4.x, wall.tmp4.y);
			renderer.triangle(wall.tmp4.x, wall.tmp4.y, wall.tmp3.x, wall.tmp3.y, wall.tmp.x, wall.tmp.y);

		}

		renderer.end();
		renderer.identity();

	}


	function addPattern(mKey)
		if mKey ==  0 then pAltBarrage(math.random(2, 4), 2)
	elseif mKey ==  1 then pMirrorSpiral(math.random(3, 6), 0)
	elseif mKey ==  2 then pBarrageSpiral(math.random(0, 3), 1, 1)
	elseif mKey ==  3 then pBarrageSpiral(math.random(0, 2), 1.2, 2)
	elseif mKey ==  4 then pBarrageSpiral(2, 0.7, 1)
	elseif mKey ==  5 then pInverseBarrage(0)
	elseif mKey ==  6 then pTunnel(math.random(1, 3))
	elseif mKey ==  7 then pMirrorWallStrip(1, 0)
	elseif mKey ==  8 then pWallExVortex(0, 1, 1)
	elseif mKey ==  9 then pDMBarrageSpiral(math.random(4, 7), 0.4, 1)
	elseif mKey == 10 then pRandomBarrage(math.random(2, 4), 2.25)
	end
end

-- shuffle the keys, and then call them to add all the patterns
-- shuffling is better than randomizing - it guarantees all the patterns will be called
keys = { 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7, 8, 9, 10, 10, 10 }

	 */
}
