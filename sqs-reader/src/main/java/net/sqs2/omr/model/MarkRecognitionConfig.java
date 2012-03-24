package net.sqs2.omr.model;

import java.io.Serializable;

public class MarkRecognitionConfig implements Serializable {
	private static final long serialVersionUID = 4L;

	public static final String HORIZONTAL_SLICES_AVERAGE_DENSITY = "horizontalSlicesAverageDensity";
	public static final String VERTICAL_SLICES_AVERAGE_DENSITY = "verticalSlicesAverageDensity";
	public static final String CONVOLUTION5x3_AVERAGE_DENSITY = "convolution5x3AverageDensity";
	public static final String CONVOLUTION5x3_AVERAGE_DENSITY_WITH_DEBUGOUT = "convolution5x3AverageDensityWithDebugOut";

	String algorithm = VERTICAL_SLICES_AVERAGE_DENSITY;
	
	float markRecognitionDensityThreshold = 0.94f;
	float doubleMarkErrrorSuppressionThreshold = 0.03f;
	float noMarkErrorSuppressionThreshold = (55.0f / 255);
	
	float resolutionScale = 1.0f;
	float horizontalTrim = 0f;
	float verticalTrim = 0f;
	float horizontalMargin = 0f;
	float verticalMargin = 0f;

	public MarkRecognitionConfig() {
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public float getMarkRecognitionDensityThreshold() {
		return this.markRecognitionDensityThreshold;
	}

	public void setMarkRecognitionDensityThreshold(float markRecognitionDensityThreshold) {
		this.markRecognitionDensityThreshold = markRecognitionDensityThreshold;
	}

	public float getDoubleMarkErrorSuppressionThreshold() {
		return this.doubleMarkErrrorSuppressionThreshold;
	}

	public void setDoubleMarkErrorSuppressionThreshold(float doubleMarkErrorSuppressionThreshold) {
		this.doubleMarkErrrorSuppressionThreshold = doubleMarkErrorSuppressionThreshold;
	}

	public float getResolutionScale() {
		return this.resolutionScale;
	}

	public void setResolutionScale(float resolutionScale) {
		this.resolutionScale = resolutionScale;
	}

	public float getHorizontalTrim() {
		return horizontalTrim;
	}

	public void setHorizontalTrim(float horizontalTrim) {
		this.horizontalTrim = horizontalTrim;
	}

	public float getVerticalTrim() {
		return verticalTrim;
	}

	public void setVerticalTrim(float verticalTrim) {
		this.verticalTrim = verticalTrim;
	}

	public float getHorizontalMargin() {
		return this.horizontalMargin;
	}

	public void setHorizontalMargin(float horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}

	public float getVerticalMargin() {
		return this.verticalMargin;
	}

	public void setVerticalMargin(float verticalMargin) {
		this.verticalMargin = verticalMargin;
	}

	public float getNoMarkErrorSuppressionThreshold() {
		return this.noMarkErrorSuppressionThreshold;
	}

	public void setNoMarkErrorSuppressionThreshold(float noMarkErrorSuppressionThreshold) {
		this.noMarkErrorSuppressionThreshold = noMarkErrorSuppressionThreshold;
	}

}
