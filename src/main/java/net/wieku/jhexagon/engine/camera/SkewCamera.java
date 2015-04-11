package net.wieku.jhexagon.engine.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.wieku.jhexagon.api.CurrentMap;

import java.util.Random;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class SkewCamera extends PerspectiveCamera {

	float currentRotation;

	public SkewCamera(){
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

		position.set(0, 1000f, 0).rotate(Vector3.X, Math.max(0.00001f, 50f * CurrentMap.skew)).rotate(Vector3.Y, currentRotation);
		lookAt(0, 0, 0);
		up.set(Vector3.Y);

		update();
	}

}
