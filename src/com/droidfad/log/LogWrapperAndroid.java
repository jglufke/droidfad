/**
 * 
 */
package com.droidfad.log;

import com.droidfad.util.SimpleFileLog;

import android.util.Log;

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
   limitations under the License.
 * -----------------------------------------------------------------------
 *
 */
public class LogWrapperAndroid implements ILog {
	
	/* (non-Javadoc)
	 * @see com.droidfad.log.ILog#e(java.lang.String, java.lang.String)
	 */
	@Override
	public void e(String pLogTag, String pMessage) {
		Log.e(pLogTag, pMessage);
	}
	/*
	 * (non-Javadoc)
	 * @see com.droidfad.log.ILog#e(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void e(String pLogTag, String pMessage, Throwable pThrowable) {
		Log.e(pLogTag, pMessage, pThrowable);
	}

	/* (non-Javadoc)
	 * @see com.droidfad.log.ILog#w(java.lang.String, java.lang.String)
	 */
	@Override
	public void w(String pLogTag, String pMessage) {
		Log.w(pLogTag, pMessage);
	}
	/*
	 * (non-Javadoc)
	 * @see com.droidfad.log.ILog#w(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void w(String pLogTag, String pMessage, Throwable pThrowable) {
		Log.w(pLogTag, pMessage, pThrowable);
	}

	/* (non-Javadoc)
	 * @see com.droidfad.log.ILog#i(java.lang.String, java.lang.String)
	 */
	@Override
	public void i(String pLogTag, String pMessage) {
		Log.i(pLogTag, pMessage);
	}
	/*
	 * (non-Javadoc)
	 * @see com.droidfad.log.ILog#i(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void i(String pLogTag, String pMessage, Throwable pThrowable) {
		Log.i(pLogTag, pMessage, pThrowable);
	}

	/* (non-Javadoc)
	 * @see com.droidfad.log.ILog#d(java.lang.String, java.lang.String)
	 */
	@Override
	public void d(String pLogTag, String pMessage) {
		Log.d(pLogTag, pMessage);
	}
	/*
	 * (non-Javadoc)
	 * @see com.droidfad.log.ILog#d(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void d(String pLogTag, String pMessage, Throwable pThrowable) {
		Log.d(pLogTag, pMessage, pThrowable);
	}
}
