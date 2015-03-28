package net.wieku.jhexagon.api;

import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public abstract class CurrentMap {

	/** rotations/s */
	public static float rotationSpeed = 0.5f;
	public static float rotationSpeedMax = 1.5f;
	public static float rotationIncrement = 0.083f;
	public static float fastRotate = 70f;
	public static boolean isFastRotation = false;

	public static float difficulty = 1f;
	public static float levelIncrement = 15f;
	public static float speed = 1f;
	public static float currentTime = 0f;

	/** sides */
	public static int sides = 6;
	public static int minSides = 5;
	public static int maxSides = 7;

	/**colors*/
	public static ArrayList<HColor> colors = new ArrayList<>();
	public static float colorPulse = 1.4f;
	public static int colorOffset = 0;
	public static float colorSwitch = 1f;
	public static HColor walls = new HColor(0, 0, 1, 1);

	/**3d settings */
	public static int layers = 6;
	public static float depth = 1.4f;
	public static float skew = 0f;
	public static float minSkew = 0f;
	public static float maxSkew = 1f;
	public static float skewTime = 5f;

	public static ArrayList<Wall> wallObjects = new ArrayList<>();

}
