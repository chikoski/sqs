package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.util.FileResourceID;

public class SourceDirectoryConfigurations {

	private SourceDirectoryConfigurationFactory sourceDirectoryConfigurationFactory;
	private Map<FileResourceID, SourceDirectoryConfiguration> configuratinCache = new HashMap<FileResourceID, SourceDirectoryConfiguration>();

	private static boolean userConfigurationEnabled = false;

	public SourceDirectoryConfigurations(SourceDirectoryConfigurationFactory configurationFactory) {
		this.sourceDirectoryConfigurationFactory = configurationFactory;
	}

	public SourceDirectoryConfiguration getConfiguration(File sourceDirectoryRoot, String configFilePath, long lastModified) throws IOException,ConfigSchemeException {
		return getConfiguration(sourceDirectoryRoot, new FileResourceID(configFilePath, lastModified));
	}

	private SourceDirectoryConfiguration getConfiguration(File sourceDirectoryRoot, FileResourceID configFileResourceID) throws IOException,ConfigSchemeException {
		SourceDirectoryConfiguration configuration = this.configuratinCache.get(configFileResourceID);
		synchronized (this.configuratinCache) {
			if (configuration == null) {
				synchronized (this.configuratinCache) {
					configuration = create(sourceDirectoryRoot, configFileResourceID);
					this.configuratinCache.put(configFileResourceID, configuration);
				}
			}
		}
		return configuration;
	}

	public SourceDirectoryConfiguration create(File rootDirectory, FileResourceID configFileResourceID) throws IOException,ConfigSchemeException {
		SourceDirectoryConfiguration config = this.sourceDirectoryConfigurationFactory.create(rootDirectory, configFileResourceID);
		return config;
	}
	
	public static void setUserDefaultConfigurationEnabled(boolean userConfigurationEnabled) {
		SourceDirectoryConfigurations.userConfigurationEnabled = userConfigurationEnabled;
	}

	public static boolean isUserConfigurationEnabled() {
		return SourceDirectoryConfigurations.userConfigurationEnabled;
	}

}
