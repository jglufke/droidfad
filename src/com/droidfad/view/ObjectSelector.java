/**
 * 
 */
package com.droidfad.view;

import java.util.TreeSet;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.droidfad.data.ADao;
import com.droidfad.iframework.data.IData;
import com.droidfad.iframework.data.IDataUser;
import com.droidfad.iframework.service.IService;

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
public class ObjectSelector implements IDataUser {

	private Activity           activity;

	private IEditorListener    listener = null;
	private Class<? extends ADao> objectType;
	private ADao selectedObject;

	private static IData data = null;

	public ObjectSelector(){}
	
	/* (non-Javadoc)
	 * @see com.droidfad.iframework.service.IServiceUser#registerService(com.droidfad.iframework.service.IService)
	 */
	@Override
	public void registerService(IService pService) {
		if(pService instanceof IData) {
			data = (IData) pService;
		}
	}
	/**
	 * 
	 * create a new instance of ObjectSelector. The ObjectSelector instance
	 * is not displayed immedeately on the ui but just with calling 
	 * selectObject method
	 * @param pActivity will be displayed as contentView of this activity
	 * @param pObjectType the ADao subclass from which an instance should be selected
	 *
	 */
	public ObjectSelector(Activity pActivity, Class<? extends ADao> pObjectType) {
		if(pActivity == null) {
			throw new IllegalArgumentException("parameter pContext must not be null");
		}
		activity   = pActivity;
		objectType = pObjectType;
	}
	/**
	 * the ObjectSelector instance will be set as contentView of the activity
	 * that has been passed as parameter of the constructor. Take care that
	 * pListener sets the contentView with call of the callbacks back to 
	 * the original contentView 
	 *
	 * @param <T>
	 * @param pListener
	 *
	 */
	public <T extends ADao> void selectObject(IEditorListener pListener) {
		listener    = pListener;
		initGui();
	}

	/**
	 * ********************************************<br>
	 *
	 *
	 * ********************************************<br>
	 */
	private void initGui() {

		synchronized (ObjectSelector.class) {
			ScrollView   lScrollView = new ScrollView(activity);
			LinearLayout lLayout     = new LinearLayout(activity);
			lLayout.setOrientation(LinearLayout.VERTICAL);
			lScrollView.addView(lLayout, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			Vector<ADao> lInstanceList = data.getInstances(objectType);
			/**
			 * create a list sorted by name
			 */
			TreeSet<String> lNameSet = new TreeSet<String>();
			for(ADao lDao : lInstanceList) {
				lNameSet.add(lDao.getName());
			}
			for(String lName : lNameSet) {
				final TextView lTextView = new TextView(activity);
				lTextView.setBackgroundColor(Color.GRAY);
				lTextView.setTextColor(Color.BLACK);
				lTextView.setText(lName);
				lLayout.addView(lTextView, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				lTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View pV) {
						onClickImpl(pV, lTextView);
					}
				});
			}
			/**
			 * add ok and cancel button
			 */
			LinearLayout lButtonView = new LinearLayout(activity);
			lButtonView.setOrientation(LinearLayout.HORIZONTAL);
			Button lButton = new Button(activity);
			lButton.setOnClickListener(new OnClickListener() {
				public void onClick(View pV) {
					cancel();
				}
			});
			lButton.setText("CANCEL");
			lButtonView.addView(lButton, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			lLayout.addView(lButtonView, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			activity.setContentView(lScrollView);
		}
	}

	/**
	 * ********************************************<br>
	 *
	 * @param pV
	 *
	 * ********************************************<br>
	 * @param pTextView 
	 */
	protected void onClickImpl(View pV, TextView pTextView) {
		String lObjectName = pTextView.getText().toString();
		pTextView.setBackgroundColor(Color.GRAY);
		selectedObject     = data.getObject(objectType, lObjectName);
		listener.onEditorConfirmed();
	}

	/**
	 * ********************************************<br>
	 *
	 *
	 * ********************************************<br>
	 */
	protected void cancel() {
		selectedObject = null;
		if(listener != null) {
			listener.onEditorCancelled();
		}
	}
	/**
	 * ********************************************<br>
	 *
	 *
	 * ********************************************<br>
	 */
	protected void commit() {
		synchronized (ObjectSelector.class) {
			if(listener != null) {
				listener.onEditorConfirmed();
			}
		}
	}
	/**
	 * 
	 * return the object that has been selected  
	 * @param <T>
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public <T extends ADao> T getSelectedObject() {
		return (T) selectedObject;
	}	
}
