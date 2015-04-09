package net.wieku.jhexagon.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.engine.timeline.TimelineObject;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class Wall extends TimelineObject{

	public int side;
	public float thickness;
	public float speed;
	public float position;

	private float delta0;
	public boolean visible = false;

	public Vector2 tmp = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2(), tmp4 = new Vector2();
	public Array<Vector2> vecs = new Array<>();

	public Wall(int side, float thickness, float speed) {
		this.side = side;
		this.thickness = thickness;
		this.speed = speed;
		vecs.add(tmp);
		vecs.add(tmp2);
		vecs.add(tmp4);
		vecs.add(tmp3);
	}

	public static float pulseSpeed = 1f;

	public static void updatePulse(){
		pulseSpeed = Math.max(1f, Math.abs((float)Math.sin(Math.toRadians(CurrentMap.currentTime * 180))) * 1.4f);
	}

	@Override
	public void update(float delta){

		delta0 += delta;

		while(delta0 >= 1f / 60){

			if(!visible){
				position = Main.diagonal;
				visible = true;
			}

			position -= speed * CurrentMap.speed * 5;

			if (position + thickness <= 0){
				setToRemove(true);
			}

			float angle1 = side / (float) CurrentMap.sides * 360f;
			float angle2 = (side + 1) / (float) CurrentMap.sides * 360f;

			tmp.set(0, Math.max(0, position) * pulseSpeed).rotate(-angle1);
			tmp2.set(0, Math.max(0, position + thickness) * pulseSpeed).rotate(-angle1);
			tmp3.set(0, Math.max(0, position) * pulseSpeed).rotate(-angle2);
			tmp4.set(0, Math.max(0, position + thickness) * pulseSpeed).rotate(-angle2);

			delta0 -= 0.016666668f;
		}

	}

}
