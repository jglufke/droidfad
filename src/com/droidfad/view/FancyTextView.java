/**
 * 
 */
package com.droidfad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.droidfad.view.FancyDrawer.IDrawer;

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
