/**
 *  SessionSourceInitializer.java

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
package net.sqs2.omr.session.init;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import net.sqs2.event.EventSource;
import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.master.NoPageMasterException;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.CacheConstants;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.ContentIndexer;
import net.sqs2.omr.model.FormMasterAccessor;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.session.init.SessionSourceInitializationLifeCycleEvent.LIFECYCLE;
import net.sqs2.omr.util.JarExtender;
import net.sqs2.omr.util.NameBasedComparableFile;
import net.sqs2.util.PathUtil;

public class SessionSourceInitializer implements Callable<SourceDirectory>{
	private SessionSource sessionSource;
	private FormMasterFactory formMasterFactory = null;
	private boolean isSearchPageMasterFromAncestorDirectory = false;
	private EventSource<SessionSourceInitializationEvent> eventSource;

	static final boolean MULTIPAGE_TIFF_ENABLED = false;
	static final boolean PDF_IMAGE_BUNDLE_ENABLED = false;
	static final boolean NUM_OF_IMAGES_PAR_ROW_CHECK_ENABLED = false;
	
	public SessionSourceInitializer(FormMasterFactory formMasterFactory,
			SessionSource sessionSource, 
			EventSource<SessionSourceInitializationEvent> eventSource,
			boolean isSearchPageMasterFromAncestorDirectory) throws IOException {
		this.formMasterFactory = formMasterFactory;
		this.sessionSource = sessionSource;
		this.eventSource = eventSource;
		this.isSearchPageMasterFromAncestorDirectory = isSearchPageMasterFromAncestorDirectory;
	}

	public SourceDirectory call() throws IOException, SessionSourceInitializationStopException, FormMasterException, ConfigSchemeException, SessionSourceException{
		File rootDirectory = this.sessionSource.getRootDirectory();
		SourceDirectory sourceDirectoryRoot = new SourceDirectory(rootDirectory);
		sessionSource.getContentAccessor().putSourceDirectory("", sourceDirectoryRoot);

		String configPath = AppConstants.RESULT_DIRNAME + File.separatorChar + AppConstants.SOURCE_CONFIG_FILENAME;
		createConfigurationTemplateFile(rootDirectory, configPath);

		SourceDirectory sourceDirectory = scanDescendant(sessionSource, sourceDirectoryRoot, null, configPath);
		
		if (sourceDirectory == null) {
			throw new RuntimeException("SourceDirectory is empty!");
		}

		sessionSource.getContentIndexer().sortSourceDirectoryList();
		
		eventSource.fireEvent(new SessionSourceInitializationLifeCycleEvent(this, LIFECYCLE.DONE));
		return sourceDirectoryRoot;
	}

	private File createConfigurationTemplateFile(File sourceDirectoryRoot, String configPath) {
		File resultDir = new File(sourceDirectoryRoot, AppConstants.RESULT_DIRNAME);
		File configFile = new File(sourceDirectoryRoot, configPath);
		if (! configFile.exists()) {
			new JarExtender().extend(new String[] { AppConstants.SOURCE_CONFIG_FILENAME }, resultDir);
		}
		return configFile;
	}
	
	private int getNumSQMPagesInPDF(File file)throws IOException{
		InputStream in = null;
		try{
			in = new BufferedInputStream(new FileInputStream(file)); 
			HashMap<?,?> map = ImageFactory.getMetadataMap("pdf", in);
			if(((String)map.get("Producer")).startsWith("SQS")){
				return ((Integer)map.get("NumberOfPages")).intValue();
			}else{
				return 0;
			}
		}finally{
			if(in != null){
				in.close();
			}
		}
	}
	
	private SourceDirectory scanDescendant(SessionSource sessionSource,
			SourceDirectory sourceDirectory, FormMaster formMaster, 
			String configPath) throws IOException, SessionSourceException, FormMasterException, ConfigSchemeException, SessionSourceInitializationStopException {
		List<File> subDirectoryList = new ArrayList<File>();

		int numAddedPages = 0;
		int numAddedFolders = 0;

		String base = (0 == sourceDirectory.getRelativePath().length()) ? "" : sourceDirectory.getRelativePath()
				+ File.separatorChar;

		
		File[] items = sourceDirectory.getDirectory().listFiles();
		if (items == null || 0 == items.length) {
			return null;
		}
		File[] files = NameBasedComparableFile.createSortedFileArray(items);

		for (File file : files) {
			String filename = file.getName();
			String lowerFilename = filename.toLowerCase();
			if (lowerFilename.endsWith(".pdf")) {
				int numAddingPages = getNumSQMPagesInPDF(file);
				if(0 < numAddingPages){
					FormMaster newFormMaster = createMaster(sessionSource, file);
					if (newFormMaster != null) {
						formMaster = newFormMaster;
					}
				}else{					
					numAddedPages += ImageFactory.getNumPages("pdf", file);
					sourceDirectory.setupPageIDList(file);
				}				
			} else if (filename.endsWith(".sqm")) {
				FormMaster newPageMaster = createMaster(sessionSource, file);
				if (newPageMaster != null) {
					formMaster = newPageMaster;
				}
			}else if (lowerFilename.endsWith(".mtiff")) {
				int numAddingPages = ImageFactory.getNumPages("tiff", file);
				numAddedPages += numAddingPages;
				sourceDirectory.setupPageIDList(file);
			}else if (file.isFile() && ImageFactory.isSupportedFileName(filename)) {
				numAddedPages++;
				sourceDirectory.setupPageIDList(file);
			} else if (file.isDirectory() && ! isIgnorableDirectory(file)) {
				subDirectoryList.add(file);
			}
			
			if (sessionSource.getSessionSourceState().equals(SessionSourceState.STOPPED)) {
				throw new SessionSourceInitializationStopException();
			}
		}

		int sessionSourceSerialID = sessionSource.getCurrentSourceDirectorySerialID();
		sessionSource.incrementCurrentSourceDirectorySerialID();
		
		for (File file : subDirectoryList) {
			String filename = file.getName();
			String path = base + filename;
			SourceDirectory subSourceDirectory = new SourceDirectory(sourceDirectory.getRoot(), path);
			SourceDirectory aChild = scanDescendant(sessionSource, subSourceDirectory, formMaster,
					configPath);
			if (aChild != null) {
				numAddedFolders++;
				sourceDirectory.addChildSourceDirectoryList(aChild);
				sessionSource.getContentAccessor().putSourceDirectory(path, aChild);
			}
		}

		if (0 == numAddedPages && 0 == numAddedFolders) {
			return null;
		}
		sourceDirectory.setID(sessionSourceSerialID);
		
		return setupSourceDirectory(sessionSource, sourceDirectory, formMaster, configPath,
				numAddedPages);
	}

	private SourceDirectory setupSourceDirectory(SessionSource sessionSource, SourceDirectory sourceDirectory, FormMaster formMaster, String configPath, int numAddedImageFiles) throws IOException, SessionSourceException, FormMasterException, ConfigSchemeException {

		eventSource.fireEvent(new SessionSourceInitializationLifeCycleEvent(this, LIFECYCLE.DONE));
		
		if (formMaster == null) {
			if (sourceDirectory != null && sourceDirectory.getChildSourceDirectoryList() != null) {
				for (SourceDirectory aChild : sourceDirectory.getChildSourceDirectoryList()) {
					formMaster = aChild.getDefaultFormMaster();
					if (formMaster != null) {
						break;
					}
				}
			}
			if (formMaster == null && this.isSearchPageMasterFromAncestorDirectory) {
				try {
					formMaster = getPageMasterFromAncestorDirectory(sessionSource, 
							sourceDirectory.getDirectory().getParentFile());
				} catch (IOException ignore) {
				}
			}
		}

		if (formMaster == null) {
			throw new NoPageMasterException(sourceDirectory.getDirectory());
		}
		
		sourceDirectory.setDefaultFormMaster(formMaster);

		File configFile = createConfigurationTemplateFile(sourceDirectory.getRoot(), configPath);

		SourceDirectoryConfiguration configuration = sessionSource.getConfiguration(configPath, configFile.lastModified());
		sourceDirectory.setSourceDirectoryConfiguration(configuration);
	
		if (NUM_OF_IMAGES_PAR_ROW_CHECK_ENABLED && (numAddedImageFiles % formMaster.getNumPages() != 0)) {

			eventSource.fireEvent(new NumOfImagesErrorEvent(this, new SpreadSheet(sessionSource.getSessionID(), formMaster, sourceDirectory), numAddedImageFiles));

			throw new InvalidNumImagesException(sourceDirectory, numAddedImageFiles);
		}
		ContentIndexer indexer = sessionSource.getContentIndexer();
		indexer.setConfiguration(configuration.getConfigFileResourceID(), configuration);
		indexer.putFormMaster(formMaster.getFileResourceID(), formMaster);
		indexer.addToSourceDirectoryFlattenList(formMaster, sourceDirectory);
		eventSource.fireEvent(new ImageFilesFoundEvent(this, numAddedImageFiles, sourceDirectory.getDirectory()));
		return sourceDirectory;
	}

	private FormMaster getPageMasterFromAncestorDirectory(SessionSource sessionSource, File directory) throws IOException {
		if (directory == null) {
			throw new IOException("No master file.");
		}
		for (File file : directory.listFiles()) {
			String filename = file.getName().toLowerCase();
			if (filename.endsWith(".pdf") || filename.endsWith(".sqm")) {
				FormMaster master = createMaster(sessionSource, file);
				if (master != null) {
					return master;
				}
			}
		}
		return getPageMasterFromAncestorDirectory(sessionSource, directory.getParentFile());
	}

	private FormMaster createMaster(SessionSource sessionSource, File file) {

		FormMaster master = null;
		try {
			String path = PathUtil.getRelativePath(file, sessionSource.getRootDirectory(), File.separatorChar);
			FormMasterAccessor formMasterAccessor = sessionSource.getContentAccessor()
					.getFormMasterAccessor();
			FormMaster cachedMaster = null;

			try {
				cachedMaster = formMasterAccessor.get(FormMaster.createKey(path, file.lastModified()));
			} catch (Error ignore) {
				ignore.printStackTrace();
			}
			
			if (cachedMaster != null && cachedMaster.getLastModified() == file.lastModified()) {
				return cachedMaster;
			}

			master = this.formMasterFactory.create(sessionSource.getRootDirectory(), path);
			if (master == null) {
				throw new FormMasterException(file);
			}
			formMasterAccessor.put(master);
			formMasterAccessor.flush();
			eventSource.fireEvent(new FormMasterFoundEvent(this, master));
			return master;
		} catch (FormMasterException ex) {
			eventSource.fireEvent(new SessionSourceInitializationErrorEvent(this, ex));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private boolean isIgnorableDirectory(File file) {
		return file.getName().endsWith(AppConstants.RESULT_DIRNAME)
				|| new File(file, CacheConstants.CACHE_ROOT_DIRNAME).exists()
				|| (new File(file, "TEXTAREA").exists() && new File(file, "CHART").exists()) 
				|| file.getName().startsWith(".");

	}

}
