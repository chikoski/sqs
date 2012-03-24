package net.sqs2.omr.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.sqs2.omr.cache.CacheConstants;
import net.sqs2.swing.DirectoryChooserPanel;

public abstract class AbstractMarkReaderPanel extends JPanel {

	private static final long serialVersionUID = 0L;
	
	final protected MarkReaderPanelModel markReaderPanelModel;
	final protected JTabbedPane tabbedPane;
	final protected DefaultPanel defaultPanel;
	protected DirectoryChooserPanel directoryChooserPanel;
	protected CardLayout cardLayout;

	abstract class DefaultPanel extends JPanel{
		private static final long serialVersionUID = 0L;
	}
	
	public AbstractMarkReaderPanel(MarkReaderPanelModel model){
		this.markReaderPanelModel = model;

		this.defaultPanel = createDefaultPanel();
		this.tabbedPane = new JTabbedPane();
		
		this.cardLayout= new CardLayout();
		setLayout(this.cardLayout);

		add(this.defaultPanel, "instruction");
		add(this.tabbedPane, "tab");

		this.directoryChooserPanel = new DirectoryChooserPanel(this, new JLabel(ImageManager.FOLDER_ICON), CacheConstants.CACHE_ROOT_DIRNAME);
		this.directoryChooserPanel.setToolTipText(Messages.SESSION_PROMPT_MESSAGE1+Messages.SESSION_PROMPT_MESSAGE2+Messages.SESSION_PROMPT_MESSAGE3);
		if(this.markReaderPanelModel.getDefaultSourceDirectoryRoot() != null){
			File defaultSourceDirectoryRoot = this.markReaderPanelModel.getDefaultSourceDirectoryRoot();
			this.directoryChooserPanel.getTextField().setText(defaultSourceDirectoryRoot.getAbsolutePath());
			this.directoryChooserPanel.setFile(defaultSourceDirectoryRoot);
		}
	}

	abstract DefaultPanel createDefaultPanel();
	
	protected JTabbedPane getTabbedPane(){
		return this.tabbedPane;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#getMarkReaderSessionPanel(java.io.File)
	 */
	public MarkReaderSessionPanel getMarkReaderSessionPanel(File targetFile){
		for(Component tab : this.tabbedPane.getComponents()){
			if(tab instanceof MarkReaderSessionPanel){
				MarkReaderSessionPanel sessionPanel = (MarkReaderSessionPanel)tab;
				File sourceDirectoryRootFile = ((MarkReaderSessionPanel)tab).getSessionProgressModel().getSourceDirectoryRoot();
				if(sourceDirectoryRootFile.equals(targetFile)){
					return sessionPanel;
				}
			}
		}
		throw new RuntimeException("no such Session:"+targetFile);
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#addMarkReaderSessionPanel(net.sqs2.exigrid.swing.MarkReaderSessionPanel, java.lang.String)
	 */
	public void addMarkReaderSessionPanel(JPanel sessionPanel, String name){		
		tabbedPane.add(sessionPanel, name);
		tabbedPane.setIconAt(getTabIndex(sessionPanel), ImageManager.FOLDER_OPEN_ICON);
		tabbedPane.setSelectedComponent(sessionPanel);
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#removeTabPanel(java.io.File)
	 */
	public void removeTabPanel(File sourceDirectoryRoot){
		MarkReaderSessionPanel sessionPanel = this.getMarkReaderSessionPanel(sourceDirectoryRoot);
		Icon icon = this.tabbedPane.getIconAt(getTabIndex(sessionPanel));
		/*
		if(icon instanceof AnimatedImageIcon){
			ImageManager.freeAnimatedImageIcon((AnimatedImageIcon)icon);
		}
		*/
		this.tabbedPane.remove(sessionPanel);
		if(this.tabbedPane.getComponentCount() == 0){
			getCardLayout().show(this, "instruction");
		}
	}

	/* (non-Javadoc)
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#getDirectoryChooserPanel()
	 */
	public DirectoryChooserPanel getDirectoryChooserPanel(){
		return this.directoryChooserPanel;
	}
	
	public void setRunningTabIcon(final File sourceDirectoryRootFile){
		setTabIcon(sourceDirectoryRootFile, ImageManager.FOLDER_OPEN_ICON, true);
	}

	public void setFinishedTabIcon(final File sourceDirectoryRootFile){
		setTabIcon(sourceDirectoryRootFile, ImageManager.FOLDER_ICON, true);
	}

	private void setTabIcon(final File sourceDirectoryRootFile, Icon icon, boolean freePrevIcon){
		for(int index = 0; index < this.tabbedPane.getComponentCount(); index++){
			Component c= this.tabbedPane.getComponentAt(index);
			if(! (c instanceof MarkReaderSessionPanel)){
				continue;
			}
			MarkReaderSessionPanel sessionPanel =(MarkReaderSessionPanel)c;

			if(sourceDirectoryRootFile.equals(sessionPanel.getSessionProgressModel().getSourceDirectoryRoot())){
				/*
				if(freePrevIcon){
					ImageManager.freeAnimatedImageIcon((ImageIcon)this.tabbedPane.getIconAt(index));
				}*/
				this.tabbedPane.setIconAt(index, icon);
				this.tabbedPane.setSelectedComponent(c);
			}
			
		}
		tabbedPane.repaint();
	}

	protected int getTabIndex(Component component){
		for(int index = 0; index < this.tabbedPane.getComponentCount(); index++){
			if(component == this.tabbedPane.getComponentAt(index)){
				return index;
			}
		}
		return -1;
	}
	
	public CardLayout getCardLayout(){
		return this.cardLayout;
	}

}
