/**
 * 
 */
package com.droidfad.alarm;

import com.droidfad.data.SystemUtil;
import com.droidfad.util.LogWrapper;
import com.droidfad.util.SimpleFileLog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.PowerManager;

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
public abstract class AAlarmWorker extends BroadcastReceiver {

	protected final String  LOGTAG  = getClass().getSimpleName(); 
	protected SimpleFileLog fileLog = null;

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context pContext, Intent pIntent) {

		PowerManager          lPowerManager = (PowerManager) pContext.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock lWakeLock     = lPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		lWakeLock.acquire();

		try {
			execute(pContext, pIntent);
		} catch(Exception e) {
			LogWrapper.e(LOGTAG, "could not execute:" + LOGTAG, e);
			if(fileLog == null) {
				fileLog = new SimpleFileLog(
						SystemUtil.getApplicationStorageDir(new ContextWrapper(pContext)), 
						LOGTAG + ".excp.log", 10000, 2);
			}
			fileLog.info("could not execute:" + LOGTAG + ":" + e, e);
		} finally {
			lWakeLock.release();
		}
	}		
	/**
	 * method that is executed if alarm is received. Configuration passed with
	 * setAlarm method can be derived with pIntent.getExtras()
	 * @param pContext 
	 *
	 *
	 */
	protected abstract void execute(Context pContext, Intent pIntent);

	/**
	 * 
	 *
	 * @param pContext
	 * @param pAlarmId as defined in manifest for receiver, 
	 * e.g. "com.droidfad.gps.START_ALARM" in case of
	 *         <receiver android:name=".AlarmWorker" android:exported="true">
            		<intent-filter>
                 		<action android:name="com.droidfad.gps.START_ALARM" ></action>
            		</intent-filter>
        		</receiver>                 
	 * @param pConfiguration 
	 *
	 */
	public void setAlarm(Context pContext, String pAlarmId, long pTriggerAtMSec, long pIntervalMSec, Intent pConfiguration) {
		if(pContext == null) {
			throw new IllegalArgumentException("parameter pContext must not be null");
		}

		AlarmManager  lAlarmManager  = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE); 
		Intent        lAlarmIntent   = new Intent(pAlarmId);
		if(pConfiguration != null) {
			lAlarmIntent.putExtras(pConfiguration);
		}

		PendingIntent lPendingIntent = PendingIntent.getBroadcast(pContext, 0, lAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		lAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, pTriggerAtMSec, pIntervalMSec, lPendingIntent);
	}

	/**
	 * 
	 *
	 * @param pContext
	 *
	 */
	public void cancel(Context pContext, String pAlarmId) {
		if(pContext == null) {
			throw new IllegalArgumentException("parameter pContext must not be null");
		}
		AlarmManager  lAlarmManager  = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE); 
		Intent        lAlarmWorker   = new Intent(pAlarmId);
		PendingIntent lPendingIntent = PendingIntent.getBroadcast(pContext, 0, lAlarmWorker, PendingIntent.FLAG_UPDATE_CURRENT);
		lAlarmManager.cancel(lPendingIntent);
	}
}
