/*

 CountRemovableResultFoldersTask.java

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

public class CountRemovableResultFoldersCommand implements Callable<Integer>{
	File targetDirectory;
	public CountRemovableResultFoldersCommand(File targetDirectory){
		this.targetDirectory = targetDirectory;
	}
	
	public Integer call(){
		return countRemovableResultFolders(this.targetDirectory);
	}

	private static int countRemovableResultFolders(File targetDirectory) {
		int count = 0;
		File resultDirectory = new File(targetDirectory, AppConstants.RESULT_DIRNAME);
		if (resultDirectory.exists()) {
			count++;
		}
		for (File targetSubFile : targetDirectory.listFiles()) {
			if (targetSubFile.isDirectory()) {
				count += countRemovableResultFolders(targetSubFile);
			}
		}
		return count;
	}
}