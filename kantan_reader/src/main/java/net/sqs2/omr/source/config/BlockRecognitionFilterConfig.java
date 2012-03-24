/**
 *  BlockRecoConfig.java

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

 Created on 2007/04/07
 Author hiroya
 */
package net.sqs2.omr.source.config;

import java.io.Serializable;

public class BlockRecognitionFilterConfig implements Serializable{
	private static final long serialVersionUID = 0L;
	float widthMax;
	float widthMin;
	float heightMax;
	float heightMin;
	float density;
	
	public BlockRecognitionFilterConfig(){}

	public float getWidthMax() {
		return widthMax;
	}

	public void setWidthMax(float wmax) {
		this.widthMax = wmax;
	}

	public float getWidthMin() {
		return widthMin;
	}

	public void setWidthMin(float widthMin) {
		this.widthMin = widthMin;
	}

	public float getHeightMax() {
		return heightMax;
	}

	public void setHeightMax(float heightMax) {
		this.heightMax = heightMax;
	}

	public float getHeightMin() {
		return heightMin;
	}

	public void setHeightMin(float heightMin) {
		this.heightMin = heightMin;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

}