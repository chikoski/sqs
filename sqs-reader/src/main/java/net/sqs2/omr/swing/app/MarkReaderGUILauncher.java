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

package net.sqs2.omr.swing.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.app.MarkReaderApp;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.swing.process.RemoteWindowPopupDecorator;

public class MarkReaderGUILauncher {
	static private final long serialVersionUID = 0L;

	MarkReaderGUILauncher(final File sourceDirectoryRoot) throws IOException {

		final JFrame frame = new JFrame();
		final int port = 1098;
		RemoteWindowPopupDecorator.activate(frame, port);
		
		final PreviousSessionSourceDirectoryStorage prevSessionSourceDirectoryStorage = new PreviousSessionSourceDirectoryStorage();
		if (sourceDirectoryRoot != null && sourceDirectoryRoot.isDirectory()) {
			prevSessionSourceDirectoryStorage.setDefaultSourceDirectoryRoot(sourceDirectoryRoot);
		} else {
			prevSessionSourceDirectoryStorage.setDefaultSourceDirectoryRoot(new File(System.getProperty("user.home")));
		}

		try {
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		} catch (Error ex) {
			throw new RuntimeException(ex);
		}

		final boolean isLocalTaskExecutorEnabled = true;
		
		final MarkReaderApp markReaderApp = new MarkReaderApp(port, isLocalTaskExecutorEnabled);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final MarkReaderPanelImpl markReaderPanel = new MarkReaderPanelImpl(frame, prevSessionSourceDirectoryStorage);

				final MarkReaderPanelController markReaderPanelController = new MarkReaderPanelController(
						markReaderApp, markReaderPanel);
				MarkReaderMenuBar menuBar = new MarkReaderMenuBar(frame, markReaderPanel,
						markReaderPanelController);
				frame.setJMenuBar(menuBar);
				frame.setTitle(AppConstants.APP_NAME);
				frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								markReaderPanelController.promptExitConfirmation();
							}
						});
					}
				});
				frame.add(markReaderPanel, BorderLayout.CENTER);
				frame.setPreferredSize(new Dimension(535, 455));
				frame.pack();
				frame.setVisible(true);
				
				if (sourceDirectoryRoot != null) {
					new OpenAndStartSessionCommand(markReaderPanelController, markReaderApp, sourceDirectoryRoot, markReaderPanel).run();
				}
			}
		});		
	}

	public static void main(final String[] args) throws Exception {
		File sourceDirectoryRoot = null;
		if (1 <= args.length) {
			if (args[0].equals("-h")) {
				showUsage();
				return;
			}
			sourceDirectoryRoot = new File(args[0]);
			if (!sourceDirectoryRoot.exists()) {
				System.err.println("no such directory:" + sourceDirectoryRoot);
				System.exit(1);
			}
		}
		new MarkReaderGUILauncher(sourceDirectoryRoot);
	}

	private static void showUsage() {
		System.err.println("Usage: MarkReaderGUILauncher <sourceDirectoryRoot>");
	}
}
