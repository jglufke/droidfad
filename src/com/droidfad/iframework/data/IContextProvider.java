/**
 * 
 */
package com.droidfad.iframework.data;

import android.app.Activity;
import android.content.Context;

import com.droidfad.iframework.service.IService;

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
 * ---------------------------------------------------------------------
 * activity is used to get the activity of an android application.
 * It only returns the correct value if DROIDFAD.init(activity) has 
 * been called before {@link DroidFad}
 *
 */
public interface IContextProvider extends IService {

	/**
	 * get the activity of the application
	 *
	 * @return
	 *
	 */
	public Context getContext();
}
