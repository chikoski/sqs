package net.sqs2.omr.source;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.source.config.ConfigHandlerImpl;
import net.sqs2.util.FileResourceID;

public class ConfigHandlers {
	
	private ConfigHandlerFactory configHandlerFactory;
	private Map<FileResourceID, SourceDirectoryConfiguration> configHandlerCache = new HashMap<FileResourceID, SourceDirectoryConfiguration>();

	public static final SourceDirectoryConfiguration DEFAULT_INSTANCE = new ConfigHandlerImpl();
	public static final SourceDirectoryConfiguration USER_CONFIGURED_INSTANCE = new ConfigHandlerImpl();
	private static boolean userConfigurationEnabled = false;

	public ConfigHandlers(ConfigHandlerFactory configHandlerFactory){
		this.configHandlerFactory = configHandlerFactory;
	}
	
	public SourceDirectoryConfiguration getConfigHandler(File base, String path, long lastModified)throws IOException{
		return getConfigHandler(base, new FileResourceID(path, lastModified));
	}

	private SourceDirectoryConfiguration getConfigHandler(File base, FileResourceID fileResourceID)throws IOException{
		SourceDirectoryConfiguration configHandler = this.configHandlerCache.get(fileResourceID);
		synchronized(this.configHandlerCache){
			if(configHandler == null){
				synchronized(this.configHandlerCache){
					configHandler = createConfigHandler(base, fileResourceID);
					this.configHandlerCache.put(fileResourceID, configHandler);
				}
			}
		}
		return configHandler; 
	}
	
	
	public SourceDirectoryConfiguration createConfigHandler(File rootDirectory, FileResourceID fileResourceID)throws IOException{
		return createConfigHandler(rootDirectory, fileResourceID.getRelativePath());
	}

	public SourceDirectoryConfiguration createConfigHandler(File base, String configFileRelativePath)throws MalformedURLException{
		SourceDirectoryConfiguration config = this.configHandlerFactory.create(base, configFileRelativePath);
		return config;
	}

	public SourceDirectoryConfiguration getDefaultConfigHandler(){
		return DEFAULT_INSTANCE;
	}
	
	public static SourceDirectoryConfiguration getUserConfiguredInstance(){
		return USER_CONFIGURED_INSTANCE;
	}
	
	public static void setUserConfigurationEnabled(boolean userConfigurationEnabled){
		ConfigHandlers.userConfigurationEnabled = userConfigurationEnabled;
	}

	public static boolean isUserConfigurationEnabled(){
		return ConfigHandlers.userConfigurationEnabled;
	}



}
