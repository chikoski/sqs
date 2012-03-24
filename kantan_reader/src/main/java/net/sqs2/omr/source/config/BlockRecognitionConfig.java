package net.sqs2.omr.source.config;

import java.io.Serializable;

public class BlockRecognitionConfig implements Serializable {
	private static final long serialVersionUID = 0L;
	
	private float horizontal;
	private float vertical;
	private float density;
	
	public float getHorizontal() {
		return horizontal;
	}
	public void setHorizontal(float horizontal) {
		this.horizontal = horizontal;
	}
	public float getVertical() {
		return vertical;
	}
	public void setVertical(float vertical) {
		this.vertical = vertical;
	}
	public float getDensity() {
		return density;
	}
	public void setDensity(float density) {
		this.density = density;
	}
	
}
