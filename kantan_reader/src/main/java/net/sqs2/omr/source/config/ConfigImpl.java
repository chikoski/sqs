/*

 ConfigImpl.java

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

 Created on Apr 7, 2007

 */

package net.sqs2.omr.source.config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sqs2.omr.app.NetworkPeer;
import net.sqs2.omr.session.MarkReaderSessions;
import net.sqs2.omr.session.Session;
import net.sqs2.omr.source.Config;
import net.sqs2.omr.source.GenericSourceConfig;
import net.sqs2.omr.task.PageTask;
import net.sqs2.util.FileUtil;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

public class ConfigImpl implements Config, Serializable{
	private final static long serialVersionUID = 0L;
	public final static Config DEFAULT_INSTANCE;
	protected static final Digester DIGESTER;

	static{
		try{
			String baseURI = NetworkPeer.getBaseURI();
			DIGESTER = DigesterLoader.createDigester(new URL(baseURI+"configRule.xml"));
			DEFAULT_INSTANCE = (Config)DIGESTER.parse(new URL(baseURI+GenericSourceConfig.SOURCE_CONFIG_FILENAME));
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	protected List<SourceConfig> sourceList = new ArrayList<SourceConfig>();
	String version;
	
	public static Config createInstance(URL url){
		try{
			Config config = (ConfigImpl)DIGESTER.parse(url);
			if(ConfigImpl.DEFAULT_INSTANCE.getVersion().equals(config.getVersion())){
				return config;
			}else{
				Logger.getAnonymousLogger().warning("Config file version in Source Directory is old. Override default config.");
				File oldConfigFile = new File(url.getFile());
				File backupConfigFile = new File(FileUtil.getBasepath(oldConfigFile)+".backup");
				if(! backupConfigFile.exists()){
					oldConfigFile.renameTo(backupConfigFile);
				}
				Session session = MarkReaderSessions.get(oldConfigFile.getParentFile());
				if(session != null){
					session.createConfigFileIfNotExists();
				}
				// TODO: GUI alert on using default config and backup config file.
				return ConfigImpl.DEFAULT_INSTANCE;
			}
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}catch(SAXException ex){
			throw new RuntimeException(ex);
		}
	}

	public void addSource(SourceConfig sourceConfig){		
		this.sourceList.add(sourceConfig);
	}

	public SourceConfig getSourceConfig(PageTask pageTask){
		return getSourceConfig(pageTask.getPageID().getFileResourceID().getRelativePath(), pageTask.getPageNumber());
	}

	public SourceConfig getSourceConfig(String path, int pageNumber){
		for(SourceConfig sourceConfig: this.sourceList){
			if(sourceConfig.match(path, pageNumber)){
				return sourceConfig;
			}
		}
		throw new RuntimeException("no matched config file");
	}

	public SourceConfig getSourceConfig(int index){
		return this.sourceList.get(index);
	}
	
	public SourceConfig getSourceConfig(){
		return this.sourceList.get(0);
	}
	
	public String getVersion(){
		return this.version;
	}
	
	public void setVersion(String version){
		this.version = version;
	}

}
