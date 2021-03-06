/**
 * 
 */
package com.droidfad.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

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
public class DateAndTimePickerDialog extends AlertDialog {

	public interface OnDateAndTimeSetListener {
		public void onDateAndTimeSet(int pYear, int pMonth, int pDay, int pHourOfDay, int pMinute);
	}
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	/**
	 *
	 * @param pKammEntGoActivity
	 * @param pDateSelectListener
	 * @param pYear
	 * @param pMonth
	 * @param pDate
	 *
	 */
	public DateAndTimePickerDialog(Context pContext,
			final OnDateAndTimeSetListener pListener, int pYear, int pMonth, int pDate) {

		super(pContext);

		setCancelable(true);
		setCanceledOnTouchOutside(true);

		LinearLayout lPanel = new LinearLayout(pContext);
		lPanel.setOrientation(LinearLayout.VERTICAL);
		lPanel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		datePicker = new DatePicker(pContext);
		lPanel.addView(datePicker);
		
		timePicker = new TimePicker(pContext);
		timePicker.setIs24HourView(true);
		lPanel.addView(timePicker);
		setView(lPanel);

		setButton(DialogInterface.BUTTON_POSITIVE, "OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface pDialog, int pWhich) {
				if(pListener != null) {
					pListener.onDateAndTimeSet(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
							timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				}
			}
		});
		setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface pDialog, int pWhich) {
			}
		});
	}
}
