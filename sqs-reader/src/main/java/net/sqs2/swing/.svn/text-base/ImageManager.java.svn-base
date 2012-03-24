/**
 * 
 */
package net.sqs2.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.base.MarkReaderJarURIContext;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Node;

public class ImageManager {
	public static class AnimatedImageIcon implements Icon {

		private int frameDelayTime = -1;
		private int frameLoopTime = -1;
		private BufferedImage[] images;
		int currentFrame = 0;

		AnimatedImageIcon(InputStream in) throws IOException {
			ImageReader irReader = ImageIO.getImageReadersByFormatName("gif").next();
			ImageInputStream isIn = new MemoryCacheImageInputStream(in);

			irReader.setInput(isIn);

			List<BufferedImage> images = new ArrayList<BufferedImage>();

			for (int index = 0;; index++) {
				try {
					BufferedImage image = irReader.read(index);
					images.add(image);
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
			}

			irReader.dispose();

			this.images = new BufferedImage[images.size()];

			for (int index = 0; index < images.size(); index++) {
				BufferedImage image = images.get(index);
				this.images[index] = image;
				IIOMetadata metadata = irReader.getImageMetadata(index);
				Node root = metadata.getAsTree("javax_imageio_gif_image_1.0");
				for (Node c = root.getFirstChild(); c != null; c = c.getNextSibling()) {
					String name = c.getNodeName();
					if (c instanceof IIOMetadataNode) {
						IIOMetadataNode metaNode = (IIOMetadataNode) c;
						if ("GraphicControlExtension".equals(name)) {
							this.frameDelayTime = Integer.parseInt(metaNode.getAttribute("delayTime"));
						}
					}
				}
			}

			this.frameLoopTime = this.frameDelayTime * this.images.length;
			// System.err.println("D "+this.frameDelayTime);
			// System.err.println("N "+this.images.length);
			// System.err.println("L "+this.frameLoopTime);
		}

		public int getIconHeight() {
			return this.images[getCurrentFrame()].getHeight();
		}

		public int getIconWidth() {
			return this.images[getCurrentFrame()].getWidth();
		}

		public BufferedImage[] getImageArray() {
			return this.images;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.drawImage(this.images[getCurrentFrame()], x, y, c);
		}

		private int getCurrentFrame() {
			if (this.frameDelayTime == 0) {
				this.currentFrame++;
				if (this.currentFrame == this.images.length) {
					this.currentFrame = 0;
				}
				return this.currentFrame;
			}
			return (int) ((System.currentTimeMillis() % this.frameLoopTime) / this.frameDelayTime);
		}
	}

	public static BufferedImage createImage(String path) {
		URL url = null;
		try {
			url = new URL(MarkReaderJarURIContext.getImageBaseURI() + path);
			return ImageFactory.createImage(FilenameUtils.getExtension(path), url.openStream(), 0);
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve: " + url);
		} catch (IOException ignore) {
			throw new RuntimeException("IO error: " + url);
		}
	}

	public static BufferedImage createPattern(String urlString) {
		try {
			URL url = new URL(MarkReaderJarURIContext.getPatternBaseURI() + urlString);
			return ImageFactory.createImage(FilenameUtils.getExtension(urlString), url.openStream(), 0);
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve:" + urlString);
		} catch (IOException ignore) {
			throw new RuntimeException("IO error:" + urlString);
		}
	}

	public static void freeAnimatedImageIcon(ImageManager.AnimatedImageIcon imageIcon) {
		BufferedImage[] images = imageIcon.getImageArray();
		if (images != null) {
			for (Image image : images) {
				image.flush();
			}
		}
	}

	public static Icon createAnimatedImageIcon(String urlString) {
		try {
			URL url = new URL(MarkReaderJarURIContext.getImageBaseURI() + urlString);

			InputStream in = url.openStream();
			Icon imageIcon = new AnimatedImageIcon(in);
			return imageIcon;
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve:" + urlString);
		} catch (IOException ignore) {
			throw new RuntimeException("IO Exception:" + urlString);
		}
	}

	public static ImageIcon createImageIcon(String urlString) {
		try {
			URL url = new URL(MarkReaderJarURIContext.getImageBaseURI() + urlString);
			return new ImageIcon(url);
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve:" + MarkReaderJarURIContext.getImageBaseURI()
					+ urlString);
		}
	}

	static ImageIcon createImageIcon(File file) {
		try {
			return new ImageIcon(file.toURI().toURL());
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve:" + file);
		}
	}
}
