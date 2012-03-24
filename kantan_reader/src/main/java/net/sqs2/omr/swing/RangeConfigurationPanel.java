package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.session.Session;
import net.sqs2.omr.source.ConfigHandlers;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.source.config.ConfigXMLFileFilter;
import net.sqs2.omr.source.config.MarkRecognitionConfig;

class MarkRecognitionConfigurationDialog extends JDialog{
	
	private static final long serialVersionUID = 0L;
	ConfigXMLFileFilter configFileFilter;
	FormMaster formMaster;
	File configFile;
	SourceDirectoryConfiguration sourceDirectoryConfiguration;
	
	MarkRecognitionConfigurationDialog(Frame owner, SourceDirectoryConfiguration sourceDirectoryConfiguration){
		this(owner, MarkReaderConstants.USER_CUSTOMIZE_DEFAULT_CONFIG_FILE, sourceDirectoryConfiguration, Messages.SESSION_PROCESS_CONFIG_FOLDER_LABEL);
	}
	
	MarkRecognitionConfigurationDialog(Frame owner, File configFile, SourceDirectoryConfiguration sourceDirectoryConfiguration){
		this(owner, configFile, sourceDirectoryConfiguration, Messages.SESSION_PROCESS_CONFIG_DEFAULT_LABEL);
	}
	
	MarkRecognitionConfigurationDialog(Frame owner, File configFile, SourceDirectoryConfiguration sourceDirectoryConfiguration, String label){
		super(owner, label, true);
		this.sourceDirectoryConfiguration = sourceDirectoryConfiguration;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		
		this.configFile = configFile;
		
		if(! this.configFile.getParentFile().isDirectory()){
			this.configFile.getParentFile().mkdirs();
		}
		
		this.configFileFilter = new ConfigXMLFileFilter(this.configFile);
		int densityThresholdValue = getDensityThresholdValue();
		int recognitionMarginValue = getRecognitionMarginValue();
		
		String label1 = Messages.SESSION_PROCESS_RECOGNITION_DENSITY_LABEL;
		String label2 = Messages.SESSION_PROCESS_RECOGNITION_MARGIN_LABEL;
		
		final MarkRecognitionConfigurationInnerPanel markRecognitionConfigurationPanel = 
			new MarkRecognitionConfigurationInnerPanel(label1, densityThresholdValue, 
					label2, recognitionMarginValue);
		add(markRecognitionConfigurationPanel, BorderLayout.CENTER);
		
		AbstractAction cancelAction = new AbstractAction("Cancel") {
			private static final long serialVersionUID = 0L;
			  public void actionPerformed(ActionEvent evt) {
				  dispose();
			  }
		};

		InputMap imap = getRootPane().getInputMap();
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		getRootPane().getActionMap().put("close-it", cancelAction);
		
		getRootPane().setDefaultButton(markRecognitionConfigurationPanel.getCancelButton());
		markRecognitionConfigurationPanel.getCancelButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				dispose();
			}
		});
		
		markRecognitionConfigurationPanel.getOKButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				try{
					int densityThreshold255 = markRecognitionConfigurationPanel.getDensityThreshold255();
					int recognitionMargin255 = markRecognitionConfigurationPanel.getRecognitionMargin255();
					storeConfiguration(densityThreshold255, recognitionMargin255);
					dispose();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		});
	}	
	
	private int getDensityThresholdValue() {		
		int defaultValue = 242;
		try{
			if(this.configFile.exists()){
				defaultValue= (int)(Float.parseFloat(this.configFileFilter.getDensityThreshold()) * 255);
			}
		}catch(Exception ignore){
			Logger.getAnonymousLogger().severe("file="+this.configFile+", density=null");
		}
		return defaultValue;
	}

	private int getRecognitionMarginValue() {		
		int defaultValue = 10;
		try{
			if(this.configFile.exists()){
				defaultValue= (int)(Float.parseFloat(this.configFileFilter.getRecognitionMargin()) * 255);
			}
		}catch(Exception ignore){
			Logger.getAnonymousLogger().severe("file="+this.configFile+", recognitionMargin=null");
		}
		return defaultValue;
	}

	void storeConfiguration(int densityValue255, int marginValue255)throws IOException{
		ConfigHandlers.setUserConfigurationEnabled(true);
		MarkRecognitionConfig markRecognitionConfig = this.sourceDirectoryConfiguration.getConfig().getSourceConfig().getMarkRecognitionConfig();
		float density = (float)(densityValue255/255.0);
		float margin = (float)(marginValue255/255.0);
		
		markRecognitionConfig.setDensity(density);
		markRecognitionConfig.setRecognitionMargin(margin);
		
		if(! this.configFile.exists()){
			Session.createConfigFile(this.configFile);
		}
		ConfigXMLFileFilter self = new ConfigXMLFileFilter(this.configFile);
		self.overwriteValues(density, margin);
	}
}

class MarkRecognitionConfigurationInnerPanel extends JPanel{
	private static final long serialVersionUID = 0L;
	
	RangeConfigurationPanel densityConfigurator;
	RangeConfigurationPanel recognitionMarginConfigurator;
	
	OKCancelButtonPanel okCancelButtonPanel;
	 
	MarkRecognitionConfigurationInnerPanel(String label1, int densityThresholdDefaultValue, String label2, int recognitionMarginDefaultValue){
		okCancelButtonPanel = new OKCancelButtonPanel();
		setBorder(new EmptyBorder(5,5,5,5));
		setLayout(new BorderLayout());
		densityConfigurator = new RangeConfigurationPanel(label1, 0, 255, 10, 50, densityThresholdDefaultValue);
		recognitionMarginConfigurator = new RangeConfigurationPanel(label2, 0, 255, 10, 50, recognitionMarginDefaultValue);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(densityConfigurator);
		centerPanel.add(recognitionMarginConfigurator);
		add(centerPanel, BorderLayout.CENTER);
		add(okCancelButtonPanel, BorderLayout.SOUTH);
	}
	
	public JButton getCancelButton(){
		return okCancelButtonPanel.getCancelButton();
	}
	
	public JButton getOKButton(){
		return okCancelButtonPanel.getOKButton();
	}
	
	public int getDensityThreshold255(){
		return densityConfigurator.getValue();
	}

	public int getRecognitionMargin255(){
		return recognitionMarginConfigurator.getValue();
	}
}


class OKCancelButtonPanel extends JPanel{
	private static final long serialVersionUID = 0L;
	JButton cancelButton;
	JButton okButton;
	OKCancelButtonPanel(){
		cancelButton = new JButton("Cancel"); 
		cancelButton.setPreferredSize(new Dimension(120, 32));
		cancelButton.setSelected(true);
		
		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(120, 32));
		
		setBorder(new EmptyBorder(5,5,5,5));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		add(cancelButton);
		add(Box.createHorizontalStrut(16));
		add(okButton);
	}
	
	public JButton getOKButton(){
		return this.okButton;
	}

	public JButton getCancelButton(){
		return this.cancelButton;
	}
}

public class RangeConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 0L;
	
	JLabel label;
	JSlider slider;
	JSpinner spinner;
	
	RangeConfigurationPanel(String title, int min, int max, int minorTickSpacing, int majorTickSpacing, int defaultValue){
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10,10,10,10));
		
		this.label = createLabel(title);
		label.setPreferredSize(new Dimension(160, 20));
		this.slider = createSlider(min, max, minorTickSpacing, majorTickSpacing, defaultValue);
		this.spinner = createSpinner(min, max, 1, defaultValue);
		
		this.slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				spinner.setValue(slider.getValue());
			}
		});

		this.spinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				slider.setValue((Integer)spinner.getValue());
			}
		});

		add(this.label, BorderLayout.WEST);
		add(this.slider, BorderLayout.CENTER);
		add(this.spinner, BorderLayout.EAST);
	}
	
	public int getValue(){
		return slider.getValue();
	}

	private JSlider createSlider(int min, int max, int minorTickSpacing, int majorTickSpacing, int defaultValue) {
		JSlider slider = new JSlider();
		slider.setMinimum(min);
		slider.setMaximum(max);		
		slider.setValue(defaultValue);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(minorTickSpacing);
		slider.setMajorTickSpacing(majorTickSpacing);
	
		slider.setPaintLabels(true);
		slider.setUI(new GrayscaleSliderUI());
		slider.setLabelTable(slider.createStandardLabels(50));
		return slider;
	}
	
	private JLabel createLabel(String title) {
		JLabel label = new JLabel(title);
		return label;
	}

	private JSpinner createSpinner(int min, int max, int stepSize, int defaultValue) {
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(defaultValue, min, max, stepSize);
		JSpinner spinner = new JSpinner(spinnerNumberModel);
		return spinner;
	}

	class GrayscaleSliderUI extends javax.swing.plaf.metal.MetalSliderUI {

		public void paintThumb(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int gray = 255 * slider.getValue() / slider.getMaximum();
			g2.setColor(new Color(gray, gray, gray));
			g2.fillOval(thumbRect.x, thumbRect.y, thumbRect.width - 1,
					thumbRect.height - 1);
			g2.setColor(Color.BLACK);
			g2.drawOval(thumbRect.x, thumbRect.y, thumbRect.width - 1,
					thumbRect.height - 1);
		}

		/*
		public void paintTrack(Graphics g) {
			super.paintTrack(g);
			
			int cx, cy, cw, ch;
			int pad;
			Rectangle trackBounds = trackRect;
			if(slider.getOrientation() == JSlider.HORIZONTAL ) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.black);
				g2.fillRect(0, 0, getWidth() * slider.getValue() / slider.getMaximum() , getHeight());
				g2.setColor(Color.white);
				g2.fillRect((getWidth() - 1) * slider.getValue() / slider.getMaximum() + 1, 0,  getWidth() * (slider.getMaximum() - slider.getValue()) / slider.getMaximum(), getHeight() - 1);
			}
		}*/
	}

}
