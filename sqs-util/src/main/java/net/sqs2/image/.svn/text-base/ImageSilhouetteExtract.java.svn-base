/*

 ImageSihouetteExtract.java

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

 Created on Oct 6, 2007

 */
package net.sqs2.image;

import java.util.HashSet;
import java.util.Set;

/**
 * Silhouette extraction, set index value and calculate area value from source
 * image.
 */
public class ImageSilhouetteExtract {

	private static final boolean NEIGHBOR_CHECK = false;

	private int width;
	private int[] silhouetteIndexArray;
	private int[] reindexTable;
	private int[] areaArray;
	private int areaTotal;

	/**
	 * Silhouette Extraction.
	 * 
	 * @param sourceImage
	 *            boolean array of source image
	 * @param width
	 *            width of source image
	 * @param height
	 *            height of source image
	 */
	public ImageSilhouetteExtract(boolean[] sourceImage, int width, int height) {
		this.width = width;
		this.silhouetteIndexArray = new int[width * height];
		this.reindexTable = createSilhouetteIndexArrayAndReindexTable(sourceImage, width, height,
				this.silhouetteIndexArray);

		this.areaArray = new int[this.reindexTable.length];
		this.areaTotal = reindexSilhouetteIndexArray(width, height, this.silhouetteIndexArray,
				this.reindexTable, this.areaArray);
	}

	/**
	 * @return Pixel value array. Each pixel values is silhouette index by
	 *         pixelIndex.
	 */
	public int[] getSilhouetteIndexArray() {
		return this.silhouetteIndexArray;
	}

	/**
	 * @param x
	 *            x position of source image
	 * @param y
	 *            y position of source image
	 * @return Pixel value. The value is silhouette index.
	 */
	public int getSilhouetteIndexAt(int x, int y) {
		return this.silhouetteIndexArray[x + this.width * y];
	}

	/**
	 * @return Area value array by silhouette index.
	 */
	public int[] getAreaArray() {
		return this.areaArray;
	}

	/**
	 * @param silhouetteIndex
	 *            silhouette index.
	 * @return Area value.
	 */
	public int getAreaAt(int silhouetteIndex) {
		return this.areaArray[silhouetteIndex];
	}

	/**
	 * @return sum of area values in this source image.
	 */
	public int getAreaTotal() {
		return this.areaTotal;
	}

	static class IntegerSet extends HashSet<Integer> {
		private static final long serialVersionUID = 0L;
	}

	static private int[] createSilhouetteIndexArrayAndReindexTable(boolean[] src, int width, int height, int[] silhouetteIndexArray) {
		Set<Integer>[] neighborSilhouetteIndexSetArray = new IntegerSet[silhouetteIndexArray.length]; // max
																										// neighborSilhouetteIndicesSetArray
		int silhouetteIndexMax = createSilhouetteIndexArray(src, width, height, silhouetteIndexArray,
				neighborSilhouetteIndexSetArray);
		return createReindexTable(neighborSilhouetteIndexSetArray, silhouetteIndexMax + 1);
	}

	static private boolean isBlack(boolean value) {
		return value;
	}

	static private boolean isWhite(boolean value) {
		return !value;
	}

	static private int createSilhouetteIndexArray(boolean[] src, int width, int height, int[] silhouetteIndexArray, Set<Integer>[] neighborSilhouetteIndexSetArray) {
		int currentSilhouetteIndex = createSilhouetteIndexArrayOfTopEdge(src, width, silhouetteIndexArray);
		return createSilhouetteIndexArrayOfBody(src, width, height, currentSilhouetteIndex,
				silhouetteIndexArray, neighborSilhouetteIndexSetArray);
	}

	static private int createSilhouetteIndexArrayOfTopEdge(boolean[] src, int width, int[] silhouetteIndexArray) {
		int currentSilhouetteIndex = 0;
		if (isBlack(src[0])) {
			silhouetteIndexArray[0] = ++currentSilhouetteIndex;
		}
		for (int x = 1; x < width; x++) {
			if (isBlack(src[x])) {
				if (isBlack(src[x - 1])) {
					silhouetteIndexArray[x] = silhouetteIndexArray[x - 1];
				} else {
					silhouetteIndexArray[x] = ++currentSilhouetteIndex;
				}
			}
		}
		return currentSilhouetteIndex;
	}

	static private int createSilhouetteIndexArrayOfBody(boolean[] src, int width, int height, int currentSilhouetteIndex, int[] silhouetteIndexArray, Set<Integer>[] neighborSilhouetteIndicesSetArray) {
		int pixelIndex = width;

		for (int y = 1; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!isBlack(src[pixelIndex])) {
					// do nothing
				} else if (NEIGHBOR_CHECK && getNeighborNumBlocks(src, width, height, pixelIndex, x, y) < 4) {
					// do nothing
				} else {
					int neighborSilhouetteIndex = getNeighborSilhouetteIndexIfAny(src, width,
							silhouetteIndexArray, neighborSilhouetteIndicesSetArray, pixelIndex, x);
					if (neighborSilhouetteIndex == 0) {
						silhouetteIndexArray[pixelIndex] = ++currentSilhouetteIndex;
					}
				}
				pixelIndex++;
			}
		}
		return currentSilhouetteIndex;
	}

	private static int getNeighborSilhouetteIndexIfAny(boolean[] src, int width, int[] silhouetteIndexArray, Set<Integer>[] neighborSilhouetteIndicesSetArray, int pixelIndex, int x) {
		int neighborSilhouetteIndex = 0;
		if (0 < x && isBlack(src[pixelIndex - 1])) {// west
			neighborSilhouetteIndex = silhouetteIndexArray[pixelIndex] = silhouetteIndexArray[pixelIndex - 1];
		}

		if (x < width - 1 && isBlack(src[pixelIndex - width + 1])) {// northeast
			neighborSilhouetteIndex = updateNeighborSilhouetteIndex(neighborSilhouetteIndex,
					silhouetteIndexArray, pixelIndex, neighborSilhouetteIndicesSetArray,
					silhouetteIndexArray[pixelIndex - width + 1], silhouetteIndexArray[pixelIndex]);
		}

		if (isBlack(src[pixelIndex - width])) {// north
			neighborSilhouetteIndex = updateNeighborSilhouetteIndex(neighborSilhouetteIndex,
					silhouetteIndexArray, pixelIndex, neighborSilhouetteIndicesSetArray,
					silhouetteIndexArray[pixelIndex - width], silhouetteIndexArray[pixelIndex]);
		}

		if (0 < x && isBlack(src[pixelIndex - width - 1])) { // northwest
			neighborSilhouetteIndex = updateNeighborSilhouetteIndex(neighborSilhouetteIndex,
					silhouetteIndexArray, pixelIndex, neighborSilhouetteIndicesSetArray,
					silhouetteIndexArray[pixelIndex - width - 1], silhouetteIndexArray[pixelIndex]);
		}
		return neighborSilhouetteIndex;
	}

	private static int getNeighborNumBlocks(boolean[] src, int width, int height, int pixelIndex, int x, int y) {
		int numBlacks = 0;
		if (!NEIGHBOR_CHECK) {
			return 0;
		}
		if (x == width - 1 || isWhite(src[pixelIndex - width + 1])) {// north
																		// east
			numBlacks++;
		}
		if (isWhite(src[pixelIndex - width])) { // north
			numBlacks++;
		}
		if (x == 0 || isWhite(src[pixelIndex - width - 1])) {// north west
			numBlacks++;
		}
		if (x == width - 1 || isWhite(src[pixelIndex + 1])) {// east
			numBlacks++;
		}
		if (x == 0 || isWhite(src[pixelIndex - 1])) {// west
			numBlacks++;
		}
		if (y == height - 1 || x == width - 1 || isWhite(src[pixelIndex + width + 1])) {// south
																						// east
			numBlacks++;
		}
		if (y == height - 1 || isWhite(src[pixelIndex + width])) { // south
			numBlacks++;
		}
		if (y == height - 1 || x == 0 || isWhite(src[pixelIndex + width - 1])) { // south
																					// west
			numBlacks++;
		}
		return numBlacks;
	}

	static private int updateNeighborSilhouetteIndex(int updatedNeighborSilhouetteIndex, int[] silhouetteIndexArray, int index, Set<Integer>[] neighborSilhouetteIndicesSetArray, int neighborSilhouetteIndex, int targetIndex) {
		if (updatedNeighborSilhouetteIndex == 0) {
			updatedNeighborSilhouetteIndex = silhouetteIndexArray[index] = neighborSilhouetteIndex;
		} else {
			addRelations(neighborSilhouetteIndicesSetArray, neighborSilhouetteIndex, targetIndex);
		}
		return updatedNeighborSilhouetteIndex;
	}

	static private void addRelations(Set<Integer>[] neighborSilhouetteIndicesSetArray, int currentIndex, int neighborSilhouetteIndex) {
		getNeighborSilhouetteIndicesSet(neighborSilhouetteIndicesSetArray, currentIndex).add(
				neighborSilhouetteIndex);
		getNeighborSilhouetteIndicesSet(neighborSilhouetteIndicesSetArray, neighborSilhouetteIndex).add(
				currentIndex);
	}

	static private Set<Integer> getNeighborSilhouetteIndicesSet(Set<Integer>[] neighborSilhouetteIndicesSetArray, int index) {
		Set<Integer> neighborSilhouetteIndicesSet = neighborSilhouetteIndicesSetArray[index];
		if (neighborSilhouetteIndicesSet == null) {
			neighborSilhouetteIndicesSet = neighborSilhouetteIndicesSetArray[index] = new IntegerSet();
		}
		return neighborSilhouetteIndicesSet;
	}

	static private int[] createReindexTable(Set<Integer>[] neighborSilhouetteIndicesSetArray, int reindexTableLength) {
		int[] reindexTable = new int[reindexTableLength];
		for (int index = 1; index < reindexTableLength; index++) {
			if (reindexTable[index] == 0) {
				Set<Integer> flattenNeighborSilhouetteIndicesSet = new HashSet<Integer>();
				flatValues(flattenNeighborSilhouetteIndicesSet, neighborSilhouetteIndicesSetArray,
						reindexTable, index);
				for (int targetIndex : flattenNeighborSilhouetteIndicesSet) {
					reindexTable[targetIndex] = index;
				}
			}
		}
		return reindexTable;
	}

	static private Set<Integer> flatValues(Set<Integer> flattenNeighborSilhouetteIndices, Set<Integer>[] neighborSilhouetteIndicesSetArray, int[] reindexTable, int silhouetteIndex) {
		flattenNeighborSilhouetteIndices.add(silhouetteIndex);
		Set<Integer> neighborSilhouetteIndicesSet = neighborSilhouetteIndicesSetArray[silhouetteIndex];
		if (neighborSilhouetteIndicesSet != null) {
			for (Object o : neighborSilhouetteIndicesSet) {
				int subSilhouetteIndex = (Integer) o;
				if (reindexTable[subSilhouetteIndex] == 0 && !flattenNeighborSilhouetteIndices.contains(o)) {
					flatValues(flattenNeighborSilhouetteIndices, neighborSilhouetteIndicesSetArray,
							reindexTable, subSilhouetteIndex);
				}
			}
		}
		return flattenNeighborSilhouetteIndices;
	}

	static private int reindexSilhouetteIndexArray(int width, int height, int[] silhouetteIndexArray, int[] reindexTable, int[] areaArray) {
		int pixelIndex = width * height - 1;
		int areaTotal = 0;
		for (int y = height - 1; 0 <= y; y--) {
			for (int x = width - 1; 0 <= x; x--) {
				int silhouetteIndex = silhouetteIndexArray[pixelIndex];
				int updatedSilhouetteIndex = reindexTable[silhouetteIndex];
				if (updatedSilhouetteIndex != 0 && silhouetteIndex != updatedSilhouetteIndex) {
					silhouetteIndex = silhouetteIndexArray[pixelIndex] = updatedSilhouetteIndex; // reindex
				}
				areaArray[silhouetteIndex]++;
				areaTotal++;
				pixelIndex--;
			}
		}
		return areaTotal;
	}
}
