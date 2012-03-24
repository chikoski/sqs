/**
 * JarExtender.java

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

 Author hiroya
 */
package net.sqs2.omr.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JarExtender {
	ClassLoader classLoader;
	public JarExtender(ClassLoader classLoader){
		this.classLoader = classLoader;
	}
	
	public void extend(String[] filenames, File targetDirectory) {
		OutputStream out = null;
		InputStream in = null;
		try {
			for (String filename : filenames) {
				File file = new File(targetDirectory, filename);
				if (!file.getParentFile().isDirectory()) {
					file.getParentFile().mkdirs();
				}
				out = new BufferedOutputStream(new FileOutputStream(file));
				in = new BufferedInputStream(classLoader.getResourceAsStream(filename));
				byte[] bytes = new byte[1024];
				int len = 0;
				while (0 < (len = in.read(bytes, 0, bytes.length))) {
					out.write(bytes, 0, len);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public JarExtender() {
		this(JarExtender.class.getClassLoader());
	}

}
