package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sqs2.image.ImageFactory;

import org.apache.commons.collections15.map.LRUMap;


public class ImageViewApp{
	
	public static final double MASTER_SCALE_DEFAULT = 0.06;
	public static final double SLAVE_SCALE_DEFAULT = 0.4;
	public static final double ON_THE_FLY_SCALING_THRESHOLD = 1.0;

	public static final double SCALEMETER_STEP = 10;
	
	public static final int MODE_DUAL_FRMAE = 0;
	public static final int MODE_SPLIT_PANE = 1;
	
	public static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	public static final Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	
	public static void main(String args[])throws Exception{
		File imageFile = new File(args[0]);
		new ImageViewApp(MODE_SPLIT_PANE, imageFile, JFrame.EXIT_ON_CLOSE);
	}
	
	static public class ImageViewAppPanel extends JPanel{

		public ImageViewAppPanel(File imageFile)throws IOException{
			BufferedImage srcImage = ImageFactory.createImage(imageFile);
			ImageViewScope imageViewScope = new ImageViewScope(srcImage, SLAVE_SCALE_DEFAULT);
			
			ImageViewPanel masterViewPanel = new ImageViewPanel(imageViewScope, true, MASTER_SCALE_DEFAULT);
			ImageViewPanel slaveViewPanel = new ImageViewPanel(imageViewScope, false, SLAVE_SCALE_DEFAULT);
			
			ImageViewScrollPanel masterPanel = new ImageViewScrollPanel(true, masterViewPanel);
			ImageViewScrollPanel slavePanel = new ImageViewScrollPanel(false, slaveViewPanel);

			imageViewScope.addObserver(masterPanel);
			imageViewScope.addObserver(slavePanel);

			final ImageViewScrollPanelController masterPanelController = new ImageViewScrollPanelController(imageViewScope, masterPanel, masterViewPanel);
			final ImageViewScrollPanelController slavePanelController = new ImageViewScrollPanelController(imageViewScope, slavePanel, slaveViewPanel);

			JSplitPane splitPane = new JSplitPane();
			splitPane.setLeftComponent(masterPanel);
			slavePanel.setPreferredSize(new Dimension(500, 200));
			splitPane.setRightComponent(slavePanel);
			
			this.setLayout(new BorderLayout());
			this.add(splitPane);
			
			this.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					slavePanelController.updateScale(e);
				}
			});
		}
	}
	
	public ImageViewApp(File imageFile)throws Exception{

		BufferedImage srcImage = ImageFactory.createImage(imageFile);
		ImageViewScope imageViewScope = new ImageViewScope(srcImage, SLAVE_SCALE_DEFAULT);
		
		ImageViewPanel masterViewPanel = new ImageViewPanel(imageViewScope, true, MASTER_SCALE_DEFAULT);
		ImageViewPanel slaveViewPanel = new ImageViewPanel(imageViewScope, false, SLAVE_SCALE_DEFAULT);
		
		ImageViewScrollPanel masterPanel = new ImageViewScrollPanel(true, masterViewPanel);
		ImageViewScrollPanel slavePanel = new ImageViewScrollPanel(false, slaveViewPanel);

		imageViewScope.addObserver(masterPanel);
		imageViewScope.addObserver(slavePanel);

		final ImageViewScrollPanelController masterPanelController = new ImageViewScrollPanelController(imageViewScope, masterPanel, masterViewPanel);
		final ImageViewScrollPanelController slavePanelController = new ImageViewScrollPanelController(imageViewScope, slavePanel, slaveViewPanel);

		JSplitPane pane = new JSplitPane();
		pane.setLeftComponent(masterPanel);
		slavePanel.setPreferredSize(new Dimension(500, 200));
		pane.setRightComponent(slavePanel);
		
		/*
		JFrame frame = new JFrame();
		frame.setTitle(imageFile.getName());
		frame.setLayout(new BorderLayout());
		frame.add(pane);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
				slavePanelController.updateScale(e);
			}
		});
		
		frame.setVisible(true);
		*/
	}
	
	public ImageViewApp(File imageFile, int closeOperation)throws Exception{
		this(MODE_SPLIT_PANE, imageFile, closeOperation);
	}
	
	public ImageViewApp(int mode, File imageFile, int closeOperation)throws Exception{

		BufferedImage srcImage = ImageFactory.createImage(imageFile);
		ImageViewScope imageViewScope = new ImageViewScope(srcImage, SLAVE_SCALE_DEFAULT);
		
		ImageViewPanel masterViewPanel = new ImageViewPanel(imageViewScope, true, MASTER_SCALE_DEFAULT);
		ImageViewPanel slaveViewPanel = new ImageViewPanel(imageViewScope, false, SLAVE_SCALE_DEFAULT);
		
		ImageViewScrollPanel masterPanel = new ImageViewScrollPanel(true, masterViewPanel);
		ImageViewScrollPanel slavePanel = new ImageViewScrollPanel(false, slaveViewPanel);

		imageViewScope.addObserver(masterPanel);
		imageViewScope.addObserver(slavePanel);

		final ImageViewScrollPanelController masterPanelController = new ImageViewScrollPanelController(imageViewScope, masterPanel, masterViewPanel);
		final ImageViewScrollPanelController slavePanelController = new ImageViewScrollPanelController(imageViewScope, slavePanel, slaveViewPanel);
		
		if(mode == MODE_SPLIT_PANE){
			JFrame frame = new JFrame();
			frame.setTitle(imageFile.getName());
			frame.setLayout(new BorderLayout());
			JSplitPane pane = new JSplitPane();
			pane.setLeftComponent(masterPanel);
			slavePanel.setPreferredSize(new Dimension(500, 200));
			pane.setRightComponent(slavePanel);
			frame.add(pane);
			frame.pack();
			frame.setDefaultCloseOperation(closeOperation);
			frame.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					slavePanelController.updateScale(e);
				}
			});
			
			frame.setVisible(true);
			
		}else if(mode == MODE_DUAL_FRMAE){
			final ImageViewFrame masterFrame = new ImageViewFrame(masterPanel);
			final ImageViewFrame slaveFrame = new ImageViewFrame(slavePanel);
			masterFrame.setTitle("master");
			slaveFrame.setTitle("slave");
			masterFrame.setDefaultCloseOperation(closeOperation);
			slaveFrame.setDefaultCloseOperation(closeOperation);
			masterFrame.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					masterPanelController.updateScale(e);
				}
			});
			slaveFrame.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					slavePanelController.updateScale(e);
				}
			});
			masterFrame.setVisible(true);
			slaveFrame.setVisible(true);
		}
	}

	class ImageViewFrame extends JFrame{
		private static final long serialVersionUID = 0L;
		
		ImageViewFrame(final ImageViewScrollPanel panel)throws IOException{
			setLayout(new BorderLayout());
			add(panel, BorderLayout.CENTER);
			//setSize(300,300);
			pack();
		}
	}
}

class ImageViewScrollPanel extends JScrollPane implements Observer{
	private static final long serialVersionUID = 0L;

	private boolean isMaster;
	private ImageViewPanel viewPanel;

	ImageViewScrollPanel(boolean isMaster, ImageViewPanel viewPanel) throws IOException{
		this.isMaster = isMaster;
		this.viewPanel = viewPanel;

		if(isMaster){
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		}else{
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		setWheelScrollingEnabled(false);
		setAutoscrolls(false);
		
		JViewport viewport = this.getViewport();
		viewport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		viewport.setAutoscrolls(true);
		viewport.add(viewPanel);
		
		viewPanel.setViewport(viewport);
	}
	
	public void update(Observable o, Object arg) {
		if(arg instanceof ImageViewScope){
			ImageViewScope viewScope = (ImageViewScope)arg;
			viewPanel.nextImageViewScope = viewScope;
			/*
			if(! isMaster){				
				double x = viewScope.getViewPosition().getX();
				double y = viewScope.getViewPosition().getY();
				double scale = viewScope.getScale();
				Point nextViewPosition = new Point((int)(x * scale), (int)(y * scale));
				if(! nextViewPosition.equals(viewport.getViewPosition())){
					viewport.setViewPosition(nextViewPosition);
				}
			}*/
		}
		revalidate();
		repaint();
	}
}

class ImageViewScrollPanelController{
	private ImageViewScope imageViewScope;
	private ImageViewScrollPanel scrollPanel;
	private ImageViewPanel viewPanel;
	private int scaleMeter;

	ImageViewScrollPanelController(final ImageViewScope imageViewScope, final ImageViewScrollPanel scrollPanel, final ImageViewPanel viewPanel){
		this.imageViewScope = imageViewScope;
		this.scrollPanel = scrollPanel;
		this.viewPanel = viewPanel;
		
		double scale = imageViewScope.getScale();
		this.scaleMeter = getScaleMeter(scale);
		this.imageViewScope.setScale(scale);
		
		if(viewPanel.isMaster()){
			MasterMouseAdapter m = new MasterMouseAdapter();
			viewPanel.addMouseMotionListener(m);
			viewPanel.addMouseListener(m);
			return;
		}
		
		JViewport viewport = scrollPanel.getViewport();
		viewport.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				updateViewPosition(scrollPanel.getViewport().getViewPosition());
				imageViewScope.notifyObservers(null);
			}
		});
		SlaveMouseAdapter m = new SlaveMouseAdapter();
		viewPanel.addMouseMotionListener(m);
		viewPanel.addMouseListener(m);
		
		scrollPanel.addMouseWheelListener(new MouseWheelListener(){
			public void mouseWheelMoved(MouseWheelEvent e) {
				updateScale(calculateNewScaleByAmount(e.getWheelRotation()));
				imageViewScope.notifyObservers(imageViewScope);
			}
		});
		
		scrollPanel.addComponentListener(new ComponentListener(){

			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentResized(ComponentEvent e) {
				updateImageViewPanelSize(scrollPanel.getViewport().getExtentSize(), imageViewScope.getScale());
				imageViewScope.notifyObservers(null);
			}

			public void componentShown(ComponentEvent e) {
			}
		});
	}		
	
	class MasterMouseAdapter implements MouseMotionListener, MouseListener{
		
		private Point prevMousePoint = null;
		
		public void mouseDragged(MouseEvent e) {
			updateViewPositionByMouseEvent(e);
		}
		
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			updateViewPositionByMouseEvent(e);
		}

		public void mouseReleased(MouseEvent e) {
		}
		
		private void updateViewPositionByMouseEvent(MouseEvent e){
			Point p = e.getPoint();
			 
			double wh = imageViewScope.getExtentSize().getWidth() * viewPanel.getScale() / 2;
			double hh = imageViewScope.getExtentSize().getHeight() * viewPanel.getScale() / 2;
			
			if(	wh <= p.x && hh <= p.y && 
					p.x < viewPanel.getWidth() / viewPanel.getScale() - wh && 
					p.y < viewPanel.getHeight() / viewPanel.getScale() - hh ){
				double nx = (p.x - wh) / viewPanel.getScale();
				double ny = (p.y - hh) / viewPanel.getScale();
				imageViewScope.setViewPosition(nx, ny);
				imageViewScope.notifyObservers(imageViewScope);
			}
		}
	}
	
	class SlaveMouseAdapter implements MouseMotionListener, MouseListener{
		
		private Point prevMousePoint = null;
		
		public void mouseDragged(MouseEvent e) {
			Point curMousePoint = e.getLocationOnScreen();
			if(prevMousePoint == null){
				this.prevMousePoint = new Point(curMousePoint);
				return;
			}
			int dx = prevMousePoint.x - curMousePoint.x;
			int dy = prevMousePoint.y - curMousePoint.y;
			this.prevMousePoint = new Point(curMousePoint);
			
			Dimension extentSize = scrollPanel.getViewport().getExtentSize();
			Point viewPosition = scrollPanel.getViewport().getViewPosition();
			Point newViewPosition = new Point(viewPosition.x + dx, viewPosition.y + dy);
			int margin = 20;
			
			if(	0 <= newViewPosition.x && 0 <= newViewPosition.y && 
					-1 * extentSize.getWidth() + margin <=  newViewPosition.x && 
					-1 * extentSize.getHeight() + margin <= newViewPosition.y &&
					newViewPosition.x < viewPanel.getWidth() - margin &&
					newViewPosition.y < viewPanel.getHeight() - margin ){

				updateViewPosition(newViewPosition);
				imageViewScope.notifyObservers(null);
			}
		}
		
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			this.prevMousePoint = new Point(e.getLocationOnScreen());
			viewPanel.setCursor(ImageViewApp.HAND_CURSOR);
		}

		public void mouseReleased(MouseEvent e) {
			viewPanel.setCursor(ImageViewApp.DEFAULT_CURSOR);
		}
	}

	void updateScale(KeyEvent e) {
		char key = e.getKeyChar();
		if(key == '+'){
			updateScale(calculateNewScaleByAmount(ImageViewApp.SCALEMETER_STEP));
			imageViewScope.notifyObservers(imageViewScope);

		}else if(key == '-'){
			updateScale(calculateNewScaleByAmount(-1*ImageViewApp.SCALEMETER_STEP));
			imageViewScope.notifyObservers(imageViewScope);
		}
	}
	
	private int getScaleMeter(double scale) {
		if(scale <= 0.03f){
			return 3;
		}else if(0.03f < scale && scale <= 1.0f){
			return (int)(100 * scale);
		}else if(1.0f < scale && scale < 3.0f){
			return (int)((scale - 1.0f) * 50 + 100); 
		}else if(3.0f <= scale){
			return 200;
		}else{
			throw new RuntimeException();
		}
	}
	
	private double calculateNewScaleByAmount(double amount) {
		double scale;
		scaleMeter += amount;
		if(scaleMeter < 3){
			scale = 0.03f;
			scaleMeter = 3;
		}else if(3 <= scaleMeter && scaleMeter < 100){
			scale = scaleMeter / 100f;
		}else if(100 <= scaleMeter && scaleMeter < 200){
			scale = 1.0f + (scaleMeter - 100) / 50f;
		}else if(200 <= scaleMeter){
			scale = 3.0f;
			scaleMeter = 200;
		}else{
			scale = 1.0f;
		}
		return scale;
	}
	
	private void updateScale(double scale){
		double prevScale = imageViewScope.getScale();
		if(prevScale != scale){

			Dimension extentSize = scrollPanel.getViewport().getExtentSize();
			
			Point2D prevScopeViewPosition = imageViewScope.getViewPosition();
			
			Point2D prevScopeViewCenter = new Point2D.Double(
					prevScopeViewPosition.getX() + extentSize.getWidth() / prevScale / 2,
					prevScopeViewPosition.getY() + extentSize.getHeight() / prevScale / 2
					);
			
			double w = extentSize.getWidth() / scale;
			double h = extentSize.getHeight() / scale;

			double x = (prevScopeViewCenter.getX()) - w / 2;
			double y = (prevScopeViewCenter.getY()) - h / 2;

			/*
			double cx = x + w / 2;
			double cy = y + h / 2;

			if(x < 0){
				x = 0;
			}else if(viewPanel.getWidth() * scale - w < x){
				x = viewPanel.getWidth() * scale - w;
			}
			if(y < 0){
				y = 0;
			}else if(viewPanel.getHeight() * scale - h < y){
				y = viewPanel.getHeight() * scale - h;
			}
			*/
			
			/*
			System.out.println(prevScopeViewPosition+"\t"+extentSize);
			System.out.println("scale= "+imageViewScope.getScale()+"->"+scale);				
			System.out.println("x,y = "+x+", "+y);
			System.out.println("w,h = "+w+", "+h);
			System.out.println("cx,cy = "+cx+", "+cy);
			System.out.println();
			*/
			
			imageViewScope.setViewScope(x, y, w, h, scale);
			
		}else{
			/*
			System.out.println("scale="+scale);
			System.out.println();
			*/
		}
	}
	
	private void updateImageViewPanelSize(Dimension slaveExtentSize, double slaveScale){
		BufferedImage srcImage = imageViewScope.getSrcImage();
		Dimension newSlaveSize = new Dimension(
				(int)Math.max(slaveExtentSize.width, srcImage.getWidth() * slaveScale),
				(int)Math.max(slaveExtentSize.height, srcImage.getHeight() * slaveScale));
		
		viewPanel.setSize(newSlaveSize);
		viewPanel.setPreferredSize(newSlaveSize);

		double width = slaveExtentSize.width / slaveScale;
		double height = slaveExtentSize.height / slaveScale;
		
		imageViewScope.setExtentSize(width, height);
	}

	private void updateViewPosition(Point viewPosition) {
		scrollPanel.getViewport().setViewPosition(viewPosition);
		imageViewScope.setViewPosition((int)(viewPosition.x / imageViewScope.getScale()), (int)(viewPosition.y / imageViewScope.getScale()));
	}
}


class ImageViewPanel extends JPanel{

	private static final Color AFFINE_MODE_COLOR = new Color(0,0,255,30);
	private static final long serialVersionUID = 0L;

	private ImageViewScope imageViewScope;
	private boolean isMaster;
	private double scale;
	
	private LRUMap<Double,Image> cache = new LRUMap<Double,Image>(8);
	
	private ExecutorService singleThreadExecutorService = Executors.newSingleThreadExecutor();
	
	ImageViewScope nextImageViewScope = null;
	JViewport viewport;

	public ImageViewPanel(ImageViewScope imageViewScope, boolean isMaster, double scale) throws IOException{
		this.imageViewScope = imageViewScope;
		this.isMaster = isMaster;
		this.scale = scale;
		setPreferredSize(new Dimension(
				(int)(this.imageViewScope.getSrcImage().getWidth()*scale),
				(int)(this.imageViewScope.getSrcImage().getHeight()*scale)));
	}
	
	void setViewport(JViewport viewport){
		this.viewport = viewport;
	}
	
	public boolean isMaster(){
		return isMaster;
	}
		
	public double getScale(){
		return this.scale;
	}
	
	public void setScale(double scale){
		this.scale = scale;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(scale <= ImageViewApp.ON_THE_FLY_SCALING_THRESHOLD){
			drawOnTheFlyScaledImage(g);
		}else{
			drawAffineTransformedScaledImage(g);
		}

		if(isMaster){
			drawSlaveViewScope(g);			
		}else{
			if(nextImageViewScope != null){
				this.scale = nextImageViewScope.getScale();
				Dimension size = new Dimension(
						(int)(nextImageViewScope.getSrcImage().getWidth()*scale),
						(int)(nextImageViewScope.getSrcImage().getHeight()*scale));
				double x = nextImageViewScope.getViewPosition().getX();
				double y = nextImageViewScope.getViewPosition().getY();
				double scale = nextImageViewScope.getScale();
				viewport.setViewPosition(new Point((int)(x * scale), (int)(y * scale)));
				setPreferredSize(size);
				setSize(size);
				imageViewScope = nextImageViewScope;
				nextImageViewScope = null;
			}
			//drawCenter(g);
		}
	}

	private void drawAffineTransformedScaledImage(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		double scale = this.scale;
		AffineTransform at = AffineTransform.getScaleInstance(scale, scale);  
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		g2d.drawImage(this.imageViewScope.getSrcImage(), at, null);
		//g2d.setColor(AFFINE_MODE_COLOR);
		//g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	Future<Image> future = null;
	
	private void drawOnTheFlyScaledImage(Graphics g) {
		final double drawingImageScale = this.scale;
		Cursor prevCursor = getCursor();
		setCursor(ImageViewApp.WAIT_CURSOR);
		Image image = getCachedImage(drawingImageScale);
		if(image == null){
			drawAffineTransformedScaledImage(g);
			
			if(future != null){
				future.cancel(true);
			}
			
			Callable<Image> task = new Callable<Image>(){
				public Image call(){
					Image image = getCachedImage(drawingImageScale);
					if(image != null){
						return image;
					}
					image = createScaledImage(imageViewScope.getSrcImage());
					putCachedImage(drawingImageScale, image);
					
					SwingUtilities.invokeLater(
							new Runnable(){
								public void run(){
									revalidate();
									repaint();
								}
							}
					);

					return image;
				}
			};
			
			future = singleThreadExecutorService.submit(task);

			setCursor(prevCursor);
			return;
		}
		setCursor(prevCursor);
		g.drawImage(image, 0, 0, this);
	}
	
	private synchronized Image getCachedImage(Double scale){
		return cache.get(scale);
	}

	private synchronized boolean containsCachedImage(Double scale){
		return cache.containsKey(scale);
	}

	private synchronized void putCachedImage(Double scale, Image image){
		cache.put(scale, image);
	}
	
	private Image createScaledImage(BufferedImage image){
		int tWidth = (int)(image.getWidth() * scale);
		int tHeight = (int)(image.getHeight() * scale);
	
		Image scaledImage = image.getScaledInstance(tWidth, tHeight, Image.SCALE_AREA_AVERAGING);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(scaledImage, 0);
		
		try{
			mediaTracker.waitForID(0);
			
			PixelGrabber pixelGrabber = new PixelGrabber(scaledImage, 0, 0, -1, -1, false);
			pixelGrabber.grabPixels();
			ColorModel cm = pixelGrabber.getColorModel();

			final int w = pixelGrabber.getWidth();
			final int h = pixelGrabber.getHeight();
			WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
			BufferedImage renderedImage =
			  new BufferedImage(
			    cm,
			    raster,
			    cm.isAlphaPremultiplied(),
			    new Hashtable());
			renderedImage.getRaster().setDataElements(0, 0, w, h, pixelGrabber.getPixels());
			scaledImage.flush();
			return renderedImage;
		
		}catch(InterruptedException ex){
			throw new RuntimeException(ex);
		}
	}
	
	private void drawSlaveViewScope(Graphics g) {
		g.setColor(Color.RED);
		
		int x1 = (int)(imageViewScope.getViewPosition().getX() * scale);
		int y1 = (int)(imageViewScope.getViewPosition().getY() * scale);
		Dimension d = imageViewScope.getExtentSize();
		int w = (int) (d.getWidth() * scale);
		int h = (int) (d.getHeight() * scale);
		
		int x2 = (int)(x1 + w);
		int y2 = (int)(y1 + h);
		g.drawRect(x1, y1, w, h);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y1, x1, y2);
	}
	
	private void drawCenter(Graphics g){
		double x =  imageViewScope.getViewPosition().getX();
		double y = imageViewScope.getViewPosition().getY();
		double w =  imageViewScope.getExtentSize().getWidth();
		double h = imageViewScope.getExtentSize().getHeight();;
		int x1 = (int)(x * scale);
		int y1 = (int)(y * scale);  
		int x2 = (int)((x + w) * scale);
		int y2 = (int)((y + h) * scale);
		g.setColor(Color.RED);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y1, x1, y2);
	}
	
}

class ImageViewScope extends Observable{
	
	private BufferedImage srcImage;
	private Point2D viewPosition = new Point2D.Double();
	private Dimension extentSize = new Dimension();
	private double scale;
	
	ImageViewScope(BufferedImage srcImage, double scale){
		this.srcImage = srcImage;
		this.scale = scale;
	}
	
	public BufferedImage getSrcImage(){
		return this.srcImage;
	}
	
	public Point2D getViewPosition() {
		return viewPosition;
	}
	
	public Dimension getExtentSize() {
		return extentSize; 
	}
	
	public double getScale() {
		return scale;
	}
	
	public void setViewPosition(double x, double y) {
		this.viewPosition.setLocation(x, y);
		setChanged();
	}
	
	public void setExtentSize(double w, double h) {
		this.extentSize.setSize(w, h);
		setChanged();
	}
	
	public void setViewScope(double x, double y, double w, double h, double scale) {
		this.viewPosition.setLocation(x, y);
		this.extentSize.setSize(w, h);
		this.scale = scale;
		setChanged();
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		setChanged();			
	}
	
	public String toString(){
		return "("+viewPosition.getX()+","+viewPosition.getY()+")["+extentSize.width+","+extentSize.height+"]="+scale;
	}
}
