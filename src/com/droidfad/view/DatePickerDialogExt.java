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
 *
 * @author John
 * copyright Jens Glufke, Germany mailto:jglufke@gmx.de
 *
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
