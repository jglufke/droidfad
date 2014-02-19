/**
 * 
 */
package com.droidfad.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.droidfad.data.ADao;
import com.droidfad.data.EditableByUI;
import com.droidfad.iframework.service.IService;
import com.droidfad.iframework.valuemapping.IValueMapperService;
import com.droidfad.iframework.valuemapping.IValueMapperUser;
import com.droidfad.util.ReflectionUtil;

/**
 * 
 *
 * Copyright 2011 Jens Glufke jglufke@googlemail.com

   Licensed under the DROIDFAD license (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.droidfad.com/html/license/license.htm

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.<br><br>
 * -----------------------------------------------------------------------<br><br>
 * class that can be used to edit instances of subclasses of ADao. see
 *  http://www.droidfad.com/html/ui/ui.htm for an usage example.
 *  Attributes of a ADao subclass instance that should be edited by Editor
 *  have to be indicated by the @EditableByUI annotation at the getter or
 *  setter method. E.g.<br>
 *	    @EditableByUI ( sortingTag="01", guiName="R.string.HostEntryGuiNameHost" )<br>
 *      public String getHostName()) { .....<br>
 * Editor tries to resolve the guiName parameter of the @EditableByUI annotation
 * with the resource class that has been passed with the constructor, e.g.<br>
 * 		final Editor          lEditor   = new Editor(hostEntry, R.string.class);<br>
 * If the resource class is null or the guiName can not be resolved the guiName
 * string itself will be used as attribute name.
 * see ({@link EditableByUI}) 
 */
public class Editor  {

	private static final String LOGTAG = Editor.class.getSimpleName();
	private Activity                   activity;
	private ADao                       object;
	private ReflectionUtil             reflectionHelper   = new ReflectionUtil();
	private static IValueMapperService valueMapper = null;
	private HashMap<String, EditText>  editorMap;
	private IEditorListener            listener = null;
	private Class<?>                   resourceClass;

	public static class RegisterService implements IValueMapperUser {
		@Override
		public synchronized void registerService(IService pService) {
			if(pService instanceof IValueMapperService) {
				valueMapper = (IValueMapperService) pService;
			}
		}
	}
	
	/**
	 * create an editor. 
	 *
	 * @param pActivity activity in which this Editor shall be displayed
	 * @param pResourceClass resource class that contains the 
	 *
	 */
	public Editor(Activity pActivity, Class<?> pResourceClass) {
		if(pActivity == null) {
			throw new IllegalArgumentException("parameter pContext must not be null");
		}
		activity      = pActivity;
		resourceClass = pResourceClass;
	}
	/**
	 * set the Editor as the contentView of the activity that has 
	 * been given as parameter of constructor. pObject can be edited.
	 * pListener will be notified after ok or cancel has been selected. pListener
	 * has to take care that the former contentView is reset as 
	 * contentView for the activity.<br>
	 * The values of object are stored in Hashmap. After selecting cancel
	 * the object remains unchanged. If ok is selected the changes will
	 * be stored to the object.
	 *
	 * @param <T>
	 * @param pObject
	 * @param pListener
	 *
	 */
	public <T extends ADao> void edit(T pObject, IEditorListener pListener) {
		if(pObject == null) {
			throw new IllegalArgumentException("parameter pContext must not be null");
		}

		object      = pObject;
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

		synchronized (Editor.class) {
			ScrollView   lScrollView = new ScrollView(activity);
			LinearLayout lLayout     = new LinearLayout(activity);
			lLayout.setOrientation(LinearLayout.VERTICAL);
			lScrollView.addView(lLayout, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			Class<? extends ADao> lClazz = object.getClass();
			editorMap                       = new HashMap<String, EditText>();
			List<String> lAttributeNameList = reflectionHelper.getEditableByUIAttributeNames(lClazz, null); 
			for(String lAttributeName : lAttributeNameList) {
				TextView lTextView = new TextView(activity);
				lTextView.setText(getGuiName(lTextView, lClazz, lAttributeName));
				lLayout.addView(lTextView, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				Object   lValue         = reflectionHelper.invokeGetter(object, lAttributeName, null); 
				Class<?> lAttributeType = reflectionHelper.getAttributeType(lClazz, lAttributeName, null);
				String   lValueString   = valueMapper.mapValue2String(lAttributeType, lValue);
				if(lValueString == null) {
					lValueString = "";
				}
				EditText lEditText  = new EditText(activity);
				editorMap.put(lAttributeName, lEditText);
				lEditText.setText(lValueString);
				if(Number.class.isAssignableFrom(lAttributeType)) {
					lEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
				}
				lLayout.addView(lEditText, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
			}
			/**
			 * add a separator
			 */
			lLayout.addView(new TextView(activity));
			/**
			 * add ok and cancel button
			 */
			LinearLayout lButtonView = new LinearLayout(activity);
			lButtonView.setOrientation(LinearLayout.HORIZONTAL);
			Button lButton = new Button(activity);
			lButton.setOnClickListener(new OnClickListener() {
				public void onClick(View pV) {
					commit();
				}
			});
			
			lButton.setText("OK");
			lButtonView.addView(lButton);

			lButton = new Button(activity);
			lButton.setOnClickListener(new OnClickListener() {
				public void onClick(View pV) {
					cancel();
				}
			});

			lButton.setText("CANCEL");
			lButtonView.addView(lButton);

			lLayout.addView(lButtonView, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			activity.setContentView(lScrollView);
		}
	}

	/**
	 * ********************************************<br>
	 * @param pView 
	 * @param pClazz 
	 *
	 * @param pAttributeName
	 * @return
	 *
	 * ********************************************<br>
	 */
	private CharSequence getGuiName(TextView pView, Class<? extends ADao> pClazz, String pAttributeName) {
		String lGuiName = pAttributeName;
		/**
		 * first, get the EditableByUI annotation.
		 */
		Method       lGetter     = reflectionHelper.getGetter(pClazz, pAttributeName, null);
		EditableByUI lAnnotation = lGetter.getAnnotation(EditableByUI.class);
		if(lAnnotation == null) {
			Method lSetter = reflectionHelper.getSetter(pClazz, pAttributeName, null);
			lAnnotation    = lSetter.getAnnotation(EditableByUI.class);
		}
		String lAnnotationGuiName = lAnnotation.guiName();
		if(lAnnotationGuiName != null && !"".equals(lAnnotationGuiName.trim()) && 
				resourceClass != null) {
			/**
			 * try to resolve a string resource id
			 */
			int lIndex = lAnnotationGuiName.lastIndexOf('.');
			if(lIndex != -1) {
				String lFieldname = lAnnotationGuiName.substring(lIndex+1);
				try {
					Field lField  = resourceClass.getDeclaredField(lFieldname);
					int   lResId  = lField.getInt(null);
					
					pView.setText(lResId);
					lGuiName      = pView.getText().toString();
					
				} catch(Exception e) {
					/**
					 * not a nice implementation, but do nothing if anything of the
					 * above implementation fails
					 */
				}
			}
		}
		return lGuiName;
	}
	/**
	 * ********************************************<br>
	 *
	 *
	 * ********************************************<br>
	 */
	protected void cancel() {
		reset();
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
		synchronized (Editor.class) {
			/**
			 * editorMap can be null if 2 onClick events are in the 
			 * queue. the first will delete editorMap with reset method.
			 * The second might run in this method again and will try 
			 * to dereference editorMap
			 */
			if(editorMap != null) {
				for(Entry<String, EditText> lEntry : editorMap.entrySet()) {
					String   lAttributeName = lEntry.getKey();
					EditText lEditText      = lEntry.getValue();
					String   lValueString   = new StringBuilder(lEditText.getEditableText()).toString();
					Class<?> lAttributeType = reflectionHelper.getAttributeType(object.getClass(), lAttributeName, null); 
					Object   lValue         = valueMapper.mapString2Value(lAttributeType, lValueString);

					reflectionHelper.invokeSetter(object, lAttributeName, null, lValue);
				}
				reset();
				if(listener != null) {
					listener.onEditorConfirmed();
				}
			}
		}
	}
	private void reset() {
		synchronized (Editor.class) {
			editorMap = null;
		}
	}
	/**
	 * return the object 
	 *
	 * @return
	 *
	 */
	public ADao getObject() {
		return object;
	}	
}
