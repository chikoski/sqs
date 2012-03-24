/**
 *  SourceDirectoryFactory.java

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

 Created on 2007/03/10
 Author hiroya
 */
package net.sqs2.omr.source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sqs2.image.ImageUtil;
import net.sqs2.omr.app.App;
import net.sqs2.omr.cache.CacheConstants;
import net.sqs2.omr.master.FormMasterFactories;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.master.PageMasterAccessor;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.task.TaskException;
import net.sqs2.omr.util.NameBasedComparableFile;
import net.sqs2.util.PathUtil;

public class SessionSourceInitializer{

	private FormMasterFactories formMasterFactories = null;
	private boolean isSearchPageMasterFromAncestorDirectory = false;
	private SourceInitializationMonitor monitor;
	
	public SessionSourceInitializer(FormMasterFactories formMasterFactories, boolean isSearchPageMasterFromAncestorDirectory)throws IOException{
		this.formMasterFactories = formMasterFactories;
		this.isSearchPageMasterFromAncestorDirectory = isSearchPageMasterFromAncestorDirectory;
	}

	/**
	 * initialize SessionSource instance
	 * @throws IOException
	 */
	public SourceDirectory initialize(SessionSource sessionSource, SourceInitializationMonitor monitor)throws IOException,TaskException, SessionSourceException, PageMasterException{

		this.monitor = monitor; 
		File rootDirectory = sessionSource.getRootDirectory();
		sessionSource.initialize();
		SourceDirectory sourceDirectoryRoot = new SourceDirectory(rootDirectory);
		sessionSource.getSessionSourceContentAccessor().putSourceDirectory("", sourceDirectoryRoot);

		SourceDirectory sourceDirectory = scanDescendant(sessionSource, sourceDirectoryRoot, null, null);
		if(sourceDirectory == null){
			throw new RuntimeException("SourceDirectory is null!");
		}
		if(monitor!= null){
			monitor.notifySourceInitializeDone(rootDirectory);
		}
		return sourceDirectoryRoot;
	}
	
	private SourceDirectory scanDescendant(SessionSource sessionSource, SourceDirectory sourceDirectory, 
			PageMaster pageMaster, SourceDirectoryConfiguration configHandler)throws IOException, SessionSourceException, PageMasterException{
		File[] items = sourceDirectory.getDirectory().listFiles();
		List<File> subDirectoryList = new ArrayList<File>();
		
		if(items == null || 0 == items.length){
			return null;
		}
		
		File[] files = NameBasedComparableFile.createSortedFileArray(items);
		
		int numAddedImageFiles = 0;
		int numAddedFolders = 0;
		
		String base = (0 == sourceDirectory.getPath().length())? "" : sourceDirectory.getPath() + File.separatorChar;
		
	    for(File file: files){
	    	String filename = file.getName();
	    	if(ImageUtil.isSupported(filename)){
				numAddedImageFiles ++;
			}else if(file.isDirectory() && ! isIgnorableDirectory(file)){
	    		subDirectoryList.add(file);
	    	}else if(filename.endsWith(".pdf")){
	    		PageMaster newPageMaster = createMasterByPDFFile(sessionSource, file);
	    		if(newPageMaster != null){
	    			pageMaster = newPageMaster;
	    		}
	    	}else if(filename.endsWith(".sqm")){
	    		PageMaster newPageMaster = createMasterBySQMFile(sessionSource, file);
	    		if(newPageMaster != null){
	    			pageMaster = newPageMaster;
	    		}
			}else if(filename.equals(GenericSourceConfig.SOURCE_CONFIG_FILENAME)){
		    	String path = base + filename;
		    	long lastModified = new File(sessionSource.getRootDirectory(), path).lastModified();
				configHandler = sessionSource.getConfigHandler(path, lastModified);
			}
	    }
	    
	    if(configHandler == null){
	    	configHandler = sessionSource.getDefaultConfigHandler();
	    }
	    
	    for(File file: subDirectoryList){
	    	String filename = file.getName();
	    	String path = base + filename;
			SourceDirectory subSourceDirectory = new SourceDirectory(sourceDirectory.getRoot(), path);
			SourceDirectory aChild = scanDescendant(sessionSource, subSourceDirectory, pageMaster, configHandler);
			if(aChild != null){
				numAddedFolders++;
				sourceDirectory.addChildSourceDirectoryList(aChild);
				sessionSource.getSessionSourceContentAccessor().putSourceDirectory(path, aChild);
			}
		}

		return setupSourceDirectory(sessionSource, sourceDirectory, pageMaster, configHandler,
				numAddedImageFiles, numAddedFolders);
    }

	private SourceDirectory setupSourceDirectory(
			SessionSource sessionSource,
			SourceDirectory sourceDirectory, 
			PageMaster pageMaster,
			SourceDirectoryConfiguration configHandler, 
			int numAddedImageFiles, int numAddedFolders) throws SessionSourceException,PageMasterException{
		
		if(this.monitor != null){
			this.monitor.notifyScanDirectory(sourceDirectory.getDirectory());
		}

		if(0 < numAddedImageFiles || 0 < numAddedFolders){
			if(pageMaster == null){
				if(sourceDirectory != null && sourceDirectory.getChildSourceDirectoryList() != null){
					for(SourceDirectory aChild: sourceDirectory.getChildSourceDirectoryList()){
						pageMaster = aChild.getPageMaster();
						if(pageMaster != null){
							break;
						}
					}
				}
				if(pageMaster == null && isSearchPageMasterFromAncestorDirectory){
					try{
						pageMaster = getPageMasterFromAncestorDirectory(sessionSource, sourceDirectory.getDirectory().getParentFile());
					}catch(IOException ignore){
					}
				}
			}

			if(pageMaster != null){

				sourceDirectory.setPageMaster(pageMaster);
				sourceDirectory.setSourceDirectoryConfiguration(configHandler);

				if(numAddedImageFiles % pageMaster.getNumPages() != 0){
					this.monitor.notifyErrorNumOfImages(sourceDirectory, numAddedImageFiles);
					throw new InvalidNumImagesException(sourceDirectory, numAddedImageFiles);
				}
								
				sessionSource.getSessionSourceContentIndexer().setConfigHandler(configHandler.getFileResourceID(), configHandler);
				sessionSource.getSessionSourceContentIndexer().addSourceDirectory(pageMaster, sourceDirectory);
				sessionSource.getSessionSourceContentIndexer().putPageMaster(pageMaster.getFileResourceID(), pageMaster);
				
				if(this.monitor != null){
					this.monitor.notifyFoundImages(numAddedImageFiles, sourceDirectory.getDirectory());
				}
				
			    return sourceDirectory;
			}else{
				throw new PageMasterException("No Master Files Found:" + sourceDirectory.getPath());
			}
		}
		return null;
	}

	private PageMaster getPageMasterFromAncestorDirectory(SessionSource sessionSource, File directory)throws IOException{
		if(directory == null){
			throw new IOException("No master file.");
		}
		for(File file: directory.listFiles()){
			String filename = file.getName().toLowerCase();
			if(filename.endsWith(".pdf")){
				PageMaster master = createMasterByPDFFile(sessionSource, file);
				if(master != null){
					return master;
				}
			}else if(filename.endsWith(".sqm")){
				PageMaster master = createMasterBySQMFile(sessionSource, file);
				if(master != null){
					return master;
				}
			}
		}
		return getPageMasterFromAncestorDirectory(sessionSource, directory.getParentFile());
	}
	
	private PageMaster createMasterBySQMFile(SessionSource sessionSource, File masterFile){
		throw new RuntimeException("NOT_IMPLEMENTED");
	}

	private PageMaster createMasterByPDFFile(SessionSource sessionSource, File pdfFile) {

		PageMaster master = null;
		try{
			String path = PathUtil.getRelativePath(pdfFile, sessionSource.getRootDirectory());
			PageMasterAccessor pageMasterAccessor = sessionSource.getSessionSourceContentAccessor().getPageMasterAccessor();
			PageMaster cachedMaster = null;

			try{
				cachedMaster = pageMasterAccessor.get(path);
			}catch(Error ignore){
				pageMasterAccessor.close();
				
			}
			if(cachedMaster != null && cachedMaster.getLastModified() == pdfFile.lastModified()){
				return cachedMaster;
			}
			
			master = this.formMasterFactories.createMasterByPDFFile(sessionSource.getRootDirectory(), path);
			if(master == null){
				throw new PageMasterException(path);
			}
			pageMasterAccessor.put(master);
			pageMasterAccessor.flush();

			if(this.monitor != null){
				this.monitor.notifyFoundMaster(master);
			}
			return master;
		}catch(PageMasterException ex){
			monitor.notifyErrorOnPageMaster(ex);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return null;
	}

	private boolean isIgnorableDirectory(File file) {
		return file.getName().endsWith(App.getResultDirectoryName()) ||
		new File(file, CacheConstants.CACHE_ROOT_DIRNAME).exists() ||
		(new File(file, "TEXTAREA").exists() && new File(file, "CHART").exists());
		
	}

}
