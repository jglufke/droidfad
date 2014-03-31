/**
 * 
 */
package com.droidfad.eula;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.droidfad.util.LogWrapper;
import com.droidfad.view.IEditorListener;

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
public class EULA {

	private final static  String CHECK_FILE_NAME = "EULA_CHECK";
	private static String swName;

	/**
	 *
	 * @return
	 *
	 */
	public static void check(final Activity pActivity, final String pSWName, final IEditorListener pListener) {

		if(pActivity == null) {
			throw new IllegalArgumentException("parameter pActivity must not be null");
		}
		if(pSWName== null) {
			throw new IllegalArgumentException("parameter pSWName must not be null");
		}
		if(pListener == null) {
			throw new IllegalArgumentException("parameter pListener must not be null");
		}

		swName           = pSWName;
		final File lFile = pActivity.getFileStreamPath(CHECK_FILE_NAME);
		if(lFile.exists()) {

			pListener.onEditorConfirmed();

		} else {

			Display lDisplay = pActivity.getWindowManager().getDefaultDisplay();
			int lDisplayWidth     = lDisplay.getWidth();
			int lDisplayHeight    = lDisplay.getHeight();

			LinearLayout lPanel      = new LinearLayout(pActivity);
			pActivity.setContentView(lPanel);
			lPanel.setOrientation(LinearLayout.VERTICAL);

			/**
			 * add the layout with the EULA
			 */
			LinearLayout lScrollBackPanel = new LinearLayout(pActivity);
			lPanel.addView(lScrollBackPanel);
			lScrollBackPanel.setOrientation(LinearLayout.VERTICAL);
			lScrollBackPanel.setBackgroundColor(Color.DKGRAY);

			ScrollView   lScrollView = new ScrollView(pActivity);
			lScrollBackPanel.addView(lScrollView);
			lScrollView.setLayoutParams(new LinearLayout.LayoutParams(lDisplayWidth, lDisplayHeight*6/8));

			TextView lTextView = new TextView(pActivity);
			lScrollView.addView(lTextView);
			lTextView.setText(eulaText);

			/**
			 * add ok and cancel button
			 */
			LinearLayout lButtonView = new LinearLayout(pActivity);
			lPanel.addView(lButtonView);
			lButtonView.setOrientation(LinearLayout.HORIZONTAL);
			lButtonView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT));

			Button lButton = new Button(pActivity);
			lButton.setWidth(lDisplayWidth/2);
			lButton.setHeight(lDisplayHeight/8);
			lButtonView.addView(lButton);
			lButton.setOnClickListener(new OnClickListener() {
				public void onClick(View pV) {
					createFile(lFile);
					pListener.onEditorConfirmed();
				}
			});
			lButton.setText("OK");

			lButton = new Button(pActivity);
			lButton.setWidth(lDisplayWidth/2);
			lButton.setHeight(lDisplayHeight/8);
			lButtonView.addView(lButton);
			lButton.setOnClickListener(new OnClickListener() {
				public void onClick(View pV) {
					pListener.onEditorCancelled();
				}
			});
			lButton.setText("CANCEL");

		}
	}

	/**
	 *
	 * @param pFile
	 *
	 */
	protected static void createFile(File pFile) {
		try {
			if(pFile.createNewFile()) {
				FileOutputStream lFOS = new FileOutputStream(pFile);
				lFOS.write(("EULA accepted on:" + new Date()).getBytes("UTF-8"));
				lFOS.close();
			}
		} catch(IOException e) {
			LogWrapper.e(EULA.class.getSimpleName(), "can not create file:" + pFile.getAbsolutePath(), e);
		}
	}

	private static String eulaText = "BY SELECTING 'OK' YOU CONFIRM THAT YOU AGREE TO THE" +
			" BELOW END USER LICENSE AGREEMENT: IF YOU DO NOT AGREE SELECT 'CANCEL'\n\n"+
			"END-USER LICENSE AGREEMENT FOR "+swName+"\n"+ 
			"IMPORTANT PLEASE READ THE TERMS AND CONDITIONS OF THIS LICENSE AGREEMENT\n"+ 
			"CAREFULLY BEFORE CONTINUING WITH THIS PROGRAM INSTALL: JENS GLUFKE \n"+
			"End-User License Agreement (\"EULA\") is a legal agreement between you (either an\n"+ 
			"individual or a single entity) and JENS GLUFKE. for the JENS GLUFKE software\n"+ 
			"product(s) identified above which may include associated software components,\n"+ 
			"media, printed materials, and \"online\" or electronic documentation \n"+
			"(\"SOFTWARE PRODUCT\"). By installing, copying, or otherwise using the SOFTWARE\n"+ 
			"PRODUCT, you agree to be bound by the terms of this EULA. This license agreement\n"+
			" represents the entire agreement concerning the program between you and\n"+ 
			" JENS GLUFKE, (referred to as \"licenser\"), and it supersedes any prior\n"+
			"  proposal, representation, or understanding between the parties. If you do not\n"+ 
			"  agree to the terms of this EULA, do not install or use the SOFTWARE PRODUCT.\n"+
			"\n"+
			"The SOFTWARE PRODUCT is protected by copyright laws and international copyright\n"+ 
			"treaties, as well as other intellectual property laws and treaties. The SOFTWARE\n"+
			" PRODUCT is licensed, not sold.\n"+
			"\n"+
			"1. GRANT OF LICENSE.\n"+
			"The SOFTWARE PRODUCT is licensed as follows:\n"+
			"(a) Installation and Use.\n"+
			"JENS GLUFKE grants you the right to install and use copies of the\n"+ 
			"SOFTWARE PRODUCT on your computer running a validly licensed copy of the\n"+ 
			"operating system for which the SOFTWARE PRODUCT was designed [Android].\n"+
			"(b) Backup Copies.\n"+
			"You may also make copies of the SOFTWARE PRODUCT as may be necessary for backup\n"+
			" and archival purposes.\n"+
			"\n"+
			"2. DESCRIPTION OF OTHER RIGHTS AND LIMITATIONS.\n"+
			"(a) Maintenance of Copyright Notices.\n"+
			"You must not remove or alter any copyright notices on any and all copies of the\n"+ 
			"SOFTWARE PRODUCT.\n"+
			"(b) Distribution.\n"+
			"You may not distribute registered copies of the SOFTWARE PRODUCT to third\n"+ 
			"parties. Evaluation versions available for download from JENS GLUFKE's\n"+
			"websites may be freely distributed.\n"+
			"(c) Prohibition on Reverse Engineering, Decompilation, and Disassembly.\n"+
			"You may not reverse engineer, decompile, or disassemble the SOFTWARE PRODUCT,\n"+ 
			"except and only to the extent that such activity is expressly permitted by\n"+ 
			"applicable law notwithstanding this limitation.\n"+
			"(d) Rental.\n"+
			"You may not rent, lease, or lend the SOFTWARE PRODUCT.\n"+
			"(e) Support Services.\n"+
			"JENS GLUFKE may provide you with support services related to the\n"+ 
			"SOFTWARE PRODUCT (\"Support Services\"). Any supplemental software code provided\n"+ 
			"to you as part of the Support Services shall be considered part of the\n"+ 
			"SOFTWARE PRODUCT and subject to the terms and conditions of this EULA.\n"+
			"(f) Compliance with Applicable Laws.\n"+
			"You must comply with all applicable laws regarding use of the SOFTWARE PRODUCT.\n"+
			"\n"+
			"3. TERMINATION\n"+
			"Without prejudice to any other rights, JENS GLUFKE may terminate this\n"+ 
			"EULA if you fail to comply with the terms and conditions of this EULA. In such\n"+ 
			"event, you must destroy all copies of the SOFTWARE PRODUCT in your possession.\n"+
			"\n"+
			"4. COPYRIGHT\n"+
			"All title, including but not limited to copyrights, in and to the\n"+ 
			"SOFTWARE PRODUCT and any copies thereof are owned by JENS GLUFKE or\n"+ 
			"its suppliers. All title and intellectual property rights in and to the content\n"+ 
			"which may be accessed through use of the SOFTWARE PRODUCT is the property of\n"+ 
			"the respective content owner and may be protected by applicable copyright or\n"+ 
			"other intellectual property laws and treaties. This EULA grants you no rights\n"+ 
			"to use such content. All rights not expressly granted are reserved by\n"+ 
			"JENS GLUFKE.\n"+
			"\n"+
			"5. NO WARRANTIES\n"+
			"JENS GLUFKE expressly disclaims any warranty for the SOFTWARE PRODUCT.\n"+ 
			"The SOFTWARE PRODUCT is provided 'As Is' without any express or implied warranty\n"+
			"of any kind, including but not limited to any warranties of merchantability,\n"+ 
			"noninfringement, or fitness of a particular purpose. JENS GLUFKE\n"+ 
			"does not warrant or assume responsibility for the accuracy or completeness of\n"+
			"any information, text, graphics, links or other items contained within the\n"+ 
			"SOFTWARE PRODUCT. JENS GLUFKE makes no warranties respecting any harm\n"+ 
			"that may be caused by the transmission of a computer virus, worm, time bomb,\n"+ 
			"logic bomb, or other such computer program. JENS GLUFKE further\n"+ 
			"expressly disclaims any warranty or representation to Authorized Users or to\n"+ 
			"any third party.\n"+
			"\n"+
			"6. LIMITATION OF LIABILITY\n"+
			"In no event shall JENS GLUFKE be liable for any damages (including,\n"+ 
			"without limitation, lost profits, business interruption, or lost information)\n"+ 
			"rising out of 'Authorized Users' use of or inability to use the\n"+ 
			"SOFTWARE PRODUCT, even if JENS GLUFKE has been advised of the\n"+ 
			"possibility of such damages. In no event will JENS GLUFKE be liable\n"+ 
			"for loss of data or for indirect, special, incidental, consequential\n"+ 
			"(including lost profit), or other damages based in contract, tort or otherwise.\n"+
			"JENS GLUFKE shall have no liability with respect to the content of\n"+ 
			"the SOFTWARE PRODUCT or any part thereof, including but not limited to errors\n"+
			"or omissions contained therein, libel, infringements of rights of publicity,\n"+ 
			"privacy, trademark rights, business interruption, personal injury, loss of\n"+ 
			"privacy, moral rights or the disclosure of confidential information.\n"; 

}
