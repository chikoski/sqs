package net.sqs2.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sqs2.image.ImageFactory;
import net.sqs2.lang.GroupThreadFactory;

import org.apache.commons.collections15.map.LRUMap;

public class ImageViewApp {

	private static final Dimension SLAVE_PANEL_PREFERRED_SIZE = new Dimension(640, 400);

	public static final double MASTER_SCALE_DEFAULT = 0.06;
	public static final double SLAVE_SCALE_DEFAULT = 0.4;
	public static final double ON_THE_FLY_SCALING_THRESHOLD = 1.0;

	public static final double SCALEMETER_STEP = 10;

	public static final int MODE_DUAL_FRMAE = 0;
	public static final int MODE_SPLIT_PANE = 1;

	public static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	public static final Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	public static void main(String args[]) throws Exception {
		File imageFile = new File(args[0]);
		new ImageViewApp(MODE_SPLIT_PANE, imageFile, ImageFactory.createImage(imageFile, 0), new Rectangle(200,
				400, 100, 200), JFrame.EXIT_ON_CLOSE);
	}
	
	ImageViewScrollPanelController masterPanelController;
	ImageViewScrollPanelController slavePanelController;

	public static class ImageViewAppPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		ImageViewScrollPanelController masterPanelController;
		ImageViewScrollPanelController slavePanelController;
		
		public ImageViewAppPanel(File imageFile, int index) throws IOException {
			BufferedImage srcImage = ImageFactory.createImage(imageFile, index);
			ImageViewScope imageViewScope = new ImageViewScope(srcImage, SLAVE_SCALE_DEFAULT);

			ImageViewPanel masterViewPanel = new ImageViewPanel(imageViewScope, true, MASTER_SCALE_DEFAULT);
			ImageViewPanel slaveViewPanel = new ImageViewPanel(imageViewScope, false, SLAVE_SCALE_DEFAULT);

			ImageViewScrollPanel masterPanel = new ImageViewScrollPanel(true, masterViewPanel);
			ImageViewScrollPanel slavePanel = new ImageViewScrollPanel(false, slaveViewPanel);

			imageViewScope.addObserver(masterPanel);
			imageViewScope.addObserver(slavePanel);

			this.masterPanelController = new ImageViewScrollPanelController(
					imageViewScope, masterPanel, masterViewPanel);
			this.slavePanelController = new ImageViewScrollPanelController(
					imageViewScope, slavePanel, slaveViewPanel);

			JSplitPane splitPane = new JSplitPane();
			splitPane.setLeftComponent(masterPanel);
			slavePanel.setPreferredSize(new Dimension(500, 200));
			splitPane.setRightComponent(slavePanel);

			this.setLayout(new BorderLayout());
			this.add(splitPane);

			this.addKeyListener(new KeyListener() {
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

	public ImageViewApp(File imageFile, int index) throws Exception {
		this(imageFile, ImageFactory.createImage(imageFile, index));
	}

	private ImageViewApp(File imageFile, BufferedImage srcImage) throws Exception {

		ImageViewScope imageViewScope = new ImageViewScope(srcImage, SLAVE_SCALE_DEFAULT);

		ImageViewPanel masterViewPanel = new ImageViewPanel(imageViewScope, true, MASTER_SCALE_DEFAULT);
		ImageViewPanel slaveViewPanel = new ImageViewPanel(imageViewScope, false, SLAVE_SCALE_DEFAULT);

		ImageViewScrollPanel masterPanel = new ImageViewScrollPanel(true, masterViewPanel);
		ImageViewScrollPanel slavePanel = new ImageViewScrollPanel(false, slaveViewPanel);

		imageViewScope.addObserver(masterPanel);
		imageViewScope.addObserver(slavePanel);

		this.masterPanelController = new ImageViewScrollPanelController(
				imageViewScope, masterPanel, masterViewPanel);
		this.slavePanelController = new ImageViewScrollPanelController(
				imageViewScope, slavePanel, slaveViewPanel);

		JSplitPane pane = new JSplitPane();
		pane.setLeftComponent(masterPanel);
		slavePanel.setPreferredSize(new Dimension(500, 200));
		pane.setRightComponent(slavePanel);
		
		/*
		 * JFrame frame = new JFrame(); frame.setTitle(imageFile.getName());
		 * frame.setLayout(new BorderLayout()); frame.add(pane); frame.pack();
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * frame.addKeyListener(new KeyListener(){ public void
		 * keyPressed(KeyEvent e) { }
		 * 
		 * public void keyReleased(KeyEvent e) { }
		 * 
		 * public void keyTyped(KeyEvent e) {
		 * slavePanelController.updatemasterScale(e); } });
		 * 
		 * frame.setVisible(true);
		 */

	}

	public ImageViewApp(File imageFile, int index, int closeOperation) throws Exception {
		this(MODE_SPLIT_PANE, imageFile, ImageFactory.createImage(imageFile, index), null, closeOperation);
	}

	public ImageViewApp(File imageFile, BufferedImage image, Rectangle scope, int closeOperation)
			throws Exception {
		this(MODE_SPLIT_PANE, imageFile, image, scope, closeOperation);
	}

	private ImageViewScope createImageViewScope(BufferedImage srcImage, Rectangle scope) {
		int w = (int) SLAVE_PANEL_PREFERRED_SIZE.getWidth();
		int h = (int) SLAVE_PANEL_PREFERRED_SIZE.getHeight();
		double scaleX = w / scope.getWidth();
		double scaleY = h / scope.getHeight();
		double scale = Math.min(scaleX, scaleY);
		ImageViewScope imageViewScope = new ImageViewScope(srcImage, scale);
		imageViewScope.setViewScope(scope.getX(), scope.getY(), w / scale, h / scale, scale);
		return imageViewScope;
	}

	public ImageViewApp(int mode, File imageFile, BufferedImage srcImage, Rectangle scope, int closeOperation)
			throws Exception {

		ImageViewScope imageViewScope = null;

		if (scope == null) {
			imageViewScope = new ImageViewScope(srcImage, SLAVE_SCALE_DEFAULT);
		} else {
			imageViewScope = createImageViewScope(srcImage, scope);
		}

		ImageViewPanel masterViewPanel = new ImageViewPanel(imageViewScope, true, MASTER_SCALE_DEFAULT);
		ImageViewPanel slaveViewPanel = new ImageViewPanel(imageViewScope, false, imageViewScope.getScale());

		ImageViewScrollPanel masterPanel = new ImageViewScrollPanel(true, masterViewPanel);
		ImageViewScrollPanel slavePanel = new ImageViewScrollPanel(false, slaveViewPanel);
		slavePanel.setPreferredSize(SLAVE_PANEL_PREFERRED_SIZE);

		imageViewScope.addObserver(masterPanel);
		imageViewScope.addObserver(slavePanel);

		final ImageViewScrollPanelController masterPanelController = new ImageViewScrollPanelController(
				imageViewScope, masterPanel, masterViewPanel);
		final ImageViewScrollPanelController slavePanelController = new ImageViewScrollPanelController(
				imageViewScope, slavePanel, slaveViewPanel);

		if (mode == MODE_SPLIT_PANE) {
			JFrame frame = new JFrame();
			frame.setTitle(imageFile.getAbsolutePath());
			frame.setLayout(new BorderLayout());
			JSplitPane pane = new JSplitPane();
			pane.setLeftComponent(masterPanel);
			pane.setRightComponent(slavePanel);

			frame.add(pane);
			frame.pack();

			frame.setDefaultCloseOperation(closeOperation);
			frame.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					slavePanelController.updateScale(e);
				}
			});

			frame.setVisible(true);

		} else if (mode == MODE_DUAL_FRMAE) {
			final ImageViewFrame masterFrame = new ImageViewFrame(masterPanel);
			final ImageViewFrame slaveFrame = new ImageViewFrame(slavePanel);
			masterFrame.setTitle("master");
			slaveFrame.setTitle("slave");
			masterFrame.setDefaultCloseOperation(closeOperation);
			slaveFrame.setDefaultCloseOperation(closeOperation);
			masterFrame.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					masterPanelController.updateScale(e);
				}
			});
			slaveFrame.addKeyListener(new KeyListener() {
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

	class ImageViewFrame extends JFrame {
		private static final long serialVersionUID = 0L;

		ImageViewFrame(final ImageViewScrollPanel panel) throws IOException {
			setLayout(new BorderLayout());
			add(panel, BorderLayout.CENTER);
			// setSize(300,300);
			pack();
		}
	}
}

class ImageViewScrollPanel extends JScrollPane implements Observer {
	private static final long serialVersionUID = 0L;

	@SuppressWarnings("unused")
	private boolean isMaster;
	private ImageViewPanel viewPanel;

	ImageViewScrollPanel(boolean isMaster, ImageViewPanel viewPanel) throws IOException {
		this.isMaster = isMaster;
		this.viewPanel = viewPanel;

		setWheelScrollingEnabled(false);
		setAutoscrolls(false);

		JViewport viewport = this.getViewport();
		viewport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		viewport.setAutoscrolls(true);
		viewport.add(viewPanel);

		viewPanel.setViewport(viewport);

		if (isMaster) {
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		} else {
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
		}

	}

	public void update(Observable o, Object arg) {
		if (arg instanceof ImageViewScope) {
			ImageViewScope viewScope = (ImageViewScope) arg;
			this.viewPanel.nextImageViewScope = viewScope;
			/*
			 * if(! isMaster){ double x = viewScope.getViewPosition().getX();
			 * double y = viewScope.getViewPosition().getY(); double scale =
			 * viewScope.getScale(); Point nextViewPosition = new Point((int)(x
			 * * scale), (int)(y * scale)); if(!
			 * nextViewPosition.equals(viewport.getViewPosition())){
			 * viewport.setViewPosition(nextViewPosition); } }
			 */
		}
		revalidate();
		repaint();
	}
}

class ImageViewScrollPanelController {
	private ImageViewScope imageViewScope;
	private ImageViewScrollPanel scrollPanel;
	private ImageViewPanel viewPanel;
	private int scaleMeter;

	ImageViewScrollPanelController(final ImageViewScope imageViewScope,
			final ImageViewScrollPanel scrollPanel, final ImageViewPanel viewPanel) {
		this.imageViewScope = imageViewScope;
		this.scrollPanel = scrollPanel;
		this.viewPanel = viewPanel;

		double scale = imageViewScope.getScale();
		this.scaleMeter = getScaleMeter(scale);
		this.imageViewScope.setScale(scale);

		if (viewPanel.isMaster()) {
			MasterMouseAdapter m = new MasterMouseAdapter();
			viewPanel.addMouseMotionListener(m);
			viewPanel.addMouseListener(m);
			return;
		}

		JViewport viewport = scrollPanel.getViewport();
		viewport.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateViewPosition(scrollPanel.getViewport().getViewPosition());
				imageViewScope.notifyObservers(null);
			}
		});
		SlaveMouseAdapter m = new SlaveMouseAdapter();
		viewPanel.addMouseMotionListener(m);
		viewPanel.addMouseListener(m);

		scrollPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				updateScale(calculateNewScaleByAmount(e.getWheelRotation()));
				imageViewScope.notifyObservers(imageViewScope);
			}
		});

		scrollPanel.addComponentListener(new ComponentListener() {

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

		if (!viewPanel.isMaster()) {
			int x = (int) (imageViewScope.getViewPosition().getX() * imageViewScope.getScale());
			int y = (int) (imageViewScope.getViewPosition().getY() * imageViewScope.getScale());
			scrollPanel.getViewport().setViewPosition(new Point(x, y));
		}

	}

	class MasterMouseAdapter implements MouseMotionListener, MouseListener {

		//private Point prevMousePoint = null;

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

		private void updateViewPositionByMouseEvent(MouseEvent e) {
			Point p = e.getPoint();

			double wh = ImageViewScrollPanelController.this.imageViewScope.getExtentSize().getWidth()
					* ImageViewScrollPanelController.this.viewPanel.getScale() / 2;
			double hh = ImageViewScrollPanelController.this.imageViewScope.getExtentSize().getHeight()
					* ImageViewScrollPanelController.this.viewPanel.getScale() / 2;

			if (wh <= p.x
					&& hh <= p.y
					&& p.x < ImageViewScrollPanelController.this.viewPanel.getWidth()
							/ ImageViewScrollPanelController.this.viewPanel.getScale() - wh
					&& p.y < ImageViewScrollPanelController.this.viewPanel.getHeight()
							/ ImageViewScrollPanelController.this.viewPanel.getScale() - hh) {
				double nx = (p.x - wh) / ImageViewScrollPanelController.this.viewPanel.getScale();
				double ny = (p.y - hh) / ImageViewScrollPanelController.this.viewPanel.getScale();
				ImageViewScrollPanelController.this.imageViewScope.setViewPosition(nx, ny);
				ImageViewScrollPanelController.this.imageViewScope
						.notifyObservers(ImageViewScrollPanelController.this.imageViewScope);
			} else {
			}

		}
	}

	class SlaveMouseAdapter implements MouseMotionListener, MouseListener {

		private Point prevMousePoint = null;

		public void mouseDragged(MouseEvent e) {
			Point curMousePoint = e.getLocationOnScreen();
			if (this.prevMousePoint == null) {
				this.prevMousePoint = new Point(curMousePoint);
				return;
			}
			int dx = this.prevMousePoint.x - curMousePoint.x;
			int dy = this.prevMousePoint.y - curMousePoint.y;
			this.prevMousePoint = new Point(curMousePoint);

			Dimension extentSize = ImageViewScrollPanelController.this.scrollPanel.getViewport()
					.getExtentSize();
			Point viewPosition = ImageViewScrollPanelController.this.scrollPanel.getViewport()
					.getViewPosition();
			Point newViewPosition = new Point(viewPosition.x + dx, viewPosition.y + dy);
			int margin = 20;

			if (0 <= newViewPosition.x && 0 <= newViewPosition.y
					&& -1 * extentSize.getWidth() + margin <= newViewPosition.x
					&& -1 * extentSize.getHeight() + margin <= newViewPosition.y
					&& newViewPosition.x < ImageViewScrollPanelController.this.viewPanel.getWidth() - margin
					&& newViewPosition.y < ImageViewScrollPanelController.this.viewPanel.getHeight() - margin) {

				updateViewPosition(newViewPosition);
				ImageViewScrollPanelController.this.imageViewScope.notifyObservers(null);
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
			ImageViewScrollPanelController.this.viewPanel.setCursor(ImageViewApp.HAND_CURSOR);
		}

		public void mouseReleased(MouseEvent e) {
			ImageViewScrollPanelController.this.viewPanel.setCursor(ImageViewApp.DEFAULT_CURSOR);
		}
	}

	void updateScale(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == '+') {
			updateScale(calculateNewScaleByAmount(ImageViewApp.SCALEMETER_STEP));
			this.imageViewScope.notifyObservers(this.imageViewScope);

		} else if (key == '-') {
			updateScale(calculateNewScaleByAmount(-1 * ImageViewApp.SCALEMETER_STEP));
			this.imageViewScope.notifyObservers(this.imageViewScope);
		} else {
			updateScale(calculateNewScaleByAmount(0));
			this.imageViewScope.notifyObservers(this.imageViewScope);
		}
	}

	private int getScaleMeter(double scale) {
		if (scale <= 0.03f) {
			return 3;
		} else if (0.03f < scale && scale <= 1.0f) {
			return (int) (100 * scale);
		} else if (1.0f < scale && scale < 3.0f) {
			return (int) ((scale - 1.0f) * 50 + 100);
		} else if (3.0f <= scale) {
			return 200;
		} else {
			throw new RuntimeException();
		}
	}

	private double calculateNewScaleByAmount(double amount) {
		double scale;
		this.scaleMeter += amount;
		if (this.scaleMeter < 3) {
			scale = 0.03f;
			this.scaleMeter = 3;
		} else if (3 <= this.scaleMeter && this.scaleMeter < 100) {
			scale = this.scaleMeter / 100f;
		} else if (100 <= this.scaleMeter && this.scaleMeter < 200) {
			scale = 1.0f + (this.scaleMeter - 100) / 50f;
		} else if (200 <= this.scaleMeter) {
			scale = 3.0f;
			this.scaleMeter = 200;
		} else {
			scale = 1.0f;
		}
		return scale;
	}

	private void updateScale(double scale) {
		double prevScale = this.imageViewScope.getScale();

		Dimension extentSize = this.scrollPanel.getViewport().getExtentSize();

		Point2D prevScopeViewPosition = this.imageViewScope.getViewPosition();

		Point2D prevScopeViewCenter = new Point2D.Double(prevScopeViewPosition.getX() + extentSize.getWidth()
				/ prevScale / 2, prevScopeViewPosition.getY() + extentSize.getHeight() / prevScale / 2);

		double w = extentSize.getWidth() / scale;
		double h = extentSize.getHeight() / scale;

		double x = (prevScopeViewCenter.getX()) - w / 2;
		double y = (prevScopeViewCenter.getY()) - h / 2;

		this.imageViewScope.setViewScope(x, y, w, h, scale);
	}

	private void updateImageViewPanelSize(Dimension slaveExtentSize, double slaveScale) {
		BufferedImage srcImage = this.imageViewScope.getSrcImage();
		Dimension newSlaveSize = new Dimension((int) Math.max(slaveExtentSize.width, srcImage.getWidth()
				* slaveScale), (int) Math.max(slaveExtentSize.height, srcImage.getHeight() * slaveScale));

		this.viewPanel.setSize(newSlaveSize);
		this.viewPanel.setPreferredSize(newSlaveSize);

		double width = slaveExtentSize.width / slaveScale;
		double height = slaveExtentSize.height / slaveScale;

		this.imageViewScope.setExtentSize(width, height);
	}

	private void updateViewPosition(Point viewPosition) {
		this.scrollPanel.getViewport().setViewPosition(viewPosition);
		this.imageViewScope.setViewPosition((int) (viewPosition.x / this.imageViewScope.getScale()),
				(int) (viewPosition.y / this.imageViewScope.getScale()));
	}
}
class ImageViewScope extends Observable {
	
	private BufferedImage srcImage;
	private Point2D viewPosition = new Point2D.Double();
	private Dimension extentSize = new Dimension();
	private double scale;

	public ImageViewScope(BufferedImage srcImage, double scale) {
		this.srcImage = srcImage;
		this.scale = scale;
	}

	public BufferedImage getSrcImage() {
		return this.srcImage;
	}

	public Point2D getViewPosition() {
		return this.viewPosition;
	}

	public Dimension getExtentSize() {
		return this.extentSize;
	}

	public double getScale() {
		return this.scale;
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

	@Override
	public String toString() {
		return "(" + this.viewPosition.getX() + "," + this.viewPosition.getY() + ")[" + this.extentSize.width
				+ "," + this.extentSize.height + "]=" + this.scale;
	}
}

class ImageViewPanel extends JPanel {

	//private static final Color AFFINE_MODE_COLOR = new Color(0, 0, 255, 30);
	private static final long serialVersionUID = 0L;

	private ImageViewScope imageViewScope;
	private boolean isMaster;
	private double scale;

	private LRUMap<Double, Image> cache = new LRUMap<Double, Image>(8);

	private ExecutorService singleThreadExecutorService = Executors
			.newSingleThreadExecutor(new GroupThreadFactory("ImageViewApp", Thread.MAX_PRIORITY, true));

	ImageViewScope nextImageViewScope = null;
	JViewport viewport;

	public ImageViewPanel(ImageViewScope imageViewScope, boolean isMaster, double scale) throws IOException {
		this.imageViewScope = imageViewScope;
		this.isMaster = isMaster;
		this.scale = scale;
		setPreferredSize(new Dimension((int) (this.imageViewScope.getSrcImage().getWidth() * scale),
				(int) (this.imageViewScope.getSrcImage().getHeight() * scale)));
	}

	void setViewport(JViewport viewport) {
		this.viewport = viewport;
	}

	public boolean isMaster() {
		return this.isMaster;
	}

	public double getScale() {
		return this.scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.scale <= ImageViewApp.ON_THE_FLY_SCALING_THRESHOLD) {
			drawOnTheFlyScaledImage(g);
		} else {
			drawAffineTransformedScaledImage(g);
		}

		if (this.isMaster) {
			drawSlaveViewScope(g);
		} else {
			if (this.nextImageViewScope != null) {
				this.scale = this.nextImageViewScope.getScale();
				Dimension size = new Dimension(
						(int) (this.nextImageViewScope.getSrcImage().getWidth() * this.scale),
						(int) (this.nextImageViewScope.getSrcImage().getHeight() * this.scale));
				double x = this.nextImageViewScope.getViewPosition().getX();
				double y = this.nextImageViewScope.getViewPosition().getY();
				double scale = this.nextImageViewScope.getScale();
				this.viewport.setViewPosition(new Point((int) (x * scale), (int) (y * scale)));
				setPreferredSize(size);
				setSize(size);
				this.imageViewScope = this.nextImageViewScope;
				this.nextImageViewScope = null;
			}
			// drawCenter(g);
		}
	}

	private void drawAffineTransformedScaledImage(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		double scale = this.scale;
		AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		g2d.drawImage(this.imageViewScope.getSrcImage(), at, null);
		// g2d.setColor(AFFINE_MODE_COLOR);
		// g2d.fillRect(0, 0, getWidth(), getHeight());
	}

	Future<Image> future = null;

	private void drawOnTheFlyScaledImage(Graphics g) {
		final double drawingImageScale = this.scale;
		Cursor prevCursor = getCursor();
		setCursor(ImageViewApp.WAIT_CURSOR);
		Image image = getCachedImage(drawingImageScale);
		if (image == null) {
			drawAffineTransformedScaledImage(g);

			if (this.future != null) {
				this.future.cancel(true);
			}

			Callable<Image> task = new Callable<Image>() {
				public Image call() {
					Image image = getCachedImage(drawingImageScale);
					if (image != null) {
						return image;
					}
					image = createScaledImage(ImageViewPanel.this.imageViewScope.getSrcImage());
					putCachedImage(drawingImageScale, image);
					revalidate();
					repaint();
					return image;
				}
			};

			this.future = this.singleThreadExecutorService.submit(task);
			setCursor(prevCursor);
			return;
		}
		setCursor(prevCursor);
		g.drawImage(image, 0, 0, this);
	}

	private synchronized Image getCachedImage(Double scale) {
		return this.cache.get(scale);
	}

	@SuppressWarnings("unused")
	private synchronized boolean containsCachedImage(Double scale) {
		return this.cache.containsKey(scale);
	}

	private synchronized void putCachedImage(Double scale, Image image) {
		this.cache.put(scale, image);
	}

	private Image createScaledImage(BufferedImage image) {
		int tWidth = (int) (image.getWidth() * this.scale);
		int tHeight = (int) (image.getHeight() * this.scale);

		Image scaledImage = image.getScaledInstance(tWidth, tHeight, Image.SCALE_AREA_AVERAGING);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(scaledImage, 0);

		try {
			mediaTracker.waitForID(0);

			PixelGrabber pixelGrabber = new PixelGrabber(scaledImage, 0, 0, -1, -1, false);
			pixelGrabber.grabPixels();
			ColorModel cm = pixelGrabber.getColorModel();

			final int w = pixelGrabber.getWidth();
			final int h = pixelGrabber.getHeight();
			WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
			BufferedImage renderedImage = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(),
					new Hashtable<Object, Object>());
			renderedImage.getRaster().setDataElements(0, 0, w, h, pixelGrabber.getPixels());
			scaledImage.flush();
			return renderedImage;

		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void drawSlaveViewScope(Graphics g) {
		g.setColor(Color.RED);

		int x1 = (int) (this.imageViewScope.getViewPosition().getX() * this.scale);
		int y1 = (int) (this.imageViewScope.getViewPosition().getY() * this.scale);
		Dimension d = this.imageViewScope.getExtentSize();
		int w = (int) (d.getWidth() * this.scale);
		int h = (int) (d.getHeight() * this.scale);

		int x2 = (x1 + w);
		int y2 = (y1 + h);
		g.drawRect(x1, y1, w, h);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y1, x1, y2);
	}

	@SuppressWarnings("unused")
	private void drawCenter(Graphics g) {
		double x = this.imageViewScope.getViewPosition().getX();
		double y = this.imageViewScope.getViewPosition().getY();
		double w = this.imageViewScope.getExtentSize().getWidth();
		double h = this.imageViewScope.getExtentSize().getHeight();
		;
		int x1 = (int) (x * this.scale);
		int y1 = (int) (y * this.scale);
		int x2 = (int) ((x + w) * this.scale);
		int y2 = (int) ((y + h) * this.scale);
		g.setColor(Color.RED);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y1, x1, y2);
	}

}
