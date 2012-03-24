/**
 *  MarkReaderPanelImpl.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/04/29
 Author hiroya
 */
package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import net.sqs2.omr.app.App;

public class MarkReaderPanelImpl extends AbstractMarkReaderPanel implements MarkReaderPanel{

	static private final long serialVersionUID = 0L;
	
	public static AbstractBorder DEFAULT_BORDER = new EmptyBorder(7,7,7,7);
	public static AbstractBorder FOCUSED_BORDER = new CompoundBorder(new LineBorder(Color.GREEN, 4), new EmptyBorder(3,3,3,3));

	Frame frame;
	boolean defaultPanelShown = false;
	
	public MarkReaderPanelImpl(Frame frame, MarkReaderPanelModel model){
		super(model);
		this.frame = frame;
		setBorder(DEFAULT_BORDER);
		this.defaultPanelShown = true;
	}
	
	public AbstractBorder getDefaultBorder(){
		return DEFAULT_BORDER;
	}
	
	public AbstractBorder getFocusedBorder(){
		return FOCUSED_BORDER;
	}
	
	@Override
	public DefaultPanel createDefaultPanel(){
		return new MarkReaderDefaultPanel();
	}

	private class MarkReaderDefaultPanel extends DefaultPanel{
		private static final long serialVersionUID = 0L;
		MarkReaderDefaultPanel(){
			setToolTipText(Messages.SESSION_PROMPT_MESSAGE1+Messages.SESSION_PROMPT_MESSAGE2+Messages.SESSION_PROMPT_MESSAGE3);
			setLayout(new BorderLayout());

			if(App.SKIN_ID.equals("sqs")){
				JLabel icon = new JLabel(ImageManager.DND_HERE_ICON);
				icon.setBorder(new EmptyBorder(20,20,20,20));
				JLabel messageLabel = new JLabel(Messages.SESSION_PROMPT_MESSAGE1+Messages.SESSION_PROMPT_MESSAGE2+Messages.SESSION_PROMPT_MESSAGE3);
				messageLabel.setBorder(new CompoundBorder(new EmptyBorder(20,40,10,40), new CompoundBorder(new EtchedBorder(), new EmptyBorder(10,10,10,10))));
				add(icon, BorderLayout.CENTER);
				add(messageLabel, BorderLayout.SOUTH);
			}else{
				JLabel topImage = new JLabel(ImageManager.DND_HERE_ICON);
				add(topImage, BorderLayout.CENTER);
			}
		}
	}
	
	NoPDFSourceFolderWarningPrompter noPDFSourceFolderPrompt = new NoPDFSourceFolderWarningPrompter(this);

	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#getNoPDFSourceFolderWarningPrompter()
	 */
	public NoPDFSourceFolderWarningPrompter getNoPDFSourceFolderWarningPrompter(){
		return this.noPDFSourceFolderPrompt;
	}
	
	public static class NoPDFSourceFolderWarningPrompter{		
		
		JPanel parent;
		boolean returnValue = false;
		
		public NoPDFSourceFolderWarningPrompter(JPanel parent){
			this.parent = parent;
		}
		
		public synchronized boolean prompt(final File sourceDirectoryRoot){
			if(this.parent == null){
				throw new RuntimeException("parent is null");
			}
			returnValue = JOptionPane.showConfirmDialog(parent, 
					Messages.SESSION_ERROR_NOPDFSOURCEFOLDER+"\n"+sourceDirectoryRoot.getAbsolutePath(),
					"Warning", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
			return returnValue;
		}
	}
	
	public Frame getFrame(){
		return this.frame;
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#setForgroundSessionPanel(java.io.File)
	 */
	public void setForgroundSessionPanel(File sourceDirectoryRootFile){
		this.tabbedPane.setSelectedComponent(getMarkReaderSessionPanel(sourceDirectoryRootFile));
	}

	private MarkReaderSessionPanel getCurrentSessionPanel(){
		return (MarkReaderSessionPanel)this.tabbedPane.getComponentAt(this.tabbedPane.getSelectedIndex());
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#exitConfirmation()
	 */
	public boolean exitConfirmation(){
		return JOptionPane.showConfirmDialog(MarkReaderPanelImpl.this,
				new String[]{Messages.EXIT_CONFIRMATION_MESSAGE},
				Messages.EXIT_CONFIRMATION_LABEL,
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION; 
	}
	
	private void promptInvalidSourceFolder(final File sourceDirectoryRootFile){
		JOptionPane.showConfirmDialog(MarkReaderPanelImpl.this, 
				Messages.SESSION_ERROR_INVALIDSOURCEFOLDER+"1",
				Messages.SESSION_ERROR_LABEL,
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#promptSessionFinished(java.io.File)
	 */
	public void promptSessionFinished(final File sourceDirectoryRootFile){
		JOptionPane.showMessageDialog(MarkReaderPanelImpl.this,
				Messages.SESSION_FINISH_MESSAGE+"\n"+
				Messages.SESSION_EXPORT_MESSAGE+"\n"+
					sourceDirectoryRootFile.getAbsolutePath()+File.separatorChar+App.getResultDirectoryName(),
				Messages.SESSION_FINISH_LABEL,				
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void openResultBrowser() {		
		if(JOptionPane.showConfirmDialog(MarkReaderPanelImpl.this,
			Messages.SESSION_FINISH_MESSAGE+"\n"+Messages.SESSION_BROWSE_PROMPT_MESSAGE,
			Messages.SESSION_FINISH_LABEL,				
			JOptionPane.OK_CANCEL_OPTION, 
			JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION){
			try{
				SessionProgressActions.openResultBrowser();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public MarkReaderSessionPanel createSessionPanel(File sourceDirectoryRoot, SessionProgressModel markReaderSessionProgressModel){
		return new MarkReaderSessionPanel(sourceDirectoryRoot, markReaderSessionProgressModel);
	}

}
