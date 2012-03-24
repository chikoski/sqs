/**
 *  SourceDirectoryBuilderMonitor.java

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

 Created on 2007/04/29
 Author hiroya
 */
package net.sqs2.omr.source;

import java.io.File;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.master.PageMasterException;

public interface SourceInitializationMonitor{	
	public void notifyFoundMaster(PageMaster master);
	public void notifyFoundConfig(SourceDirectoryConfiguration config);
	public void notifyScanDirectory(File subSourceDirectoryRoot);
	public void notifyFoundImages(int numAddedImages, File sourceDirectory);
	public void notifySourceInitializeDone(File sourceDirectoryRootFile);
	
	public void notifyErrorDirectoryUnreadable(SourceDirectory sourceDirectory);
	public void notifyErrorNumOfImages(SourceDirectory SourceDirectory, int numImages);
	public void notifyErrorOnPageMaster(PageMasterException ex);
}