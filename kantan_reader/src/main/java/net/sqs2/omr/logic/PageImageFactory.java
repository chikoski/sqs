/**
 * 
 */
package net.sqs2.omr.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.source.config.FrameConfig;
import net.sqs2.omr.source.config.MarkRecognitionConfig;
import net.sqs2.omr.source.config.PageGuideAreaConfig;
import net.sqs2.omr.source.config.SourceConfig;
import net.sqs2.omr.task.FormAreaCommand;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.omr.task.TaskError;
import net.sqs2.omr.task.TaskExceptionCore;
import net.sqs2.omr.task.TaskResult;

public class PageImageFactory{

	public static BufferedImage createImage(long sessionID, SessionSource sessionSource, PageMaster master,
			SourceDirectory sourceDirectory, int rowIndex, int pageIndex, String mode)
			throws IOException {
		
		float densityThreshold = sourceDirectory.getConfiguration().getConfig().getSourceConfig().getMarkRecognitionConfig().getDensity();
		PageID pageID = sourceDirectory.getPageID(rowIndex * master.getNumPages() + pageIndex);
		BufferedImage src = ImageFactory.createImage(new File(sourceDirectory.getDirectory(), pageID.getFileResourceID().getRelativePath()), pageID.getIndex());
		Logger.getAnonymousLogger().info("create image width="+src.getWidth()+", height"+ src.getHeight()+"");
		BufferedImage image = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		copyImage(src, image);
		if(mode == null){
			TaskAccessor pageTaskAccessor = sessionSource.getSessionSourceContentAccessor().getPageTaskAccessor();
			PageTask pageTask = pageTaskAccessor.get(master, pageIndex + 1, pageID);
			SourceDirectoryConfiguration config = getSourceDirectoryConfiguration(sessionSource, pageTask);
			drawPageState(sessionSource, pageTask, config, pageIndex, densityThreshold, master, pageID, image);
		}
		image.getGraphics().finalize();
		return image;
	}
	
	private static SourceDirectoryConfiguration getSourceDirectoryConfiguration(SessionSource sessionSource, PageTask pageTask)throws IOException{
		return sessionSource.getConfigHandler(pageTask.getConfigHandlerFileResourceID().getRelativePath(),
				pageTask.getConfigHandlerFileResourceID().getLastModified());
	}
	
	private static void copyImage(BufferedImage src, BufferedImage image) {
		Graphics2D g = (Graphics2D)image.getGraphics();
		int w = image.getWidth(); 
		int h = image.getHeight(); 
		g.drawImage(src, 0, 0, w, h, null);
		src.flush();
	}

	private static final Color blue = new Color(0, 0, 255, 50);
	private static final Color red = new Color(255, 0, 0, 100);

	private static void drawPageState(SessionSource sessionSource, PageTask pageTask,
			SourceDirectoryConfiguration configHandler, int pageIndex,
			float densityThreshold, PageMaster master, PageID pageID, BufferedImage image) throws IOException{
		
		TaskResult pageTaskResult  = pageTask.getTaskResult();
		TaskError pageTaskError = pageTask.getTaskError();

		SourceConfig sourceConfig = configHandler.getConfig().getSourceConfig(pageTask);
		FrameConfig frameConfig = sourceConfig.getFrameConfig();
		PageGuideAreaConfig pageGuideAreaConfig = frameConfig.getPageGuideAreaConfig();
		MarkRecognitionConfig markRecognizationConfig = sourceConfig.getMarkRecognitionConfig();
		
		float headerVerticalMargin = pageGuideAreaConfig.getHeaderVerticalMargin();
		float footerVerticalMargin = pageGuideAreaConfig.getFooterVerticalMargin();
		float pageGuideAreaHeight = pageGuideAreaConfig.getHeight();
		float pageGuideAreaHorizontalMargin = pageGuideAreaConfig.getHeaderVerticalMargin();

		int w = image.getWidth(); 
		int h = image.getHeight(); 
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor(blue);
		g.fillRect((int)(w*pageGuideAreaHorizontalMargin), (int)(h*headerVerticalMargin), (int)(w - 2 * w*pageGuideAreaHorizontalMargin), (int)(h * pageGuideAreaHeight));
		g.fillRect((int)(w*pageGuideAreaHorizontalMargin), (int)(h - h*(footerVerticalMargin + pageGuideAreaHeight)- 1), (int)(w - 2 * w*pageGuideAreaHorizontalMargin), (int)(h * pageGuideAreaHeight));

		if(pageTaskResult != null){
			Point[] corners = pageTaskResult.getCorners();
			PageSource pageSource = drawCorners(master, corners, image, g);
			drawFormAreas(pageIndex, densityThreshold, (FormMaster)master, pageTaskResult, g, markRecognizationConfig, pageSource);
		}
		if(pageTaskError != null){
			TaskExceptionCore core = pageTaskError.getExceptionCore();
			if(core instanceof PageFrameExceptionCore){
				PageFrameExceptionCore c = (PageFrameExceptionCore)core;
				drawCorners(master, c.getCorners(), image, g);
			}
		}
	}

	private static void drawFormAreas(int pageIndex, float densityThreshold,
			FormMaster master, TaskResult pageTaskResult, Graphics2D g,
			MarkRecognitionConfig markRecognizationConfig, PageSource pageSource) {
		int formAreaIndexInPage = 0;
		for(FormArea formArea: master.getFormAreaListByPageIndex(pageIndex)){
			FormAreaCommand command = (FormAreaCommand)pageTaskResult.getPageAreaCommandList().get(formAreaIndexInPage);
			if(formArea.isMarkArea()){
				if(command.getDensity() < densityThreshold){
					g.setColor(red);
				}else{
					g.setColor(blue);
				}
				g.fillPolygon(pageSource.createRectPolygon(getExtendedRectangle(formArea.getRect(),
						markRecognizationConfig.getHorizontalMargin() , 
						markRecognizationConfig.getVerticalMargin() )));
				g.drawPolygon(pageSource.createRectPolygon(getExtendedRectangle(formArea.getRect(),
						markRecognizationConfig.getHorizontalMargin() + 3 ,
						markRecognizationConfig.getVerticalMargin() + 3)));
			}else{
				g.setColor(red);
				g.fillPolygon(pageSource.createRectPolygon(formArea.getRect()));
			}
			formAreaIndexInPage++;
		}
	}

	private static PageSource drawCorners(PageMaster master,
			Point[] corners, BufferedImage image, Graphics2D g) {
		PageSource pageSource = new PageSource(image, master.getCorners(), corners);
		g.setColor(red);
		g.setStroke(new BasicStroke(5));
		double size = 40;
		for(int y = 0; y < 2; y++){
			int i = y*2;
			g.fillPolygon(pageSource.createPolygon(new Point2D[]{corners[i], new Point2D.Double(corners[i].x-1*size, corners[i].y+1*size), new Point2D.Double(corners[i].x-1*size, corners[i].y-1*size) }));
			i++;
			g.fillPolygon(pageSource.createPolygon(new Point2D[]{corners[i], new Point2D.Double(corners[i].x+1*size, corners[i].y-1*size), new Point2D.Double(corners[i].x+1*size, corners[i].y+1*size) }));
		}
		return pageSource;
	}
	
	private static Rectangle getExtendedRectangle(Rectangle rect, int horizontal, int vertical){
		return new Rectangle(rect.x - horizontal, rect.y - vertical, rect.width + horizontal * 2, rect.height + vertical * 2);
	}	
}