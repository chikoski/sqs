package net.sqs2.omr.app;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.session.logic.PageImageRenderer;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.OutputEventRecieversFactory;
import net.sqs2.swing.FileDropTargetDecorator;

public class PageImageRendererDemo {
	
	static protected MarkReaderApp markReaderAppSingleton = null;

	public static void main(String[] args)throws Exception{
		new PageImageRendererDemo();
	}
	
	PageImageRendererDemo()throws IOException{
		
		markReaderAppSingleton = new MarkReaderApp(1099, true);
		
		JFrame frame = new JFrame();
		JPanel p = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g){
				g.drawString("Drop your SourceFolder Here!", 10, 10);
			}
		};
		p.setPreferredSize(new Dimension(100, 100));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
		
		new FileDropTargetDecorator(frame){
			public void drop(File[] sourceDirectories){
				try{
					for(File directory:sourceDirectories){
						SessionState state = new SessionState(directory);
						state.showPageImage(directory, 0);
					}
				}catch(IOException ignore){
					ignore.printStackTrace();
				}
					
			}
		};
	}
	
	class SessionState{
		File sourceDirectoryRootFile;
		MarkReaderSession markReaderSession;
		
		long sessionID = 0l;
		SessionSource sessionSource = null;
		FormMaster master = null;
		SourceDirectory sourceDirectory = null;

		SessionState(File sourceDirectoryRootFile)throws IOException{
			this.sourceDirectoryRootFile = sourceDirectoryRootFile;
			OutputEventRecieversFactory consumersFactory = new OutputEventRecieversFactory(OutputEventRecieversFactory.Mode.BASE);
			this.markReaderSession = markReaderAppSingleton.createSession(sourceDirectoryRootFile, consumersFactory);
			markReaderSession.startSession();
		}

		private void showPageImage(File sourceDirectoryRootFile, int pageIDndex){
			
			int rowIndex = 0;
			int focusedColumnIndex = 0;
			int pageIndex = 0;
			Rectangle scope = null;
			
			
			try{
				PageImageRenderer.createImage(sessionID, sessionSource, master,
						sourceDirectory, rowIndex, focusedColumnIndex, pageIndex, scope);
			}catch(IOException ex){
			}catch(ConfigSchemeException ex){ 
			}
		}
	}
	
	
	
	
}
