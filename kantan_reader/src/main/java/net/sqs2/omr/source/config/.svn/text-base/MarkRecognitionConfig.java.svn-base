package net.sqs2.omr.source.config;

import java.io.Serializable;

public class MarkRecognitionConfig implements Serializable {
	private static final long serialVersionUID = 3L;
	
	public static final String AVERAGE_DENSITY = "averageDensity";
	public static final String CONVOLUTION3X3_AVERAGE_DENSITY = "convolution3x3AverageDensity";
	
	String algorithm = AVERAGE_DENSITY;
	float noMarkThreshold = (55.0f/255);
	float density = 0.94f;
	float recognitionMargin = 0.03f;
	float resolutionScale = 3f;
	int horizontalMargin = 1;
	int verticalMargin = -1;
	
	public static MarkRecognitionConfig DEFAULT_INSTANCE = new MarkRecognitionConfig();
	
	public MarkRecognitionConfig(){}
	
	public String getAlgorithm(){
		return this.algorithm;
	}
	public void setAlgorithm(String algorithm){
		this.algorithm = algorithm;
	}
	
	public float getDensity() {
		return density;
	}

	public float getRecognitionMargin() {
		return recognitionMargin;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public void setRecognitionMargin(float recognitionMargin) {
		this.recognitionMargin = recognitionMargin;
	}

	public float getResolutionScale() {
		return resolutionScale;
	}
	public void setResolutionScale(float resolutionScale) {
		this.resolutionScale = resolutionScale;
	}

	public int getHorizontalMargin() {
		return horizontalMargin;
	}
	
	public void setHorizontalMargin(int horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}
	
	public int getVerticalMargin() {
		return verticalMargin;
	}
	public void setVerticalMargin(int verticalMargin) {
		this.verticalMargin = verticalMargin;
	}
	
	public float getNoMarkThreshold(){
		return this.noMarkThreshold;
	}
	
	public void setNoMarkThreshold(float noMarkThreshold){
		this.noMarkThreshold = noMarkThreshold;
	}
	
}
