/*

 RemoveResultFoldersTask.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package net.sqs2.omr.app.command;

import java.io.File;
import java.util.concurrent.Callable;

import net.sqs2.omr.model.AppConstants;
import net.sqs2.util.FileUtil;

public class RemoveResultFoldersCommand implements Callable<Void>{
	File targetDirectory;
	public RemoveResultFoldersCommand(File targetDirectory){
		this.targetDirectory = targetDirectory;
	}
	
	public Void call(){
		removeResultFolders(this.targetDirectory);
		return null;
	}

	private static void removeResultFolders(File targetDirectory) {
		synchronized(RemoveResultFoldersCommand.class){
			for (File targetSubFile : targetDirectory.listFiles()) {
				if (targetSubFile.isDirectory()) {
					removeResultFolders(targetSubFile);
				}
			}
			
			File resultDirectory = new File(targetDirectory, AppConstants.RESULT_DIRNAME);
			if (! resultDirectory.exists()) {
				return;
			}
			for(int i = 1; i <= 10 && resultDirectory.exists(); i++) {
				FileUtil.deleteDirectory(resultDirectory);
				// wait and retry delete
				try{
					Thread.sleep(100*i);
				}catch(InterruptedException ignore){}
			}
			if (resultDirectory.exists()) {
				throw new RuntimeException("Error on remove: "+resultDirectory.getAbsolutePath());
			}
		}
	}
}