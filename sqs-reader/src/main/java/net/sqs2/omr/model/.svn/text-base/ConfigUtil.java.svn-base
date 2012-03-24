/*

 SessionConfigUtil.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

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
package net.sqs2.omr.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import net.sqs2.util.FileUtil;

public class ConfigUtil{

	public static void createConfigFileIfNotExists(File sourceDirectoryRootFile) throws MalformedURLException {
		File configFile = new File(new File(sourceDirectoryRootFile, AppConstants.RESULT_DIRNAME),
				AppConstants.SOURCE_CONFIG_FILENAME);
		createConfigFile(configFile);
	}

	public static void createConfigFile(File configFile) throws MalformedURLException {
		if (!configFile.exists()) {
			InputStream in = null;
			OutputStream out = null; 
			try {
				in = createDefaultConfigFileInputStream();
				out = new BufferedOutputStream(new FileOutputStream(configFile));
				FileUtil.pipe(in, out);
			} catch (IOException ignore) {
			}finally{
				try{
					if(in != null){
						in.close();
					}
					if(out != null){
						out.close();
					}
				} catch (IOException ignore) {}
			}
		}
	}

	private static InputStream createDefaultConfigFileInputStream() throws IOException {
		if (AppConstants.USER_CUSTOMIZED_DEFAULT_CONFIG_FILE.exists()) {
			return new BufferedInputStream(new FileInputStream(
					AppConstants.USER_CUSTOMIZED_DEFAULT_CONFIG_FILE));
		} else {
			return ConfigUtil.class.getClassLoader().getResourceAsStream(
					AppConstants.SOURCE_CONFIG_FILENAME);
		}
	}
}