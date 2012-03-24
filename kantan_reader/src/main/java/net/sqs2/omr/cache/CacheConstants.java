package net.sqs2.omr.cache;

import java.io.File;

import net.sqs2.omr.app.App;

public class CacheConstants {
	public static final String CACHE_DB_VERSION = "1.3.1";
	public static final String CACHE_ROOT_DIRNAME = "_CACHE";

	public static String getCacheDirname(){
		return App.getResultDirectoryName()+File.separatorChar+CACHE_ROOT_DIRNAME+File.separatorChar+CACHE_DB_VERSION;
	}
}
