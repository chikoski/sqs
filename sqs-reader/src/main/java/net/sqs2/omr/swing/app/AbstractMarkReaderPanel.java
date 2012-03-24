package net.sqs2.omr.swing.app;

import java.awt.CardLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import net.sqs2.omr.swing.Images;

public abstract class AbstractMarkReaderPanel extends JPanel {

	private static final long serialVersionUID = 0L;

	final protected PreviousSessionSourceDirectoryStorage markReaderPanelModel;
	final protected JTabbedPane tabbedPane;
	final protected DefaultPanel defaultPanel;
	protected JFileChooser directoryChooser;
	protected CardLayout cardLayout;

	abstract class DefaultPanel extends JPanel {
		private static final long serialVersionUID = 0L;
	}

	public AbstractMarkReaderPanel(PreviousSessionSourceDirectoryStorage model) {
		this.markReaderPanelModel = model;

		this.defaultPanel = createDefaultPanel();
		this.tabbedPane = new JTabbedPane();

		this.cardLayout = new CardLayout();
		setLayout(this.cardLayout);

		add(this.defaultPanel, "instruction");
		add(this.tabbedPane, "tab");

		this.directoryChooser = new JFileChooser();
		if (this.markReaderPanelModel.getDefaultSourceDirectoryRoot() != null) {
			File defaultSourceDirectoryRoot = this.markReaderPanelModel.getDefaultSourceDirectoryRoot();
			this.directoryChooser.setSelectedFile(defaultSourceDirectoryRoot);
		}
		this.directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.directoryChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		this.directoryChooser.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Directory Chooser";
			}

		});
	}

	abstract DefaultPanel createDefaultPanel();

	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sqs2.exigrid.swing.MarkReaderPanel#getMarkReaderSessionPanel(java
	 * .io.File)
	 */
	public MarkReaderSessionPanel getMarkReaderSessionPanel(File targetFile) {
		for (Component tab : this.tabbedPane.getComponents()) {
			if (tab instanceof MarkReaderSessionPanel) {
				MarkReaderSessionPanel sessionPanel = (MarkReaderSessionPanel) tab;
				File sourceDirectoryRootFile = ((MarkReaderSessionPanel) tab).getSessionProgressModel()
						.getSourceDirectoryRoot();
				if (sourceDirectoryRootFile.equals(targetFile)) {
					return sessionPanel;
				}
			}
		}
		throw new RuntimeException("no such Session:" + targetFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sqs2.exigrid.swing.MarkReaderPanel#addMarkReaderSessionPanel(net.
	 * sqs2.exigrid.swing.MarkReaderSessionPanel, java.lang.String)
	 */
	public void addMarkReaderSessionPanel(JPanel sessionPanel, String name) {
		this.tabbedPane.add(sessionPanel, name);
		this.tabbedPane.setIconAt(getTabIndex(sessionPanel), Images.FOLDER_OPEN_ICON);
		this.tabbedPane.setSelectedComponent(sessionPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#removeTabPanel(java.io.File)
	 */
	public void removeTabPanel(File sourceDirectoryRoot) {
		MarkReaderSessionPanel sessionPanel = this.getMarkReaderSessionPanel(sourceDirectoryRoot);
		//Icon icon = this.tabbedPane.getIconAt(getTabIndex(sessionPanel));
		/*
		 * if(icon instanceof AnimatedImageIcon){
		 * ImageManager.freeAnimatedImageIcon((AnimatedImageIcon)icon); }
		 */
		this.tabbedPane.remove(sessionPanel);
		if (this.tabbedPane.getComponentCount() == 0) {
			getCardLayout().show(this, "instruction");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sqs2.exigrid.swing.MarkReaderPanel#getDirectoryChooserPanel()
	 */
	public JFileChooser getDirectoryChooser() {
		return this.directoryChooser;
	}

	public void setRunningTabIcon(final File sourceDirectoryRootFile) {
		setTabIcon(sourceDirectoryRootFile, Images.FOLDER_OPEN_ICON, true);
	}

	public void setFinishedTabIcon(final File sourceDirectoryRootFile) {
		setTabIcon(sourceDirectoryRootFile, Images.FOLDER_ICON, true);
	}

	private void setTabIcon(final File sourceDirectoryRootFile, Icon icon, boolean freePrevIcon) {
		for (int index = 0; index < this.tabbedPane.getComponentCount(); index++) {
			Component c = this.tabbedPane.getComponentAt(index);
			if (!(c instanceof MarkReaderSessionPanel)) {
				continue;
			}
			MarkReaderSessionPanel sessionPanel = (MarkReaderSessionPanel) c;

			if (sourceDirectoryRootFile.equals(sessionPanel.getSessionProgressModel()
					.getSourceDirectoryRoot())) {
				/*
				 * if(freePrevIcon){
				 * ImageManager.freeAnimatedImageIcon((ImageIcon
				 * )this.tabbedPane.getIconAt(index)); }
				 */
				this.tabbedPane.setIconAt(index, icon);
				this.tabbedPane.setSelectedComponent(c);
			}

		}
		this.tabbedPane.repaint();
	}

	protected int getTabIndex(Component component) {
		for (int index = 0; index < this.tabbedPane.getComponentCount(); index++) {
			if (component == this.tabbedPane.getComponentAt(index)) {
				return index;
			}
		}
		return -1;
	}

	public CardLayout getCardLayout() {
		return this.cardLayout;
	}

}
