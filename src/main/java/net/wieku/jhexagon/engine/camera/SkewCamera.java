package net.wieku.jhexagon.engine.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;

import java.util.Random;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class SkewCamera extends PerspectiveCamera {

	float currentRotation;

	Vector2 tmp = new Vector2();
	private Random random = new Random();
	private float rumbleX;
	private float rumbleY;
	private float rumbleTime = 0;
	private float currentRumbleTime = 1;
	private float rumblePower = 0;
	private float currentRumblePower = 0;

	public void updateViewport(int width, int height){
		viewportWidth = 4;
		viewportHeight = 3;
		far = 10000f;
		near = 0.00001f;
		fieldOfView = 47f;
		update();
	}

	public void orbit(float rotation){
		currentRotation += rotation;
		currentRotation = (currentRotation >= 360f ? currentRotation - 360f : currentRotation);

		float skw = 40f + 50f * (CurrentMap.maxSkew - CurrentMap.skew - 0.0001f);


		if(currentRumbleTime <= rumbleTime) {
			currentRumblePower = rumblePower * ((rumbleTime - currentRumbleTime) / rumbleTime);

			rumbleX = (random.nextFloat() - 0.5f) * 2 * currentRumblePower;
			rumbleY = (random.nextFloat() - 0.5f) * 2 * currentRumblePower;

			currentRumbleTime += 1f/60;
		} else {
			rumbleX = 0;
			rumbleY = 0;
		}


		position.set(
				1000f * MathUtils.cos((skw + rumbleY) / 360f * MathUtils.PI2) * MathUtils.cos((currentRotation + rumbleX) / 360f * MathUtils.PI2),
				1000f * MathUtils.sin((skw + rumbleY) / 360f * MathUtils.PI2),
				1000f * MathUtils.cos((skw + rumbleY) / 360f * MathUtils.PI2) * MathUtils.sin((currentRotation + rumbleX) / 360f * MathUtils.PI2) );

		lookAt(0, 0, 0);
		up.set(Vector3.Y);
		update();
	}

	public void reset(){
		rumblePower = 0;
		rumbleTime = 0;
		currentRumbleTime = 0;
	}

	public void rumble(float power, float time) {
		rumblePower = power;
		rumbleTime = time;
		currentRumbleTime = 0;
	}
}
