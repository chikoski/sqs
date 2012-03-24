package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.xml.XMLFilterUtil;

public class ConfigXMLFileFilter {

	File target;
	String densityThreshold = null;
	String doubleMarkIgnoranceThreshold = null;
	String noMarkRecoveryThreshold = null;
	
	private static final String DENSITY_XPATH = "/config/sources/source/markRecognition/@density";
	private static final String DOUBLE_MARK_IGNORANCE_THRESHOLD_XPATH = "/config/sources/source/markRecognition/@doubleMarkIgnoranceThreshold";
	private static final String NO_MARK_RECOVERY_THRESHOLD_XPATH = "/config/sources/source/markRecognition/@noMarkRecoveryThreshold";

	public ConfigXMLFileFilter(File target) {
		this.target = target;
		try {
			String[] values = XMLFilterUtil.selectValues(this.target, new String[] { DENSITY_XPATH,
					DOUBLE_MARK_IGNORANCE_THRESHOLD_XPATH,
					NO_MARK_RECOVERY_THRESHOLD_XPATH});
			this.densityThreshold = values[0];
			this.doubleMarkIgnoranceThreshold = values[1];
			this.noMarkRecoveryThreshold = values[2];
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).info(ex.getLocalizedMessage());
			this.densityThreshold = null;
			this.doubleMarkIgnoranceThreshold = null;
			this.noMarkRecoveryThreshold = null;
		}
	}

	public void overwriteValues(float densityThreshold, float doubleMarkIgnoranceThreshold, float noMarkRecoveryThreshold) {
		String[][] args = new String[][] { { DENSITY_XPATH, Float.toString(densityThreshold) },
				{ DOUBLE_MARK_IGNORANCE_THRESHOLD_XPATH, Float.toString(doubleMarkIgnoranceThreshold) },
				{ NO_MARK_RECOVERY_THRESHOLD_XPATH, Float.toString(noMarkRecoveryThreshold) }};
		this.densityThreshold = args[0][1];
		this.doubleMarkIgnoranceThreshold = args[1][1];
		this.noMarkRecoveryThreshold = args[2][1];
		XMLFilterUtil.overwrite(this.target, args);
	}

	public String getDensityThreshold() {
		return this.densityThreshold;
	}

	public String getDoubleMarkIgnoranceThreshold() {
		return this.doubleMarkIgnoranceThreshold;
	}
	
	public String getNoMarkRecoveryThreshold(){
		return this.noMarkRecoveryThreshold;
	}
}
