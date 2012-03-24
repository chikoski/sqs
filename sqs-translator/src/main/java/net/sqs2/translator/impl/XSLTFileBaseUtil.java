/**
 * 
 */
package net.sqs2.translator.impl;

import java.io.File;

class XSLTFileBaseUtil{
	public static String userCustomizedURI(String groupID, String appID){
		String userCustomizePath = System.getProperty("user.home") + File.separatorChar + '.'+ groupID;
		String appPath = userCustomizePath + File.separatorChar+appID;
		File appFile = new File(appPath);
		if(! appFile.exists()){
			appFile.mkdirs();
		}
		String userCustomizedXSLTFileBasePath = appPath + File.separatorChar + "xslt";
		String userCustomizedXSLTFileBaseURIString = new File(userCustomizedXSLTFileBasePath).toURI().toString()+'/';
		return userCustomizedXSLTFileBaseURIString;
	}
}