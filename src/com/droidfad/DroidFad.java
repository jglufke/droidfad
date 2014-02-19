/**
 * 
 */
package com.droidfad;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.droidfad.classloading.AndroidClazzFinder;
import com.droidfad.classloading.ClazzFinder;
import com.droidfad.iframework.data.IContextProvider;
import com.droidfad.iframework.data.IData;
import com.droidfad.iframework.data.IDataUser;
import com.droidfad.iframework.service.IService;
import com.droidfad.log.ILog;
import com.droidfad.log.LogWrapperAndroid;
import com.droidfad.persistency.Persistency;
import com.droidfad.service.ServiceAdministrator;
import com.droidfad.util.LogWrapper;

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
   limitations under the License.<br>
 * -----------------------------------------------------------------------<br>
 * this class has to be called with initialization of the DroidFad framework.
 * see http://www.droidfad.com/ for details. It is possible to change the
 * directory where the persistency has to be located if you use the appropriate
 * init method
 */
public class DroidFad implements IContextProvider {

	/**
	 * enum that defines if internal or external memory should be used
	 * for persistency
	 */
	public enum MemoryType { internal, external }  

	/**
	 * a wrapper class to access class Persistency directly
	 */
	static class PersistencyWrapper extends Persistency {
		static void setPersistencyPath(String pPath) {
			File lRootPath = new File(pPath);
			if(lRootPath.isFile()) {
				throw new IllegalArgumentException("persistency root path must not be an existing file:" + pPath);
			}
			Persistency.setRootDir(pPath);
		}
	}
	/**
	 * helper class to register services at this
	 *
	 * @author base
	 * copyright Jens Glufke, Germany mailto:jglufke@gmx.de
	 *
	 */
	public static class RegisterService implements IDataUser {

		/* (non-Javadoc)
		 * @see com.yaffa.iframework.service.IServiceUser#registerService(com.yaffa.iframework.service.IService)
		 */
		@Override
		public synchronized void registerService(IService pService) {
			if(pService instanceof IData) {
				data = (IData) pService;
			}
		}
	}

	private static final String LOGTAG = DroidFad.class.getSimpleName();

	private volatile static AndroidClazzFinder androidClazzFinder = null;
	private volatile static ILog               logger             = null;

	private static ServiceAdministrator serviceAdministrator;
	private static Context  context;
	private static IData    data = null;
	private static UncaughtExceptionHandler uncaughtExceptionHandler;

	/**
	 * this class also implements the service IContextProvider.
	 * Thus, it has to implement the default constructor as every service
	 */
	public DroidFad() {}
	/**
	 * initialize the framework. Very first method of the framework
	 * that has to be called at all. Internal memory is used for persistency
	 *
	 * @param pContext
	 *
	 */
	public synchronized static void init(Context pContext) {

		init(pContext, MemoryType.internal);

	}
	/**
	 * initialize the framework. Very first method of the framework
	 * that has to be called at all
	 *
	 * @param pContext
	 *
	 */
	public synchronized static void init(Context pContext, MemoryType pMemoryType) {
		
		if(pContext == null) {
			throw new IllegalArgumentException("parameter pContext must not be null");
		}
		if(pMemoryType == null) {
			throw new IllegalArgumentException("parameter pMemoryType must not be null");
		}
		
		String lPersistencyRootPath = null;
		switch(pMemoryType) {
		case external:
			lPersistencyRootPath = pContext.getExternalFilesDir(null).getAbsolutePath();
			break;
		case internal:
			lPersistencyRootPath = pContext.getFilesDir().getAbsolutePath();
			break;
		default:
			throw new IllegalArgumentException("not handled memory type:" + pMemoryType);
		}
		PersistencyWrapper.setPersistencyPath(lPersistencyRootPath);

		/**
		 * take care that init method is only processed once
		 */
		context = pContext;

		if(logger == null) {
			logger = new LogWrapperAndroid();
			LogWrapper.setLogImpl(logger);
			uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
			
			
			LogWrapper.i(LOGTAG, "============================================");
			LogWrapper.i(LOGTAG, "init =======================================");
			LogWrapper.i(LOGTAG, "============================================");

		}
		if(androidClazzFinder == null) {
			androidClazzFinder = new AndroidClazzFinder(pContext);
			ClazzFinder.setClazzFinder(androidClazzFinder);
		}
		if(serviceAdministrator == null) {
			serviceAdministrator = ServiceAdministrator.getInstance();
			serviceAdministrator.registerServices();
		}
		data.init();
	}
	/* (non-Javadoc)
	 * @see com.droidfad.iframework.data.IContextProvider#getActivity()
	 */
	@Override
	public Context getContext() {
		return context;
	}

	public static class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
		;
		/* (non-Javadoc)
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		@Override
		public void uncaughtException(Thread pThread, Throwable pEx) {
			Log.e("UNCAUGHT", pEx.getMessage(), pEx);
			if(uncaughtExceptionHandler != null) {
				uncaughtExceptionHandler.uncaughtException(pThread, pEx);
			}
		}
	}
}
