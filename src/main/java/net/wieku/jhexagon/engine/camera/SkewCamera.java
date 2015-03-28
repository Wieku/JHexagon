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
		viewportWidth = width;
		viewportHeight = height;
		far = 10000f;
		near = 0.00001f;
		fieldOfView = 47f;
		update();
	}

	public void orbit(float rotation){
		currentRotation += rotation;
		currentRotation = (currentRotation >= 360f ? currentRotation - 360f : currentRotation);

		tmp.set(MathUtils.sin(currentRotation / 360f * MathUtils.PI2), MathUtils.cos(currentRotation / 360f * MathUtils.PI2)).scl(Main.diagonal/2 * (CurrentMap.skew+0.00001f));

		position.set(tmp.x, Main.diagonal/2, tmp.y);
		lookAt(0, 0, 0);
		up.set(Vector3.Y);
		update();
	}

}
