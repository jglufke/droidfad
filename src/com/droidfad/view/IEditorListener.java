/**
 * 
 */
package com.droidfad.view;

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
 * a listener that will be notified as soon as ok or cancel has been selected
 * in an Editor instance. An instance of this interface has to take care that 
 * the original contentView is reset to the activity that has been passed
 * as parameter to {@link Editor} constructor.
 */
public interface IEditorListener {
		/**
		 * 
		 * the editing has been canceled. The necessary actions have to
		 * be taken. The edited object remains unchanged. Take care
		 * that the original contentView is reset to the activity that
		 * has been passed as parameter to the {@link Editor} constructor
		 *
		 */
		public void onEditorCancelled();
		/**
		 * 
		 * the editing has been confirmed with selecting ok. 
		 * The necessary actions have to be taken. The edited object 
		 * has been changed according to the configurations in the editor
		 * UI. Take care that the original contentView is reset to the 
		 * activity that has been passed as parameter to the 
		 * {@link Editor} constructor
		 *
		 */
		public void onEditorConfirmed();

}
