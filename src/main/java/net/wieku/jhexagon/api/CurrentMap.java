package net.wieku.jhexagon.api;

import net.wieku.jhexagon.engine.timeline.Timeline;
import net.wieku.jhexagon.engine.timeline.TimelineRunnable;

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
	public static float delayMult = 1f;
	public static float speed = 2.65f ;
	public static float currentTime = 0f;

	/** sides */
	public static int sides = 6;
	public static int minSides = 5;
	public static int maxSides = 7;
	public static boolean mustChangeSides = false;

	/**pulse*/
	public static float beatPulseMin = 1.0f;
	public static float beatPulseMax = 1.2f;
	public static float beatPulseDelay = 0.5f;
	public static float beatPulse = 1.0f;

	/**wallpulse*/
	public static float pulseMin = 70;
	public static float pulseMax = 90;
	public static float pulseSpeed = 1.0f;
	public static float pulseSpeedR = 0.6f;
	public static float pulseDelayMax = 0;
	public static float pulse = 90;
	public static int pulseDir = 1;

	/**colors*/
	public static ArrayList<HColor> colors = new ArrayList<>();
	public static float menuColor = 0f;
	public static float colorPulse = 3f;
	public static int colorOffset = 0;
	public static float colorSwitch = 1f;
	public static HColor walls = new HColor(0, 0, 1, 1);

	/**gfx settings */
	public static int layers = 6;
	public static float depth = 1.6f;
	public static float skew = 0f;
	public static float minSkew = 0f;
	public static float maxSkew = 1f;
	public static float skewTime = 5f;
	public static float wallSkewLeft = 0f;
	public static float wallSkewRight = 0f;

	public static Timeline<Wall> wallTimeline = new Timeline<>();
	public static Timeline<TimelineRunnable> eventTimeline = new Timeline<>();


	public static void reset(){
		wallTimeline.reset();
		eventTimeline.reset();
		resetValues();
	}

	private static void resetValues(){
		rotationSpeed = 0.25f;
		rotationSpeedMax = 0.5f;
		rotationIncrement = 0.01f;
		fastRotate = 70f;
		isFastRotation = false;
		
		difficulty = 1f;
		levelIncrement = 15f;
		delayMult = 1f;
		speed = 1f ;
		currentTime = 0f;
		
		/** sides */
		sides = 6;
		minSides = 5;
		maxSides = 7;
		mustChangeSides = false;

		/**pulse*/
		beatPulseMin = 1.0f;
		beatPulseMax = 1.2f;
		beatPulseDelay = 0.5f;
		beatPulse = 1.0f;

		/**wallpulse*/
		pulseMin = 70;
		pulseMax = 90;
		pulseSpeed = 1.0f;
		pulseSpeedR = 0.6f;
		pulseDelayMax = 0;
		pulse = 90;
		pulse = pulseMax;
		pulseDir = 1;

		/**colors*/
		colors.clear();
		colorPulse = 3f;
		colorOffset = 0;
		colorSwitch = 1f;
		walls = new HColor(0, 0, 1, 1);
		
		/**gfx settings */
		layers = 6;
		depth = 1.6f;
		skew = 0f;
		minSkew = 0f;
		maxSkew = 1f;
		skewTime = 5f;
		wallSkewLeft = 0f;
		wallSkewRight = 0f;
	}
	
	
	public static class TextInfo {
		public String text;
		public float duration;
		public boolean visible = false;
		public TextInfo(String text, float duration) {
			this.text = text;
			this.duration = duration;
		}
	}

	public static TextInfo currentText = null;

	public static void pushText(String text, float duration){
		CurrentMap.currentText = new TextInfo(text, duration);
	}

	public static void setRotationSpeed(float rotationSpeed) {
		CurrentMap.rotationSpeed = rotationSpeed;
	}

	public static void setRotationSpeedMax(float rotationSpeedMax) {
		CurrentMap.rotationSpeedMax = rotationSpeedMax;
	}

	public static void setRotationIncrement(float rotationIncrement) {
		CurrentMap.rotationIncrement = rotationIncrement;
	}

	public static void setFastRotate(float fastRotate) {
		CurrentMap.fastRotate = fastRotate;
	}

	public static void setIsFastRotation(boolean isFastRotation) {
		CurrentMap.isFastRotation = isFastRotation;
	}

	public static void setDifficulty(float difficulty) {
		CurrentMap.difficulty = difficulty;
	}

	public static void setLevelIncrement(float levelIncrement) {
		CurrentMap.levelIncrement = levelIncrement;
	}

	public static void setDelayMult(float delay) {
		CurrentMap.delayMult = delay;
	}

	public static void setSpeed(float speed) {
		CurrentMap.speed = speed;
	}

	public static void setCurrentTime(float currentTime) {
		CurrentMap.currentTime = currentTime;
	}

	public static void setSides(int sides) {
		CurrentMap.sides = sides;
	}

	public static void setMinSides(int minSides) {
		CurrentMap.minSides = minSides;
	}

	public static void setMaxSides(int maxSides) {
		CurrentMap.maxSides = maxSides;
	}

	public static void setMustChangeSides(boolean mustChangeSides) {
		CurrentMap.mustChangeSides = mustChangeSides;
	}

	public static void setBeatPulseMin(float beatPulseMin) {
		CurrentMap.beatPulseMin = beatPulseMin;
	}

	public static void setBeatPulseMax(float beatPulseMax) {
		CurrentMap.beatPulseMax = beatPulseMax;
	}

	public static void setBeatPulseDelay(float beatPulseDelay) {
		CurrentMap.beatPulseDelay = beatPulseDelay;
	}

	public static void setPulseMin(float pulseMin) {
		CurrentMap.pulseMin = pulseMin;
	}

	public static void setPulseMax(float pulseMax) {
		CurrentMap.pulseMax = pulseMax;
		CurrentMap.pulse = pulseMax;
		CurrentMap.pulseDir = 1;
	}

	public static void setPulseSpeed(float pulseSpeed) {
		CurrentMap.pulseSpeed = pulseSpeed;
	}

	public static void setPulseSpeedR(float pulseSpeedR) {
		CurrentMap.pulseSpeedR = pulseSpeedR;
	}

	public static void setPulseDelayMax(float pulseDelayMax) {
		CurrentMap.pulseDelayMax = pulseDelayMax;
	}

	public static void setColors(ArrayList<HColor> colors) {
		CurrentMap.colors = colors;
	}

	public static void setColorPulse(float colorPulse) {
		CurrentMap.colorPulse = colorPulse;
	}

	public static void setColorOffset(int colorOffset) {
		CurrentMap.colorOffset = colorOffset;
	}

	public static void setColorSwitch(float colorSwitch) {
		CurrentMap.colorSwitch = colorSwitch;
	}

	public static void setWalls(HColor walls) {
		CurrentMap.walls = walls;
	}

	public static void setLayers(int layers) {
		CurrentMap.layers = layers;
	}

	public static void setDepth(float depth) {
		CurrentMap.depth = depth;
	}

	public static void setSkew(float skew) {
		CurrentMap.skew = skew;
	}

	public static void setMinSkew(float minSkew) {
		CurrentMap.minSkew = minSkew;
	}

	public static void setMaxSkew(float maxSkew) {
		CurrentMap.maxSkew = maxSkew;
	}

	public static void setSkewTime(float skewTime) {
		CurrentMap.skewTime = skewTime;
	}

	public static void setWallSkewLeft(float wallSkewLeft) {
		CurrentMap.wallSkewLeft = wallSkewLeft;
	}

	public static void setWallSkewRight(float wallSkewRight) {
		CurrentMap.wallSkewRight = wallSkewRight;
	}

}
