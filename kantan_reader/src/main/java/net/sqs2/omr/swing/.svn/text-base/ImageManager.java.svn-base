package net.sqs2.omr.swing;

import java.awt.Image;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import net.sqs2.image.ImageManagerUtil;
import net.sqs2.omr.MarkReaderJarURIContext;
import net.sqs2.omr.app.App;


public class ImageManager {

	/*
	static{
		URL.setURLStreamHandlerFactory(new ClassURLStreamHandlerFactory());
	}*/
	
	//private static Map<Icon,BufferedImage[]> icon2ImageArrayMap = new ConcurrentHashMap<Icon,BufferedImage[]>();
	
	public static final ImageIcon FOLDER_OPEN_ICON = ImageManagerUtil.createImageIcon(createURL("folder.open.gif"));
	public static final ImageIcon FOLDER_ICON = ImageManagerUtil.createImageIcon(createURL("folder.gif"));
	public static final ImageIcon DIR_ICON = ImageManagerUtil.createImageIcon(createURL("dir0.gif"));
	
	public static final ImageIcon STOP_ICON = ImageManagerUtil.createImageIcon(createURL("Stop24.gif"));
	public static final ImageIcon REFRESH_ICON = ImageManagerUtil.createImageIcon(createURL("Refresh24.gif"));
	//public static final ImageIcon OPEN24_ICON = ImageManager.createImageIcon("Open24.gif");
	public static final ImageIcon OPEN_ICON = ImageManagerUtil.createImageIcon(createURL("Open16.gif"));
	public static final ImageIcon PREF_ICON = ImageManagerUtil.createImageIcon(createURL("Preferences16.gif"));
	public static final ImageIcon AUTH_ICON = ImageManagerUtil.createImageIcon(createURL("Bookmarks16.gif"));
	
	public static final ImageIcon HELP_ICON = ImageManagerUtil.createImageIcon(createURL("Help16.gif"));
	public static final ImageIcon INFO_ICON = ImageManagerUtil.createImageIcon(createURL("Information16.gif"));
	public static final ImageIcon ABOUT_ICON = ImageManagerUtil.createImageIcon(createURL("About16.gif"));
	public static final ImageIcon CONFIG_ICON = ImageManagerUtil.createImageIcon(createURL("Preferences24.gif"));
	
	public static final Image TRIANGLE_DOWN_IMAGE = ImageManagerUtil.createImage(createURL("triangle.down.gif"));
	public static final Image TRANSPARENT_16X16 = ImageManagerUtil.createImage(createURL("16x16transparent.gif"));
	
	public static final ImageIcon DND_HERE_ICON = ImageManagerUtil.createImageIcon(createURL(App.MAIN_ICON));
	
	public static URL createURL(String path){
		try {
			return new URL(MarkReaderJarURIContext.getImageBaseURI()+path);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}

