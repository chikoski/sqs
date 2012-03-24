/**
 * TemplateLoader.java

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

package net.sqs2.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class TemplateLoader {
	
	static{
		try{
			Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
		}catch(ClassNotFoundException ex){
		}
	}
	
	private Configuration[] configurations = new Configuration[2];

	public TemplateLoader(File baseDirectory, String path, String skin) throws IOException {
		String localPath = (baseDirectory.getAbsolutePath() + File.separatorChar + path + File.separatorChar + skin);
		this.configurations[0] = this.createDirectoryForTemplateLoadingConfiguration(new File(localPath));
		String classPath = "/" + path + "/" + skin;
		this.configurations[1] = this.createClassForTemplateLoadingConfiguration(classPath);
		// Logger.getLogger(getClass().getName()).info("localPath:"+localPath);
		// Logger.getLogger(getClass().getName()).info("classPath:"+classPath);
	}

	public Template getUserCustomizedTemplate(String templateName, String encoding) throws IOException{
		return this.configurations[0].getTemplate(templateName, encoding);
	}
	
	public Template getDefaultTemplate(String templateName, String encoding) throws IOException{
		return this.configurations[1].getTemplate(templateName, encoding);
	}
	
	/**
	 * @param templateName
	 * @param encoding
	 * @return one of templates: user customized template or default built-in template
	 * @throws IOException
	 */
	public Template getTemplate(String templateName, String encoding) throws IOException {

		for (Configuration configuration : this.configurations) {
			try {
				Template template = configuration.getTemplate(templateName, encoding);
				return template;
			} catch (FileNotFoundException ignore) {
			} catch (SocketException ex) {
				ex.printStackTrace();
			}
		}
		throw new IOException("template loading failure: " + templateName);
	}

	public Template getTemplate(int index, String templateName, String encoding) throws IOException{
		return this.configurations[index].getTemplate(templateName, encoding);
	}
	
	private Configuration createDirectoryForTemplateLoadingConfiguration(File ftlDirectory) throws IOException {
		Configuration cfg = new Configuration();
		ftlDirectory.mkdirs();
		cfg.setDirectoryForTemplateLoading(ftlDirectory);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		// cfg.setEncoding(Locale.JAPANESE, "UTF-8");
		return cfg;
	}

	private Configuration createClassForTemplateLoadingConfiguration(String ftlPath) throws IOException {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(getClass(), ftlPath);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		// cfg.setEncoding(Locale.JAPANESE, "UTF-8");
		return cfg;
	}

}
