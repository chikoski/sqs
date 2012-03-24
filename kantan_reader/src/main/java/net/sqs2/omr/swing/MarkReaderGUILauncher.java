/*

 MarkReaderGUILauncher.java

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

 Created on 2006/01/10

 */

package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.net.ClassURLConnection;

import net.sqs2.omr.app.App;
import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.app.MarkReaderControllerImpl;
import net.sqs2.omr.httpd.SQSHttpdManager;
import net.sqs2.swing.process.RemoteWindowDecorator;

public class MarkReaderGUILauncher {
	static private final long serialVersionUID = 0L;
	
	MarkReaderGUILauncher(final File sourceDirectoryRoot)throws IOException{

		final MarkReaderPanelModel markReaderPanelModel = new MarkReaderPanelModel();
		if(sourceDirectoryRoot != null && sourceDirectoryRoot.isDirectory()){
			markReaderPanelModel.setDefaultSourceDirectoryRoot(sourceDirectoryRoot);
		}else{
			markReaderPanelModel.setDefaultSourceDirectoryRoot(new File(System.getProperty("user.home")));
		}
		
		final JFrame frame = new JFrame();
		final int port = 1099;
		
		RemoteWindowDecorator.activate(frame, port);
		
		try{
		    ClassURLStreamHandlerFactory factory = new ClassURLStreamHandlerFactory();
		    URLStreamHandler h = factory.createURLStreamHandler("http"); // XXXX ad-hoc
		    ClassURLConnection con = 
				new ClassURLConnection(new URL("http://localhost/")); // XXX
		    URL.setURLStreamHandlerFactory(factory);
		}catch(Error ex){
			throw new RuntimeException(ex);
		}
		
		SQSHttpdManager.initHttpds();
		
		final MarkReaderControllerImpl markReaderController = MarkReaderControllerImpl.getInstance(port);
		final MarkReaderPanelImpl markReaderPanel = new MarkReaderPanelImpl(frame, markReaderPanelModel);
		final MarkReaderPanelController markReaderPanelController = new MarkReaderPanelController(markReaderController, markReaderPanel);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try{
					MarkReaderMenuBar menuBar = new MarkReaderMenuBar(frame, markReaderPanel, markReaderPanelController);
					frame.setJMenuBar(menuBar);
					frame.setTitle(App.APP_NAME);
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					frame.addWindowListener(new WindowAdapter(){
						public void windowClosing(WindowEvent e) {
							SwingUtilities.invokeLater(new Runnable(){
								public void run(){
									markReaderPanelController.userExitConfirmation();
								}
							});
						}
					});
					frame.add(markReaderPanel, BorderLayout.CENTER);
					frame.setPreferredSize(new Dimension(535, 385));
					frame.pack();
					frame.setVisible(true);
					if(sourceDirectoryRoot != null){
						markReaderPanelController.userOpen(sourceDirectoryRoot);
					}
				}catch(Exception ignore){
					ignore.printStackTrace();
				}
			}
		});
	}
	
	public static void main(final String[] args)throws Exception{
		File sourceDirectoryRoot = null;
		if(1 <= args.length){
			if(args[0].equals("-h")){
				showUsage();
				return;
			}
			sourceDirectoryRoot = new File(args[0]);	
			if(! sourceDirectoryRoot.exists()){
				System.err.println("no such directory:"+sourceDirectoryRoot);
				System.exit(1);
			}
		}
		new MarkReaderGUILauncher(sourceDirectoryRoot);
	}
 

	public static File getDefaultSourceDirectoryRoot(MarkReaderControllerImpl app, String sourceDirectoryRootPath)throws MalformedURLException{
		if(sourceDirectoryRootPath != null){
			return new File(sourceDirectoryRootPath);
		}else{
			String path = app.getPreferences().get(MarkReaderConstants.SOURCE_DIRECTORY_ROOT_KEY, MarkReaderConstants.DEFAULT_SOURCEDIRECTORY_PATH);
			if(path != null){
				return new File(path);
			}
		}
		return null;
	}
		
	private static void showUsage(){
		System.err.println("Usage: MarkReaderGUILauncher <sourceDirectoryRoot>");
	}
}