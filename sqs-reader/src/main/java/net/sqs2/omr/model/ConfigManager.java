package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import net.sqs2.util.FileUtil;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

public class ConfigManager {
	
	private static Digester DIGESTER;
	private static Config DEFAULT_INSTANCE;
	
	private static void init(String configRuleFileName, String configFileName) throws ConfigSchemeException{
		try {
			if(DEFAULT_INSTANCE == null){
				String baseURI = "class://"+ConfigManager.class.getCanonicalName()+"/";
				URL configRuleFileURL = new URL(baseURI + configRuleFileName);
				URL configFileURL = new URL(baseURI + configFileName);
				DIGESTER = DigesterLoader.createDigester(configRuleFileURL);
				DEFAULT_INSTANCE = (Config) DIGESTER.parse(configFileURL);
			}
		} catch (Exception ex) {
			throw new ConfigSchemeException(ex);
		}
	}
	
	public static Config createConfigInstance(URL url, String configRuleFileName, String configFileName) throws ConfigSchemeException{
		try {
			init(configRuleFileName, configFileName);
			Config config = (ConfigImpl) DIGESTER.parse(url);
			if (! DEFAULT_INSTANCE.getVersion().equals(config.getVersion())) {
				Logger.getLogger("ConfigManager").warning(
						"Config file version in Source Directory contains version mismatch. Override it.");
				File configFile = new File(url.getFile());
				File backupConfigFile = new File(FileUtil.getBasepath(configFile) + "-"
						+ config.getVersion() + ".old.xml");
				if (!backupConfigFile.exists()) {
					configFile.renameTo(backupConfigFile);
				}
				ConfigUtil.createConfigFile(configFile);
				config = (ConfigImpl) DIGESTER.parse(url);
			}
			return config;

		} catch (IOException ex) {
			throw new ConfigSchemeException(ex);
		} catch (SAXException ex) {
			throw new ConfigSchemeException(ex);
		}
	}

}
