package net.wieku.jhexagon.api;

import java.util.Random;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public abstract class Patterns {

	static Random random = new Random();
	static public int random (int start, int end) {
		return start + random.nextInt(end - start + 1);
	}
	//getRandomDir: returns either 1 or -1

	static float THICKNESS = 40f;

	public static int getRandomDir(){
		if (random(0, 100) > 50) return 1;
			return -1;
	}

	//getHalfSides: returns half the number of sides (integer)
	public static int getHalfSides() { return (int) Math.ceil(CurrentMap.sides / 2); }

	//getRandomSide: returns random mSide
	public static int getRandomSide() { return random(0, CurrentMap.sides - 1); }

	//getPerfectDelayDM: returns getPerfectDelay calculated with difficulty mutliplier
	public static float getPerfectDelay(float mThickness) { return mThickness / (5.02f * CurrentMap.speed) * 1.1f/*u_getDelayMultDM()*/; }

	//getPerfectDelayDM: returns getPerfectDelay calculated with difficulty mutliplier
	public static float getPerfectDelayDM(float mThickness) { return mThickness / (5.02f * CurrentMap.speed) * 1.1f/*u_getDelayMultDM()*/; }

	//getPerfectThickness: returns a good THICKNESS value in relation to human reflexes
	public static float getPerfectThickness(float mThickness) { return mThickness * CurrentMap.speed; }

	//getSideDistance: returns shortest distance from a side to another
	public static int getSideDistance(int mSide1, int mSide2){
		int start = mSide1;
		int rightSteps = 0;
		while (start != mSide2) {
			rightSteps = rightSteps + 1;
			start = start + 1;
			if (start > CurrentMap.sides - 1 ){ start = 0;}
		}

		start = mSide1;
		int leftSteps = 0;
		while (start != mSide2) {
			leftSteps = leftSteps + 1;
			start = start - 1;
			if (start < 0 ) { start = CurrentMap.sides - 1; }
		}

		if (rightSteps < leftSteps) { return rightSteps; }
		return leftSteps;
	}

	//cWall: creates a wall with the common THICKNESS
	public static void cWall(int mSide){ CurrentMap.wallTimeline.submit(new Wall(mSide, THICKNESS, 1f)); }

	//oWall: creates a wall opposite to the mSide passed
	public static void oWall(int mSide){ cWall(mSide + getHalfSides()); }

	//rWall: union of cwall and owall (created 2 walls facing each other)
	public static void rWall(int mSide) {
		cWall(mSide);
		oWall(mSide);
	}

	//cWallEx: creates a wall with mExtra walls attached to it
	public static void cWallEx(int mSide, int mExtra) {
		cWall(mSide);
		int loopDir = 1;

		if (mExtra < 0) { loopDir = -1; }
		for (int i = 0; i < mExtra; i+=loopDir) { cWall(mSide + i); }
	}

	//oWallEx: creates a wall with mExtra walls opposite to mSide
	public static void oWallEx(int mSide, int mExtra) {
		cWallEx(mSide + getHalfSides(), mExtra);
	}

	//rWallEx: union of cwallex and owallex
	public static void  rWallEx(int mSide, int mExtra) {
		cWallEx(mSide, mExtra);
		oWallEx(mSide, mExtra);
	}

	//cBarrageN: spawns a barrage of walls, with a free mSide plus mNeighbors
	public static void cBarrageN(int mSide, int mNeighbors) {
		for (int i = mNeighbors; i <= CurrentMap.sides - 2 - mNeighbors; ++i) {
			cWall(mSide + i + 1);
		}
	}

	//cBarrage: spawns a barrage of walls, with a single free mSide
	public static void  cBarrage(int mSide) { cBarrageN(mSide, 0); }

	//cBarrageOnlyN: spawns a barrage of wall, with only free mNeighbors
	public static void  cBarrageOnlyN(int mSide, int mNeighbors){
		cWall(mSide);
		cBarrageN(mSide, mNeighbors);
	}

	//cAltBarrage: spawns a barrage of alternate walls
	public static void  cAltBarrage(int mSide, int mStep) {
		for (int i = 0; i <= CurrentMap.sides / mStep; ++i ) {
			cWall(mSide + i * mStep);
		}
	}
	
	public static void  pAltBarrage(int mTimes, int mStep) {
		float delay = getPerfectDelayDM(THICKNESS) * 5.6f;
		for (int i = 0; i<= mTimes; ++i) {
			cAltBarrage(i, mStep);
			t_wait(delay);
		}

		t_wait(delay);
	}
	
	//pMirrorSpiral: spawns a spiral of rWallEx
	public static void  pMirrorSpiral(int mTimes, int mExtra) {
		float oldThickness = THICKNESS;
		THICKNESS = getPerfectThickness(THICKNESS);
		float delay = getPerfectDelay(THICKNESS);
		int startSide = getRandomSide();
		int loopDir = getRandomDir();
		int j = 0;

		for (int i = 0; i <= mTimes; ++i ) {
			rWallEx(startSide + j, mExtra);
			j = j + loopDir;
			t_wait(delay);
		}

		THICKNESS = oldThickness;

		t_wait(getPerfectDelayDM(THICKNESS) * 6.5f);
	}
	
	//pMirrorSpiralDouble: spawns a spiral of rWallEx where you need to change direction
	public static void  pMirrorSpiralDouble(int mTimes, int mExtra) {
		float oldThickness = THICKNESS;
		THICKNESS = getPerfectThickness(THICKNESS);
		float delay = getPerfectDelayDM(THICKNESS);
		int startSide = getRandomSide();
		int currentSide = startSide;
		int loopDir = getRandomDir();
		int j = 0;

		for (int i = 0; i <= mTimes; ++i) {
			rWallEx(startSide + j, mExtra);
			j = j + loopDir;
			t_wait(delay);
		}
	
		rWallEx(startSide + j, mExtra);
		t_wait(delay * 0.9f);

		rWallEx(startSide + j, mExtra);
		t_wait(delay * 0.9f);

		loopDir *= -1;

		for (int i = 0; i <= mTimes + 1; ++i) {
			currentSide = currentSide + loopDir;
			rWallEx(startSide + j, mExtra);
			j = j + loopDir;
			t_wait(delay);
		}

		THICKNESS = oldThickness;
		t_wait(getPerfectDelayDM(THICKNESS) * 7.5f);
	}
	
	//pBarrageSpiral: spawns a spiral of cBarrage
	public static void  pBarrageSpiral(int mTimes, float mDelayMult, int mStep) {
		float delay = getPerfectDelayDM(THICKNESS) * 5.6f * mDelayMult;
		int startSide = getRandomSide();
		int loopDir = mStep * getRandomDir();
		int j = 0;

		for (int i = 0; i <= mTimes; ++i) {
			cBarrage(startSide + j);
			j = j + loopDir;
			t_wait(delay);
			if(CurrentMap.sides < 6) { t_wait(delay * 0.6f); }
		}

		t_wait(getPerfectDelayDM(THICKNESS) * 6.1f);
	}
	
	//pDMBarrageSpiral: spawns a spiral of cBarrage, with static delay
	public static void  pDMBarrageSpiral(int mTimes, float mDelayMult, int mStep) {
		float delay = (getPerfectDelayDM(THICKNESS) * 5.42f) * (mDelayMult / (float) Math.pow(CurrentMap.difficulty, 0.4)) * (float)Math.pow(CurrentMap.speed, 0.35);
		int  startSide = getRandomSide();
		int loopDir = mStep * getRandomDir();
		int j = 0;

		for (int i = 0; i <= mTimes; ++i) {
			cBarrage(startSide + j);
			j = j + loopDir;
			t_wait(delay);
			if(CurrentMap.sides < 6) { t_wait(delay * 0.49f); }
		}

		t_wait(getPerfectDelayDM(THICKNESS) * (6.7f * (float)Math.pow(CurrentMap.difficulty, 0.7)));
	}
	
	//pWallExVortex: spawns left-left right-right spiral patters
	public static void  pWallExVortex(int mTimes, int  mStep, int mExtraMult) {
		float delay = getPerfectDelayDM(THICKNESS) * 5.0f;
		int startSide = getRandomSide();
		int loopDir = getRandomDir();
		int currentSide = startSide;

		for (int j = 0; j <= mTimes; ++j) {
			for (int i = 0; i <= mStep; ++i) {
				currentSide = currentSide + loopDir;
				rWallEx(currentSide, loopDir * mExtraMult);
				t_wait(delay);
			}

			loopDir = loopDir * -1;

			for (int i = 0; i <= mStep + 1; ++i) {
				currentSide = currentSide + loopDir;
				rWallEx(currentSide, loopDir * mExtraMult);
				t_wait(delay);
			}
		}

		t_wait(getPerfectDelayDM(THICKNESS) * 5.5f);
	}
	
	//pInverseBarrage: spawns two barrages who force you to turn 180 degrees
	public static void  pInverseBarrage(int mTimes) {
		float delay = getPerfectDelayDM(THICKNESS) * 9.9f;
		int startSide = getRandomSide();

		for (int i = 0; i<= mTimes; ++i) {
			cBarrage(startSide);
			t_wait(delay);
			if(CurrentMap.sides < 6) { t_wait(delay * 0.8f); }
			cBarrage(startSide + getHalfSides());
			t_wait(delay);
		}

		t_wait(getPerfectDelayDM(THICKNESS) * 2.5f);
	}
	
	//pRandomBarrage: spawns barrages with random side, and waits humanly-possible times dep}ing on the sides distance
	public static void  pRandomBarrage(int mTimes, float mDelayMult) {
		int side = getRandomSide();
		int oldSide = 0;

		for (int i = 0; i < mTimes; ++i) {
			cBarrage(side);
			oldSide = side;
			side = getRandomSide();
			t_wait(getPerfectDelayDM(THICKNESS) * (2 + (getSideDistance(side, oldSide) * mDelayMult)));
		}

		t_wait(getPerfectDelayDM(THICKNESS) * 5.6f);
	}
	
	//pMirrorWallStrip: spawns rWalls close to one another on the same side
	public static void  pMirrorWallStrip(int mTimes, int mExtra) {
		float delay = getPerfectDelayDM(THICKNESS) * 3.65f;
		int startSide = getRandomSide();

		for (int i = 0; i < mTimes; ++i) {
			rWallEx(startSide, mExtra);
			t_wait(delay);
		}

		t_wait(getPerfectDelayDM(THICKNESS) * 5.00f);
	}
	
	//pTunnel: forces you to circle around a very thick wall
	public static void pTunnel(int mTimes){
		float oldThickness = THICKNESS;
		float myThickness = getPerfectThickness(THICKNESS);
		float delay = getPerfectDelay(myThickness) * 4;
		int startSide = getRandomSide();
		int loopDir = getRandomDir();

		THICKNESS = myThickness;

		for (int i = 0; i < mTimes; ++i) {
			CurrentMap.wallTimeline.submit(new Wall(startSide, myThickness + 5 * CurrentMap.speed * delay, 1f));

			cBarrage(startSide + loopDir);
			t_wait(delay);
			loopDir = loopDir * -1;
		}
			
		THICKNESS = oldThickness;
		t_wait(delay);
	}

	public static void t_wait(float delay){
		//System.out.println(delay / 60);
		CurrentMap.wallTimeline.wait(delay / 60);
	}

}
