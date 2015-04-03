package net.wieku.jhexagon.api;

/**
 * @author Sebastian Krajewski on 21.03.15.
 */
public class HColor {

	float fr, fg, fb, fa;
	float pr, pg, pb, pa;
	boolean dynamic = false;
	public float r, g, b, a;

	int inc = 1;

	public HColor (float r, float g, float b, float a){
		this.r = fr = r;
		this.g = fg = g;
		this.b = fb = b;
		this.a = fa = a;
	}
	
	public HColor (float fr, float fg, float fb, float fa, float pr, float pg, float pb, float pa){
		this.fr = fr;
		this.fg = fg;
		this.fb = fb;
		this.fa = fa;
		this.pr = pr;
		this.pg = pg;
		this.pb = pb;
		this.pa = pa;
		dynamic = true;
	}
	
	float delta0;

	public void update (float delta){

		if(dynamic){

			inc = (delta0 == 0 ? 1 : (delta0 == CurrentMap.colorPulse ? -1 : inc));

			delta0 += delta * inc;
			delta0 = Math.min(CurrentMap.colorPulse, Math.max(delta0, 0));

			float percent = delta0 / CurrentMap.colorPulse;

			r = pr + percent * (pr - fr);
			g = pg + percent * (pg - fg);
			b = pb + percent * (pb - fb);
			a = pa + percent * (pa - fa);

			if (r < 0)
				r = 0;
			else if (r > 1) r = 1;
			
			if (g < 0)
				g = 0;
			else if (g > 1) g = 1;
			
			if (b < 0)
				b = 0;
			else if (b > 1) b = 1;
			
			if (a < 0)
				a = 0;
			else if (a > 1) a = 1;
			
		}

	}

}
