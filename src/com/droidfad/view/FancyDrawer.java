/**
 * 
 */
package com.droidfad.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
Copyright 2014 Jens Glufke

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
public class FancyDrawer {

	Paint paint                = null;
	private int     bgColor    = Color.BLACK;
	private boolean drawBorder = true;
	
	public static interface IDrawer {
		void drawSuper(Canvas pCanvas);
		void setTextColor(int pColor);
		
		int getWidth();
		int getHeight();
	}
	
	public FancyDrawer() {
		
		bgColor = Color.BLACK;
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStrokeWidth(1);
	}
	
	public void draw(IDrawer pSuperDrawer, Canvas pCanvas) {
		if(        bgColor == DFColor.COLOR_BLUE
				|| bgColor == DFColor.COLOR_DK_BLUE) {
			pSuperDrawer.setTextColor(DFColor.COLOR_LT_GRAY);
		}

		pSuperDrawer.drawSuper(pCanvas);

		int width      = pSuperDrawer.getWidth();
		int height     = pSuperDrawer.getHeight();

		int   lAlphaStart  = 100;
		int   lAlphaVal    = lAlphaStart;
		int   lAlphaStep   = 10;
		int   lBorderWidth = lAlphaVal/lAlphaStep;
		/**
		 * upper border
		 */
		lAlphaVal = lAlphaStart;
		paint.setColor(Color.WHITE);
		for(int i=0; i<lBorderWidth; i++, lAlphaVal-=lAlphaStep) {
			paint.setAlpha((int) lAlphaVal);
			pCanvas.drawLine(i, i, width-i, i, paint);
		}

		/**
		 * upper right inner frame arc and lines 
		 */
		if(drawBorder) {
			lAlphaStep = drawBorder(pCanvas, lAlphaStart, width, height);
		}
		/**
		 * upper light reflection arc
		 */
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		paint.setAlpha(25);
		RectF lOval = new RectF(-width/3, -height/3, width+width/3, height/3);
		pCanvas.drawArc(lOval, 0, 180, true, paint);

		/**
		 * upper right and lower left stroke
		 */
		if(false) {
			paint.setColor(Color.WHITE);
			paint.setAlpha(30);
			paint.setStrokeWidth(3);
			int x = width-width/4;
			pCanvas.drawLine(x, 5, width-5, width-x, paint);
			int y = height-width/4;
			pCanvas.drawLine(5, y, height-y, height-5, paint);
		}
		/**
		 * lower boarder
		 */
		lAlphaVal  = 100;
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(1);
		for(int i=height; i>=height-lBorderWidth; i--, lAlphaVal-=lAlphaStep) {
			paint.setAlpha((int) lAlphaVal);
			pCanvas.drawLine(0, i, width, i, paint);
		}
	}

	/**
	 *
	 * @param pCanvas
	 * @param pAlphaStart
	 * @param width
	 * @param height
	 * @return
	 *
	 */
	private int drawBorder(Canvas pCanvas, int pAlphaStart, int width, int height) {

		int lAlphaVal;
		int lAlphaStep;
		paint.setColor(Color.LTGRAY);
		paint.setAlpha(pAlphaStart);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		final int lArcWidth    = 40;
		final int lDist2Border = 3;
		RectF lRect = new RectF(width-lArcWidth-lDist2Border, lDist2Border, width-lDist2Border, lArcWidth+lDist2Border);
		pCanvas.drawArc(lRect, -90, 90, false, paint);

		lAlphaVal  = pAlphaStart;
		lAlphaStep = width / 50;
		for(int i=width-lArcWidth/2-lDist2Border; i>=0; i-=lAlphaStep, lAlphaVal-=lAlphaStep) {
			if(lAlphaVal<=0) {
				break;
			}
			paint.setAlpha(lAlphaVal);
			pCanvas.drawLine(i, lDist2Border, i-lAlphaStep, lDist2Border, paint);
		}

		lAlphaVal=pAlphaStart;
		int x = width-lDist2Border;
		for(int i= lArcWidth/2+lDist2Border; i<=height; i+=lAlphaStep, lAlphaVal-=lAlphaStep) {
			if(lAlphaVal<=0) {
				break;
			}
			paint.setAlpha(lAlphaVal);
			pCanvas.drawLine(x, i, x, i+lAlphaStep, paint);
		}

		/**
		 * lower left inner frame arc and lines 
		 */
		paint.setAlpha(pAlphaStart);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		lRect = new RectF(lDist2Border, height-lArcWidth-lDist2Border, lDist2Border+lArcWidth, height-lDist2Border);
		pCanvas.drawArc(lRect, -180, -90, false, paint);

		lAlphaVal  = pAlphaStart;
		lAlphaStep = width / 50;
		for(int i=lDist2Border+lArcWidth/2; i<=width; i+=lAlphaStep, lAlphaVal-=lAlphaStep) {
			if(lAlphaVal<=0) {
				break;
			}
			paint.setAlpha(lAlphaVal);
			pCanvas.drawLine(i, height-lDist2Border, i+lAlphaStep, height-lDist2Border, paint);
		}

		lAlphaVal=pAlphaStart;
		x = width-lDist2Border;
		for(int i= height-lArcWidth/2-lDist2Border; i>=0; i-=lAlphaStep, lAlphaVal-=lAlphaStep) {
			if(lAlphaVal<=0) {
				break;
			}
			paint.setAlpha(lAlphaVal);
			pCanvas.drawLine(lDist2Border, i, lDist2Border, i-lAlphaStep, paint);
		}
		return lAlphaStep;
	}
	public void setDrawBorder(boolean pDrawBorder) {
		drawBorder = pDrawBorder;
	}

	public void setBgColor(int pBgColor) {
		bgColor = pBgColor;
	}
}
