/**
 * 
 */
package com.droidfad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.Button;
import android.widget.TextView;

import com.droidfad.view.FancyDrawer.IDrawer;

/**
 *
 * @author John
 * copyright Jens Glufke, Germany mailto:jglufke@gmx.de
 *
 */
public class FancyTextView extends TextView implements IDrawer {

	private FancyDrawer fancyDrawer;
	/**
	 *
	 * @param pContext
	 *
	 */
	public FancyTextView(Context pContext) {
		super(pContext);
		fancyDrawer = new FancyDrawer();
	}

	/* (non-Javadoc)
	 * @see android.view.View#setBackgroundColor(int)
	 */
	@Override
	public void setBackgroundColor(int pColor) {
		fancyDrawer.setBgColor(pColor);
		super.setBackgroundColor(pColor);
	}

	/* (non-Javadoc)
	 * @see android.view.View#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas pCanvas) {
		fancyDrawer.draw(this, pCanvas);
	}

	/**
	 *
	 * @param pB
	 *
	 */
	public void setDrawBorder(boolean pB) {
		fancyDrawer.setDrawBorder(pB);
	}

	/* (non-Javadoc)
	 * @see com.droidfad.view.FancyDrawer.IDrawer#drawSuper(android.graphics.Canvas)
	 */
	@Override
	public void drawSuper(Canvas pCanvas) {
		super.draw(pCanvas);
	}
}
