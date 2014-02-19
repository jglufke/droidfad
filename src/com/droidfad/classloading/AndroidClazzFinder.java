package com.droidfad.classloading;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.droidfad.util.FileUtil;
import com.droidfad.util.LogWrapper;

import dalvik.system.DexFile;
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

 *
 * with the help of the AndroidClazzFinder class it is possible for an 
 * application to determine which classes are contained in the 
 * classpath or which jar files contain which classes etc.
 * A very helpful class if an plugin environment has to be implemented.
 * 
 */
public class AndroidClazzFinder implements IClazzFinder {

	public static final boolean ISDEBUG = true;

	public static final String LOGTAG = AndroidClazzFinder.class.getSimpleName();

	private File cacheDir;

	private static HashSet<ClazzDescriptor> classEntries = null;

	private String androidPackageName;

	private String apkFilename;

	/**
	 * ********************************************<br>
	 *
	 * @param pA_cm_testActivity
	 *
	 * ********************************************<br>
	 */
	public AndroidClazzFinder(Context pContext) {
		/**
		 * if pContext is null it indicates that this runs in a regular
		 * JSE environment. In this case the JSEClazzfinder has to be used
		 */
		if(pContext != null) {
			androidPackageName = pContext.getPackageName();
			cacheDir           = pContext.getCacheDir();
			apkFilename        = pContext.getApplicationInfo().sourceDir;
		} else {
			LogWrapper.e(LOGTAG, "can not find application for activity:" + pContext);
		}
	}

	/**
	 * 
	 * ************************************************************<br>
	 *
	 * find all the classes in the System.properties classpath 
	 * "java.class.path"
	 *
	 * @author glufkeje
	 * @return
	 * 
	 * <br>************************************************************<br>
	 */
	public HashSet<ClazzDescriptor> findClasses() {

		synchronized (AndroidClazzFinder.class) {
			if(classEntries == null) {
				findClassesImpl();
			}
		}
		return classEntries;
	}

	/**
	 * ********************************************<br>
	 *
	 *
	 * ********************************************<br>
	 */
	protected void findClassesImpl() {

		classEntries = new HashSet<ClazzDescriptor>();
		/**
		 * get file location
		 */
		if(!new File(apkFilename).exists()) {
			throw new RuntimeException("apk file not found for package:" + androidPackageName);
		}
		/**
		 * open apk zip file and  get classes.dex file
		 */
		File   lClassDexFile = getClassDexFile(apkFilename);

		try {
			File                lTempFile        = File.createTempFile("classes.", ".dex", cacheDir);
			lTempFile.deleteOnExit();
			DexFile             lDexFile         = DexFile.loadDex(lClassDexFile.getAbsolutePath(), lTempFile.getAbsolutePath(), 0);
			Enumeration<String> lNameEnumeration = lDexFile.entries();

			/**
			 * cleanup the temp files
			 */
			// lDexFile.close();
			lTempFile.delete();
			lClassDexFile.delete();

			ClassLoader         lClassLoader     = getClass().getClassLoader();
			while(lNameEnumeration.hasMoreElements()) {
				String   lClassname = lNameEnumeration.nextElement();
				try {

					Class<?>        lClass = lClassLoader.loadClass(lClassname);
					ClazzDescriptor lDescr = new ClazzDescriptor(apkFilename, lClass, apkFilename);
					classEntries.add(lDescr);

				} catch (ClassNotFoundException e) {
					Log.e(LOGTAG, "findClasses error loading class:" + lClassname);
				}
			}
		}  catch(IOException e) {
			Log.e(LOGTAG, "could not handle file:" + lClassDexFile);
		}
	}

	/**
	 * ********************************************<br>
	 *
	 * @param pApkFilename
	 * @return
	 *
	 * ********************************************<br>
	 */
	private File getClassDexFile(String pApkFilename) {
		File lClassDexFile = null;
		try {
			lClassDexFile = File.createTempFile("classes.", ".apk", cacheDir);
			lClassDexFile.deleteOnExit();
			FileUtil.copy(pApkFilename, lClassDexFile.getAbsolutePath());

		} catch (IOException e) {
			Log.e(LOGTAG, "getClassDexFile: could not created temp file for classes.dex:" + e.getMessage());
		}
		return lClassDexFile;
	}

	/**
	 * ********************************************<br>
	 *
	 * @param pParentClass
	 * @return
	 *
	 * ********************************************<br>
	 */
	@SuppressWarnings("unchecked")
	public <T> HashSet<Class<? extends T>> findSubclasses(Class<?> pParentClass) {
		if(pParentClass == null) {
			throw new IllegalArgumentException("parameter pClass must not be null");
		}

		HashSet<Class<? extends T>> lSubclassSet = new HashSet<Class<? extends T>>();
		for(ClazzDescriptor lDescriptor : findClasses()) {
			Class<?> lClazz = lDescriptor.getClazz();
			if(pParentClass.isAssignableFrom(lClazz) && !pParentClass.equals(lClazz)) {
				lSubclassSet.add((Class<? extends T>) lClazz);
			}
		}

		return lSubclassSet;
	}
}
