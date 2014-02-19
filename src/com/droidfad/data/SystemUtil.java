/**
 * 
 */
package com.droidfad.data;

import java.io.File;

import android.content.ContextWrapper;
import android.os.Environment;

/**
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
   limitations under the License.<br>
 * -----------------------------------------------------------------------<br><br>
 *
 */
public class SystemUtil {

	public static boolean isSdCardWritable() {
		String lStorageState = Environment.getExternalStorageState();
		boolean lIsSdcardWritable;
		boolean lIsSdCardAvailable;

		if (Environment.MEDIA_MOUNTED.equals(lStorageState)) {
			lIsSdCardAvailable = lIsSdcardWritable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(lStorageState)) {
			lIsSdCardAvailable = true;
			lIsSdcardWritable = false;
		} else {
			lIsSdCardAvailable = lIsSdcardWritable = false;
		}
		return lIsSdCardAvailable && lIsSdcardWritable;
	}
	/**
	 *
	 *
	 */
	public static File getApplicationStorageDir(ContextWrapper pContextWrapper) {
		/**
		 * check if the external (sd card) memory is available.
		 * If yes, open the calendar file there. Otherwise, use
		 * internal memory
		 */
		File lApplicationDir = null;
		if(isSdCardWritable()) {
			lApplicationDir = pContextWrapper.getExternalFilesDir(null);
		} else {
			lApplicationDir = pContextWrapper.getDir("data", ContextWrapper.MODE_WORLD_WRITEABLE);
		}
		return lApplicationDir;
	}
}
