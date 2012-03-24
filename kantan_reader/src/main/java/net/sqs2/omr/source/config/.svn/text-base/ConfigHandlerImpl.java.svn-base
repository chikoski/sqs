/*

 ConfigHandlerImpl.java
 
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
 
 Created on 2007/01/11

 */
package net.sqs2.omr.source.config;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;

import net.sqs2.omr.source.Config;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.util.FileResourceID;

public class ConfigHandlerImpl implements Serializable, SourceDirectoryConfiguration{
	
	private static final long serialVersionUID = 0L;
	
	private FileResourceID fileResourceID;
	private Config config;
	
	public static final FileResourceID DEFAULT_SOURCE_CONFIG_URI_WITH_LASTMODIFIED = new FileResourceID(null, 0L);
	
	public ConfigHandlerImpl(File sourceDirectoryRoot, String configPath)throws MalformedURLException{
		File configFile = new File(sourceDirectoryRoot, configPath);
		this.fileResourceID = new FileResourceID(configPath, configFile.lastModified());
		this.config = ConfigImpl.createInstance(configFile.toURI().toURL());
	}
	
	public ConfigHandlerImpl(){
		this.fileResourceID = ConfigHandlerImpl.DEFAULT_SOURCE_CONFIG_URI_WITH_LASTMODIFIED;
		this.config = ConfigImpl.DEFAULT_INSTANCE;
	}
	
	public Config getConfig(){
		return this.config;
	}
	
	public FileResourceID getFileResourceID(){
		return this.fileResourceID;
	}
	
	public long lastModified(){
		return this.fileResourceID.getLastModified();
	}
	
	public String getPath(){
		return this.fileResourceID.getRelativePath();
	}
	
	@Override
	public String toString(){
		return "Config="+this.getFileResourceID().getRelativePath();
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof ConfigHandlerImpl){
			ConfigHandlerImpl o = (ConfigHandlerImpl) obj;
			return this.fileResourceID.equals(o.fileResourceID);
		}else{
			return false;
		}
	}

	@Override
	public int hashCode(){
		return this.fileResourceID.hashCode();
	}
}
