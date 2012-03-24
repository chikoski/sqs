/*

 FreeMarkerHandler.java

 Copyright 2010 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/11/01

 */

package net.sqs2.omr.result.export;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.template.TemplateLoader;
import freemarker.template.Template;

public class FreeMarkerHandler{
	private static final long serialVersionUID = 0L;
	private File userConfigDir;
	private String skinName;
	private String templateFileName;
	
	public FreeMarkerHandler(File userConfigDir, String skinName, String templateFileName){
		this.userConfigDir = userConfigDir;
		this.skinName = skinName;
		this.templateFileName = templateFileName;
	}

	public void write(ResultBrowserContext contentSelection, Writer writer, Map<String,Object>map) throws IOException {
		writeTo(writer, createTemplate(contentSelection, this.templateFileName, this.skinName, "UTF-8", this.userConfigDir), map);
	}

	protected Template createTemplate(ResultBrowserContext contentSelection, String filename, String skinName, String encoding, File userConfigDir) throws IOException{
		TemplateLoader loader = new TemplateLoader(userConfigDir, "ftl", skinName);
		Template template = loader.getTemplate(filename, encoding);
		return template;
	}

	private void writeTo(Writer writer, Template template, Map<String,Object> map) {
		try {
			template.process(map, writer);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
