package net.wieku.jhexagon.engine.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.wieku.jhexagon.Main;
import net.wieku.jhexagon.api.CurrentMap;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class SkewCamera extends PerspectiveCamera {

	float currentRotation;

	Vector2 tmp = new Vector2();

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

		position.set(
				1000f * MathUtils.cos(skw / 360f * MathUtils.PI2) * MathUtils.cos(currentRotation / 360f * MathUtils.PI2),
				1000f * MathUtils.sin(skw / 360f * MathUtils.PI2),
				1000f * MathUtils.cos(skw / 360f * MathUtils.PI2) * MathUtils.sin(currentRotation / 360f * MathUtils.PI2));

		lookAt(0, 0, 0);
		up.set(Vector3.Y);
		update();
	}

}
