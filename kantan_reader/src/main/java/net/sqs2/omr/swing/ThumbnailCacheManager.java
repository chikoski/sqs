/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sqs2.image.ImageFactory;

import org.apache.commons.collections15.map.LRUMap;

public class ThumbnailCacheManager{

	static ExecutorService service = Executors.newFixedThreadPool(16);
	static LRUMap<File,ThumbnailEntry> map = new LRUMap<File,ThumbnailEntry>(32); 
	
	static public class ThumbnailEntry{
		Image image;
		int originalImageWidth;
		int originalImageHight;
		
		public ThumbnailEntry(Image image, int width, int height){
			this.image = image;
			this.originalImageWidth = width;
			this.originalImageHight = height;
		}

		public Image getImage() {
			return image;
		}

		public int getOriginalImageWidth() {
			return originalImageWidth;
		}

		public int getOriginalImageHeight() {
			return originalImageHight;
		}
		
	}
	
	public static ThumbnailEntry loadImage(final File file){
		try {
			return service.submit(new Callable<ThumbnailEntry>(){
				public ThumbnailEntry call(){
					ThumbnailEntry entry = get(file);
					if(entry != null){
						return entry;
					}
					try{
						BufferedImage image = ImageFactory.createImage(file);
						int sWidth = image.getWidth();
						int sHeight = image.getHeight();
						int tHeight = 64;
						int tWidth = tHeight * sWidth / sHeight; 
				    
						ImageFilter filter = new AreaAveragingScaleFilter(tWidth, tHeight);
						ImageProducer im = new FilteredImageSource(image.getSource(), filter);
						Image thumbnail = Toolkit.getDefaultToolkit().createImage(im);
						entry = new ThumbnailEntry(thumbnail, sWidth, sHeight);
						put(file, entry);
						image.flush();
						return entry;
					}catch(IOException ex){
						return null;
					}
				}
			}).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static synchronized ThumbnailEntry get(File file){
		return map.get(file);
	}

	private static synchronized void put(File file, ThumbnailEntry entryValue){
		map.put(file, entryValue);
	}
}