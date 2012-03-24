/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

import net.sqs2.omr.app.App;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class TemplateLoader{
	Configuration[] configurations = new Configuration[2];
	
	TemplateLoader(File baseDirectory, String path, String skin)throws IOException{
		String localPath = (baseDirectory.getAbsolutePath()+File.separatorChar+path+File.separatorChar+skin);
		this.configurations[0] = this.createDirectoryForTemplateLoadingConfiguration(new File(localPath));
		String classPath = "/"+path+"/"+skin;
		this.configurations[1] = this.createClassForTemplateLoadingConfiguration(classPath);
		Logger.getAnonymousLogger().info("localPath:"+localPath);
		Logger.getAnonymousLogger().info("classPath:"+classPath);
	}
	
	TemplateLoader(File baseDirectory, String path)throws IOException{
		this(baseDirectory, path, App.SKIN_ID);
	}
	
	private Configuration createDirectoryForTemplateLoadingConfiguration(File ftlDirectory)throws IOException{
		Configuration cfg = new Configuration();
		ftlDirectory.mkdirs();
		cfg.setDirectoryForTemplateLoading(ftlDirectory);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		//cfg.setEncoding(Locale.JAPANESE, "UTF-8");
		return cfg;
	}

	private Configuration createClassForTemplateLoadingConfiguration(String ftlPath)throws IOException{
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(getClass(), ftlPath);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		//cfg.setEncoding(Locale.JAPANESE, "UTF-8");
		return cfg;
	}
	
	public Template getTemplate(String templateName, String encoding)throws IOException{
				
		for(Configuration configuration: this.configurations){
			try{
				Logger.getAnonymousLogger().info("template loader:"+configuration.getTemplateLoader().getClass().getName()+
						"\t"+
						templateName);
				Template template = configuration.getTemplate(templateName, encoding);
				return template;
			}catch(FileNotFoundException ignore){
			}catch(SocketException ex){
				ex.printStackTrace();
				/*
				String uri = templateName;
				ProxySelector.getDefault().connectFailed(uri, ex., ioe);
				*/
			}
		}
		throw new IOException("template loading failure: "+templateName);
	}
}