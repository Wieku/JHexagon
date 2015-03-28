package net.wieku.jhexagon.api;

import com.badlogic.gdx.math.MathUtils;
import net.wieku.jhexagon.Main;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class Wall {

	public int side;
	public float thickness;
	public float show;
	public float speed;
	public float position;

	private float delta0;
	public boolean visible = false;
	public boolean toRemove = false;


	public Wall(int side, float thickness, float show, float speed) {
		this.side = side;
		this.thickness = thickness;
		this.show = show;
		this.speed = speed;
	}

	public static float pulseSpeed = 1f;

	public static void updatePulse(){
		pulseSpeed = Math.max(1f, Math.abs(MathUtils.sinDeg(CurrentMap.currentTime * 180)) * 1.2f);
	}

	public void update(float delta){

		if(CurrentMap.currentTime >= show){

			if(!visible){
				position = Main.diagonal;
				visible = true;
			}

			//if ((delta0+=delta) >= 1f/60 ){

				position -= speed * CurrentMap.speed * 5 ;// * pulseSpeed;

				if (position + thickness <= 0){
					toRemove = true;
				}

				//delta0 = 0;
			//}
		}

	}

}
