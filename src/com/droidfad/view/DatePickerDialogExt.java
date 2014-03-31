/**
 * 
 */
package com.droidfad.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

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
public class DatePickerDialogExt extends AlertDialog {

	private DatePicker datePicker;
	/**
	 *
	 * @param pKammEntGoActivity
	 * @param pDateSelectListener
	 * @param pYear
	 * @param pMonth
	 * @param pDate
	 *
	 */
	public DatePickerDialogExt(Context pContext,
			final OnDateSetListener pListener, int pYear, int pMonth, int pDate) {

		super(pContext);

		setCancelable(true);
		setCanceledOnTouchOutside(true);

		datePicker = new DatePicker(pContext);
		setView(datePicker);

		setButton(DialogInterface.BUTTON_POSITIVE, "OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface pDialog, int pWhich) {
				if(pListener != null) {
					pListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
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
