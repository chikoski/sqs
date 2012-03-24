/*

 SourceEditorMediator.java
 
 Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2004/08/05

 */
package net.sqs2.editor.base.swing;

import java.awt.BorderLayout;

import java.io.File;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;

import net.sqs2.exsed.source.SourceManager;
import net.sqs2.swing.SwingUtil;
import net.sqs2.swing.process.RemoteWindowPopupDecorator;
import net.sqs2.translator.impl.PageSetting;

/**
 * @author hiroya
 * 
 */
public abstract class SourceEditorMediator {
	JFrame frame = null;
	SourceEditorTabbedPane tabbedPane;
	SourceEditorMenuBarMediator menuBarMediator;
	SourceEditorToolBar toolBar;
	SourceManager sourceManager;
	EditorResourceFactory resourceFactory = null;

	public SourceEditorMediator() throws ClassCastException, IllegalAccessException, InstantiationException,
			ClassNotFoundException, UnsupportedLookAndFeelException {
		System.setProperty("swing.plaf.metal.controlFont", "Monospaced-12");
		this.frame = new JFrame();
		//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		//UIManager.put("swing.boldMetal", Boolean.FALSE);
		/*
    	for(LookAndFeel laf: new LookAndFeel[]{new WindowsLookAndFeel(), new MetalLookAndFeel()}){
    	    try {
    	        UIManager.setLookAndFeel(laf);
    	        SwingUtilities.updateComponentTreeUI(frame);
    	        break;
    	    } catch (javax.swing.UnsupportedLookAndFeelException e) {
    	        e.printStackTrace();
    	    }
    	}
    	*/
		RemoteWindowPopupDecorator.activate(this.frame, 6099);
		this.sourceManager = createSourceManager();
		this.menuBarMediator = createSourceEditorMenuBarMediator();
		this.toolBar = createSourceEditorToolBar();
		this.tabbedPane = createSourceEditorTabbedPane(this);
		this.frame = setupFrame();
	}

	public abstract EditorResourceFactory createEditorResourceFactory();

	public EditorResourceFactory getEditorResourceFactory() {
		if (resourceFactory == null) {
			resourceFactory = createEditorResourceFactory();
		}
		return resourceFactory;
	}

	public SourceEditorMediator(File filename) throws ClassCastException, IllegalAccessException,
			InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException {
		this();
		this.menuBarMediator.open(filename);
		this.menuBarMediator.updateMenu();
	}

	public SourceEditorMediator(URL url) throws ClassCastException, IllegalAccessException,
			InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException {
		this();
		this.menuBarMediator.open(url, false, null);
		this.menuBarMediator.updateMenu();
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public JFrame setupFrame() {
		SourceEditorPanel panel = new SourceEditorPanel(this.toolBar, this.tabbedPane);
		this.frame.addWindowListener(SwingUtil.createConfirmOnExitAdapter(this.frame, "Exit Confirmation",
				Messages.EXIT_CONFIRMATION_MESSAGE));
		this.frame.getContentPane().setLayout(new BorderLayout());
		this.frame.getContentPane().add(this.menuBarMediator.getMenuBar(), java.awt.BorderLayout.NORTH);
		this.frame.getContentPane().add(panel, BorderLayout.CENTER);

		this.frame.setTitle(getTitle());
		this.frame.setSize(800, 600);
		this.frame.setVisible(true);
		return this.frame;
	}

	public SourceEditorToolBar getToolBar() {
		return toolBar;
	}

	public SourceEditorMenuBarMediator getMenuBarMediator() {
		return menuBarMediator;
	}

	public abstract String getTitle();

	public abstract SourceManager createSourceManager();

	public abstract SourceEditorMenuBarMediator createSourceEditorMenuBarMediator();

	public abstract SourceEditorToolBar createSourceEditorToolBar();

	public abstract SourceEditorTabbedPane createSourceEditorTabbedPane(SourceEditorMediator mediator);

	public SourceEditorTabbedPane getSourceEditorTabbedPane() {
		return tabbedPane;
	}

	public SourceManager getSourceManager() {
		return sourceManager;
	}

	public NodeTreePane getCurrentTreePane() {
		return (NodeTreePane) getSourceEditorTabbedPane().getCurrentSplitPane().getTreePane();
	}

	public JScrollPane getCurrentEditorScrollPane() {
		return getSourceEditorTabbedPane().getCurrentSplitPane().getEditorScrollPane();
	}

	public JComponent getCurrentEditorPane() {
		return (JComponent) getSourceEditorTabbedPane().getCurrentSplitPane().getEditorPane();
	}

	public abstract JPanel getBackgroundPanel();

	/*
	 * ファイルメニューやタブの更新を行う． 編集内容の更新をしたら，必ずこのメソッドを呼び出すようにすること．
	 */

	public void fireSourceChanged() {
		tabbedPane.updateCurrentTitle();
		menuBarMediator.updateMenu();
	}
	
	public PageSetting getCurrentPageSetting(){
		return menuBarMediator.getCurrentPageSetting();
	}
}
