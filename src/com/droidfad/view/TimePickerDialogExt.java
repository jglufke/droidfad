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
 *
 * @author John
 * copyright Jens Glufke, Germany mailto:jglufke@gmx.de
 *
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
