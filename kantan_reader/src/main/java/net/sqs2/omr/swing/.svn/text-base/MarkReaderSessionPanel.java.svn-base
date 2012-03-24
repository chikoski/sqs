/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalIconFactory;

import net.sqs2.image.ImageManagerUtil;
import net.sqs2.omr.app.App;
import net.sqs2.omr.result.event.SpreadSheetExportEventProducer;

public class MarkReaderSessionPanel extends JPanel{

	private static final long serialVersionUID = 0L;

	static final Color DEFAULT_BACKGROUND_COLOR = new Color(224,240,255);

	private JTabbedPane tabbedPane;
	protected ConsolePanel consolePanel;

	MarkReaderSessionProgressPanel markReaderSessionProgressPanel;
	MarkReaderSessionExportPanel markReaderSessionExportPanel;
	MarkReaderSessionResultPanel markReaderSessionResultPanel;
	SessionResultPanel sessionResultPanel;
	
	SessionExportPanelImpl sessionExportPanel;
	
	SessionProgressModel sessionProgressModel;
	
	public SessionResultPanel getSessionResultPanel(){
		return sessionResultPanel;
	}
	
	class SessionPanel extends JPanel{
		private static final long serialVersionUID = 0L;	

		SessionPanel(){
			setLayout(new BorderLayout());
		}
		
		protected void setPlayStateGUI() {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			this.repaint();
		}

		protected void setPauseStateGUI() {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.repaint();
		}
	}

	
	class MarkReaderSessionProgressPanel extends SessionPanel{
		private static final long serialVersionUID = 0L;
		
		SessionProgressBar sessionProgressBar; 

		public MarkReaderSessionProgressPanel(final SessionProgressModel sessionProgressModel){
			this.sessionProgressBar = new SessionProgressBar(sessionProgressModel);			
			SessionProgressMeter progressMeter = new SessionProgressMeter(sessionProgressModel);
			ProgressPanel progressPanel = new ProgressPanel(progressMeter);
			add(progressPanel, BorderLayout.CENTER);
			add(this.sessionProgressBar, BorderLayout.SOUTH);
		}
		
		protected void setPlayStateGUI() {
			super.setPlayStateGUI();
			this.sessionProgressBar.setIndeterminate(true);
		}
		
		protected void setPauseStateGUI() {
			super.setPauseStateGUI();
			this.sessionProgressBar.setIndeterminate(false);
		}
	}
	
	class MarkReaderSessionResultPanel extends SessionPanel{
		private static final long serialVersionUID = 0L;
		public MarkReaderSessionResultPanel(){
			add(sessionResultPanel, BorderLayout.CENTER);
		}
	}

	class MarkReaderSessionExportPanel extends SessionPanel{
		private static final long serialVersionUID = 0L;
		public MarkReaderSessionExportPanel(){
			sessionExportPanel = new SessionExportPanelImpl(); 
			add(sessionExportPanel, BorderLayout.CENTER);
		}
		
		public void reset(){
			sessionExportPanel.reset();
		}
	}
	
	public void bindSpreadSheetExportEventProducer(SpreadSheetExportEventProducer eventProducer,
			MarkAreasTableModel noAnswerMarkAreasTableModel,
			MarkAreasTableModel multipleAnswersAnswerMarkAreasTableModel) {
		sessionExportPanel.bindSpreadSheetExportEventProducer(eventProducer, noAnswerMarkAreasTableModel, multipleAnswersAnswerMarkAreasTableModel);
	}
	
	public class JTabbedPaneWithBackgroundImage extends JTabbedPane{
		private static final long serialVersionUID = 0L;

		private Image image;
		
		JTabbedPaneWithBackgroundImage(Image image){
			super(JTabbedPane.LEFT);
			this.image = image;
		}

		@Override
		public void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    
		    int y = getSize().height - image.getHeight(null) - 10;
		    Rectangle lastTab = getUI().getTabBounds(this, getTabCount()-1);
		    int tabEnd = lastTab.y + lastTab.height;
		    if(y < tabEnd){
		    	y = tabEnd;
		    }
		    g.setColor(DEFAULT_BACKGROUND_COLOR);
		    g.fillRect(0, 0, lastTab.x - 1, lastTab.y + lastTab.width - 1);
		    g.fillRect(0, tabEnd, lastTab.x + lastTab.width, getHeight() - tabEnd);
		    g.drawImage(image, 10, y, null);
		}
	}
	
	public MarkReaderSessionPanel(final File sourceDirectoryRoot, final SessionProgressModel sessionProgressModel){
		this.sessionProgressModel = sessionProgressModel;
		this.consolePanel = new ConsolePanel(sourceDirectoryRoot);
		this.tabbedPane = new JTabbedPaneWithBackgroundImage(ImageManagerUtil.createImage(ImageManager.createURL(App.SUB_ICON)));
		this.tabbedPane.setBackground(Color.GRAY);
		this.tabbedPane.setBorder(new EmptyBorder(5,5,5,5));
				
		this.markReaderSessionProgressPanel = new MarkReaderSessionProgressPanel(sessionProgressModel);
		this.markReaderSessionExportPanel = new MarkReaderSessionExportPanel();
		this.sessionResultPanel = new SessionResultPanel();
		this.markReaderSessionResultPanel = new MarkReaderSessionResultPanel();

		this.tabbedPane.addTab("1. "+Messages.SESSION_READ_LABEL, this.markReaderSessionProgressPanel);
		this.tabbedPane.addTab("2. "+Messages.SESSION_EXPORT_LABEL, this.markReaderSessionExportPanel);
		this.tabbedPane.addTab("3. "+Messages.SESSION_RESULT_LABEL, markReaderSessionResultPanel);
		
		this.tabbedPane.setEnabledAt(1, false);
		this.tabbedPane.setEnabledAt(2, false);
		
		setLayout(new BorderLayout());
		add(this.consolePanel, BorderLayout.NORTH);
		add(this.tabbedPane, BorderLayout.CENTER);
		
		this.markReaderSessionProgressPanel.setPlayStateGUI();
		
	}
	
	public void reset(){
		this.markReaderSessionExportPanel.reset();
	}
	
	public boolean isProgressTabSelected(){
		return this.tabbedPane.getSelectedIndex() == 0;
	}
	
	public boolean isExportTabSelected(){
		return this.tabbedPane.getSelectedIndex() == 1;
	}

	public boolean isResultTabSelected(){
		return this.tabbedPane.getSelectedIndex() == 2;
	}

	public void selectProgressTab(){
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
		this.tabbedPane.setEnabledAt(2, false);
	}
	
	public void selectExportTab(){
		this.tabbedPane.setEnabledAt(1, true);
		this.tabbedPane.setSelectedIndex(1);
	}
	
	public void selectResultTab(){
		this.tabbedPane.setEnabledAt(2, true);
		this.tabbedPane.setSelectedIndex(2);
	}
	
	public SessionProgressModel getSessionProgressModel(){
		return this.sessionProgressModel;
	}
	
	static class ConsolePanel extends JPanel{
		private static final long serialVersionUID = 0L;
		protected JTextField sourceDirectoryPathField;
		protected JButton restartButton;
		protected JButton pauseButton;
		protected JButton configButton;
		protected JButton closeButton;
				
		static class ConsolePanelButton extends JButton{
			
			static final Border EMPTY_BORDER = new EmptyBorder(6,1,6,1);
			static final Border FOCUSED_BORDER = new LineBorder(Color.ORANGE);
			
			private static final long serialVersionUID = 0L;
			ConsolePanelButton(String text, Icon icon){
				super(text, icon);
				setBorder(EMPTY_BORDER);
				setBackground(DEFAULT_BACKGROUND_COLOR);
				addMouseListener(new MouseAdapter(){

					@Override
					public void mouseEntered(MouseEvent e) {
						setBorder(FOCUSED_BORDER);
					}

					@Override
					public void mouseExited(MouseEvent e) {
						setBorder(EMPTY_BORDER);
					}
					
				});
			}
			
		}
		
		ConsolePanel(File file){
			setBackground(DEFAULT_BACKGROUND_COLOR);
			this.restartButton = new ConsolePanelButton(Messages.SESSION_RESTART_LABEL, ImageManager.REFRESH_ICON);
			this.pauseButton = new ConsolePanelButton(Messages.SESSION_STOP_LABEL, ImageManager.STOP_ICON);
			this.sourceDirectoryPathField = new JTextField();
			this.configButton = new ConsolePanelButton(Messages.SESSION_CONFIG_LABEL, ImageManager.CONFIG_ICON);
			this.closeButton = new JButton(MetalIconFactory.getInternalFrameCloseIcon(18));
			
			closeButton.setPreferredSize(new Dimension(30,20));
			closeButton.setBorder(new EmptyBorder(0,10,10,0));
			closeButton.setBackground(DEFAULT_BACKGROUND_COLOR);
			closeButton.setToolTipText(Messages.SESSION_CLOSE_TOOLTIP);
						
			this.sourceDirectoryPathField.setBackground(DEFAULT_BACKGROUND_COLOR);
			this.sourceDirectoryPathField.setBorder(new CompoundBorder(new EmptyBorder(5,3,5,3), new LineBorder(Color.LIGHT_GRAY, 1)));
			this.sourceDirectoryPathField.setEditable(false);
			this.sourceDirectoryPathField.setText(file.getAbsolutePath());
			this.sourceDirectoryPathField.setToolTipText(file.getAbsolutePath());
			
			JPanel consolePanelUpper = new JPanel();
			consolePanelUpper.setBackground(DEFAULT_BACKGROUND_COLOR);
			consolePanelUpper.setLayout(new BoxLayout(consolePanelUpper, BoxLayout.X_AXIS));
			consolePanelUpper.add(Box.createHorizontalStrut(20));
			consolePanelUpper.add(pauseButton);
			consolePanelUpper.add(Box.createHorizontalStrut(20));
			consolePanelUpper.add(restartButton);
			consolePanelUpper.add(Box.createHorizontalStrut(20));
			consolePanelUpper.add(configButton);
			consolePanelUpper.add(Box.createHorizontalStrut(10));			
			restartButton.setEnabled(false);
			pauseButton.setEnabled(false);
			configButton.setEnabled(true);
			pauseButton.setToolTipText(Messages.SESSION_STOP_TOOLTIP);
			restartButton.setToolTipText(Messages.SESSION_RESTART_TOOLTIP);
			configButton.setToolTipText(Messages.SESSION_CONFIG_TOOLTIP);
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(consolePanelUpper);
			add(sourceDirectoryPathField);
			add(Box.createHorizontalGlue());
			add(closeButton);
		}
		
		public void setPauseStateGUI(){
			this.pauseButton.setEnabled(false);
			this.restartButton.setEnabled(true);
			this.configButton.setEnabled(true);
			this.pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.restartButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
		}
		
		public void setPlayStateGUI(){
			this.pauseButton.setEnabled(true);
			this.restartButton.setEnabled(true);
			this.configButton.setEnabled(false);
			this.pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.restartButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	class ProgressPanel extends JPanel{
		private static final long serialVersionUID = 0L;
		ProgressPanel(SessionProgressMeter progressMeter) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(progressMeter);
			add(Box.createVerticalStrut(10));
			setPreferredSize(new Dimension(150,150));
		}
	}
	
	public void setPauseStateGUI(){
		this.consolePanel.setPauseStateGUI();
		this.markReaderSessionProgressPanel.setPauseStateGUI();
		this.markReaderSessionExportPanel.setPauseStateGUI();
		this.markReaderSessionResultPanel.setPauseStateGUI();
	}
	
	public void setPlayStateGUI(){
		this.consolePanel.setPlayStateGUI();
		this.markReaderSessionProgressPanel.setPlayStateGUI();
		this.markReaderSessionExportPanel.setPlayStateGUI();
		this.markReaderSessionResultPanel.setPlayStateGUI();
	}
	
	public void setRestartButtonActionListener(ActionListener listener) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.consolePanel.restartButton.addActionListener(listener);
	}
	
	public void setPauseButtonActionListener(ActionListener listener) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.consolePanel.pauseButton.addActionListener(listener);
	}

	public void setCloseButtonActionListener(ActionListener listener) {
		this.consolePanel.closeButton.addActionListener(listener);
	}
	
	public void setConfigButtonActionListener(ActionListener listener){
		this.consolePanel.configButton.addActionListener(listener);
	}
}

/*
 * 
// code from "tempra memo"

class ClippedTitleTabbedPane extends JTabbedPane {
    private final Insets tabInsets; //= UIManager.getInsets("TabbedPane.tabInsets");
    private final Insets tabAreaInsets; // = UIManager.getInsets("TabbedPane.tabAreaInsets");
//     private final Insets contentInsets = UIManager.getInsets("TabbedPane.contentBorderInsets");
//     private final Insets selectedTabPadInsets = UIManager.getInsets("TabbedPane.selectedTabPadInsets");
    public ClippedTitleTabbedPane() {
        super(JTabbedPane.TOP);
        Insets insets = UIManager.getInsets("TabbedPane.tabInsets");
        tabInsets = (insets!=null)?insets:new Insets(0,0,0,0);
        insets = UIManager.getInsets("TabbedPane.tabAreaInsets");
        tabAreaInsets = (insets!=null)?insets:new Insets(0,0,0,0);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initTabWidth();
            }
        });
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                initTabWidth();
            }
        });
    }
    
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip==null?title:tip, index);
        JLabel label = new JLabel(title, JLabel.CENTER);
        Dimension dim = label.getPreferredSize();
        label.setPreferredSize(new Dimension(0, dim.height+tabInsets.top+tabInsets.bottom));
        setTabComponentAt(index, label);
        initTabWidth();
    }
    
    private void initTabWidth() {
        Insets insets = getInsets();
        int areaWidth = getWidth() - tabAreaInsets.left - tabAreaInsets.right - insets.left - insets.right;
        //- contentInsets.left - contentInsets.right;

        int tabCount = getTabCount();
        int tabWidth = 0; // = tabInsets.left + tabInsets.right + 3;
        switch(getTabPlacement()) {
          case LEFT: case RIGHT:
            tabWidth = areaWidth/4;
            break;
          case BOTTOM: case TOP: default:
            tabWidth = areaWidth/tabCount;
        }
        int gap = areaWidth - (tabWidth * tabCount); //System.out.println(gap);
        if(tabWidth>80) {
            tabWidth = 80;
            gap = 0;
        }
        tabWidth = tabWidth - tabInsets.left - tabInsets.right - 3;
        for(int i=0;i<tabCount;i++) {
            JLabel l = (JLabel)getTabComponentAt(i);
            if(l==null) break;
            l.setPreferredSize(
                new Dimension(tabWidth+(i<gap?1:0),
                              l.getPreferredSize().height));
        }
        revalidate();
    }
}
*/