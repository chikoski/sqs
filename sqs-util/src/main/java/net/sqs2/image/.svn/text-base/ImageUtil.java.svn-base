/*
 * 

 ImageUtil.java

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
 */
package net.sqs2.image;

public class ImageUtil {
/*
	private static final String PERIOD = ".";

	public static final String MULTIPAGE_TIF_ID = "mtif";
	public static final String MULTIPAGE_TIFF_ID = "mtiff";

	public static final String TIF_ID = "tif";
	public static final String TIFF_ID = "tiff";
	public static final String JPG_ID = "jpg";
	public static final String JPEG_ID = "jpeg";
	public static final String PNG_ID = "png";
	public static final String PBM_ID = "pbm";
	public static final String PPM_ID = "ppm";
	public static final String GIF_ID = "gif";
	public static final String PDF_ID = "pdf";

	public static final Map<String, String> MAP = new HashMap<String, String>();
	static {
		MAP.put(TIF_ID, TIFF_ID);
		MAP.put(MULTIPAGE_TIF_ID, TIFF_ID);
		MAP.put(JPG_ID, JPEG_ID);
		for (String type : new String[] { MULTIPAGE_TIFF_ID, TIFF_ID, JPEG_ID, PNG_ID, PBM_ID, PPM_ID, GIF_ID }) {
			MAP.put(type, type);
		}
	}

	public static final String getType(String suffix) throws IOException {
		String type = MAP.get(suffix);
		if (type == null) {
			throw new IOException("Unknown format:" + suffix);
		}
		return type;
	}

	public static final boolean isSupported(String filename) {
		return isMultipageTiff(filename) || isTiff(filename) || isJPEG(filename) || isPNG(filename)
				|| isGIF(filename) || isPBM(filename) || isPPM(filename);
	}

	public static final boolean isPDF(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+PDF_ID);
	}

	public static final boolean isTiff(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+TIF_ID) || name.endsWith("."+TIFF_ID);// || name.equals(TIF_ID)|| name.equals(TIFF_ID)
	}

	public static final boolean isMultipageTiff(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+MULTIPAGE_TIF_ID) || name.endsWith("."+MULTIPAGE_TIFF_ID);// name.equals(MULTIPAGE_TIFF_ID);
	}

	public static final boolean isJPEG(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+JPG_ID) || name.endsWith("."+JPEG_ID);// || name.equals(JPEG_ID)|| name.equals(JPEG_ID);
	}

	public static final boolean isPNG(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+PNG_ID);// || name.equals(PNG_ID);
	}

	public static final boolean isPBM(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+PBM_ID);// || name.equals(PBM_ID);
	}

	public static final boolean isPPM(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+PPM_ID);// || name.equals(PPM_ID);
	}

	public static boolean isGIF(final String filename) {
		String name = filename.toLowerCase();
		return name.endsWith("."+GIF_ID);// || name.equals(GIF_ID);
	}
*/

	/*
	 * public static BufferedImage createRGBImage(BufferedImage src) { if
	 * (src.getColorModel() instanceof IndexColorModel) { BufferedImage newImage
	 * = new BufferedImage(src.getWidth(), src.getHeight(),
	 * BufferedImage.TYPE_INT_RGB); int color; for (int y = 0; y <
	 * src.getHeight(); y++) { for (int x = 0; x < src.getWidth(); x++) { color
	 * = src.getRGB(x, y); newImage.setRGB(x, y, color); } } return newImage; }
	 * return src; }
	 */

	public static final int rgb2gray(final int color) {
		return (((color & 0x00ff0000) >> 16) * 3 + ((color & 0x0000ff00) >> 8) * 6 + ((color & 0x000000ff))) / 10; // NTSC
	}

	public static final void rgb2arr(final int color, final int[] argbarr) {
		argbarr[0] = (color & 0x00ff0000) >> 16;
		argbarr[1] = (color & 0x0000ff00) >> 8;
		argbarr[2] = (color & 0x000000ff);
	}
}
