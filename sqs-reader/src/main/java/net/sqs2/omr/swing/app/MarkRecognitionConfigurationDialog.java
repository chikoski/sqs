package net.sqs2.omr.swing.app;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalSliderUI;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.ConfigUtil;
import net.sqs2.omr.model.ConfigXMLFileFilter;
import net.sqs2.omr.model.MarkRecognitionConfig;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.model.SourceDirectoryConfigurations;
import net.sqs2.omr.swing.Images;

public class MarkRecognitionConfigurationDialog extends JDialog {

	private static final int RECOGNITION_DENSITY = 239;
	private static final int DOUBLE_MARK_ERROR_SUPPRESSION = 8;
	private static final int NO_MARK_ERROR_SUPPRESSION = 10;

	private static final long serialVersionUID = 0L;
	ConfigXMLFileFilter configFileFilter;
	FormMaster formMaster;
	File configFile;
	SourceDirectoryConfiguration sourceDirectoryConfiguration;

	MarkRecognitionConfigurationDialog(Frame owner, SourceDirectoryConfiguration sourceDirectoryConfiguration) {
		this(owner, AppConstants.USER_CUSTOMIZED_DEFAULT_CONFIG_FILE, sourceDirectoryConfiguration,
				Messages.SESSION_PROCESS_CONFIG_DEFAULT_LABEL);
	}

	public MarkRecognitionConfigurationDialog(Frame owner, File configFile,
			SourceDirectoryConfiguration sourceDirectoryConfiguration) {
		this(owner, configFile, sourceDirectoryConfiguration, Messages.SESSION_PROCESS_CONFIG_FOLDER_LABEL
				+ ": " + configFile.getAbsolutePath());
	}

	MarkRecognitionConfigurationDialog(Frame owner, File configFile,
			SourceDirectoryConfiguration sourceDirectoryConfiguration, String label) {
		super(owner, label, true);
		this.sourceDirectoryConfiguration = sourceDirectoryConfiguration;
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);

		this.configFile = configFile;

		if (!this.configFile.getParentFile().isDirectory()) {
			this.configFile.getParentFile().mkdirs();
		}

		this.configFileFilter = new ConfigXMLFileFilter(this.configFile);
		int densityThresholdValue = getDensityThresholdValue();
		int doubleMarkIgnoranceThresholdValue = getDoubleMarkIgnoranceThresholdValue();
		int noMarkRecoveryThresholdValue = getNoMarkRecoveryThresholdValue();

		String label1 = Messages.SESSION_PROCESS_RECOGNITION_DENSITY_LABEL;
		String label2 = Messages.SESSION_PROCESS_DOUBLE_MARK_SUPRESSION_LABEL;
		String label3 = Messages.SESSION_PROCESS_NO_MARK_SUPRESSION_LABEL;

		final MarkRecognitionConfigurationInnerPanel markRecognitionConfigurationPanel = new MarkRecognitionConfigurationInnerPanel(
				label1, densityThresholdValue, 
				label2, doubleMarkIgnoranceThresholdValue,
				label3, noMarkRecoveryThresholdValue);
		add(markRecognitionConfigurationPanel, BorderLayout.CENTER);
		pack();

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
		markRecognitionConfigurationPanel.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});

		markRecognitionConfigurationPanel.getOKButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					int densityThreshold255 = markRecognitionConfigurationPanel.getDensityThreshold255();
					int recognitionMargin255 = markRecognitionConfigurationPanel.getRecognitionMargin255();
					int noMarkRecovery255 = markRecognitionConfigurationPanel.getNoMarkRecoveryThreshold255();
					storeConfiguration(densityThreshold255, recognitionMargin255, noMarkRecovery255);
					dispose();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private int getDensityThresholdValue() {
		int defaultValue = RECOGNITION_DENSITY;
		try {
			if (this.configFile.exists()) {
				defaultValue = (int) (Float.parseFloat(this.configFileFilter.getDensityThreshold()) * 255);
			}
		} catch (Exception ignore) {
			Logger.getLogger(getClass().getName())
					.severe(
							"file=" + this.configFile
									+ ", density=null, set default value");
		}
		return defaultValue;
	}

	private int getDoubleMarkIgnoranceThresholdValue() {
		int defaultValue = DOUBLE_MARK_ERROR_SUPPRESSION;
		try {
			if (this.configFile.exists()) {
				defaultValue = (int) (Float.parseFloat(this.configFileFilter
						.getDoubleMarkIgnoranceThreshold()) * 255);
			}
		} catch (Exception ignore) {
			Logger.getLogger(getClass().getName()).severe(
					"file=" + this.configFile
							+ ", recognitionMargin=null, set default value");
		}
		return defaultValue;
	}

	private int getNoMarkRecoveryThresholdValue(){
		int defaultValue = NO_MARK_ERROR_SUPPRESSION;
		try {
			if (this.configFile.exists()) {
				defaultValue = (int) (Float.parseFloat(this.configFileFilter
						.getNoMarkRecoveryThreshold()) * 255);
			}
		} catch (Exception ignore) {
			Logger.getLogger(getClass().getName()).severe(
					"file=" + this.configFile
							+ ", noMarkRecovery=null, set default value");
		}
		return defaultValue;
	}
	
	void storeConfiguration(int densityValue255, int doubleMarkIgnoranceValue255, int noMarkRecoveryValue255) throws IOException {
		SourceDirectoryConfigurations.setUserDefaultConfigurationEnabled(true);
		float density = (float) (densityValue255 / 255.0);
		float doubleMarkIgnoranceThreshold = (float) (doubleMarkIgnoranceValue255 / 255.0);
		float noMarkRecoveryThreshold = (float) (noMarkRecoveryValue255 / 255.0);
		if (!this.configFile.exists()) {
			ConfigUtil.createConfigFile(this.configFile);
		}

		if(this.sourceDirectoryConfiguration != null){			
			MarkRecognitionConfig markRecognitionConfig = ((SourceConfig)this.sourceDirectoryConfiguration.getConfig().getPrimarySourceConfig()).getMarkRecognitionConfig();
			markRecognitionConfig.setMarkRecognitionDensityThreshold(density);
			markRecognitionConfig.setDoubleMarkErrorSuppressionThreshold(doubleMarkIgnoranceThreshold);
			ConfigXMLFileFilter self = new ConfigXMLFileFilter(this.configFile);
			self.overwriteValues(density, doubleMarkIgnoranceThreshold, noMarkRecoveryThreshold);
		}else{
			ConfigXMLFileFilter self = new ConfigXMLFileFilter(this.configFile);
			self.overwriteValues(density, doubleMarkIgnoranceThreshold, noMarkRecoveryThreshold);
		}
	}

	class MarkRecognitionConfigurationInnerPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		RangeConfigurationPanel densityConfigurator;
		RangeConfigurationPanel recognitionMarginConfigurator;
		RangeConfigurationPanel noMarkRecoveryThresholdConfigurator;

		OKCancelButtonPanel okCancelButtonPanel;

		MarkRecognitionConfigurationInnerPanel(String title1, int densityThresholdDefaultValue,
				String label2, int recognitionMarginDefaultValue,
				String label3, int noMarkRecoveryThresholdDefaultValue) {
			this.okCancelButtonPanel = new OKCancelButtonPanel();
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setLayout(new BorderLayout());
			
			this.densityConfigurator = new RangeConfigurationPanel(title1,
					Messages.CONFIG_RECOGNITION_DENSITY_TOOLTIP, 0, 255, 10, 50, densityThresholdDefaultValue, new GrayscaleSliderUI());
			this.recognitionMarginConfigurator = new RangeConfigurationPanel(label2, Messages.CONFIG_RECOGNITION_DOUBLE_MARK_SUPPRESSION_TOOLTIP,
					0, 255, 10, 50, recognitionMarginDefaultValue, new MetalSliderUI());
			this.noMarkRecoveryThresholdConfigurator = new RangeConfigurationPanel(label3, Messages.CONFIG_RECOGNITION_NO_MARK_SUPPRESSION_TOOLTIP,
					0, 255, 10, 50, noMarkRecoveryThresholdDefaultValue, new MetalSliderUI());

			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
			centerPanel.add(this.densityConfigurator);
			centerPanel.add(this.recognitionMarginConfigurator);
			centerPanel.add(this.noMarkRecoveryThresholdConfigurator);
			centerPanel.setBorder(new TitledBorder(Messages.CONFIG_RECOGNITION_TITLE));

			add(centerPanel, BorderLayout.CENTER);
			add(this.okCancelButtonPanel, BorderLayout.SOUTH);
		}

		public JButton getCancelButton() {
			return this.okCancelButtonPanel.getCancelButton();
		}

		public JButton getOKButton() {
			return this.okCancelButtonPanel.getOKButton();
		}

		public int getDensityThreshold255() {
			return this.densityConfigurator.getValue();
		}

		public int getRecognitionMargin255() {
			return this.recognitionMarginConfigurator.getValue();
		}
		
		public int getNoMarkRecoveryThreshold255(){
			return this.noMarkRecoveryThresholdConfigurator.getValue();
		}
	}

	class OKCancelButtonPanel extends JPanel {
		private static final long serialVersionUID = 0L;
		JButton okButton;
		JButton cancelButton;

		OKCancelButtonPanel() {
			this.okButton = new JButton("OK", Images.ACCEPT_ICON);
			this.okButton.setPreferredSize(new Dimension(130, 32));

			this.cancelButton = new JButton("Cancel", Images.CANCEL_ICON);
			this.cancelButton.setPreferredSize(new Dimension(120, 32));
			this.cancelButton.setSelected(true);

			setBorder(new EmptyBorder(5, 5, 5, 5));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(Box.createHorizontalGlue());
			add(this.okButton);
			add(Box.createHorizontalStrut(16));
			add(this.cancelButton);
		}

		public JButton getOKButton() {
			return this.okButton;
		}

		public JButton getCancelButton() {
			return this.cancelButton;
		}
	}

	class RangeConfigurationPanel extends JPanel {

		private static final long serialVersionUID = 0L;

		JLabel label;
		JSlider slider;
		JSpinner spinner;

		RangeConfigurationPanel(String title, String tooltipText, int min, int max, int minorTickSpacing,
				int majorTickSpacing, int defaultValue, MetalSliderUI sliderUI) {
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(10, 10, 10, 10));

			this.label = createLabel(title);
			this.label.setToolTipText(tooltipText);
			this.label.setPreferredSize(new Dimension(255, 80));
			this.slider = createSlider(min, max, minorTickSpacing, majorTickSpacing, defaultValue, sliderUI);

			this.spinner = createSpinner(min, max, 1, defaultValue);

			this.slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					RangeConfigurationPanel.this.spinner.setValue(RangeConfigurationPanel.this.slider
							.getValue());
				}
			});

			this.spinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					RangeConfigurationPanel.this.slider
							.setValue((Integer) RangeConfigurationPanel.this.spinner.getValue());
				}
			});

			// this.slider.setBorder(new EmptyBorder(0,15,0,15));

			add(this.label, BorderLayout.WEST);
			add(this.slider, BorderLayout.CENTER);
			add(this.spinner, BorderLayout.EAST);
		}

		public int getValue() {
			return this.slider.getValue();
		}

		private JSlider createSlider(int min, int max, int minorTickSpacing, int majorTickSpacing, int defaultValue, MetalSliderUI sliderUI) {
			JSlider slider = new JSlider();
			slider.setMinimum(min);
			slider.setMaximum(max);
			slider.setValue(defaultValue);
			slider.setPaintTicks(true);
			slider.setMinorTickSpacing(minorTickSpacing);
			slider.setMajorTickSpacing(majorTickSpacing);
			slider.setPreferredSize(new Dimension(300, 50));
			slider.setPaintLabels(true);
			slider.setUI(sliderUI);
			slider.setLabelTable(slider.createStandardLabels(50));
			return slider;
		}

		private JLabel createLabel(String title) {
			JLabel label = new JLabel(title);
			// label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setBorder(new EmptyBorder(0, 0, 0, 10));
			return label;
		}

		private JSpinner createSpinner(int min, int max, int stepSize, int defaultValue) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(defaultValue, min, max, stepSize);
			JSpinner spinner = new JSpinner(spinnerNumberModel);
			return spinner;
		}
	}

	public static class GrayscaleSliderUI extends javax.swing.plaf.metal.MetalSliderUI {

		@Override
		public void paintThumb(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int gray = 255 * this.slider.getValue() / this.slider.getMaximum();
			g2.setColor(new Color(gray, gray, gray));
			g2.fillOval(this.thumbRect.x, this.thumbRect.y, this.thumbRect.width - 1,
					this.thumbRect.height - 1);
			g2.setColor(Color.gray);
			g2.drawOval(this.thumbRect.x, this.thumbRect.y, this.thumbRect.width - 1,
					this.thumbRect.height - 1);
		}

		@Override
		public void paintTrack(Graphics g) {
			boolean leftToRight = true;

			g.translate(this.trackRect.x, this.trackRect.y);

			int trackLeft = 0;
			int trackTop = 0;
			int trackRight = 0;
			int trackBottom = 0;

			// Draw the track
			if (this.slider.getOrientation() == SwingConstants.HORIZONTAL) {
				trackBottom = (this.trackRect.height - 1) - getThumbOverhang();
				trackTop = trackBottom - (getTrackWidth() - 1);
				trackRight = this.trackRect.width - 1;
			} else {
				if (leftToRight) {
					trackLeft = (this.trackRect.width - getThumbOverhang()) - getTrackWidth();
					trackRight = (this.trackRect.width - getThumbOverhang()) - 1;
				} else {
					trackLeft = getThumbOverhang();
					trackRight = getThumbOverhang() + getTrackWidth() - 1;
				}
				trackBottom = this.trackRect.height - 1;
			}

			if (this.slider.isEnabled()) {
				g.setColor(MetalLookAndFeel.getControlDarkShadow());
				g.drawRect(trackLeft, trackTop, (trackRight - trackLeft) - 1,
						(trackBottom - trackTop) - 1);

				g.setColor(MetalLookAndFeel.getControlHighlight());
				g.drawLine(trackLeft + 1, trackBottom, trackRight, trackBottom);
				g.drawLine(trackRight, trackTop + 1, trackRight, trackBottom);

				g.setColor(MetalLookAndFeel.getControlShadow());
				g.drawLine(trackLeft + 1, trackTop + 1, trackRight - 2, trackTop + 1);
				g.drawLine(trackLeft + 1, trackTop + 1, trackLeft + 1, trackBottom - 2);

			} else {
				g.setColor(MetalLookAndFeel.getControlShadow());
				g.drawRect(trackLeft, trackTop, (trackRight - trackLeft) - 1,
						(trackBottom - trackTop) - 1);

			}

			int _middleOfThumb = this.thumbRect.x + (this.thumbRect.width / 2);
			_middleOfThumb -= this.trackRect.x; // To compensate for the
			g.setColor(Color.BLACK);
			g.fillRect(trackLeft + 1, trackTop + 1, _middleOfThumb - 2, (trackBottom - trackTop) - 2);
			g.setColor(Color.WHITE);
			g.fillRect(_middleOfThumb, trackTop + 1, (trackRight - trackLeft) - _middleOfThumb - 2,
					(trackBottom - trackTop) - 2);

			// Draw the fill
			if (this.filledSlider) {
				int middleOfThumb = 0;
				int fillTop = 0;
				int fillLeft = 0;
				int fillBottom = 0;
				int fillRight = 0;

				if (this.slider.getOrientation() == SwingConstants.HORIZONTAL) {
					middleOfThumb = this.thumbRect.x + (this.thumbRect.width / 2);
					middleOfThumb -= this.trackRect.x; // To compensate for
					// the
					// g.translate()
					fillTop = !this.slider.isEnabled() ? trackTop : trackTop + 1;
					fillBottom = !this.slider.isEnabled() ? trackBottom - 1 : trackBottom - 2;

					if (!drawInverted()) {
						fillLeft = !this.slider.isEnabled() ? trackLeft : trackLeft + 1;
						fillRight = middleOfThumb;
					} else {
						fillLeft = middleOfThumb;
						fillRight = !this.slider.isEnabled() ? trackRight - 1 : trackRight - 2;
					}

				} else {
					middleOfThumb = this.thumbRect.y + (this.thumbRect.height / 2);
					middleOfThumb -= this.trackRect.y; // To compensate for
					// the
					// g.translate()
					fillLeft = !this.slider.isEnabled() ? trackLeft : trackLeft + 1;
					fillRight = !this.slider.isEnabled() ? trackRight - 1 : trackRight - 2;

					if (!drawInverted()) {
						fillTop = middleOfThumb;
						fillBottom = !this.slider.isEnabled() ? trackBottom - 1 : trackBottom - 2;
					} else {
						fillTop = !this.slider.isEnabled() ? trackTop : trackTop + 1;
						fillBottom = middleOfThumb;
					}
				}

				if (this.slider.isEnabled()) {
					g.setColor(this.slider.getBackground());
					g.drawLine(fillLeft, fillTop, fillRight, fillTop);
					g.drawLine(fillLeft, fillTop, fillLeft, fillBottom);

					// g.setColor( MetalLookAndFeel.getControlShadow() );
					g.setColor(Color.WHITE);
					g.fillRect(middleOfThumb, fillTop + 1, fillRight - fillLeft - middleOfThumb,
							fillBottom - fillTop);
				} else {
					g.setColor(Color.WHITE);
					// g.setColor( MetalLookAndFeel.getControlShadow() );
					g.fillRect(fillLeft, fillTop, fillRight - fillLeft, trackBottom - trackTop);
				}
			}

			g.translate(-this.trackRect.x, -this.trackRect.y);
		}

	}

}
