/**
 * 
 */
package com.droidfad.view;

import android.graphics.Color;

/**
 *
 * @author John
 * copyright Jens Glufke, Germany mailto:jglufke@gmx.de
 *
 */
public class DFColor {

	public final static int hi=255, mi=150, lo=50;

	public static final int COLOR_LT_GREEN = Color.rgb(mi-50, hi-50, mi-50);
	public static final int COLOR_GREEN    = Color.rgb(lo, mi, lo);
	
	public static final int COLOR_BLUE     = Color.rgb(lo, lo, hi);
	public static final int COLOR_DK_BLUE  = Color.rgb(lo, lo, mi); 
	public static final int COLOR_LT_BLUE  = Color.rgb(mi, mi, hi);
	
	public static final int COLOR_RED      = Color.rgb(mi, lo, lo);
	public static final int COLOR_DK_RED   = Color.rgb(mi, 0, 0); 
	public static final int COLOR_LT_RED   = Color.rgb(hi, mi-50, mi-50);
	public static final int COLOR_LT_GRAY  = Color.LTGRAY;
	
	public static int changeColor(int pColor, int pDelta) {
		int r = Math.max(0, Math.min(255, Color.red(pColor)+pDelta));
		int g = Math.max(0, Math.min(255, Color.green(pColor)+pDelta));
		int b = Math.max(0, Math.min(255, Color.blue(pColor)+pDelta));
		return Color.rgb(r,g,b);
	}

}
