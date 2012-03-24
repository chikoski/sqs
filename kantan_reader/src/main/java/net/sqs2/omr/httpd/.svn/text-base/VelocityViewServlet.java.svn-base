/*

 VelocityViewServlet.java

 Copyright 2008 KUBO Hiroya (hiroya@cuc.ac.jp).

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

package net.sqs2.omr.httpd;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityViewServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;
	public VelocityViewServlet()throws ServletException{
		super();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		res.setHeader("CacneControl", "no-cache;");
		long sessionID = Long.parseLong(req.getParameter("sid"));
		writeTo(res.getWriter(), getTemplateResourceName(), createVelocityContext(sessionID));
	}
	
	protected String getTemplateResourceName(){
		throw new RuntimeException("not implemented yet");
	}
	
	protected VelocityContext createVelocityContext(long sessionID){
		VelocityContext vc = new VelocityContext();
		return vc;
	}

	public static void main(String[] args)throws Exception{
		VelocityViewServlet v = new VelocityViewServlet();
		Writer writer = new OutputStreamWriter(System.out, "UTF-8");
		v.writeTo(writer, "vm/edit.vm", new VelocityContext());
		writer.close();
	}
	
	private void writeTo(Writer writer, String templateName, VelocityContext vc){
		try{      
			Properties properties = new Properties();
			properties.setProperty("resource.loader", "ClasspathResourceLoader");
			properties.setProperty("ClasspathResourceLoader.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			properties.setProperty("ClasspathResourceLoader.resource.loader.cache",
					"false");// release: true

			Velocity.init(properties);
			
			writeTo(writer, templateName, "UTF-8", vc);
		}catch(Exception ex){
		}
	}
	
	private void writeTo(Writer writer, String templateResourceName, String templateResourceEncoding, VelocityContext vc){
		try{      
			Template template = Velocity.getTemplate(templateResourceName, templateResourceEncoding);
			template.merge(vc, writer);
			writer.flush();       
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
