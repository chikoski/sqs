/*

 MarkReaderHttpd.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2004/10/17

 */
package net.sqs2.omr.result.httpd;

import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.httpd.GenericHttpd;
import net.sqs2.httpd.JarContentServlet;
import net.sqs2.omr.result.tree.FormMasterListConnectorServlet;
import net.sqs2.omr.result.tree.QuestionTreeConnectorServlet;
import net.sqs2.omr.result.tree.RowListConnectorServlet;
import net.sqs2.omr.result.tree.SourceDirectoryListConnectorServlet;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class MarkReaderHttpd{
	private static final long serialVersionUID = 0L;

	void initContext(Context rootContext){
		try {

			rootContext.addServlet(new ServletHolder(new JarContentServlet()), '/'+ JarContentServlet.getContextString() + "/*"); // jar
			
			//ConsoleServlet consoleServlet = new ConsoleServlet();
			FormMasterListConnectorServlet masterOptionConnectorServlet = new FormMasterListConnectorServlet();
			SourceDirectoryListConnectorServlet sourceDirectoryListConnectorServlet = new SourceDirectoryListConnectorServlet();
			RowListConnectorServlet rowOptionConnectorServlet = new RowListConnectorServlet();
			
			QuestionTreeConnectorServlet questionTreeConnectorServlet = new QuestionTreeConnectorServlet();
			
			//rootContext.addServlet(new ServletHolder(consoleServlet),"/c/*");
			rootContext.addServlet(new ServletHolder(masterOptionConnectorServlet),"/m/*"); 
			rootContext.addServlet(new ServletHolder(rowOptionConnectorServlet),"/r/*"); 
			rootContext.addServlet(new ServletHolder(sourceDirectoryListConnectorServlet),"/p/*");
			rootContext.addServlet(new ServletHolder(questionTreeConnectorServlet),"/q/*");

			/*
			context.addServlet(new ServletHolder(new PageImageServlet()), '/'+ PageImageServlet.getContextString() + "/*"); // p
			context.addServlet(new ServletHolder(new AnswerUpdateServlet()), '/'+ AnswerUpdateServlet.getContextString() + "/*"); // u
			context.addServlet(new ServletHolder(new CSVSpreadsheetServlet()), '/'+ CSVSpreadsheetServlet.getContextString() + "/*"); // exportCSV
			context.addServlet(new ServletHolder(new XLSSpreadsheetServlet()), '/'+ XLSSpreadsheetServlet.getContextString() + "/*"); // exportXLS
			context.addServlet(new ServletHolder(new FormAreaImageServlet()), '/'+ FormAreaImageServlet.getContextString() + "/*"); // i
			*/
			
			/*
			context.addServlet(new ServletHolder(new SimpleChartImageServlet()), '/' + SimpleChartImageServlet.getContextString()+ "/*"); // c
			 */
			
			MimeTypes mimeTypes = new MimeTypes();
			for (String[] entry : GenericHttpd.MIME_TYPE_ARRAY) {
				mimeTypes.addMimeMapping(entry[0], entry[1]);
			}
			rootContext.setMimeTypes(mimeTypes);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(getClass().getName()).warning("Another Process may be running.");
			throw new RuntimeException("Another Process may be running.");
		}
		
	}
	
	Server localServer, remoteServer;
	Context localServerRootContext, remoteServerRootContext;
	
	public MarkReaderHttpd(int localPort, int remotePort) throws IOException{
		
		localServer = new Server(localPort);
		localServerRootContext = new Context(localServer, "/", Handler.ALL);
		initContext(localServerRootContext);
		
		remoteServer = new Server(remotePort);
		remoteServerRootContext = new Context(remoteServer, "/", Handler.ALL);
		initContext(remoteServerRootContext);
		
		localServer.setStopAtShutdown(true);
		remoteServer.setStopAtShutdown(true);
	
		try{
			initAuthConfig(remoteServer, remoteServerRootContext);
		}catch(IOException ignore){}
	}

	public void start()throws Exception{
		localServer.start();
		remoteServer.start();
	}
	
	public boolean isStarted(){
		return localServer.isStarted() || remoteServer.isStarted();
	}
	
	private void initAuthConfig(Server server, Context context) throws IOException {
		HashUserRealm myrealm = new HashUserRealm("MyRealm", System.getProperty("user.home")+".sqs/realm.properties");
		server.setUserRealms(new UserRealm[]{myrealm});
		Constraint constraint = new Constraint();
		constraint.setName(Constraint.__DIGEST_AUTH);
		constraint.setRoles(new String[]{"user","admin","moderator"});
		constraint.setAuthenticate(true);
		
		ConstraintMapping cm = new ConstraintMapping();
		cm.setConstraint(constraint);
		cm.setPathSpec("/*");
		
		SecurityHandler sh = new SecurityHandler();
		sh.setUserRealm(myrealm);
		sh.setConstraintMappings(new ConstraintMapping[]{cm});
		
		context.setHandler(sh);
	}
}
