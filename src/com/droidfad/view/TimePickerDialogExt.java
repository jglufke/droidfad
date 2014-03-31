/**
 * 
 */
package com.droidfad.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
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
public class TimePickerDialogExt extends AlertDialog {

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
	public TimePickerDialogExt(Context pContext, final OnTimeSetListener pListener) {

		super(pContext);

		setCancelable(true);
		setCanceledOnTouchOutside(true);

		timePicker = new TimePicker(pContext);
		timePicker.setIs24HourView(true);
		setView(timePicker);
		
		setButton(DialogInterface.BUTTON_POSITIVE, "OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface pDialog, int pWhich) {
				if(pListener != null) {
					pListener.onTimeSet(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
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
