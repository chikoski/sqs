/*
 * 

 PersistentCacheManager.java

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
 */
package net.sqs2.cache;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;

public class PersistentCacheManager{

	private static Map<File, PersistentCacheManager> instanceMap = new HashMap<File, PersistentCacheManager>();

	private File directory; 
	private CacheManager manager;
	private Map<String, Ehcache> cacheMap = new HashMap<String, Ehcache>();

	public synchronized static PersistentCacheManager getInstance(File dir, String dirname) throws IOException {
		File path = createStorageDirectory(dir, dirname);
		return PersistentCacheManager.getInstance(path);
	}

	private PersistentCacheManager(File directory) throws IOException {
		this(createConfiguration(directory));
		this.directory = directory;
	}

	private PersistentCacheManager(Configuration configuration) {
		this.manager = CacheManager.create(configuration);
	}

	public Ehcache getECache(String name) {
		Ehcache cache = this.cacheMap.get(name);
		if (cache != null) {
			return cache;
		}
		try {
			cache = this.manager.getEhcache(name);
		} catch (IllegalStateException ex) {
			this.manager.removeCache(name);
		} catch (ClassCastException ex) {
			this.manager.removeCache(name);
		}
		if (cache == null) {
			cache = new Cache(name, 
					32, //maxElementsInMemory
					null, //MemoryStoreEvictionPolicy
					true, //overflowToDisk,
					null, //diskStorePath
					true, //eternal,
					0L, //timeToLiveSeconds
					0L, //timeToIdleSeconds
					true, //diskPersistent
					5L, //diskExpiryThreadIntervalSeconds,
					null, // RegisteredEventListeners
					null//BootstrapCacheLoader 
					);
			this.manager.addCache(cache);
			this.cacheMap.put(name, cache);
		}
		return cache;
	}

	public static void closeAll() {
		for(PersistentCacheManager persistentCacheManager : PersistentCacheManager.instanceMap.values()) {
			persistentCacheManager.close();
		}
	}
	
	public void close() {
		shutdown();
	}

	public void shutdown() {
		for (Ehcache cache: cacheMap.values()){
			if(cache.getStatus() == Status.STATUS_ALIVE){
				cache.flush();
				this.manager.removeCache(cache.getName());
			}
		}
		cacheMap.clear();
		PersistentCacheManager.instanceMap.remove(this.directory);
		this.manager.shutdown();
	}

	private static Configuration createConfiguration(File directory) {
		Configuration configuration = new Configuration();

		DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration();
		diskStoreConfiguration.setPath(directory.getAbsolutePath());
		configuration.addDiskStore(diskStoreConfiguration);

		CacheConfiguration defaultCacheConfiguration = new CacheConfiguration("SQS", 65536);
		defaultCacheConfiguration.setDiskPersistent(true);
		defaultCacheConfiguration.setEternal(true);

		configuration.addDefaultCache(defaultCacheConfiguration);
		configuration.setName(directory.toString()); // XXX We may name the configuration more appropriately

		return configuration;
	}

	private static File createStorageDirectory(File dir, String dirname) throws IOException {
		File storageDirectory = new File(dir.getAbsoluteFile() + File.separator + dirname);
		if (!storageDirectory.isDirectory()) {
			storageDirectory.mkdirs();
		}
		if (storageDirectory.exists() && storageDirectory.canWrite()) {
			if (File.separatorChar == '\\') {
				setHiddenFileAttribute(storageDirectory);
			}
			return storageDirectory;
		} else {
			throw new IOException("ReadOnlyFileSystem");
		}
	}

	private static void setHiddenFileAttribute(File storageDirectory) throws IOException {
		String cmd = "attrib.exe +h " + storageDirectory.getAbsolutePath();
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (InterruptedException ignore) {
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private synchronized static PersistentCacheManager getInstance(File directory) throws IOException {
		PersistentCacheManager ret = PersistentCacheManager.instanceMap.get(directory);
		if (ret == null) {
			ret = new PersistentCacheManager(directory);
			PersistentCacheManager.instanceMap.put(directory, ret);
		}
		return ret;
	}

}
